package com.ivanfranchin.movieapi.service;

import com.ivanfranchin.movieapi.exception.OpenSearchServiceException;
import com.ivanfranchin.movieapi.property.AwsProperties;
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
import org.opensearch.common.xcontent.XContentType;
import org.opensearch.index.query.QueryBuilders;
import org.opensearch.search.SearchHits;
import org.opensearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OpenSearchServiceImpl implements OpenSearchService {

    private final RestHighLevelClient restHighLevelClient;
    private final AwsProperties awsProperties;
    private final PosterService posterService;

    @Override
    public Map<String, Object> getMovie(String imdb) {
        try {
            GetRequest getRequest = new GetRequest(awsProperties.getOpensearch().getIndexes(), imdb);
            GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
            return getResponse.getSource();
        } catch (Exception e) {
            String errorMessage = String.format("An exception occurred while getting document with imdb '%s'. %s", imdb, e.getMessage());
            throw new OpenSearchServiceException(errorMessage, e);
        }
    }

    @Override
    public SearchHits searchMovies(String title) {
        try {
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.size(50).sort("createdAt");
            if (StringUtils.hasText(title)) {
                searchSourceBuilder.query(QueryBuilders.matchQuery("title", title));
            }
            SearchRequest searchRequest = new SearchRequest(awsProperties.getOpensearch().getIndexes())
                    .source(searchSourceBuilder);
            //log.info(searchRequest.source().toString());
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            return searchResponse.getHits();
        } catch (Exception e) {
            String errorMessage = String.format("An exception occurred while searching for title '%s'. %s", title, e.getMessage());
            throw new OpenSearchServiceException(errorMessage, e);
        }
    }

    @Override
    public Map<String, Object> saveMovie(Map<String, Object> movieMap) {
        try {
            handlePoster(movieMap);
            IndexRequest indexRequest = new IndexRequest(awsProperties.getOpensearch().getIndexes())
                    .source(movieMap, XContentType.JSON)
                    .id(String.valueOf(movieMap.get("imdb")));
            IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            log.info("Document for '{}' {} successfully in ES!", movieMap, indexResponse.getResult());
            // We could have set back to the movieMap the indexResponse.getId() value
            // However, the imdb is already the id so, we keep the same movieMap
            return movieMap;
        } catch (Exception e) {
            String errorMessage = String.format("An exception occurred while indexing '%s'. %s", movieMap, e.getMessage());
            throw new OpenSearchServiceException(errorMessage, e);
        }
    }

    private void handlePoster(Map<String, Object> movieMap) {
        String poster = posterService.getPosterNotAvailableUrl();
        if (movieMap.containsKey("poster") && movieMap.get("poster") != null) {
            poster = String.valueOf(movieMap.get("poster"));
        } else if (movieMap.containsKey("posterUrl")) {
            Object posterUrlObj = movieMap.get("posterUrl");
            if ((posterUrlObj instanceof URL || posterUrlObj instanceof String)) {
                URL posterUrl = posterUrlObj instanceof URL ?
                        (URL) posterUrlObj : validateAndGetUrl((String) posterUrlObj);
                if (posterUrl != null) {
                    String imdb = String.valueOf(movieMap.get("imdb"));
                    Optional<String> filePathOptional = posterService.downloadFile(posterUrl, imdb);
                    poster = filePathOptional.isPresent() ?
                            posterService.uploadFile(new File(filePathOptional.get())) : posterService.getPosterNotAvailableUrl();
                    movieMap.remove("posterUrl");
                }
            }
        }
        movieMap.put("poster", poster);
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
}
