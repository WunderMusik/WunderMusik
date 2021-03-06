package ru.bmstu.wundermusik;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
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

/**
 * Экран списка треков в приложении
 * После запуска экрана пользователю отображается строка поиска
 * Результаты поиска отображаются в виде списка с помощью {@link TrackListAdapter TrackListAdapter}
 * @author Max
 * @author Eugene
 */
public class MainActivity extends AppCompatActivity {

    /**
     * LinkedList нужен для сохранения порядка вставки треков
     */
    private List<Track> trackList = new LinkedList<>();
    private TrackListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        if (savedInstanceState != null) {
            trackList = (List<Track>) savedInstanceState.getSerializable(PlayerActivity.TRACK_LIST);
        }
        ListView listView = (ListView)findViewById(R.id.track_list_view);
        mAdapter = new TrackListAdapter(this, trackList);
        listView.setAdapter(mAdapter);

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View container, int position, long id) {
                Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
                intent.putExtra(PlayerActivity.TRACK_LIST, (Serializable) trackList);
                intent.putExtra(PlayerActivity.CURRENT_TRACK, position);
                MainActivity.this.startActivity(intent);
            }
        };
        listView.setOnItemClickListener(itemClickListener);
    }

    /**
     * Обработка нажатия на кнопку "Поиск"
     * @param v - View элемента, на который произошел клик
     */
    public void searchButtonClick(View v) {
        String searchString = ((TextView) findViewById(R.id.search_field)).getText().toString();
        if (isOnline(this)) {
            askTracksByName(searchString);
        } else {
            displayMessage(findViewById(android.R.id.content), getResources().getString(R.string.no_net_status));
        }
    }

    /**
     * Заполнение запрошенного списка треков в адаптер
     * @param tracks
     */
    private void addTrackList(List<Track> tracks) {
        trackList.clear();
        trackList.addAll(tracks);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Запрос за списком треков по поисковой строке
     * @param trackName - строка для поиска треков в сервисе
     */
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable(PlayerActivity.TRACK_LIST, (Serializable) trackList);
    }
}
