package ru.bmstu.wundermusik.api.soundcloud.query;

import android.os.Bundle;
import android.os.ResultReceiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import ru.bmstu.wundermusik.api.soundcloud.utils.HttpMethod;
import ru.bmstu.wundermusik.api.soundcloud.utils.StatusCode;

/**
 * Created by max on 22.03.16.
 */
public class ResourceQuery extends AbstractQuery {

    public static void execute(ResultReceiver callback, String uri) {
        try {
            HttpURLConnection conn = openConnectionByURI(uri, HttpMethod.GET);
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                String status = conn.getResponseMessage();
                callback.send(StatusCode.ERROR, errorBundle(status));
                return;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            try {
                String response = readResponse(reader);
                callback.send(StatusCode.OK, successBundle(response));
            }
            finally {
                conn.disconnect();
            }
        }
        catch (IOException e) {
            callback.send(StatusCode.ERROR, errorBundle(e.toString()));
        }
    }
}
