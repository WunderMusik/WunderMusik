package ru.bmstu.wundermusik.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ali on 29.03.16.
 */
public class Singer implements Serializable {
    private long id;
    private String name;
    private String avatarUrl;

    public Singer(long id, String name, String avatarUrl) {
        this.id = id;
        this.name = name;
        this.avatarUrl = avatarUrl;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public static Singer parseSingleSingerInternal(JSONObject dataJsonObj){
        Singer res = null;
        try {
            res = new Singer(dataJsonObj.getInt("id"),
                             dataJsonObj.getString("avatar_url"),
                             dataJsonObj.getString("username"));
        } catch (JSONException e) {
            res = null;
            e.printStackTrace();
        }
        return res;
    }
}
