package ru.bmstu.wundermusik.api.soundcloud;

import android.content.Context;
import android.content.Intent;

import java.util.UUID;

/**
 * Created by max on 22.03.16.
 */
public class Invoker {
    private Context context;
    private String clientId, clientSecret;

    private Invoker(Context context, String clientId, String clientSecret) {
        this.context = context;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    private final static Object lock = new Object();
    private static Invoker instance;

    public static Invoker getInstance(Context context, String clientId, String clientSecret) {
        synchronized (lock) {
            if (instance == null) {
                instance = new Invoker(context, clientId, clientSecret);
            }
        }
        return instance;
    }

    public boolean connect() {
        return false;
    }

    public AccessToken queryToken() {
        return new AccessToken("", "");
    }

    public String queryTracks() {
        return "{}";
    }

    public String queryTrack(long trackId) {
        return "{}";
    }

    public String queryResource(String uri) {
        return "";
    }

    private class Result {
        private long requestId;
        private Intent intent;
        private boolean isPending;

        private Result(Intent intent, boolean isPending) {
            this.requestId = UUID.randomUUID().getLeastSignificantBits();
            this.intent = intent;
            this.isPending = isPending;
        }

        public long getRequestId() {
            return requestId;
        }

        public Intent getIntent() {
            return intent;
        }

        public boolean isPending() {
            return isPending;
        }
    }
}
