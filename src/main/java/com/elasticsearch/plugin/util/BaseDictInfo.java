package com.elasticsearch.plugin.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.SpecialPermission;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.env.Environment;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class BaseDictInfo {

    private static Logger logger = Loggers.getLogger(BaseDictInfo.class, "");

    public static final String BASE_DICTIONARY = "BASE_DICTIONARY";

    public static final String DICT_SYNONYM = "synonym"; // 동의어
    public static final String DICT_USER = "user"; //사용자
    public static final String DICT_STOP = "stop"; // 불용어

    private static final String BASE_DICT_PROPS = "base-dictionary.yml";

    public static enum Type {
        SET, MAP
    }

    private static File baseFile;
    private static File configFile;

    public static Map<String, DictInfo> loadDictionary(final Environment environment) {
        SpecialPermission.check();

        baseFile = environment.configFile().toFile();
        configFile = new File(baseFile, BASE_DICT_PROPS);

        if (configFile.exists()) {
            logger.info("DICTIONARY PROPERTIES : {}", configFile.getAbsolutePath());
        }

        Map<String, Map<String, String>> dictMap = readYmlConfig(configFile);

        for (Map.Entry<String, Map<String, String>> dictEntry : dictMap.entrySet()) {
            String key = dictEntry.getKey();
            Map<String, String> dictPropMap = dictEntry.getValue();

            DictInfo dictInfo = new DictInfo();
            dictInfo.setName(key);
            dictInfo.setType(dictPropMap.get("type"));
            dictInfo.setFilePath(dictPropMap.get("filePath"));
            dictInfo.setLabel(dictPropMap.get("label"));


        }

        return null;
    }

    private static Map<String, Map<String, String>> readYmlConfig(File configFile) {
        Map<String, Map<String, String>> map = new HashMap<>();
        try {
            // ObjectMapper를 설정하여 YAML 데이터를 읽을 수 있게 함
            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

            // YAML 파일을 읽고 Map으로 변환
            map = objectMapper.readValue(configFile, Map.class);
        } catch (Exception e) {
            logger.error("e: " + e);
        }

        return map;
    }

    private static Object readDictFile(String filePath, Type type) {
        File dictFile = new File(baseFile, filePath);

        return null;
    }
}
