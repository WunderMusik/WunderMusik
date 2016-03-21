package ru.bmstu.wundermusik;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.List;

import ru.bmstu.wundermusik.api.soundcloud.ApiCallback;
import ru.bmstu.wundermusik.api.soundcloud.Invoker;
import ru.bmstu.wundermusik.models.Track;
import ru.bmstu.wundermusik.models.parsers.JsonParser;
import ru.bmstu.wundermusik.models.parsers.TrackJsonParser;

public class MainActivity extends AppCompatActivity {



    private void askTrack(long trackId) {
        Invoker invoker = Invoker.getInstance(this);
        invoker.queryTrack(trackId, new ApiCallback() {
            @Override
            public void onResult(String data) {
                JsonParser<Track> trackJsonParser = new TrackJsonParser();
                Track track = trackJsonParser.parseSingleObject(data);
                if (track != null)
                    Toast.makeText(MainActivity.this, "track received " + track.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int code, String errorMsg) {

            }
        });
    }

    private void askTracksByName(String trackName) {
        Invoker invoker = Invoker.getInstance(this);
        invoker.queryTracksByName(trackName, new ApiCallback() {
            @Override
            public void onResult(String data) {
                JsonParser<Track> trackJsonParser = new TrackJsonParser();
                List<Track> tracks = trackJsonParser.parseMultipleObjects(data);
                Toast.makeText(MainActivity.this, "tracks were received " + tracks.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                Toast.makeText(MainActivity.this, "tracks were received", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
