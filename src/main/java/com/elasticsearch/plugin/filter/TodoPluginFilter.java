package com.elasticsearch.plugin.filter;

import com.elasticsearch.plugin.TodoPlugin;
import com.elasticsearch.plugin.util.BaseDictInfo;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.TokenStream;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.analysis.AbstractTokenFilterFactory;

public class TodoPluginFilter extends AbstractTokenFilterFactory {

    private static Logger logger = Loggers.getLogger(TodoPluginFilter.class, "");

    public TodoPluginFilter(IndexSettings indexSettings, Environment environment, String name, Settings settings) {
        super(indexSettings, name, settings);

        if (!TodoPlugin.dictMap.containsKey(BaseDictInfo.BASE_DICTIONARY)) {
            TodoPlugin.dictMap = BaseDictInfo.loadDictionary(environment);
        }
    }

    @Override
    public TokenStream create(TokenStream tokenStream) {
        return new CustomFilter(tokenStream);
    }
}
