package ru.bmstu.wundermusik.api.soundcloud;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import java.util.UUID;

/**
 * Created by max on 22.03.16.
 */
public class Invoker {
    public final static String KEY_RESULT_CODE = "result_code";
    public final static String KEY_QUERY_TYPE = "query_type";

    private final static Object lock = new Object();
    private static Invoker instance;

    private Context context;

    private Invoker(Context context) {
        this.context = context;
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

    public void queryTrack(long trackId) {
        Intent intent = makeIntent(InvokerService.TYPE_GET_TRACK);
        intent.putExtra(InvokerService.KEY_TRACK_ID, trackId);
        context.startService(intent);
    }

    public void queryResource(String uri) {
        Intent intent = makeIntent(InvokerService.TYPE_GET_RESOURCE);
        intent.putExtra(InvokerService.KEY_RESOURCE_URI, uri);
        context.startService(intent);
    }

    private Intent makeIntent(final String queryType) {
        ResultReceiver serviceCallback = new ResultReceiver(null) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                handleResponse(queryType, resultCode, resultData);
            }
        };

        Intent intent = new Intent(context, InvokerService.class);
        intent.putExtra(InvokerService.KEY_QUERY_CALLBACK, serviceCallback);
        intent.putExtra(InvokerService.KEY_QUERY_TYPE, queryType);
        return intent;
    }

    private void handleResponse(String queryType, int resultCode, Bundle resultData) {
        Intent resultBroadcast = new Intent(queryType);
        resultBroadcast.putExtra(KEY_RESULT_CODE, resultCode);
        resultBroadcast.putExtras(resultData);
        context.sendBroadcast(resultBroadcast);
    }
}
