package com.elasticsearch.plugin;

import com.elasticsearch.plugin.util.CustomClient;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.rest.BaseRestHandler;
import org.elasticsearch.rest.BytesRestResponse;
import org.elasticsearch.rest.RestRequest;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.elasticsearch.rest.RestRequest.Method.GET;
import static org.elasticsearch.rest.RestRequest.Method.POST;
import static org.elasticsearch.rest.RestStatus.OK;

public class TodoPluginAction extends BaseRestHandler {

    private static final String TEST_ACTION = "_nocode";
    private static final String TEST_SEARCH_ACTION = "_test-search";
    private static final String TRAN_SEARCH_ACTION = "_tran";
    private static final String INDEX_JOIN_ACTION = "_join";

    @Override
    public List<Route> routes() {
        return Collections.unmodifiableList(Arrays.asList(
                new Route(GET, "/{action}"),
                new Route(POST, "/{index}/{action}")));
    }

    @Override
    public String getName() {
        return "todo-plugin";
    }

    @Override
    protected RestChannelConsumer prepareRequest(final RestRequest request, final NodeClient client) throws IOException {
        final String action = request.param("action");

        XContentBuilder builder = JsonXContent.contentBuilder();

        if (TEST_ACTION.equals(action)) {
            builder.startObject();
            builder.field("description", "Elasticsearch-todo-plugin-custom response\n: " + new Date().toString());
            builder.endObject();
        } else if (TEST_SEARCH_ACTION.equals(action)) {

        } else if (TRAN_SEARCH_ACTION.equals(action)) { //영한 변경
            final String index = request.param("index");

        } else if (INDEX_JOIN_ACTION.equals(action)) { //left join
            final String index = request.param("index");

            joinAction(request, client, index);
        }

        return channel -> channel.sendResponse(new BytesRestResponse(OK, builder));
    }

    private void joinAction(RestRequest request, NodeClient client, String index) {
        /*
        POST prod/_join
        {
            "targetIndex": "prod_detail",
            "query": {
                "match": {
                    "prodCode": "1511111"
                }
            }
        }
        */
        JSONObject requestBody = new JSONObject(new JSONTokener(request.content().utf8ToString()));

        String host = requestBody.optString("host", null);
        int port = requestBody.optInt("port", 9200);
        String username = requestBody.optString("username", null);
        String password = requestBody.optString("password", null);
        String joinIndex = requestBody.optString("joinIndex", null);

        CustomClient customClient = new CustomClient(client.settings(), client.threadPool(), index, joinIndex, host, port, username, password);

        //customClient.search()
    }
}