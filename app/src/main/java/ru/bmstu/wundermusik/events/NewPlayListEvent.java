package ru.bmstu.wundermusik.events;

import java.util.List;

import ru.bmstu.wundermusik.models.Track;

/**
 * Событие от UI к плееру о том, что был задан новый плейлист
 * @author Eugene
 */
public class NewPlayListEvent {
    private List<Track> trackList;
    private int position;

    /**
     * Конструктор с параметрами
     * @param trackList новый плейлист
     * @param position позиция в плейлисте, с которой начать воспроизведение
     */
    public NewPlayListEvent(List<Track> trackList, int position) {
        this.trackList = trackList;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public List<Track> getTrackList() {
        return trackList;
    }
}
