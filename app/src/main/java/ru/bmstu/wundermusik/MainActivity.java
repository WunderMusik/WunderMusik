package ru.bmstu.wundermusik;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import ru.bmstu.wundermusik.adapters.TrackListAdapter;
import ru.bmstu.wundermusik.api.soundcloud.ApiCallback;
import ru.bmstu.wundermusik.api.soundcloud.Invoker;
import ru.bmstu.wundermusik.models.Track;
import ru.bmstu.wundermusik.models.parsers.JsonParser;
import ru.bmstu.wundermusik.models.parsers.TrackJsonParser;

import static ru.bmstu.wundermusik.utils.UtilSystem.isOnline;
import static ru.bmstu.wundermusik.utils.UtilSystem.displayMessage;

public class MainActivity extends AppCompatActivity {

//    LinkedList для сохранения порядка вставки
    private List<Track> trackList = new LinkedList<>();
    private TrackListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        ListView listView = (ListView)findViewById(R.id.track_list_view);
        mAdapter = new TrackListAdapter(this, trackList);
        listView.setAdapter(mAdapter);
    }

    public void searchButtonClick(View v) {
        String searchString = ((TextView) findViewById(R.id.search_field)).getText().toString();
        // FIXME: 21.04.16 Какие-нибудь преобразования строки ввода
        if (isOnline(this)) {
            askTracksByName(searchString);
        } else {
            displayMessage(findViewById(android.R.id.content), getResources().getString(R.string.no_net_status));
        }
    }

    private void addTrackList(List<Track> tracks) {
        trackList.clear();
        trackList.addAll(tracks);
        mAdapter.notifyDataSetChanged();
    }

    private void addSingleTrack(Track track) {
        trackList.add(0, track);
        mAdapter.notifyDataSetChanged();
    }

    private void askTracksByName(String trackName) {
        Invoker invoker = Invoker.getInstance(this);
        invoker.queryTracksByName(trackName, new ApiCallback() {
            @Override
            public void onResult(String data) {
                JsonParser<Track> trackJsonParser = new TrackJsonParser();
                List<Track> tracks = trackJsonParser.parseMultipleObjects(data);
                addTrackList(tracks);
                for (Track track : tracks) {
                    Log.i("TRACKS_RECEIVED", track.getTitle());
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // FIXME: 21.04.16 Удалить?
    private void askTrack(long trackId) {
        Invoker invoker = Invoker.getInstance(this);
        invoker.queryTrack(trackId, new ApiCallback() {
            @Override
            public void onResult(String data) {
                JsonParser<Track> trackJsonParser = new TrackJsonParser();
                Track track = trackJsonParser.parseSingleObject(data);
                addSingleTrack(track);
                if (track != null)
                    Toast.makeText(MainActivity.this, "track received " + track.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int code, String errorMsg) {

            }
        });
    }
}
