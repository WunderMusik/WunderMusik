package ru.bmstu.wundermusik.api.soundcloud.query;

import android.net.Uri;
import android.os.ResultReceiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import ru.bmstu.wundermusik.api.soundcloud.utils.HttpMethod;
import ru.bmstu.wundermusik.api.soundcloud.utils.Routes;
import ru.bmstu.wundermusik.api.soundcloud.utils.StatusCode;

/**
 * Запрос данных о треке. Формирует запрос как запрос к ресурсу
 * @author max
 */
public class TrackQuery extends ResourceQuery {
    public static final String trackUri = Routes.BASE_API_URI + Routes.TRACK;

    public static void execute(ResultReceiver callback, long trackId) {
        String route = Uri.parse(trackUri).buildUpon().path(Long.toString(trackId)).build().toString();
        ResourceQuery.execute(callback, route);
    }

    public static void execute(ResultReceiver callback, String trackName) {
        String route = Uri.parse(trackUri).buildUpon().appendQueryParameter("q", trackName).build().toString();
        ResourceQuery.execute(callback, route);
    }
}
