package ru.bmstu.wundermusik;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ru.bmstu.wundermusik.MusicPlayer.MusicPlayer;
import ru.bmstu.wundermusik.events.NextPrevEvent;
import ru.bmstu.wundermusik.events.PlayPauseEvent;
import ru.bmstu.wundermusik.fragments.PlayerFragment;
import ru.bmstu.wundermusik.models.Track;

public class PlayerActivity extends AppCompatActivity {

    public static final String TRACK_LIST = "TRACK_LIST";
    public static final String CURRENT_TRACK = "CURRENT_TRACK";

    private List<Track> trackList = new LinkedList<>();
    private int currentTrack = 0;
    private static final String TAG = "PlayerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

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

    protected void prepareAndStartService(List<Track> trackListToPlay, Integer currentTrackIndex) {
        Intent intent = new Intent(this, MusicPlayer.class);
        intent.setAction(MusicPlayer.ACTION_PLAY);
        intent.putExtra(TRACK_LIST, (ArrayList<Track>) trackListToPlay);
        intent.putExtra(CURRENT_TRACK, currentTrackIndex);
        this.startService(intent);
    }

    public void onClickControl(View btn) {
        PlayerFragment frag = (PlayerFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        EventBus.getDefault().post(new PlayPauseEvent());
        frag.changeControlView(this);
    }
    public void onClickNext(View btn) {
        PlayerFragment frag = (PlayerFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        if (currentTrack < trackList.size() - 2) {
            currentTrack++;
        } else {
            currentTrack = 0;
        }
        EventBus.getDefault().post(new NextPrevEvent(NextPrevEvent.Direction.NEXT));
        frag.setTrackData(trackList.get(currentTrack));
    }
    public void onClickPrevious(View btn) {
        PlayerFragment frag = (PlayerFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        if (currentTrack > 0) {
            currentTrack--;
        } else {
            currentTrack = trackList.size() - 1;
        }
        EventBus.getDefault().post(new NextPrevEvent(NextPrevEvent.Direction.PREV));
        frag.setTrackData(trackList.get(currentTrack));
    }

    private Bundle getPlayerArguments() {
        Bundle args = new Bundle();
        args.putSerializable(CURRENT_TRACK, trackList.get(currentTrack));
        return args;
    }
}
