package ru.bmstu.wundermusik.models.parsers;

import org.json.JSONObject;

import java.util.List;

/***
 * Интерфейс json парсера
 * @param <T> тип объекта парсинга (песня, исполнитель)
 * @author ali
 */
public interface JsonParser<T> {
    /***
     * Метод разбора единичного объекта. Используется как внутренний метод
     * при парсинге строкого представления
     * @param dataJsonObj json объект (не строка), из которой можно запрашивать данные
     * @return сформированный объект
     */
    T parseSingleObjectInternal(JSONObject dataJsonObj);

    /***
     * Парсинг строки, содеражащей множество объектов
     * @param jsonStr строковое представление json
     * @return список сформированных объектов (пустой список если неудача)
     */
    List<T> parseMultipleObjects(String jsonStr);

    /***
     * Парсинг строки, содеражащей единичный объект
     * @param jsonStr строковое представление json
     * @return сформированный объект типа T (null если неудача)
     */
    T parseSingleObject(String jsonStr);
}
