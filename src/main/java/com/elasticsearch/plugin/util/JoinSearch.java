package com.elasticsearch.plugin.util;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.HashMap;
import java.util.Map;

public class JoinSearch {

    public static Map<String, Object> searchData(NodeClient client, String index) {
        Map<String, Object> result = new HashMap<>();

        try {
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices(index).source(SearchSourceBuilder.searchSource().query(QueryBuilders.matchAllQuery()).size(10000));
            SearchResponse searchResponse = client.search(searchRequest).actionGet();


        } catch (Exception e) {

        }

        return result;
    }
}
