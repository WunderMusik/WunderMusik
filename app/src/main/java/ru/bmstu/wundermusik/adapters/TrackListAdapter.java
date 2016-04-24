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
 * Created by eugene on 20.04.16.
 */
public class TrackListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Track> mTracks;

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
    public View getView(int position, View convertView, ViewGroup parent) {

        Track track = getItem(position);

        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.track_list_item, parent, false);
            holder = new ViewHolder();
            holder.trackImageView = (ImageView) convertView.findViewById(R.id.avatarView);
            holder.titleView = (TextView) convertView.findViewById(R.id.titleView);
            holder.singerView = (TextView) convertView.findViewById(R.id.artistView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.titleView.setText(track.getTitle());
        holder.singerView.setText(track.getSinger().getName());

        Log.i("Singer", track.getSinger().getName());
        Log.i("Title", track.getTitle());
        // Trigger the download of the URL asynchronously into the image view.
        Picasso.with(mContext)
                .load(track.getSinger().getAvatarUrl())
                .into(holder.trackImageView);
        return convertView;
    }

    static class ViewHolder {
        ImageView trackImageView;
        TextView titleView;
        TextView singerView;
    }
}
