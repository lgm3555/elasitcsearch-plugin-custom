package com.elasticsearch.plugin.util;

public class DictInfo {
    private String name;
    private String label;
    private String type;
    private String filePath;
    private Object dict;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Object getDict() {
        return dict;
    }

    public void setDict(Object dict) {
        this.dict = dict;
    }
}
