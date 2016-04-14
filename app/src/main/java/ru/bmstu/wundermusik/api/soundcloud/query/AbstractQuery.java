package ru.bmstu.wundermusik.api.soundcloud.query;

import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.bmstu.wundermusik.api.soundcloud.utils.Routes;

/**
 * Created by max on 22.03.16.
 */
public abstract class AbstractQuery {

    public static final String KEY_DATA = "key_data";
    public static final String KEY_ERROR = "key_error";

    public static HttpURLConnection openConnection(String route, String method)
            throws IOException
    {
        return openConnectionByURI(Routes.BASE_API_URI + route, method);
    }

    public static HttpURLConnection openConnectionByURI(String uri, String method)
            throws IOException
    {
        uri += ("?client_id=" + Routes.CLIENT_ID);
        URL url = new URL(uri);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setConnectTimeout(15 * 1000);
        conn.setReadTimeout(15 * 1000);
        conn.connect();
        return conn;
    }

    public static String readResponse(BufferedReader reader)
            throws IOException
    {
        StringBuilder buf = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            buf.append(line);
        }
        return buf.toString();
    }

    public static Bundle successBundle(String data) {
        Bundle result = new Bundle();
        result.putString(KEY_DATA, data);
        return result;
    }

    public static Bundle errorBundle(String error) {
        Bundle errorData = new Bundle();
        errorData.putString(KEY_ERROR, error);
        return errorData;
    }
}
