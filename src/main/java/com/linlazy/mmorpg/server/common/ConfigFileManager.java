package com.linlazy.mmorpg.server.common;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author linlazy
 */
public class ConfigFileManager {

    private static ConfigFile prop = null;

    private static final ConcurrentHashMap<String, ConfigFile> MAP = new ConcurrentHashMap<>();
    private static final String DEFAULT_ENCODE = "UTF-8";

    public static ConfigFile use(String fileName) {
        ConfigFile result = MAP.get(fileName);
        if (result == null) {
            synchronized (ConfigFileManager.class) {
                result = MAP.get(fileName);
                if (result == null) {
                    result = new ConfigFile(fileName,DEFAULT_ENCODE);
                    MAP.put(fileName, result);
                    if (ConfigFileManager.prop == null) {
                        ConfigFileManager.prop = result;
                    }
                }
            }
        }
        return result;
    }

    public static ConfigFile getConfigFile() {
        if (prop == null) {
            throw new IllegalStateException("Load propties file by invoking PropKit.use(String fileName) method first.");
        }
        return prop;
    }

    public static ConfigFile getConfigFile(String fileName) {
        return MAP.get(fileName);
    }
}
