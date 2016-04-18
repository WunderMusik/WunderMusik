package ru.bmstu.wundermusik.models.parsers;

import org.json.JSONException;
import org.json.JSONObject;

import ru.bmstu.wundermusik.api.soundcloud.utils.Routes;
import ru.bmstu.wundermusik.models.Singer;
import ru.bmstu.wundermusik.models.Track;

/**
 * Created by ali on 18.04.16.
 */
public class TrackJsonParser extends SoundCloudJsonParser<Track> {
    private JsonParser<Singer> singerJsonParser = new SingerJsonParser();

    @Override
    public Track parseSingleObjectInternal(JSONObject dataJsonObj) {
        Track res = new Track();
        try {
            res.setId(dataJsonObj.getInt("id"));
            res.setTitle(dataJsonObj.getString("title"));
            res.setSinger(singerJsonParser.parseSingleObjectInternal(dataJsonObj.getJSONObject("user")));
            if (dataJsonObj.getBoolean("streamable")){
                res.setContentSize(dataJsonObj.getInt("original_content_size"));
                res.setDuration(dataJsonObj.getInt("duration"));
                res.setFormat(dataJsonObj.getString("original_format"));
                res.setStreamUrl(dataJsonObj.getString("stream_url") + "?client_id=" + Routes.CLIENT_SECRET);
            } else
                res = null;
        } catch (JSONException e) {
            res = null;
            e.printStackTrace();

        }
        return res;
    }
}
