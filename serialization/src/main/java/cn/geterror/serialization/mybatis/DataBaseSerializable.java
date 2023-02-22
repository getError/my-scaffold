package cn.geterror.serialization.mybatis;

import cn.geterror.serialization.json.JsonUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataBaseSerializable {

    public String serialize(){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("class",this.getClass().getName());
        map.put("data",this);
        return JsonUtils.toJSONString(map);
    }

    public static DataBaseSerializable deserialization(String s){
        if(StringUtils.isEmpty(s)){
            return null;
        }
        if(!JsonUtils.isJson(s)){
            throw new RuntimeException("not json");
        }
        Map<String, Object> map = JsonUtils.toMap(s);
        Class clazz;
        try {
            clazz = Class.forName(map.get("class").toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return (DataBaseSerializable) JsonUtils.toObject(JsonUtils.toJSONString(map.get("data")),clazz);
    }

    public static List<Object> listDeserialization(String s){
        if(StringUtils.isEmpty(s)){
            return new ArrayList<>();
        }
        if(!JsonUtils.isJson(s)){
            throw new RuntimeException("not json");
        }
        Map<String, Object> map = JsonUtils.toMap(s);
        Class clazz = null;
        try {
            clazz = Class.forName(map.get("class").toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return JsonUtils.toObject(JsonUtils.toJSONString(map.get("data")),List.class);
    }

}
