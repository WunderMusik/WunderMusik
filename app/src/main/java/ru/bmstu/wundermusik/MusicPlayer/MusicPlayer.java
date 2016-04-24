package ru.bmstu.wundermusik.MusicPlayer;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.List;

import ru.bmstu.wundermusik.PlayerActivity;
import ru.bmstu.wundermusik.events.NewPlayListEvent;
import ru.bmstu.wundermusik.events.NextPrevEvent;
import ru.bmstu.wundermusik.events.PlayPauseEvent;
import ru.bmstu.wundermusik.events.StopEvent;
import ru.bmstu.wundermusik.models.Track;

public class MusicPlayer extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener,
        AudioManager.OnAudioFocusChangeListener, MediaPlayer.OnErrorListener {

    public static final String ACTION_PLAY = "ACTION_PLAY";

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
    private EventBus bus = EventBus.getDefault();
    private PlayerState playerState = PlayerState.NOT_INITIALISED;

    @Subscribe
    public void onEvent(PlayPauseEvent event){
        switch(playerState) {
            case PLAYING: onPauseRequest(); break;
            case PAUSED: onPlayRequest(); break;
            default: break;
        }
    }
    @Subscribe
    public void onEvent(StopEvent event) {
        switch (playerState)
        {
            case PLAYING:
            case PAUSED: onStopRequest(); break;
            default: break;
        }
    }
    @Subscribe
    public void onEvent(NextPrevEvent event) {
        NextPrevEvent.Direction direction = event.getDirection();
        switch (direction) {
            case NEXT: onNextRequest(); break;
            case PREV: onPrevRequest(); break;
        }
    }
    @Subscribe
    public void onEvent(NewPlayListEvent event) {
        setTrackListAndStart(event.getTrackList(), event.getPosition());
    }

    private void setUpMediaPlayer() {
        if(this.mediaPlayer == null) {
            this.mediaPlayer = new MediaPlayer();
            this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            this.mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
            this.mediaPlayer.setOnPreparedListener(this);
            this.mediaPlayer.setOnCompletionListener(this);
            this.mediaPlayer.setOnErrorListener(this);
        }
        changePlayerState(PlayerState.NOT_INITIALISED);
        this.mediaPlayer.reset();
    }

    public void onPlayRequest() {
        changePlayerState(PlayerState.PLAYING);
        mediaPlayer.start();
    }

    public void onPauseRequest() {
        changePlayerState(PlayerState.PAUSED);
        mediaPlayer.pause();
    }

    public void onNextRequest() {
        playSongAt(++currentTrackPos);
    }

    public void onStopRequest() {
        changePlayerState(PlayerState.STOPPED);
        mediaPlayer.stop();
    }

    public void onPrevRequest() {
        playSongAt(--currentTrackPos);
    }

    public void playSongAt(int index) {
        //Play tracklist over and over again..
        if(index > trackList.size() - 1) {
            index = 0;
        } else if(index < 0) {
            index = trackList.size() - 1;
        }
        try {
            setUpMediaPlayer();
            currentTrackPos = index;
            mediaPlayer.setDataSource(trackList.get(currentTrackPos).getStreamUrl());
            changePlayerState(PlayerState.PREPARING);
            mediaPlayer.prepareAsync();
        } catch (IOException ioExc) {
            changePlayerState(PlayerState.ERROR);
            //TODO: notify about io error
        }
    }

    private void changePlayerState(PlayerState newState) {
        //TODO: notify about changed state
        this.playerState = newState;
    }

    public void setTrackListAndStart(List<Track> trackList, int startPos) {
        setTrackList(trackList);
        playSongAt(startPos);
    }

    private void setTrackList(List<Track> trackList) {
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

    @Override
    public void onAudioFocusChange(int focusChange) {

    }
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        changePlayerState(PlayerState.ERROR);
        //return value determines if error was handled. Returning false will couse OnCompletionListener
        //to be called
        //TODO: show error msg
        return true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ACTION_PLAY.equals(intent.getAction())) {
            trackList = (List<Track>) intent.getSerializableExtra(PlayerActivity.TRACK_LIST);
            setTrackListAndStart(trackList, intent.getIntExtra(PlayerActivity.CURRENT_TRACK, 0));
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null)
            mediaPlayer.release();
        bus.unregister(this);
    }

    @Override
    public void onCreate() {
        bus.register(this);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
