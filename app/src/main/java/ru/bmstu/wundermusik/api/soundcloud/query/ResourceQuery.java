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
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            try {
                String response = readResponse(reader);
                Bundle data = new Bundle();
                data.putString(KEY_DATA, response);
                callback.send(StatusCode.OK, data);
            }
            finally {
                conn.disconnect();
            }
        }
        catch (IOException e) {
            Bundle errorData = new Bundle();
            errorData.putString(KEY_ERROR, e.toString());
            callback.send(StatusCode.ERROR, errorData);
        }
    }
}
