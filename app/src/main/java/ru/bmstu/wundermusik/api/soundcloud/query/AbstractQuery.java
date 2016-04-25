package ru.bmstu.wundermusik.api.soundcloud.query;

import android.net.Uri;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.bmstu.wundermusik.api.soundcloud.utils.Routes;

/**
 * Базовый класс для запросов к API внешнего сервиса
 * @author max
 */
public abstract class AbstractQuery {

    /**
     * Ключи для формирования Bundle с результатом
     */
    public static final String KEY_DATA = "key_data";
    public static final String KEY_ERROR = "key_error";

    public static HttpURLConnection openConnection(String route, String method)
            throws IOException
    {
        return openConnectionByURI(Routes.BASE_API_URI + route, method);
    }

    /**
     * Открытие HTTP соединение и первичная настройка запроса
     * @param uri адрес запроса
     * @param method метод запроса
     * @return открытое соединение
     * @throws IOException если нет сети
     */
    public static HttpURLConnection openConnectionByURI(String uri, String method)
            throws IOException
    {
        uri = Uri.parse(uri).buildUpon().appendQueryParameter("client_id", Routes.CLIENT_ID).build().toString();
        URL url = new URL(uri);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setConnectTimeout(15 * 1000);
        conn.setReadTimeout(15 * 1000);
        conn.connect();
        return conn;
    }

    /**
     * Чтение ответа от внешнего сервиса
     * @param reader Буферизованный поток ввода
     * @return строка с данными
     * @throws IOException если не удалось выполнить операцию ввода-вывода
     */
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

    /**
     * Формирование Bundle с результатми об успехе
     * @param data данные ответа на запрос
     * @return Bundle
     */
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
