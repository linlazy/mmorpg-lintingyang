package com.linlazy.mmorpg.server.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author linlazy
 */
public class ConfigFile {

    private JSONArray jsonArray;

    public  ConfigFile(String fileName, String encoding) {

        InputStreamReader inputStreamReader = null;
        try {
            InputStream inputStream = getClassLoader().getResourceAsStream(fileName);
            if (inputStream == null) {
                throw new IllegalArgumentException("Properties file not found in classpath: " + fileName);
            }
            StringBuilder stringBuilder = new StringBuilder();
             inputStreamReader = new InputStreamReader(inputStream, encoding);
            char[] buffer = new char[1024];
            int count;
            while ((count = inputStreamReader.read(buffer)) >= 0) {
                stringBuilder.append(buffer,0,count);
            }
            jsonArray = JSONObject.parseArray(stringBuilder.toString());
        } catch (IOException e) {
            throw new RuntimeException("Error loading properties file.", e);
        }
        finally {
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private ClassLoader getClassLoader() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader!= null ? classLoader : getClass().getClassLoader();
    }

    public JSONArray getJsonArray() {
        return jsonArray;
    }
}
