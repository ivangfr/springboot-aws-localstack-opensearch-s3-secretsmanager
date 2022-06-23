package com.mycompany.movieapi.service;

import com.mycompany.movieapi.exception.MovieNotFoundException;
import com.mycompany.movieapi.exception.MovieServiceException;
import com.mycompany.movieapi.property.AwsProperties;
import com.mycompany.movieapi.rest.SearchMovieResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.action.get.GetRequest;
import org.opensearch.action.get.GetResponse;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.action.index.IndexResponse;
import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.common.unit.TimeValue;
import org.opensearch.common.xcontent.XContentType;
import org.opensearch.index.query.QueryBuilders;
import org.opensearch.search.SearchHit;
import org.opensearch.search.SearchHits;
import org.opensearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MovieServiceImpl implements MovieService {

    private final RestHighLevelClient restHighLevelClient;
    private final AwsProperties awsProperties;
    private final PosterService posterService;

    @Override
    public Optional<Map<String, Object>> getMovie(String imdb) {
        try {
            GetRequest getRequest = new GetRequest(awsProperties.getOpensearch().getIndexes(), imdb);
            GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
            Map<String, Object> source = getResponse.getSource();
            return getResponse.getSource() != null ? Optional.of(source) : Optional.empty();
        } catch (Exception e) {
            String errorMessage = String.format("An exception occurred while getting document with imdb '%s'. %s", imdb, e.getMessage());
            log.error(errorMessage);
            return Optional.empty();
        }
    }

    @Override
    public Map<String, Object> validateAndGetMovie(String imdb) {
        return getMovie(imdb).orElseThrow(() -> new MovieNotFoundException(String.format("Movie with imdb '%s' not found", imdb)));
    }

    @Override
    public SearchMovieResponse searchMovies(String title) {
        try {
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.size(50);
            if (StringUtils.hasText(title)) {
                searchSourceBuilder.query(QueryBuilders.termQuery("title", title));
            }
            SearchRequest searchRequest = new SearchRequest(awsProperties.getOpensearch().getIndexes())
                    .source(searchSourceBuilder);
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            return toSearchMovieResponse(searchResponse.getHits(), searchResponse.getTook());
        } catch (Exception e) {
            String errorMessage = String.format("An exception occurred while searching for title '%s'. %s", title, e.getMessage());
            log.error(errorMessage);
            return createSearchMovieResponseError(errorMessage);
        }
    }

    @Override
    public String saveMovie(Map<String, Object> source) {
        try {
            handlePoster(source);
            IndexRequest indexRequest = new IndexRequest(awsProperties.getOpensearch().getIndexes())
                    .source(source, XContentType.JSON)
                    .id(String.valueOf(source.get("imdb")));
            IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            log.info("Document for '{}' {} successfully in ES!", source, indexResponse.getResult());
            return indexResponse.getId();
        } catch (Exception e) {
            String errorMessage = String.format("An exception occurred while indexing '%s'. %s", source, e.getMessage());
            log.error(errorMessage);
            throw new MovieServiceException(errorMessage, e);
        }
    }

    private void handlePoster(Map<String, Object> source) throws MalformedURLException {
        String poster = posterService.getPosterNotAvailableUrl();
        if (source.containsKey("poster")) {
            poster = String.valueOf(source.get("poster"));
        } else if (source.containsKey("posterUrl")) {
            Object posterUrlObj = source.get("posterUrl");
            if ((posterUrlObj instanceof URL || posterUrlObj instanceof String)) {
                URL posterUrl = posterUrlObj instanceof URL ?
                        (URL) posterUrlObj : validateAndGetUrl((String) posterUrlObj);
                if (posterUrl != null) {
                    String imdb = String.valueOf(source.get("imdb"));
                    Optional<String> filePathOptional = posterService.downloadFile(posterUrl, imdb);
                    poster = filePathOptional.isPresent() ?
                            posterService.uploadFile(new File(filePathOptional.get())) : posterService.getPosterNotAvailableUrl();
                    source.remove("posterUrl");
                }
            }
        }
        source.put("poster", poster);
    }

    private URL validateAndGetUrl(String url) {
        if (StringUtils.hasText(url)) {
            try {
                return new URL(url);
            } catch (Exception e) {
            }
        }
        return null;
    }

    private SearchMovieResponse toSearchMovieResponse(SearchHits searchHits, TimeValue took) {
        SearchMovieResponse searchMovieResponse = new SearchMovieResponse();
        List<SearchMovieResponse.Hit> hits = new ArrayList<>();
        for (SearchHit searchHit : searchHits.getHits()) {
            SearchMovieResponse.Hit hit = new SearchMovieResponse.Hit();
            hit.setIndex(searchHit.getIndex());
            hit.setId(searchHit.getId());
            hit.setScore(searchHit.getScore());
            hit.setSource(searchHit.getSourceAsMap());
            hits.add(hit);
        }
        searchMovieResponse.setHits(hits);
        searchMovieResponse.setTook(String.valueOf(took));
        return searchMovieResponse;
    }

    private SearchMovieResponse createSearchMovieResponseError(String errorMessage) {
        SearchMovieResponse searchMovieResponse = new SearchMovieResponse();
        searchMovieResponse.setError(new SearchMovieResponse.Error(errorMessage));
        return searchMovieResponse;
    }
}
