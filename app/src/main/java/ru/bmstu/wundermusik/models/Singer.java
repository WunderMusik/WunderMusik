package ru.bmstu.wundermusik.models;

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
    private List<Track> trackList = new ArrayList<>();

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

    public List<Track> getTrackList() {
        return trackList;
    }
}
