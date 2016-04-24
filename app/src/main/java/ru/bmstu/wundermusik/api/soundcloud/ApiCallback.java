package ru.bmstu.wundermusik.api.soundcloud;

/**
 * Обработчик запроса к API
 * @author max
 */
public interface ApiCallback {
    /**
     * Будет вызван при успешном запросе
     * @param data данные от внешнего сервиса
     */
    void onResult(String data);

    /**
     * Будет вызван при не 200-ом ответе от внешнего сервиса
     * @param code статус ответа для клиента
     * @param errorMsg статус ответа для пользователя
     */
    void onFailure(int code, String errorMsg);
}