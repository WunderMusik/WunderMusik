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
import co.mobiwise.library.OnProgressDraggedListener;
import ru.bmstu.wundermusik.PlayerActivity;
import ru.bmstu.wundermusik.R;
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


    /**
     * Для фрагмента всегда требуется пустой конструктор
     */
    public PlayerFragment() {}

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
            currentState = ControlState.PLAY;
            setTrackData((Track) args.getSerializable(PlayerActivity.CURRENT_TRACK));
            maskProgressView = (MaskProgressView) playerView.findViewById(R.id.maskProgressView);
            titleView = (TextView) playerView.findViewById(R.id.titleView);
            artistView = (TextView) playerView.findViewById(R.id.artistView);
        } else {
            UtilSystem.displayMessage(
                    playerView.findViewById(android.R.id.content),
                    getResources().getString(R.string.player_parameters_error)
            );
        }
        return playerView;
    }

    /**
     * Метод, который по треку позволяет наполнить лейаут фрагмента плеера
     * @param track - трек, данные которого будут использованы при наполнении фрагмента
     */
    public void setTrackData(Track track) {
        titleView.setText(track.getTitle());
        artistView.setText(track.getSinger().getName());
        maskProgressView.setmMaxSeconds((int) TimeUnit.SECONDS.convert(track.getDuration(), TimeUnit.MILLISECONDS));

        /**
         * Асинхронная загрузка изображения в панели управления с помощью {@link Picasso Picasso}
         */
        final ImageView artistImage = (ImageView) playerView.findViewById(R.id.avatarView);
        Picasso.with(getActivity())
                .load(track.getSinger().getAvatarUrl())
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

    /**
     * Установить текущее положение плеера в секундах
     * @param position - положение в секундах
     */
    public void setCurrentPosition (int position) {
        maskProgressView.setmCurrentSeconds(position);
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
