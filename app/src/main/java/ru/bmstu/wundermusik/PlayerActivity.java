package ru.bmstu.wundermusik;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ru.bmstu.wundermusik.musicplayer.MusicPlayer;
import ru.bmstu.wundermusik.events.NextPrevEvent;
import ru.bmstu.wundermusik.events.PlayPauseEvent;
import ru.bmstu.wundermusik.events.PlayerError;
import ru.bmstu.wundermusik.events.PlayerStateChangeAnswer;
import ru.bmstu.wundermusik.fragments.PlayerFragment;
import ru.bmstu.wundermusik.models.Track;
import ru.bmstu.wundermusik.utils.UtilSystem;


/**
 * Основной экран плеера
 * Содержит логику, контролирующую поведение фрагмента плеера {@link PlayerFragment PlayerFragment}
 * @author Eugene
 * @author Nikita
 */
public class PlayerActivity extends AppCompatActivity {

    /**
     * Параметры, которые необходимы для запуска этого экрана
     */
    public static final String TRACK_LIST = "TRACK_LIST";
    public static final String CURRENT_TRACK = "CURRENT_TRACK";

    private List<Track> trackList = new LinkedList<>();
    private static final String TAG = "PlayerActivity";
    private EventBus bus = EventBus.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        bus.register(this);
        trackList = (List<Track>) getIntent().getSerializableExtra(TRACK_LIST);
        int currentTrack = getIntent().getIntExtra(CURRENT_TRACK, 0);

        if (findViewById(R.id.fragment_container) != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, fillPlayerFragment(currentTrack))
                    .commit();

            prepareAndStartService(trackList, currentTrack);
        }
    }

    /**
     * Обработка нажатия на кнопку Пауза
     * @param btn - View кнопки
     */
    public void onClickPause(View btn) {
        bus.post(new PlayPauseEvent());
    }

    /**
     * Обработка нажатия на кнопку Следующий
     * @param btn - View кнопки
     */
    public void onClickNext(View btn) {
        bus.post(new NextPrevEvent(NextPrevEvent.Direction.NEXT));
    }

    /**
     * Обработка нажатия на кнопку Предыдущий
     * @param btn - View кнопки
     */
    public void onClickPrevious(View btn) {
        bus.post(new NextPrevEvent(NextPrevEvent.Direction.PREV));
    }

    @Subscribe
    /**
     * Обработка различных ответов от плеера
     * @param event - объект события {@link PlayerStateChangeAnswer PlayerStateChangeAnswer}
     */
    public void onEvent(PlayerStateChangeAnswer event) {
        Log.i(TAG, event.getState().name());
        PlayerFragment playerFragment = (PlayerFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        ProgressBar preloader = (ProgressBar) findViewById(R.id.preloader);
        if (playerFragment != null) {
            switch (event.getState()) {
                case PAUSED:
                    playerFragment.setPlayerState(this, PlayerFragment.ControlState.PLAY);
                    playerFragment.pauseMaskProgress();
                    break;
                case PLAYING:
                    playerFragment.setPlayerState(this, PlayerFragment.ControlState.PAUSE);
                    playerFragment.setCurrentPosition(event.getPosition());
                    if (preloader != null) {
                        preloader.setVisibility(View.GONE);
                    }
                    break;
                case PREPARING:
                    if (preloader != null) {
                        preloader.setVisibility(View.VISIBLE);
                    }
                    break;
            }
            playerFragment.setTrackData(event.getTrack());
        }
    }

    @Subscribe
    /**
     * Обработчик события ошибки от плеера
     */
    public void onEvent(PlayerError event) {
//        UtilSystem.displayMessage(findViewById(android.R.id.content), event.getMsg());
        Log.i(TAG, event.getMsg());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
    }

    /**
     * Метод, который запускает плеер
     * @param trackListToPlay - трек-лист для плеера
     * @param currentTrackIndex - индекс текущего трека
     */
    protected void prepareAndStartService(List<Track> trackListToPlay, Integer currentTrackIndex) {
        Intent intent = new Intent(this, MusicPlayer.class);
        intent.setAction(MusicPlayer.ACTION_PLAY);
        intent.putExtra(TRACK_LIST, (ArrayList<Track>) trackListToPlay);
        intent.putExtra(CURRENT_TRACK, currentTrackIndex);
        this.startService(intent);
    }

    /**
     * Создание фрагмента плеера и заполнение его первоначальными параметрами
     * @param currentTrack - позиция текущего трека в списке
     * @return - фрагмент плеера со списком параметров внутри
     */
    private PlayerFragment fillPlayerFragment (int currentTrack) {
        Bundle args = new Bundle();
        args.putSerializable(CURRENT_TRACK, trackList.get(currentTrack));

        PlayerFragment playerFragment = new PlayerFragment();
        playerFragment.setArguments(args);
        return playerFragment;
    }
}
