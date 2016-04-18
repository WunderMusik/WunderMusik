package ru.bmstu.wundermusik.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ru.bmstu.wundermusik.R;


public class PlayerFragment extends Fragment {

    private View playerView = null;
    private ControlState currentState = ControlState.PLAY;

    public PlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        playerView = inflater.inflate(R.layout.fragment_player, container, false);
        Bundle args = getArguments();
        if (args != null) {
            String title = args.getString("title");
            String artist = args.getString("artist");
            String state = args.getString("state");

            if (state != null) {
                currentState = ControlState.valueOf(state);
            }
            setTrack(title, artist);
        }
        return playerView;
    }

    public void setTrack(String title, String artist) {
        TextView titleView = (TextView) playerView.findViewById(R.id.titleView);
        titleView.setText(title);
        TextView artistView = (TextView) playerView.findViewById(R.id.artistView);
        artistView.setText(artist);
    }

    public enum ControlState {
        PLAY("ic_play_circle_filled_white_48dp"),
        PAUSE("ic_pause_circle_filled_white_48dp");

        private String image;

        ControlState(String image) {
            this.image = image;
        }

        public Drawable getDrawable(Context context) {
            Resources resources = context.getResources();
            int resourceId = resources.getIdentifier(this.image, "drawable", context.getPackageName());
            return resources.getDrawable(resourceId);
        }
        public ControlState inverse() {
            if (this == PLAY) return PAUSE;
            if (this == PAUSE) return PLAY;
            return null;
        }
    }

    public void changeControlView(Context context) {
        Button btnControl = (Button) playerView.findViewById(R.id.buttonControl);
        ControlState newState = currentState.inverse();
        try {
            if (newState != null) {
                btnControl.setBackground(newState.getDrawable(context));
                currentState = newState;
            }
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
