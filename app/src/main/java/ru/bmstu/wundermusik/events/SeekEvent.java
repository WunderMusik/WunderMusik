package ru.bmstu.wundermusik.events;

/**
 * Запрос от UI к плееру на переход в определенную позицию по времени в текущем треке
 * @author Nikita
 */
public class SeekEvent {
    private int seekValue;

    /**
     * Конструктор с параметрами
     * @param seekValue желаемое время в текущем треке, в секундах
     */
    public SeekEvent(int seekValue) {
        this.seekValue = seekValue;
    }

    public int getSeekValue() {
        return seekValue;
    }
}
