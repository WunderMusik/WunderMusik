package ru.bmstu.wundermusik.MusicPlayer;
import android.media.AudioManager;
import android.media.MediaPlayer;

import java.util.List;

import ru.bmstu.wundermusik.models.Track;

public class MusicPlayer implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {
    public enum PlayerState {
        NOT_INITIALISED,
        STOPPED,
        PAUSED,
        PLAYING,
        PREPARING
    }
    private MediaPlayer mediaPlayer = null;
    private List<Track> trackList = null;
    private Integer currentTrackPos = null;
    private PlayerState playerState = PlayerState.NOT_INITIALISED;
    private void setUpMediaPlayer()
    {
        if(this.mediaPlayer == null)
        {
            this.mediaPlayer = new MediaPlayer();
            this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            this.mediaPlayer.setOnCompletionListener(this);
            //TODO: set up player properties, e.g. WakeMode
        }
    }

    public void onPlayRequest()
    {

    }

    public void onPauseRequest()
    {

    }

    public void onNextRequest()
    {

    }

    public void onStopRequest()
    {

    }

    public void onPrevRequest()
    {

    }

    private void changePlayerState(PlayerState newState)
    {
        //TODO: notify about changed state
        this.playerState = newState;
    }

    public void setTrackList(List<Track> trackList)
    {
        this.trackList = trackList;
        this.currentTrackPos = 0;
    }


    private void startPlayng() {
        if(!mediaPlayer.isPlaying())
        {
            changePlayerState(PlayerState.PLAYING);
            mediaPlayer.start();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        startPlayng();
    }

}
