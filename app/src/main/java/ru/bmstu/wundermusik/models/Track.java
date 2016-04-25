package ru.bmstu.wundermusik.models;

import android.support.annotation.Nullable;

import java.io.Serializable;

/***
 * Модель трека
 * @author ali
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
    private Singer singer; // not null
    // track image info
    @Nullable
    private String trackImageUrl;

    /***
     * Конструктор трека
     * @param id уникальный идентификатор трека
     * @param title название трека
     * @param duration продолжительность песни
     * @param format формат хранения данных (mp3 for example)
     * @param contentSize размер файла песни, в формате указанном выше
     * @param streamUrl url для поточного проигрывания музыки
     * @param singer объект исполнителя песни
     * @param trackImageUrl url изображения песни
     */
    public Track(long id, String title, long duration, String format, long contentSize, String streamUrl, Singer singer, String trackImageUrl) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.format = format;
        this.contentSize = contentSize;
        this.streamUrl = streamUrl;
        this.singer = singer;
        this.trackImageUrl = trackImageUrl;
    }

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

    public String getTrackImageUrl() {
        if (trackImageUrl == null)
            return singer.getAvatarUrl();
        return trackImageUrl;
    }

    public void setTrackImageUrl(String trackImageUrl) {
        this.trackImageUrl = trackImageUrl;
    }
}
