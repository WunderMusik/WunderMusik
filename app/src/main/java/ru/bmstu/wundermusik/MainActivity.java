package ru.bmstu.wundermusik;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import ru.bmstu.wundermusik.api.soundcloud.ApiCallback;
import ru.bmstu.wundermusik.api.soundcloud.Invoker;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askTrack(13158665);
            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
