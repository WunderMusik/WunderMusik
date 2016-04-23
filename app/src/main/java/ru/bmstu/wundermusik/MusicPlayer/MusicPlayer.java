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
import ru.bmstu.wundermusik.events.PlayPauseEvent;
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

    // FIXME: 23.04.16 EG: объект, который нужно зарегистрировать, чтобы принимать события
    private EventBus bus = EventBus.getDefault();

    private PlayerState playerState = PlayerState.NOT_INITIALISED;

    // FIXME: 23.04.16 EG: пример метода, который обрабатывает событие с клиентской части
    @Subscribe
    public void onEvent(PlayPauseEvent event){
        onPauseRequest();
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
        if(index > trackList.size() - 1) {
            changePlayerState(PlayerState.STOPPED);
            //TODO: notify that playlist is out of songs to play
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

    public void setTrackListAndStart(List<Track> trackList) {
        setTrackList(trackList);
        playSongAt(0);
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

    // FIXME: 23.04.16 EG: Обязательный метод, чтобы хоть как-то стартануть сервис
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ACTION_PLAY.equals(intent.getAction())) {
            trackList = (List<Track>) intent.getSerializableExtra(PlayerActivity.TRACK_LIST);
            setTrackList(trackList);
            playSongAt(intent.getIntExtra(PlayerActivity.CURRENT_TRACK, 0));
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null)
            mediaPlayer.release();
        // FIXME: 23.04.16 EG:
        bus.unregister(this);
    }

    @Override
    public void onCreate() {
        // FIXME: 23.04.16 EG:
        bus.register(this);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
