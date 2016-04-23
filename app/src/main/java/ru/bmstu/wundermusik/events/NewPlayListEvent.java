package ru.bmstu.wundermusik.events;

import java.util.List;

import ru.bmstu.wundermusik.models.Track;

/**
 * Created by eugene on 23.04.16.
 */
// FIXME: 23.04.16 EG: Пример класса-события
public class NewPlayListEvent {

    private List<Track> trackList;
    private int position;

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
