package ru.bmstu.wundermusik.api.soundcloud;

/**
 * Created by max on 22.03.16.
 */
public class Routes {
    public static final String BASE_URI = "https://soundcloud.com";
    public static final String BASE_API_URI = "https://api.soundcloud.com";

    public static final String AUTH = "/connect";
    public static final String OAUTH_TOKEN = "/oauth2/token";

    public static final String TRACK_LIST = "/tracks";
    public static final String TRACK = "/tracks/{id}";
}
