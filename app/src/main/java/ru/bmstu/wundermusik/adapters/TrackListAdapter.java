package ru.bmstu.wundermusik.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.bmstu.wundermusik.PlayerActivity;
import ru.bmstu.wundermusik.R;
import ru.bmstu.wundermusik.models.Track;

public class TrackListAdapter extends RecyclerView.Adapter<TrackListAdapter.ViewHolder> {

    public ArrayList<Track> data;
    public Context context;

    public TrackListAdapter(ArrayList<Track> data, Context context) {
        this.data = data;
    }

    @Override
    public TrackListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_list_item, parent, false);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Intent intent = new Intent(view.getContext(), PlayerActivity.class);
                    view.getContext().startActivity(intent);
                    return true;
                } else {
                    return false;
                }
            }
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrackListAdapter.ViewHolder holder, int position) {
        Track track = data.get(position);
        String title = track.getTitle(),
                singer = track.getSinger().getName(),
                avatarUrl = track.getSinger().getAvatarUrl();


        // FIXME: 19.04.16 Пока что падает
        /*
        if ((context != null) && (holder.avatarView != null) && (avatarUrl != null)) {
            Picasso.with(context).load(avatarUrl).into(holder.avatarView);
        }
        */
        if (title != null) {
            holder.titleView.setText(title);
        }
        if (singer != null) {
            holder.artistView.setText(singer);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void add(Track track) {
        insert(track, data.size());
    }

    public void insert(Track track, int position) {
        data.add(position, track);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    public void clear() {
        int size = data.size();
        data.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void addAll(List<Track> tracks) {
        int startIndex = data.size();
        data.addAll(startIndex, tracks);
        notifyItemRangeInserted(startIndex, tracks.size());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleView;
        public TextView artistView;
        public ImageView avatarView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleView = (TextView) itemView.findViewById(R.id.titleView);
            artistView = (TextView) itemView.findViewById(R.id.artistView);
            avatarView = (ImageView) itemView.findViewById(R.id.avatarView);
        }
    }
}
