package ru.bmstu.wundermusik.api.soundcloud;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.ResultReceiver;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import ru.bmstu.wundermusik.api.soundcloud.query.AbstractQuery;
import ru.bmstu.wundermusik.api.soundcloud.utils.StatusCode;

/**
 * Created by max on 22.03.16.
 */
public class Invoker {
    public final static String KEY_RESULT_CODE = "result_code";
    public final static String KEY_QUERY_ID = "query_id";
    public final static String KEY_QUERY_TYPE = "query_type";
    public final static String QUERY_ACTION = "query_action";

    private final static Object lock = new Object();
    private static Invoker instance;

    private Context context;

    private Invoker(Context context) {
        this.context = context;
        IntentFilter trackFilter = new IntentFilter(Invoker.QUERY_ACTION);
        context.registerReceiver(mApiReceiver, trackFilter);
    }

    public static Invoker getInstance(Context context) {
        synchronized (lock) {
            if (instance == null) {
                instance = new Invoker(context);
            }
        }
        return instance;
    }

    private long generateRequestID() {
        return UUID.randomUUID().getLeastSignificantBits();
    }

    public void queryTrack(long trackId, ApiCallback callback) {
        Intent intent = makeIntent(InvokerService.TYPE_GET_TRACK);
        intent.putExtra(InvokerService.KEY_TRACK_ID, trackId);
        context.startService(intent);

        long queryId = intent.getLongExtra(KEY_QUERY_ID, -1);
        queryMap.put(queryId, callback);
    }

    public void queryResource(String uri, ApiCallback callback) {
        Intent intent = makeIntent(InvokerService.TYPE_GET_RESOURCE);
        intent.putExtra(InvokerService.KEY_RESOURCE_URI, uri);
        context.startService(intent);

        long queryId = intent.getLongExtra(KEY_QUERY_ID, -1);
        queryMap.put(queryId, callback);
    }

    private Intent makeIntent(final String queryType) {
        final long queryId = generateRequestID();
        ResultReceiver serviceCallback = new ResultReceiver(null) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                handleResponse(queryId, queryType, resultCode, resultData);
            }
        };
        Intent intent = new Intent(context, InvokerService.class);
        intent.putExtra(InvokerService.KEY_QUERY_CALLBACK, serviceCallback);
        intent.putExtra(InvokerService.KEY_QUERY_TYPE, queryType);
        intent.putExtra(KEY_QUERY_ID, queryId);
        return intent;
    }

    private void handleResponse(long queryId, String queryType, int resultCode, Bundle resultData) {
        Intent resultBroadcast = new Intent(QUERY_ACTION);
        resultBroadcast.putExtra(KEY_RESULT_CODE, resultCode);
        resultBroadcast.putExtra(KEY_QUERY_ID, queryId);
        resultBroadcast.putExtra(KEY_QUERY_TYPE, queryId);
        resultBroadcast.putExtras(resultData);
        context.sendBroadcast(resultBroadcast);
    }

    static Map<Long, ApiCallback> queryMap = new HashMap<>();

    private BroadcastReceiver mApiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long queryId = intent.getLongExtra(Invoker.KEY_QUERY_ID, -1);
            ApiCallback callback = queryMap.get(queryId);

            int statusCode = intent.getIntExtra(Invoker.KEY_RESULT_CODE, -1);
            if (statusCode == StatusCode.OK) {
                String data = intent.getStringExtra(AbstractQuery.KEY_DATA);
                callback.onResult(data);
            }
            else {
                String errorMsg = intent.getStringExtra(AbstractQuery.KEY_ERROR);
                callback.onFailure(statusCode, errorMsg);
            }
        }
    };
}
