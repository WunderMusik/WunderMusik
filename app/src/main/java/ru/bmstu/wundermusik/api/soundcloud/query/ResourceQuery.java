package ru.bmstu.wundermusik.api.soundcloud.query;

import android.os.ResultReceiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import ru.bmstu.wundermusik.api.soundcloud.utils.HttpMethod;
import ru.bmstu.wundermusik.api.soundcloud.utils.StatusCode;

/**
 * Запрос данных о ресурсе
 * @author max
 */
public class ResourceQuery extends AbstractQuery {

    /**
     * Исполнение HTTP запроса. Возвращает Bundle в поток клиента, через {@see Invoker}
     * @param callback обратный вызов для возвращения результата
     * @param uri адрес ресурса и get параметры
     */
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
