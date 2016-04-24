package ru.bmstu.wundermusik;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

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

public class PlayerActivity extends AppCompatActivity {

    public static final String TRACK_LIST = "TRACK_LIST";
    public static final String CURRENT_TRACK = "CURRENT_TRACK";

    private List<Track> trackList = new LinkedList<>();
    private int currentTrack = 0;
    private static final String TAG = "PlayerActivity";
    private EventBus bus = EventBus.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        bus.register(this);
        trackList = (List<Track>) getIntent().getSerializableExtra(TRACK_LIST);
        currentTrack = getIntent().getIntExtra(CURRENT_TRACK, 0);
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            PlayerFragment firstFragment = new PlayerFragment();
            firstFragment.setArguments(getPlayerArguments());

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, firstFragment)
                    .commit();

            prepareAndStartService(trackList, currentTrack);
        }
    }

    public void onClickPause(View btn) {
        bus.post(new PlayPauseEvent());
    }

    public void onClickNext(View btn) {
        bus.post(new NextPrevEvent(NextPrevEvent.Direction.NEXT));
    }

    public void onClickPrevious(View btn) {
        bus.post(new NextPrevEvent(NextPrevEvent.Direction.PREV));
    }

    @Subscribe
    public void onEvent(PlayerStateChangeAnswer event) {
        Log.i(TAG, event.getState().name());
        PlayerFragment playerFragment = (PlayerFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);

        if (playerFragment != null) {
            switch (event.getState()) {
                case PAUSED:
                    playerFragment.setPlayerState(this, PlayerFragment.ControlState.PLAY);
                    break;
                case PLAYING:
                    playerFragment.setPlayerState(this, PlayerFragment.ControlState.PAUSE);
                    playerFragment.setCurrentPosition(event.getPosition());
                    break;

            }
            playerFragment.setTrackData(event.getTrack());
        }
    }

    @Subscribe
    public void onEvent(PlayerError event) {
        UtilSystem.displayMessage(findViewById(android.R.id.content), event.getMsg());
    }

    protected void prepareAndStartService(List<Track> trackListToPlay, Integer currentTrackIndex) {
        Intent intent = new Intent(this, MusicPlayer.class);
        intent.setAction(MusicPlayer.ACTION_PLAY);
        intent.putExtra(TRACK_LIST, (ArrayList<Track>) trackListToPlay);
        intent.putExtra(CURRENT_TRACK, currentTrackIndex);
        this.startService(intent);
    }

    private Bundle getPlayerArguments() {
        Bundle args = new Bundle();
        args.putSerializable(CURRENT_TRACK, trackList.get(currentTrack));
        return args;
    }
}
