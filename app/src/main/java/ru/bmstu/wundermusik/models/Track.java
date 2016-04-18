package ru.bmstu.wundermusik.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    public String getStreamUrl() {

        return streamUrl;
    }

    public static Track parseSingleTrackInternal(JSONObject dataJsonObj){
        Track res = new Track();
        try {
            res.id = dataJsonObj.getInt("id");
            res.title = dataJsonObj.getString("title");
            res.singer = Singer.parseSingleSingerInternal(dataJsonObj.getJSONObject("user"));
            if (dataJsonObj.getBoolean("streamable")){
                res.contentSize = dataJsonObj.getInt("original_content_size");
                res.duration = dataJsonObj.getInt("duration");
                res.format = dataJsonObj.getString("original_format");
                res.streamUrl = dataJsonObj.getString("stream_url");
            } else
                res = null;
        } catch (JSONException e) {
            res = null;
            e.printStackTrace();

        }
        return null;
    }

    public static Track parseSingleTrack(String strJson){
        try {
            JSONObject dataJsonObj = new JSONObject(strJson);
            return parseSingleTrackInternal(dataJsonObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Track> parseMultipleTracks(String strJson){
        List<Track> resultList = new ArrayList<>();
        try {
            JSONArray dataJsonObj = new JSONArray(strJson);
            for (int i=0; i<dataJsonObj.length(); i++) {
                Track track = parseSingleTrackInternal(dataJsonObj.getJSONObject(i));
                if (track != null)
                    resultList.add(track);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return resultList;
        }
    }
}
