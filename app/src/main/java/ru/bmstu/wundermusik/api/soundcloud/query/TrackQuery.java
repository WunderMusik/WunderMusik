package ru.bmstu.wundermusik.api.soundcloud.query;

import android.os.ResultReceiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import ru.bmstu.wundermusik.api.soundcloud.utils.HttpMethod;
import ru.bmstu.wundermusik.api.soundcloud.utils.Routes;
import ru.bmstu.wundermusik.api.soundcloud.utils.StatusCode;

/**
 * Created by max on 22.03.16.
 */
public class TrackQuery extends ResourceQuery {
    public static final String trackUri = Routes.BASE_API_URI + Routes.TRACK;

    public static void execute(ResultReceiver callback, long trackId) {
        String route = trackUri + "/" + Long.toString(trackId) + "?";
        ResourceQuery.execute(callback, route);
    }

    public static void execute(ResultReceiver callback, String trackName) {
        String route = trackUri + "?q=" + trackName + "&";
        ResourceQuery.execute(callback, route);
    }
}
