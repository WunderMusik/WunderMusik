package ru.bmstu.wundermusik;
import org.json.JSONObject;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

import ru.bmstu.wundermusik.models.Singer;
import ru.bmstu.wundermusik.models.Track;
import ru.bmstu.wundermusik.models.parsers.*;


/**
 * Created by Nikita on 4/19/2016.
 */
public class TrackJSONParseTest {
    private final String SINGLE_TRACK_JSON = "{\n" +
            "  \"streamable\": true,\n" +
            "  \"original_content_size\": 1034274,\n" +
            "  \"original_format\": \"mp3\",\n" +
            "  \"user\": {\n" +
            "    \"avatar_url\": \"https://i1.sndcdn.com/avatars-000063045564-sauf8o-large.jpg\",\n" +
            "    \"id\": 40592234,\n" +
            "    \"kind\": \"user\",\n" +
            "    \"permalink_url\": \"http://soundcloud.com/shimaa-sami\",\n" +
            "    \"uri\": \"https://api.soundcloud.com/users/40592234\",\n" +
            "    \"username\": \"Shimaa Sami\",\n" +
            "    \"permalink\": \"shimaa-sami\",\n" +
            "    \"last_modified\": \"2015/10/09 11:16:04 +0000\"\n" +
            "  },\n" +
            "  \"id\": 90771495,\n" +
            "  \"title\": \"Song Title\",\n" +
            "  \"duration\": 129265,\n" +
            "  \"stream_url\": \"https://api.soundcloud.com/tracks/90771495/stream\"\n" +
            "}";

    private final long SINGLE_TRACK_ID = 90771495;
    private final String SINGLE_TRACK_TITLE = "Song Title";
    private final long SINGLE_TRACK_DURATION = 129265;
    private final String SINGLE_TRACK_FORMAT = "mp3";
    private final long SINGLE_TRACK_CONTENT_SIZE = 1034274;
    private final String SINGLE_TRACK_STREAM_URL = "https://api.soundcloud.com/tracks/90771495/stream";
    //private final Singer SINGLE_TRACK_SINGER = null;


    @Test
    @Ignore
    public void CreateNewTrackTest() {
        JsonParser<Track> trackJsonParser = new TrackJsonParser();
        Track track = trackJsonParser.parseSingleObject(SINGLE_TRACK_JSON);
        assertNotNull(track);
        assertEquals(SINGLE_TRACK_ID, track.getId());
        assertEquals(SINGLE_TRACK_TITLE, track.getTitle());
        assertEquals(SINGLE_TRACK_DURATION, track.getDuration());
        assertEquals(SINGLE_TRACK_FORMAT, track.getFormat());
        assertEquals(SINGLE_TRACK_CONTENT_SIZE, track.getContentSize());
        assertEquals(SINGLE_TRACK_STREAM_URL, track.getStreamUrl());
        //assertEquals(SINGLE_TRACK_SINGER, track.getId());

    }


}
