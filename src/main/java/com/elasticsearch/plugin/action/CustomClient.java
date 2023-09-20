package com.elasticsearch.plugin.action;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.PlainActionFuture;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.DeprecationHandler;
import org.elasticsearch.common.xcontent.NamedXContentRegistry;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.threadpool.ThreadPool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;

public class CustomClient extends NodeClient {
    private String host;
    private int port;
    private String username;
    private String password;

    public CustomClient(Settings settings, ThreadPool threadPool, String host, int port, String username, String password) {
        super(settings, threadPool);
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    @Override
    public ActionFuture<SearchResponse> search(SearchRequest request) {
        PlainActionFuture<SearchResponse> result = new PlainActionFuture<>();
        HttpURLConnection co = null;
        BufferedReader reader = null;
        try {
            String index = request.indices()[0];
            co = (HttpURLConnection) new URL(String.format("%s:%d/%s/_search", host, port, index)).openConnection();
            co.setRequestMethod("GET");
            co.setRequestProperty("Content-Type", "application/json");
            if (username != null && !"".equals(username)) {
                String token = new String(Base64.getEncoder().encode(String.format("%s:%s", username, password).getBytes(StandardCharsets.UTF_8))).replace("==", "");
                co.setRequestProperty("Authorization", "Basic " + token);
            }
            co.setDoOutput(true);
            co.setDoInput(true);
            co.getOutputStream().write(request.source().toString().getBytes(StandardCharsets.UTF_8));
            co.getOutputStream().flush();

            reader = new BufferedReader(new InputStreamReader(co.getInputStream()));
            StringBuilder sb = new StringBuilder();
            for (String rl; (rl = reader.readLine()) != null;) {
                sb.append(rl);
            }
            NamedXContentRegistry registry = new NamedXContentRegistry(new ArrayList<>());
            XContentParser parser = JsonXContent.jsonXContent.createParser(registry, DeprecationHandler.IGNORE_DEPRECATIONS, sb.toString());
            result.onResponse(SearchResponse.fromXContent(parser));
        } catch (IOException e) {
            logger.warn("", e);
            result.onFailure(e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception ignore){}
            if (co != null) {
                co.disconnect();
            }
        }
        return result;
    }
}
