package ru.bmstu.wundermusik.models.parsers;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by ali on 18.04.16.
 */
public interface JsonParser<T> {
    T parseSingleObjectInternal(JSONObject dataJsonObj);
    List<T> parseMultipleObjects(String jsonStr);
    T parseSingleObject(String jsonStr);
}
