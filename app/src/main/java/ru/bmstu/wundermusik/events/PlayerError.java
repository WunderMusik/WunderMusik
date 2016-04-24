package ru.bmstu.wundermusik.events;

/**
 * Created by Nikita on 4/24/2016.
 */
public class PlayerError {
    private String msg;
    public PlayerError(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
