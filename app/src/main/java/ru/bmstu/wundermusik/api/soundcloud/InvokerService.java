package ru.bmstu.wundermusik.api.soundcloud;

import android.app.IntentService;
import android.content.Intent;
import android.os.ResultReceiver;

import java.util.HashMap;
import java.util.Map;

import ru.bmstu.wundermusik.api.soundcloud.query.ResourceQuery;
import ru.bmstu.wundermusik.api.soundcloud.query.TrackQuery;


public class InvokerService extends IntentService {
    public final static String KEY_QUERY_TYPE = "query_type";
    public final static String KEY_QUERY_CALLBACK = "key_request_callback";

    public final static String TYPE_GET_TRACK = "type_get_track";
    public final static String KEY_TRACK_ID = "key_track_id";

    public final static String TYPE_GET_RESOURCE = "type_resource";
    public final static String KEY_RESOURCE_URI = "key_resource_uri";

    private ResultReceiver queryCallback;
    public InvokerService() {
        super("InvokerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String queryType = intent.getStringExtra(KEY_QUERY_TYPE);
            queryCallback = intent.getParcelableExtra(KEY_QUERY_CALLBACK);

            switch (queryType) {
                case TYPE_GET_TRACK:
                    handleGetTrack(intent);
                case TYPE_GET_RESOURCE:
                    handleGetResource(intent);
            }
        }
    }

    private void handleGetTrack(Intent intent) {
        long trackId = intent.getLongExtra(KEY_TRACK_ID, 0);
        if (trackId != 0) {
            TrackQuery.execute(queryCallback, trackId);
        }
    }

    private void handleGetResource(Intent intent) {
        String uri = intent.getStringExtra(KEY_RESOURCE_URI);
        ResourceQuery.execute(queryCallback, uri);
    }
}
