package com.education.shengnongcollege.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by wuweixiang on 17/2/21.
 */

public class GsonUtil {

    public static Gson gson=new Gson();
    public static Gson getCommonGson(){
        return gson;
    }

    public static final Gson keepNumbertypeGson = new GsonBuilder().
            registerTypeAdapter(Double.class, new JsonSerializer<Double>() {

                @Override
                public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
                    if (src == src.longValue())
                        return new JsonPrimitive(src.longValue());
                    if (src == src.intValue()) {
                        return new JsonPrimitive(src.intValue());
                    }
                    return new JsonPrimitive(src);
                }
            }).create();

    public static Gson getGson() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    /**
     * map的value是Object
     *
     * @return
     */
    public static Gson getGsonMapValueByObject() {
        return new GsonBuilder().registerTypeAdapter(
                new TypeToken<HashMap<String, Object>>() {
                }.getType(),
                new JsonDeserializer<HashMap<String, Object>>() {
                    @Override
                    public HashMap<String, Object> deserialize(
                            JsonElement json, Type typeOfT,
                            JsonDeserializationContext context) throws JsonParseException {

                        HashMap<String, Object> treeMap = new HashMap<>();
                        JsonObject jsonObject = json.getAsJsonObject();
                        Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
                        for (Map.Entry<String, JsonElement> entry : entrySet) {
                            treeMap.put(entry.getKey(), entry.getValue());
                        }
                        return treeMap;
                    }
                }).excludeFieldsWithoutExposeAnnotation().create();
    }

    /**
     * 将自定义的json对象转成JsonObject
     *
     * @param obj
     * @return
     */
    public static JsonObject toJsonObject(Object obj) {
        JsonObject reqData = new JsonParser().parse(GsonUtil.getGson().toJson(obj)).getAsJsonObject();
        return reqData;
    }
}
