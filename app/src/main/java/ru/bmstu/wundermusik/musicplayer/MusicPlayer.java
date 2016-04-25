package ru.bmstu.wundermusik.musicplayer;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.List;

import ru.bmstu.wundermusik.PlayerActivity;
import ru.bmstu.wundermusik.events.NewPlayListEvent;
import ru.bmstu.wundermusik.events.NextPrevEvent;
import ru.bmstu.wundermusik.events.PlayPauseEvent;
import ru.bmstu.wundermusik.events.PlayerError;
import ru.bmstu.wundermusik.events.PlayerStateChangeAnswer;
import ru.bmstu.wundermusik.events.SeekEvent;
import ru.bmstu.wundermusik.events.StopEvent;
import ru.bmstu.wundermusik.events.TrackEndedEvent;
import ru.bmstu.wundermusik.models.Track;

/**
 * Движок плеера.
 * Реализован в виде Service, поскольку плеер должен работать в фоновом режиме.
 * Внутри себя хранит плейлист, состояния плеера
 * @author Nikita
 * @author Eugene
 */
public class MusicPlayer extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener,
        AudioManager.OnAudioFocusChangeListener, MediaPlayer.OnErrorListener {

    public static final String ACTION_PLAY = "ACTION_PLAY";
    public static final int FORWARD = 1;
    public static final int BACKWARD = -1;

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

    /**
     * Обработчик событий, пришедших по EventBus
     * Перегруженный метод: в зависимости от типа входного аргумента выполняется одна из реализаций
     * обработчика
     * @param event - определяет событие, которое необходимо обработать. Может содержать
     *              дополнительную информацию по событию
     */
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
        switch (playerState) {
            case PLAYING:
            case PAUSED: onStopRequest(); break;
            default: break;
        }
    }
    @Subscribe
    public void onEvent(NextPrevEvent event) {
        NextPrevEvent.Direction direction = event.getDirection();
        switch (direction) {
            case NEXT: onNextPrevRequest(FORWARD); break;
            case PREV: onNextPrevRequest(BACKWARD); break;
        }
    }
    @Subscribe
    public void onEvent(NewPlayListEvent event) {
        setTrackListAndStart(event.getTrackList(), event.getPosition());
    }
    @Subscribe
    public void onEvent(SeekEvent event) {
        switch (playerState) {
            case PAUSED:
            case PLAYING: onSeekRequest(event.getSeekValue() * 1000); break;
            default: break;
        }
    }

    /**
     * Настройка плеера. При первом вызове создает объект MediaPlayer и устанавливает значения
     * свойствам. При последующих вызовах сбрасывает состояние плеера.
     */
    private void setUpMediaPlayer() {
        if(this.mediaPlayer == null) {
            this.mediaPlayer = new MediaPlayer();
            this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            this.mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
            this.mediaPlayer.setOnPreparedListener(this);
            this.mediaPlayer.setOnCompletionListener(this);
            this.mediaPlayer.setOnErrorListener(this);
        }
        this.mediaPlayer.reset();
        changePlayerState(PlayerState.NOT_INITIALISED);
    }

    /**
     * Обработчик запроса "Играть"
     */
    public void onPlayRequest() {
        mediaPlayer.start();
        changePlayerState(PlayerState.PLAYING);
    }

    /**
     * Обработчик запроса "Пауза"
     */
    public void onPauseRequest() {
        mediaPlayer.pause();
        changePlayerState(PlayerState.PAUSED);
    }

    /**
     * Обработчик запроса "Следующий трек" и "Предыдущий трек"
     */
    public void onNextPrevRequest(int direction) {
        currentTrackPos += direction;
        //Play tracklist over and over again..
        if(currentTrackPos > trackList.size() - 1) {
            currentTrackPos = 0;
        } else if(currentTrackPos < 0) {
            currentTrackPos = trackList.size() - 1;
        }
        playSongAt(currentTrackPos);
    }

    /**
     * Обработчик запроса "Стоп"
     */
    public void onStopRequest() {
        mediaPlayer.stop();
        changePlayerState(PlayerState.STOPPED);
    }

    /**
     * Обработчик запроса "Поиск" Переводит текущую позицию плеера в треке на указанную
     * @param seekValue время в милисекундах
     */
    public void onSeekRequest(int seekValue) {
        Log.i("PLAYER", "position = " + seekValue);
        mediaPlayer.seekTo(seekValue);
    }

    /**
     * Запрос сыграть трек из текущего списка воспроизведения
     * @param index позиция трека в списке воспроизведения, индексация ведется с нуля
     */
    public void playSongAt(int index) {
        try {
            setUpMediaPlayer();
            currentTrackPos = index;
            mediaPlayer.setDataSource(trackList.get(currentTrackPos).getStreamUrl());
            changePlayerState(PlayerState.PREPARING);
            mediaPlayer.prepareAsync();
        } catch (IOException ioExc) {
            changePlayerState(PlayerState.ERROR);
            String errorMsg = "IO error catch;Stack trace: " + ioExc.getMessage();
            bus.post(new PlayerError(errorMsg));
        }
    }

    /**
     * Переводит плеер в новое состояние
     * @param newState новое состояние плеера
     */
    private void changePlayerState(PlayerState newState) {
        this.playerState = newState;
        bus.post(new PlayerStateChangeAnswer(trackList.get(currentTrackPos), this.playerState,
                mediaPlayer.getCurrentPosition() / 1000));
    }

    /**
     * Задает новый список воспроизведения и начинает воспроизводить желаемый трек
     * @param trackList список воспроизведения. Для сохранения позиций треков в списке рекомендуется
     *                  использовать списки, сохраняющие порядок внутри
     * @param startPos позиция трека в списке воспроизведения, индексация ведется с нуля
     */
    public void setTrackListAndStart(List<Track> trackList, int startPos) {
        setTrackList(trackList);
        playSongAt(startPos);
    }

    private void setTrackList(List<Track> trackList) {
        this.trackList = trackList;
        this.currentTrackPos = 0;
        setUpMediaPlayer();
    }

    private void startPlaying() {
        changePlayerState(PlayerState.PLAYING);
        mediaPlayer.start();
    }

    /**
     * Вызывается при завершении воспроизведения трека
     * @param mp объект плеера, который вызвал обработчик
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        bus.post(new TrackEndedEvent());
        onNextPrevRequest(FORWARD);
    }

    /**
     * Вызывается при завершении подготовки трека к воспроизведению
     * @param mp объект плеера, который вызвал обработчик
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        startPlaying();
    }

    @Override
    public void onAudioFocusChange(int focusChange) {

    }

    /**
     * Обработчик асинхронной ошибки
     * @param mp объект плеера, который вызвал обработчик
     * @param what код ошибки
     * @param extra дополнительный код ошибки
     * @return возвращает True, если ошибка обработана, false иначе. Если возвращать false, будет
     * вызван обработчик OnCompletion()
     */
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        changePlayerState(PlayerState.ERROR);
        // return value determines if error was handled.
        // Returning false will cause OnCompletionListener to be called
        String errorMsg = "Async error catch;What: " + what + "; Extra: " + extra;
        bus.post(new PlayerError(errorMsg));
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

    /**
     * Освобождает ресурсы плеера и отменяет подписку на события
     */
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
