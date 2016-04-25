package ru.bmstu.wundermusik.models.parsers;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import ru.bmstu.wundermusik.api.soundcloud.utils.Routes;
import ru.bmstu.wundermusik.models.Singer;
import ru.bmstu.wundermusik.models.Track;

/***
 * Реализация парсера представления песни
 * @author ali
 */
public class TrackJsonParser extends SoundCloudJsonParser<Track> {
    private JsonParser<Singer> singerJsonParser = new SingerJsonParser();

    @Override
    public Track parseSingleObjectInternal(JSONObject dataJsonObj) {
        Track res = null;
        try {
            int trackId = dataJsonObj.getInt("id");
            String trackTitle = dataJsonObj.getString("title");
            Singer singer = singerJsonParser.parseSingleObjectInternal(dataJsonObj.getJSONObject("user"));
            if (dataJsonObj.getBoolean("streamable")){
                int contentSize = dataJsonObj.getInt("original_content_size");
                int trackDuration = dataJsonObj.getInt("duration");
                String trackFormat = dataJsonObj.getString("original_format");
                String streamUrl = dataJsonObj.getString("stream_url");
                streamUrl = Uri.parse(streamUrl).buildUpon().appendQueryParameter("client_id", Routes.CLIENT_ID).build().toString();
                String imageUrl = dataJsonObj.getString("artwork_url");
                if (imageUrl.equals("null"))
                    imageUrl = null;
                if (imageUrl != null)
                    imageUrl = imageUrl.replaceAll("large.jpg", "t500x500.jpg");
                res = new Track(trackId, trackTitle, trackDuration, trackFormat, contentSize, streamUrl, singer, imageUrl);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }
}
