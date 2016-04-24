package ru.bmstu.wundermusik.events;

import ru.bmstu.wundermusik.musicplayer.MusicPlayer;
import ru.bmstu.wundermusik.models.Track;

/**
 * Created by eugene on 24.04.16.
 */
public class PlayerStateChangeAnswer {
    public PlayerStateChangeAnswer(Track currentTrack, MusicPlayer.PlayerState newState, int position) {
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

    private Track track;
    private MusicPlayer.PlayerState state;
    private int position;

    public int getPosition() {
        return position;
    }
}
