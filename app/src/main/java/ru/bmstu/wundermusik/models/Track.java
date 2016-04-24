package ru.bmstu.wundermusik.models;

import java.io.Serializable;

/**
 * Created by ali on 29.03.16.
 */
public class Track implements Serializable {
    // track info
    private long id;
    private String title;
    private long duration; // as I understand this property is measured in seconds
    // track content info
    private String format;
    private long contentSize;
    private String streamUrl;
    private Singer singer;

    public Track(){}

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public long getDuration() {
        return duration;
    }

    public String getFormat() {
        return format;
    }

    public long getContentSize() {
        return contentSize;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public Singer getSinger() {
        return singer;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setContentSize(long contentSize) {
        this.contentSize = contentSize;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    public void setSinger(Singer singer) {
        this.singer = singer;
    }
}
