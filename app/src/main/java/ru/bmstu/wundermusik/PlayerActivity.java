package ru.bmstu.wundermusik;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

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
                // FIXME: 21.04.16 Сохранять состояние
                return;
            }

            PlayerFragment firstFragment = new PlayerFragment();
            firstFragment.setArguments(getPlayerArguments());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, firstFragment)
                    .commit();
        }
    }

    private Bundle getPlayerArguments() {
        Bundle args = new Bundle();
        args.putSerializable(CURRENT_TRACK, trackList.get(currentTrack));
        return args;
    }

    public void onClickControl(View btn) {
        PlayerFragment frag = (PlayerFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        // FIXME: 21.04.16 SEND MESSAGE TO Player
        frag.changeControlView(this);
    }
    public void onClickNext(View btn) {
        PlayerFragment frag = (PlayerFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        // FIXME: 21.04.16 SEND MESSAGE TO Player
        if (currentTrack < trackList.size() - 2) {
            currentTrack++;
        } else {
            currentTrack = 0;
        }
        frag.setTrackData(trackList.get(currentTrack));
    }
    public void onClickPrevious(View btn) {
        PlayerFragment frag = (PlayerFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        // FIXME: 21.04.16 SEND MESSAGE TO Player
        if (currentTrack > 0) {
            currentTrack--;
        } else {
            currentTrack = trackList.size() - 1;
        }
        frag.setTrackData(trackList.get(currentTrack));
    }
}
