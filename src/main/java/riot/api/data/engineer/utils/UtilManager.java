package riot.api.data.engineer.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UtilManager {

    public JsonObject StringToJsonObject(String response){
        Gson gson = new Gson();
        return gson.fromJson(response,JsonObject.class);
    }

    public JsonArray StringToJsonArray(String response){
        Gson gson = new Gson();
        return gson.fromJson(response,JsonArray.class);
    }
    public String getStringConcat(String a, String b){
        return new StringBuilder().append(a).append(b).toString();
    }

    public static Map convertObjectToMap(Object obj){
        Map map = new HashMap();
        Field[] fields = obj.getClass().getDeclaredFields();
        for(int i=0; i <fields.length; i++){
            fields[i].setAccessible(true);
            try{
                map.put((fields[i].getName()), fields[i].get(obj));
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return map;
    }

    public static Object convertMapToObject(Map<String,Object> map, Object obj){
        String keyAttribute = null;
        String setMethodString = "set";
        String methodString = null;
        Iterator itr = map.keySet().iterator();

        while(itr.hasNext()){
            keyAttribute = (String) itr.next();
            methodString = setMethodString+keyAttribute.substring(0,1).toUpperCase()+keyAttribute.substring(1);
            Method[] methods = obj.getClass().getDeclaredMethods();
            for(int i=0;i<methods.length;i++){
                if(methodString.equals(methods[i].getName())){
                    try{
                        methods[i].invoke(obj, map.get(keyAttribute));
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
        return obj;
    }
}
