package ru.bmstu.wundermusik.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.concurrent.TimeUnit;

import co.mobiwise.library.MaskProgressView;
import ru.bmstu.wundermusik.PlayerActivity;
import ru.bmstu.wundermusik.R;
import ru.bmstu.wundermusik.models.Track;
import ru.bmstu.wundermusik.utils.UtilSystem;


public class PlayerFragment extends Fragment {

    private View playerView = null;
    private ControlState currentState = ControlState.PLAY;
    private static final String TAG = "PlayerFragment";

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
            String state = "PLAY";

            if (state != null) {
                currentState = ControlState.valueOf(state);
            }
            setTrackData((Track) args.getSerializable(PlayerActivity.CURRENT_TRACK));
        } else {
            UtilSystem.displayMessage(
                    playerView.findViewById(android.R.id.content),
                    getResources().getString(R.string.player_parameters_error)
            );
        }
        return playerView;
    }

    public void setTrackData(Track track) {
        TextView titleView = (TextView) playerView.findViewById(R.id.titleView);
        titleView.setText(track.getTitle());
        TextView artistView = (TextView) playerView.findViewById(R.id.artistView);
        artistView.setText(track.getSinger().getName());
        final ImageView artistImage = (ImageView) playerView.findViewById(R.id.avatarView);
        final MaskProgressView maskProgressView = (MaskProgressView) playerView.findViewById(R.id.maskProgressView);
        maskProgressView.setmMaxSeconds((int) TimeUnit.SECONDS.convert(track.getDuration(), TimeUnit.MILLISECONDS));

        Picasso.with(getActivity())
                .load(track.getTrackImageUrl())
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                        maskProgressView.setCoverImage(bitmap);
                        artistImage.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        maskProgressView.setCoverImage(BitmapFactory.decodeResource(getActivity().getResources(),
                                R.drawable.icon_header));
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                });
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
            Log.i(TAG, e.getMessage());
        }
    }
}
