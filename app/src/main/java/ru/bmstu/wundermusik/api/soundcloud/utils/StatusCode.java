package ru.bmstu.wundermusik.api.soundcloud.utils;

/**
 * Статусы обработки запросов для взаимодействия между производными от {@link ru.bmstu.wundermusik.api.soundcloud.query.AbstractQuery AbstractQuery}
 * классов и клиентами API
 *
 * @author max
 */
public class StatusCode {
    public static final int OK = 0;
    public static final int ERROR = 1;
    public static final int FORBIDDEN = 3;
    public static final int BAD_REQUEST = 4;
}
