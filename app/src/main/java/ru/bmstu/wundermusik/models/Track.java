package ru.bmstu.wundermusik.models;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by ali on 29.03.16.
 */
public class Track implements Serializable {
    // track info
    private long id;
    private String title;
    private Calendar createdDate;
    private long duration; // as I understand this property is measured in seconds
    private String descrition;
    private Calendar releaseDate;
    private String genre;
    // track content info
    private String format;
    private long contentSize;
    private String streamUrl;
    private Singer singer;

    public Track(long id, String title, Calendar createdDate, long duration, String descrition, Calendar releaseDate, String genre, String format, long contentSize, String streamUrl, Singer singer) {
        this.id = id;
        this.title = title;
        this.createdDate = createdDate;
        this.duration = duration;
        this.descrition = descrition;
        this.releaseDate = releaseDate;
        this.genre = genre;
        this.format = format;
        this.contentSize = contentSize;
        this.streamUrl = streamUrl;
        this.singer = singer;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Calendar getCreatedDate() {
        return createdDate;
    }

    public long getDuration() {
        return duration;
    }

    public String getDescrition() {
        return descrition;
    }

    public Calendar getReleaseDate() {
        return releaseDate;
    }

    public String getGenre() {
        return genre;
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
}
