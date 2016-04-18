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
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {

    }

    public enum PlayerState {
        NOT_INITIALISED,
        IDLE,
        STOPPED,
        PAUSED,
        PLAYING,
        PREPARING
    }
    private MediaPlayer mediaPlayer = null;
    private List<Track> trackList = null;
    private Integer currentTrackPos = null;
    private PlayerState playerState = PlayerState.IDLE;
    private void setUpMediaPlayer()
    {
        if(this.mediaPlayer == null)
        {
            this.mediaPlayer = new MediaPlayer();


        }
        //TODO: set up player properties, e.g. WakeMode
        this.mediaPlayer.reset();
        this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        this.mediaPlayer.setOnPreparedListener(this);
        this.mediaPlayer.setOnCompletionListener(this);
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

    public void playSongAt(int index) throws IOException
    {
        if(this.playerState == PlayerState.NOT_INITIALISED)
        {
            //TODO: exception, fail
        }
        if(index > trackList.size() - 1)
        {
            //TODO: OUT OF RANGE
        }
        setUpMediaPlayer();
        currentTrackPos = index;
        mediaPlayer.setDataSource(trackList.get(currentTrackPos).getStreamUrl());
        changePlayerState(PlayerState.PREPARING);
        mediaPlayer.prepareAsync();
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
        changePlayerState(PlayerState.PLAYING);

        mediaPlayer.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(currentTrackPos < trackList.size() - 1)
        {
            try{
            playSongAt(currentTrackPos+1);

                } catch (Exception e)
            {

            }
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

        startPlayng();
    }

}
