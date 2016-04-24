package ru.bmstu.wundermusik.events;

import ru.bmstu.wundermusik.musicplayer.MusicPlayer;
import ru.bmstu.wundermusik.models.Track;

/**
 * Событие ответа от плеера. Посылается на каждое изменение состояния плеера
 */
public class PlayerStateChangeAnswer {
    /**
     * Конструктор события
     * @param currentTrack - текущий трек для отображения информации в плеере
     * @param newState - текущее состояние плеера
     * {@link ru.bmstu.wundermusik.musicplayer.MusicPlayer.PlayerState PlayerState}.
     * @param position - положение плеера в секундах
     */
    public PlayerStateChangeAnswer(Track currentTrack, MusicPlayer.PlayerState newState,
                                   int position)
    {
        track = currentTrack;
        state = newState;
        this.position = position;
    }

    public MusicPlayer.PlayerState getState() {
        return state;
    }

    public Track getTrack() {
        return track;
    }

    public int getPosition() {
        return position;
    }

    private Track track;
    private MusicPlayer.PlayerState state;
    private int position;
}
