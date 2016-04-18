package ru.bmstu.wundermusik;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import ru.bmstu.wundermusik.MusicPlayer.MusicPlayer;
import ru.bmstu.wundermusik.api.soundcloud.ApiCallback;
import ru.bmstu.wundermusik.api.soundcloud.Invoker;
import ru.bmstu.wundermusik.models.Track;

import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button);
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
                    startActivity(intent);
                }
            });
        }

        Button btn = (Button) findViewById(R.id.superButton);
        if (btn != null) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView txtView = (TextView) findViewById(R.id.superUsefulTextbox);
                    txtView.setText("CHANGED TEXT");
                }

            });
        }

        btn = (Button) findViewById(R.id.playMusicButton);
        if (btn != null){
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayList<Track> tracks = new ArrayList<Track>();
                    Track track = new Track();
                    track.setStreamUrl("http://picosong.com/cdn/b8a5e824821af95194c3cf6e1f0202d7.mp3");
                    tracks.add(track);
                    track = new Track();
                    track.setStreamUrl("http://picosong.com/cdn/77ac3e92040cb44de28c06a3a16dce43.mp3");
                    tracks.add(track);
                    MusicPlayer mp = new MusicPlayer();
                    mp.setTrackList(tracks);

                    try{
                    mp.playSongAt(0);}
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void askTrack(long trackId) {
        Invoker invoker = Invoker.getInstance(this);
        invoker.queryTrack(trackId, new ApiCallback() {
            @Override
            public void onResult(String data) {
                Toast.makeText(MainActivity.this, "track received", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                Toast.makeText(MainActivity.this, "track received", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
