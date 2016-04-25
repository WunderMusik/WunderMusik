package ru.bmstu.wundermusik.fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import co.mobiwise.library.MaskProgressView;
import co.mobiwise.library.OnProgressDraggedListener;
import ru.bmstu.wundermusik.PlayerActivity;
import ru.bmstu.wundermusik.R;
import ru.bmstu.wundermusik.events.PlayerStateChangeAnswer;
import ru.bmstu.wundermusik.events.SeekEvent;
import ru.bmstu.wundermusik.models.Track;
import ru.bmstu.wundermusik.utils.UtilSystem;

/**
 * Фрагмент панели управления плеером
 * @author Eugene
 * @author Max
 */
public class PlayerFragment extends Fragment {

    private View playerView = null;
    private ControlState currentState = ControlState.PLAY;
    private static final String TAG = "PlayerFragment";
    private MaskProgressView maskProgressView;
    private TextView titleView;
    private TextView artistView;
    private TextView durationView;
    private ImageView avatarView;
    private Track currentTrack;
    private ProgressBar spinner;
    private EventBus bus = EventBus.getDefault();


    /**
     * Для фрагмента всегда требуется пустой конструктор
     */
    public PlayerFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);
        playerView = view;
        if (getArguments() != null) {
            currentState = ControlState.PLAY;
            currentTrack = (Track) getArguments().getSerializable(PlayerActivity.CURRENT_TRACK);
        } else {
            UtilSystem.displayMessage(
                    playerView.findViewById(android.R.id.content),
                    getResources().getString(R.string.player_parameters_error)
            );
        }
        spinner = (ProgressBar) view.findViewById(R.id.preloader);
        spinner.setVisibility(View.VISIBLE);
        initializeProgressBar(view);
        initializeLayout(view);
        return view;
    }

    private void initializeProgressBar(View view) {
        maskProgressView = (MaskProgressView) view.findViewById(R.id.maskProgressView);
        maskProgressView.setmMaxSeconds((int) currentTrack.getDuration() / 1000);
        maskProgressView.setOnProgressDraggedListener(new CustomProgressDraggedListener());
    }

    private void initializeLayout(View view) {
        titleView = (TextView) view.findViewById(R.id.titleView);
        titleView.setText(currentTrack.getTitle());

        artistView = (TextView) view.findViewById(R.id.artistView);
        artistView.setText(currentTrack.getSinger().getName());

        durationView = (TextView) view.findViewById(R.id.track_duration);
        if (durationView != null) {
            durationView.setText(UtilSystem.getDuration(currentTrack.getDuration()));
        }

        avatarView = (ImageView) view.findViewById(R.id.avatarView);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private class CustomProgressDraggedListener implements OnProgressDraggedListener {
        @Override
        public void onProgressDragged(int position) {
            bus.post(new SeekEvent(position));
        }

        @Override
        public void onProgressDragging(int position) {
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bus.register(this);
    }

    private void redrawLayout () {
        titleView.setText(currentTrack.getTitle());
        artistView.setText(currentTrack.getSinger().getName());
        if (durationView != null) {
            durationView.setText(UtilSystem.getDuration(currentTrack.getDuration()));
        }

        Picasso.with(getActivity())
                .load(currentTrack.getSinger().getAvatarUrl())
                .into(avatarView);

        maskProgressView.setmMaxSeconds((int) currentTrack.getDuration() / 1000);
        Picasso.with(getActivity())
            .load(currentTrack.getTrackImageUrl())
            .into(new Target() {
                @Override
                public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                    maskProgressView.setCoverImage(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    maskProgressView.setCoverImage(
                            BitmapFactory.decodeResource(
                                    getActivity().getResources(), R.drawable.icon_header
                            )
                    );
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            });
    }

    @Subscribe
    /**
     * Обработка различных ответов от плеера
     * @param event - объект события {@link PlayerStateChangeAnswer PlayerStateChangeAnswer}
     */
    public void onEvent(PlayerStateChangeAnswer event) {
        Log.i(TAG, event.getState().name());
        Log.i(TAG, "Position = " + event.getPosition());
        currentTrack = event.getTrack();
        redrawLayout();

        switch (event.getState()) {
            case PREPARING:
                spinner.setVisibility(View.VISIBLE);
                break;
            case STOPPED:
                maskProgressView.stop();
                setPlayerState(getActivity(), PlayerFragment.ControlState.PLAY);
                break;
            case PLAYING:
                maskProgressView.setmCurrentSeconds(event.getPosition());
                maskProgressView.start();
                setPlayerState(getActivity(), PlayerFragment.ControlState.PAUSE);
                spinner.setVisibility(View.GONE);
                break;
            case PAUSED:
                maskProgressView.pause();
                setPlayerState(getActivity(), PlayerFragment.ControlState.PLAY);
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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

    public void setPlayerState(Context context, ControlState state) {
        Button btnControl = (Button) playerView.findViewById(R.id.buttonControl);
        try {
            btnControl.setBackground(state.getDrawable(context));
            currentState = state;
        }
        catch (Exception e) {
            Log.i(TAG, e.getMessage());
        }
    }
}
