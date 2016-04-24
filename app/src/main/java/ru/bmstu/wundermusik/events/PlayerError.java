package ru.bmstu.wundermusik.events;

/**
 * Событие от плеера к UI о произошедшей внутренней ошибке
 * @author Nikita
 */
public class PlayerError {
    private String msg;

    /**
     * Конструктор с параметрами
     * @param msg сообшение об ошибке
     */
    public PlayerError(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
