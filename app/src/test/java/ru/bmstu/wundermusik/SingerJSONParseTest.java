package ru.bmstu.wundermusik;

import org.json.JSONObject;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

import ru.bmstu.wundermusik.models.Singer;
import ru.bmstu.wundermusik.models.parsers.*;


/**
 * Created by Nikita on 4/18/2016.
 */
public class SingerJSONParseTest {
    private final String SINGLE_SINGER_JSON = "{\"id\": \"1\",  \"username\": \"Singer name\",  \"avatar_url\": \"http://www.avatar.url.com/avatar.png\"}";
    private final String SINGLE_SINGER_AVATAR_URL ="http://www.avatar.url.com/avatar.png";
    private final long SINGLE_SINGER_ID = 359;
    private final String SINGLE_SINGER_NAME ="Singer name";

    @Test
    public void CreateNewSingerTest() {
        JsonParser<Singer> singerJsonParser = new SingerJsonParser();
        Singer singer = singerJsonParser.parseSingleObject(SINGLE_SINGER_JSON);
        assertEquals(SINGLE_SINGER_AVATAR_URL, singer.getAvatarUrl());
        assertEquals(SINGLE_SINGER_ID, singer.getId());
        assertEquals(SINGLE_SINGER_NAME, singer.getName());
    }
}
