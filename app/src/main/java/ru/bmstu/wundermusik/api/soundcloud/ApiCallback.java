package ru.bmstu.wundermusik.api.soundcloud;

/**
 * Created by max on 31.03.16.
 */
public interface ApiCallback {
    void onResult(String data);
    void onFailure(int code, String errorMsg);
}