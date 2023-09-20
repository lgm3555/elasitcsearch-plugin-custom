package com.elasticsearch.plugin.action;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.BaseRestHandler;
import org.elasticsearch.rest.BytesRestResponse;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.elasticsearch.rest.RestRequest.Method.GET;
import static org.elasticsearch.rest.RestStatus.OK;

public class TodoPluginAction extends BaseRestHandler {

    private static final String TEST_ACTION = "_nocode";
    private static final String BASE_URI = "_custom";
    private static final String TRAN_SEARCH_ACTION = "_tran";
    private static final String INDEX_JOIN_ACTION = "_join";

    @Override
    public List<Route> routes() {
        return Collections.unmodifiableList(Arrays.asList(
                new Route(GET, BASE_URI + "/{action}")));
    }

    @Override
    public String getName() {
        return "custom-analysis";
    }

    @Override
    protected RestChannelConsumer prepareRequest(final RestRequest request, final NodeClient client) throws IOException {
        final String action = request.param("action");

        XContentBuilder builder = JsonXContent.contentBuilder();

        if (TEST_ACTION.equals(action)) {
            builder.startObject();
            builder.field("description", "Elasticsearch-todo-plugin-custom response\n: " + new Date().toString());
            builder.endObject();
        } else if (TRAN_SEARCH_ACTION.equals(action)) { //영한 변경
            final String index = request.param("index");

        } else if (INDEX_JOIN_ACTION.equals(action)) { //left join
            final String index = request.param("index");

        }

        return channel -> channel.sendResponse(new BytesRestResponse(OK, builder));
    }

    private void joinAction(RestRequest request, NodeClient client, String index) {
        JSONObject requestBody = new JSONObject(new JSONTokener(request.content().utf8ToString()));

        String host = requestBody.optString("host", null);
        int port = requestBody.optInt("port", 9200);
        String username = requestBody.optString("username", null);
        String password = requestBody.optString("password", null);
        String joinIndex = requestBody.optString("joinIndex", null);
        String query = requestBody.optString("query");

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(index).source(SearchSourceBuilder.searchSource().query(QueryBuilders.wrapperQuery(query)));

        CustomClient customClient = new CustomClient(client.settings(), client.threadPool(), host, port, username, password);

        SearchResponse searchResponse = customClient.search(searchRequest).actionGet();
        for(SearchHit hit : searchResponse.getHits().getHits()){
            System.out.println("hit = " + hit);
        }
    }
}