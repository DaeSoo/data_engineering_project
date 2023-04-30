package riot.api.data.engineer.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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

}
