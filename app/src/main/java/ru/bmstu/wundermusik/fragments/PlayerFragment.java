package ru.bmstu.wundermusik.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.bmstu.wundermusik.R;


public class PlayerFragment extends Fragment {

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

        View playerView = inflater.inflate(R.layout.fragment_player, container, false);
        Bundle args = getArguments();
        if (args != null) {
            String title = args.getString("title");
            String artist = args.getString("artist");

            TextView titleView = (TextView) playerView.findViewById(R.id.titleView);
            titleView.setText(title);
            TextView artistView = (TextView) playerView.findViewById(R.id.artistView);
            artistView.setText(artist);
        }
        return playerView;
    }
}
