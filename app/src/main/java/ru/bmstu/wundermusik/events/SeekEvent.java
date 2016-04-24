package ru.bmstu.wundermusik.events;

/**
 * Created by Nikita on 4/24/2016.
 */
public class SeekEvent {
    private int seekValue;
    public SeekEvent(int seekValue) {
        this.seekValue = seekValue;
    }

    public int getSeekValue() {
        return seekValue;
    }
}
