package ru.bmstu.wundermusik.events;

/**
 * Запрос от UI к плееру о смене трека на 1 позицию в списке
 * @author Nikita
 */
public class NextPrevEvent {
    /**
     * Направление движения по списку воспроизведения
     */
    public enum Direction {
        NEXT,
        PREV
    }
    private Direction direction;

    /**
     * Конструктор с параметрами
     * @param direction указывает на какой трек сменить: предыдущий (PREV), или следующий (NEXT)
     */
    public NextPrevEvent(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }
}
