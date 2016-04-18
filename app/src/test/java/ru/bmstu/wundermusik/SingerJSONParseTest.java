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
    private final String SINGLE_SINGER_JSON = "{\n" +
            "  \"id\": \"359\",\n" +
            "  \"username\": \"Singer name\",\n" +
            "  \"avatar_url\": \"http://www.avatar.url.com/avatar.png\"\n" +
            "}";
    private final String SINGLE_SINGER_AVATAR_URL ="Singer name";
    private final long SINGLE_SINGER_ID = 359;
    private final String SINGLE_SINGER_NAME ="http://www.avatar.url.com/avatar.png";

    @Test
    public void CreateNewSingerTest() {
        JsonParser<Singer> singerJsonParser = new SingerJsonParser();
        Singer singer = singerJsonParser.parseSingleObject(SINGLE_SINGER_JSON);
        assertEquals(singer.getAvatarUrl(), SINGLE_SINGER_AVATAR_URL);
        assertEquals(singer.getId(), SINGLE_SINGER_ID);
        assertEquals(singer.getName(), SINGLE_SINGER_NAME);
    }


}
