package ru.bmstu.wundermusik;

import org.junit.Before;

import android.support.test.InstrumentationRegistry;

import android.test.ActivityInstrumentationTestCase2;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class ActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity mainActivity;
    private final String MESSAGE_BEFORE = "TEXT #1";
    private final String MESSAGE_AFTER = "CHANGED TEXT";

    public ActivityTest() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mainActivity = getActivity();
    }


    public void testDoSomething_MainActivity() {
        onView(withId(R.id.superUsefulTextbox)).check(matches(withText(MESSAGE_BEFORE)));
        onView(withId(R.id.superButton)).perform(click());
        onView(withId(R.id.superUsefulTextbox)).check(matches(withText(MESSAGE_AFTER)));
    }

}
