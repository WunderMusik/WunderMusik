package ru.bmstu.wundermusik.api.soundcloud;

import android.app.IntentService;
import android.content.Intent;


public class InvokerService extends IntentService {
    private static final String ACTION_AUTH = "ru.bmstu.wundermusik.api.soundcloud.action.AUTH";
    private static final String ACTION_QUERY = "ru.bmstu.wundermusik.api.soundcloud.action.QUERY";

    public InvokerService() {
        super("InvokerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();

            if (ACTION_AUTH.equals(action)) {
                handleActionAuth(intent);
            }
            else if (ACTION_QUERY.equals(action)) {
                handleActionQuery(intent);
            }
        }
    }

    private void handleActionAuth(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void handleActionQuery(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
