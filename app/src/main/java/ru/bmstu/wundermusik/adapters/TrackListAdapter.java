package ru.bmstu.wundermusik.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ru.bmstu.wundermusik.R;
import ru.bmstu.wundermusik.models.Track;

/**
 * Реализация паттерна ViewHolder для хранения списка треков
 * ViewHolder эффективно сохраняет список треков и отображает его в Android
 * {@link android.widget.ListView ListView}
 * Кастомизация отображения трека в списке происходит здесь
 * @author Eugene
 */
public class TrackListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Track> mTracks;

    /**
     * Конструктор
     * @param context - контекст активити (или приложения) для отобраения
     * @param tracks - список треков для отображения
     */
    public TrackListAdapter(Context context, List<Track> tracks) {
        mContext = context;
        mTracks = tracks;
    }

    @Override
    public int getCount() {
        return mTracks.size();
    }

    @Override
    public Track getItem(int position) {
        return mTracks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    /**
     * Получение объекта класса View для дальнейшего отображения на экране
     * @param position - номер элемента списка, который нужно отобразить
     * @param convertView - View элемента списка, которую нужно заполнить данными
     * @param parent - родительский элемент для convertView
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        Track track = getItem(position);

        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.track_list_item, parent, false);
            holder = new ViewHolder();
            holder.trackImageView = (ImageView) convertView.findViewById(R.id.avatarView);
            holder.titleView = (TextView) convertView.findViewById(R.id.titleView);
            holder.singerView = (TextView) convertView.findViewById(R.id.artistView);
            holder.durationView = (TextView) convertView.findViewById(R.id.track_duration);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.titleView.setText(track.getTitle());
        holder.singerView.setText(track.getSinger().getName());
        holder.durationView.setText(getDuration(track.getDuration()));

        Log.i("Singer", track.getSinger().getName());
        Log.i("Title", track.getTitle());

        Picasso.with(mContext)
                .load(track.getTrackImageUrl())
                .into(holder.trackImageView);
        return convertView;
    }

    /**
     * Форматирование продолжительности в милисекундах в формат mm:ss
     * @param duration - продолжительность трека в милисекундах
     */
    private String getDuration (long duration) {
        int total = (int) (duration / 1000);
        int minutes = (total % 3600) / 60;
        int seconds = total % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Содержит информацию, из каких объектов View состоит отображаемый список
     */
    static class ViewHolder {
        ImageView trackImageView;
        TextView titleView;
        TextView singerView;
        TextView durationView;
    }
}
