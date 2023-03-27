package com.elasticsearch.plugin.util;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.PlainActionFuture;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.threadpool.ThreadPool;

public class CustomClient extends NodeClient {
    private String index;
    private String joinIndex;
    private String host;
    private int port;
    private String username;
    private String password;

    public CustomClient(Settings settings, ThreadPool threadPool, String index, String joinIndex, String host, int port, String username, String password) {
        super(settings, threadPool);
        this.index = index;
        this.joinIndex = joinIndex;
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    /*@Override
    public ActionFuture<SearchResponse> search(SearchRequest request) {
        PlainActionFuture<SearchResponse> result = new PlainActionFuture<>();

        return result;
    }*/
}
