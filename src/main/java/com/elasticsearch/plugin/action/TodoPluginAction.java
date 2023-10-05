package com.elasticsearch.plugin.action;

import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.rest.BaseRestHandler;
import org.elasticsearch.rest.BytesRestResponse;
import org.elasticsearch.rest.RestRequest;

import java.io.IOException;
import java.util.*;

import static org.elasticsearch.rest.RestRequest.Method.GET;
import static org.elasticsearch.rest.RestRequest.Method.POST;
import static org.elasticsearch.rest.RestStatus.OK;

public class TodoPluginAction extends BaseRestHandler {

    private static Logger logger = Loggers.getLogger(TodoPluginAction.class, "");

    private static final String BASE_URI = "custom";
    private static final String TEST_ACTION = "_nocode";
    private static final String MORE_ACTION = "_more";

    @Override
    public List<Route> routes() {
        return Collections.unmodifiableList(Arrays.asList(
                new Route(GET, BASE_URI + "/{action}"),
                new Route(POST, BASE_URI + "/{action}")));
    }

    @Override
    public String getName() {
        return "custom-analysis";
    }

    @Override
    protected RestChannelConsumer prepareRequest(final RestRequest request, final NodeClient client) throws IOException {
        final String action = request.param("action");
        final MoreSearch moreSearch = new MoreSearch();

        XContentBuilder builder = JsonXContent.contentBuilder();

        logger.info("action : " + action);

        if (TEST_ACTION.equals(action)) {
            builder.startObject();
            builder.field("description", "Elasticsearch-todo-plugin-custom response\n: " + new Date().toString());
            builder.endObject();
        } else if (MORE_ACTION.equals(action)) {
            builder.startObject();
            moreSearch.moreAction(request, client, builder);
            builder.endObject();
        }

        return channel -> channel.sendResponse(new BytesRestResponse(OK, builder));
    }
}