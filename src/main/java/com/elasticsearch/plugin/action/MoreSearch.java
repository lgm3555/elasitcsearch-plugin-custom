package com.elasticsearch.plugin.action;

import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Map;

/**
 * Scroll 검색
 */
public class MoreSearch {

    private static Logger logger = Loggers.getLogger(MoreSearch.class, "");

    public void moreAction(RestRequest request, NodeClient client, XContentBuilder builder) {
        try {
            // JSON 요청 본문 파싱
            JSONObject requestBody = new JSONObject(new JSONTokener(request.content().utf8ToString()));

            String index = requestBody.optString("index");
            String query = requestBody.optString("query", "");

            // 검색 요청 작성
            SearchRequest searchRequest = new SearchRequest(index);

            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(QueryBuilders.wrapperQuery(query));
            sourceBuilder.size(10000); // 한 번에 가져올 문서 수 설정
            searchRequest.source(sourceBuilder);

            // Scroll 설정
            searchRequest.scroll(TimeValue.timeValueMinutes(1)); // Scroll 유지 시간 설정

            // 첫 번째 Scroll 검색 요청 실행
            SearchResponse response = client.search(searchRequest).get();

            // Scroll ID 가져오기
            String scrollId = response.getScrollId();

            // 검색 결과 처리
            SearchHits hits = response.getHits();

            while (hits.getHits().length > 0) {
                for (SearchHit hit : hits.getHits()) {
                    Map<String, Object> sourceAsMap = hit.getSourceAsMap();

                    builder.field(String.valueOf(sourceAsMap.get("id")), sourceAsMap);
                }

                // 다음 Scroll 검색 요청 실행
                SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
                scrollRequest.scroll(TimeValue.timeValueMinutes(1)); // Scroll 유지 시간 설정
                response = client.searchScroll(scrollRequest).get();
                hits = response.getHits();
            }

            // Scroll 검색 완료 후 Scroll ID를 사용하여 Scroll 해제
            ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
            clearScrollRequest.addScrollId(scrollId);
            client.clearScroll(clearScrollRequest);
        } catch (Exception e) {
            logger.error("", e);
        }
    }
}