package ru.bmstu.wundermusik.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ali on 29.03.16.
 */

/***
 * Модель исполнителя (музыканта)
 * @author ali
 */
public class Singer implements Serializable {
    private long id;
    private String name;
    private String avatarUrl;

    /***
     * Конструктор песни
     * @param id уникальный идентификатор исполнителя
     * @param name имя исполнителя
     * @param avatarUrl url его аватара
     */
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

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
