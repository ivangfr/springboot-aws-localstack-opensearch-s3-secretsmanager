package com.ivanfranchin.movieui.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.List;
import java.util.Map;

@JsonInclude(Include.NON_NULL)
public record SearchResponse(List<Hit> hits, Error error) {

    public SearchResponse() {
        this(null, null);
    }

    public record Hit(String index, String id, Float score, Map<String, Object> source) {
    }

    public record Error(String message) {
    }
}
