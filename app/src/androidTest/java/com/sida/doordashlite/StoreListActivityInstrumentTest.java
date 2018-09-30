package com.sida.doordashlite;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.sida.doordashlite.activity.StoreListActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;



import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class StoreListActivityInstrumentTest {
    @Rule
    public ActivityTestRule<StoreListActivity> activityTestRule =
            new ActivityTestRule<>(StoreListActivity.class);

    @Test
    public void validateRecyclerViewLoading() {
        onView(withId(R.id.rv_storelist)).check(matches(isDisplayed()));
    }
}
