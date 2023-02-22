package cn.geterror.serialization.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.LinkedHashMap;

public class JsonUtils {

    private final static ObjectMapper mapper = new ObjectMapper();

    public static ObjectMapper getMapper() {
        return mapper;
    }

    static {
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);//对象转换为json时，输出所有字段，不管是否为空。
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);//json解析为对象时，属性不存在时不报错。
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);//解析json时，允许不带引号的字段名。
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);//解析json时，允许但引号。
        mapper.configure(JsonParser.Feature.ALLOW_MISSING_VALUES, true);//解析json时，数组中允许数据缺失，解析为null。
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);//解析json时，允许注释
    }

    public static String toJSONString(Object obj) {
        return toJSONString(obj, false);
    }

    public static String toJSONString(Object obj, boolean pretty) {
        return toJSONString(obj, "{}", pretty);
    }

    public static String toJSONString(Object obj, String defaultStrIfNull) {
        return toJSONString(obj, defaultStrIfNull, false);
    }

    public static String toJSONString(Object obj, String defaultStrIfNull, boolean pretty) {
        if (null == obj) {
            return defaultStrIfNull;
        }
        try {
            if (pretty) {
                return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            }
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return defaultStrIfNull;
        }
    }

    public static <T> T toObject(String jsonStr, Class<T> cls) {
        return toObject(jsonStr, cls, null);
    }

    public static <T> T toObject(String jsonStr, Class<T> cls, T defaultValue) {
        if (StringUtils.isBlank(jsonStr)) {
            return defaultValue;
        }

        try {
            return mapper.readValue(jsonStr, cls);
        } catch (IOException e) {
            return defaultValue;
        }
    }

    public static LinkedHashMap<String, Object> toMap(String jsonStr) {
        if (StringUtils.isBlank(jsonStr)) {
            return new LinkedHashMap<>(0);
        }

        try {
            return mapper.readValue(jsonStr, LinkedHashMap.class);
        } catch (IOException e) {
            throw new RuntimeException(String.format("json转换map异常, json=%s", jsonStr), e);
        }
    }

    public static JsonNode toJsonNode(String jsonStr) {
        try {
            return mapper.readTree(jsonStr);
        } catch (IOException e) {
            return null;
        }
    }

    public static boolean isJson(String jsonStr) {
        if (StringUtils.isBlank(jsonStr)) {
            return false;
        }
        try {
            mapper.readTree(jsonStr);
        } catch (IOException e) {
            // ignore
            return false;
        }
        return true;
    }

    public static <T> T fromJSON(String jsonString, TypeReference<T> typeReference) {
        if(StringUtils.isEmpty(jsonString))
            return null;
        try {
            return mapper.readValue(jsonString, typeReference);
        } catch (Exception e) {
            return null;
        }
    }
    public static boolean compareJson(String json1,String json2){
        if(StringUtils.equals(json1,json2)){
            return true;
        }
        JsonNode node1 = toJsonNode(json1);
        JsonNode node2 = toJsonNode(json2);
        return compareJson(node1,node2);


    }


    public static boolean compareJson(JsonNode node1,JsonNode node2){
        if (node1 == null || node2 == null) {
            return node1 == node2;
        }
        if (node1.isMissingNode() || node2.isMissingNode()) {
            return false;
        }

        return node1.equals(JsonUtils::CustomJsonComparator, node2);
    }

    private static int CustomJsonComparator(JsonNode node1, JsonNode node2) {
        if(node1.equals(node2)){
            return 0;
        }
        if (node1.isNumber() && node2.isNumber()) {

            Double d1 = node1.asDouble(Double.NaN);
            Double d2 = node2.asDouble(Double.NaN);

            return d1.compareTo(d2);
        }
        return node1.hashCode() - node2.hashCode();
    }
}
