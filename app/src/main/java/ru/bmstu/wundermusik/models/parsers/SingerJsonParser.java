package ru.bmstu.wundermusik.models.parsers;

import org.json.JSONException;
import org.json.JSONObject;

import ru.bmstu.wundermusik.models.Singer;

/**
 * Created by ali on 18.04.16.
 */
public class SingerJsonParser extends SoundCloudJsonParser<Singer> {
    @Override
    public Singer parseSingleObjectInternal(JSONObject dataJsonObj) {
        Singer res = null;
        try {
            res = new Singer(dataJsonObj.getInt("id"),
                    dataJsonObj.getString("username"),
                    dataJsonObj.getString("avatar_url")
            );
        } catch (JSONException e) {
            res = null;
            e.printStackTrace();
        }
        return res;
    }
}
