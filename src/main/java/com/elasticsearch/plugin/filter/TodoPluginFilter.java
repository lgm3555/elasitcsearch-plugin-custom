package com.elasticsearch.plugin.filter;

import com.elasticsearch.plugin.TodoPlugin;
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
    }

    @Override
    public TokenStream create(TokenStream tokenStream) {
        return new CustomFilter(tokenStream);
    }
}
