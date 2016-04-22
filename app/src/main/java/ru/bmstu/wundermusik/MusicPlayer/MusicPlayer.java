package ru.bmstu.wundermusik.MusicPlayer;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.List;

import ru.bmstu.wundermusik.models.Track;

public class MusicPlayer extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, AudioManager.OnAudioFocusChangeListener {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {

    }

    public enum PlayerState {
        ERROR,
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
        }
        //TODO: set up player properties, e.g. WakeMode
        changePlayerState(PlayerState.NOT_INITIALISED);
        this.mediaPlayer.reset();
        this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        this.mediaPlayer.setOnPreparedListener(this);
        this.mediaPlayer.setOnCompletionListener(this);
    }

    public void onPlayRequest()
    {
        changePlayerState(PlayerState.PLAYING);
        mediaPlayer.start();
    }

    public void onPauseRequest()
    {
        changePlayerState(PlayerState.PAUSED);
        mediaPlayer.pause();
    }

    public void onNextRequest()
    {
        playSongAt(++currentTrackPos);
    }

    public void onStopRequest()
    {
        changePlayerState(PlayerState.STOPPED);
        mediaPlayer.stop();
    }

    public void onPrevRequest()
    {
        playSongAt(--currentTrackPos);
    }

    public void playSongAt(int index)
    {
        if(index > trackList.size() - 1)
        {
            //TODO: OUT OF RANGE
        }
        try {
            setUpMediaPlayer();
            currentTrackPos = index;
            mediaPlayer.setDataSource(trackList.get(currentTrackPos).getStreamUrl());
            changePlayerState(PlayerState.PREPARING);
            mediaPlayer.prepareAsync();
        } catch (IOException ioExc)
        {
            changePlayerState(PlayerState.ERROR);
            //TODO: notify about io error
        }
    }

    private void changePlayerState(PlayerState newState)
    {
        //TODO: notify about changed state
        this.playerState = newState;
    }

    public void setTrackListAndStart(List<Track> trackList) {
        setTrackList(trackList);
        playSongAt(0);
    }


    private void setTrackList(List<Track> trackList)
    {
        this.trackList = trackList;
        this.currentTrackPos = 0;
        setUpMediaPlayer();
    }

    private void startPlayng() {
        changePlayerState(PlayerState.PLAYING);
        mediaPlayer.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        onNextRequest();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        startPlayng();
    }

}
