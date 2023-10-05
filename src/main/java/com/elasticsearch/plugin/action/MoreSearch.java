package com.elasticsearch.plugin.action;

import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.json.JSONObject;
import org.json.JSONTokener;

public class MoreSearch {

    private static Logger logger = Loggers.getLogger(MoreSearch.class, "");


    public void moreAction(RestRequest request, NodeClient client, XContentBuilder writer) {
        JSONObject requestBody = new JSONObject(new JSONTokener(request.content().utf8ToString()));

        String index = requestBody.optString("index");
        String query = requestBody.optString("query", null);

        logger.info(query);


        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(index).source(SearchSourceBuilder.searchSource().query(QueryBuilders.wrapperQuery(query)));

        ActionFuture<SearchResponse> search = client.search(searchRequest);

        SearchResponse response = search.actionGet();

        for (SearchHit hit : response.getHits().getHits()){

        }
    }
}
