package ru.bmstu.wundermusik.api.soundcloud.query;

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
        URL url = new URL(uri);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setReadTimeout(10000);

        conn.addRequestProperty("client_id", Routes.CLIENT_ID);
        conn.addRequestProperty("client_secret", Routes.CLIENT_SECRET);

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
}
