package com.elasticsearch.plugin;

import com.elasticsearch.plugin.action.TodoPluginAction;
import com.elasticsearch.plugin.filter.TodoPluginFilter;
import com.elasticsearch.plugin.tokenizer.TodoTokenizer;
import com.elasticsearch.plugin.util.DictInfo;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.elasticsearch.cluster.metadata.IndexNameExpressionResolver;
import org.elasticsearch.cluster.node.DiscoveryNodes;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.ClusterSettings;
import org.elasticsearch.common.settings.IndexScopedSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.SettingsFilter;
import org.elasticsearch.index.analysis.AnalyzerProvider;
import org.elasticsearch.index.analysis.TokenFilterFactory;
import org.elasticsearch.index.analysis.TokenizerFactory;
import org.elasticsearch.indices.analysis.AnalysisModule;
import org.elasticsearch.ingest.Processor;
import org.elasticsearch.plugins.ActionPlugin;
import org.elasticsearch.plugins.AnalysisPlugin;
import org.elasticsearch.plugins.IngestPlugin;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestHandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static java.util.Collections.singletonMap;

public class TodoPlugin extends Plugin implements AnalysisPlugin, ActionPlugin, IngestPlugin {

    private static Logger logger = Loggers.getLogger(TodoPlugin.class, "");
    // 사전
    public static Map<String, DictInfo> dictMap = new HashMap<>();
    
    public TodoPlugin() {
        logger.info("TODO CUSTOM PLUGIN!!");
    }

    /**
     * 토크나이저 등록
     * 분석기를 구성할 떄 하나만 사용할 수 있으며 텍스트를 어떻게 나눌지 정의함.
     */
    @Override
    public Map<String, AnalysisModule.AnalysisProvider<TokenizerFactory>> getTokenizers() {
        return singletonMap("custom", TodoTokenizer::new);
    }

    /**
     * 필터 등록
     * 토큰화된 단어를 하나씩 필터링해서 사용자가 원하는 토큰으로 반환.
     */
    @Override
    public Map<String, AnalysisModule.AnalysisProvider<TokenFilterFactory>> getTokenFilters() {
        Map<String, AnalysisModule.AnalysisProvider<TokenFilterFactory>> extra = new HashMap<>();
        extra.put("custom", TodoPluginFilter::new);
        return extra;
    }

    /**
     * 분석기 등록
     */
    @Override
    public Map<String, AnalysisModule.AnalysisProvider<AnalyzerProvider<? extends Analyzer>>> getAnalyzers() {
        return AnalysisPlugin.super.getAnalyzers();
    }

    /**
     * 전처리 등록
     */
    @Override
    public Map<String, Processor.Factory> getProcessors(Processor.Parameters parameters) {
        return IngestPlugin.super.getProcessors(parameters);
    }

    /**
     * action 등록
     */
    @Override
    public List<RestHandler> getRestHandlers(final Settings settings,
                                             final RestController restController,
                                             final ClusterSettings clusterSettings,
                                             final IndexScopedSettings indexScopedSettings,
                                             final SettingsFilter settingsFilter,
                                             final IndexNameExpressionResolver indexNameExpressionResolver,
                                             final Supplier<DiscoveryNodes> nodesInCluster) {
        return Collections.singletonList(new TodoPluginAction());
    }
}