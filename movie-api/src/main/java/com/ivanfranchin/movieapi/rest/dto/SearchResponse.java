package com.ivanfranchin.movieapi.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.opensearch.search.SearchHit;
import org.opensearch.search.SearchHits;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JsonInclude(Include.NON_NULL)
public record SearchResponse(List<Hit> hits, Error error) {

    public SearchResponse(List<Hit> hits) {
        this(hits, null);
    }

    public SearchResponse(Error error) {
        this(null, error);
    }

    public record Hit(String index, String id, Float score, Map<String, Object> source) {
    }

    public record Error(String message) {
    }

    public static SearchResponse from(SearchHits searchHits) {
        List<SearchResponse.Hit> hits = new ArrayList<>();
        for (SearchHit searchHit : searchHits.getHits()) {
            hits.add(new SearchResponse.Hit(
                    searchHit.getIndex(),
                    searchHit.getId(),
                    searchHit.getScore(),
                    searchHit.getSourceAsMap()
            ));
        }
        return new SearchResponse(hits);
    }
}
