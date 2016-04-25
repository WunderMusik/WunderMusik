package ru.bmstu.wundermusik.models.parsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/***
 * Реализация интерфейса json парсера для api сервиса soundcloud.com
 * @param <T> тип объекта, который планируется парсить
 * @author ali
 */
public abstract class SoundCloudJsonParser<T> implements JsonParser<T> {
    public List<T> parseMultipleObjects(String jsonStr) {
        List<T> resultList = new ArrayList<>();
        try {
            JSONArray dataJsonObj = new JSONArray(jsonStr);
            for (int i=0; i<dataJsonObj.length(); i++) {
                T object = parseSingleObjectInternal(dataJsonObj.getJSONObject(i));
                if (object != null)
                    resultList.add(object);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return resultList;
        }
    }

    public T parseSingleObject(String strJson){
        try {
            JSONObject dataJsonObj = new JSONObject(strJson);
            return parseSingleObjectInternal(dataJsonObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
