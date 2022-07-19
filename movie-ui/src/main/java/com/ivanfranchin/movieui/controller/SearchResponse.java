package com.ivanfranchin.movieui.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonInclude(Include.NON_NULL)
public class SearchResponse {

    private List<Hit> hits;
    private String took;
    private Error error;

    @Data
    public static class Hit {
        private String index;
        private String id;
        private Float score;
        private Map<String, Object> source;
    }

    @Data
    @AllArgsConstructor
    public static class Error {
        private String message;
    }
}
