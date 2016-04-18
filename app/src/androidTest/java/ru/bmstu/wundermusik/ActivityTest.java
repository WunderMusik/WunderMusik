package ru.bmstu.wundermusik;

import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;
import org.junit.Ignore;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@Ignore
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
        UiDevice uiDevice = UiDevice.getInstance(getInstrumentation());
        try {
            uiDevice.sleep();
            Thread.sleep(1000);
            uiDevice.wakeUp();
            Thread.sleep(1000);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
