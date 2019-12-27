package com.example.finalassignment;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.finalassignment.activities.DashboardActivity;
import com.example.finalassignment.activities.LoginActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest

public class LoginUITest {

    @Rule

    public ActivityTestRule<LoginActivity> testRule = new ActivityTestRule<>(LoginActivity.class);
    private String mtext = "sumnima11";
    private String mtext1 = "sumnima11";

    @Test
    public void TestUi() throws Exception {
        onView(withId(R.id.etUsername)).perform(typeText(mtext));
        closeSoftKeyboard();
        onView(withId(R.id.etPassword)).perform(typeText(mtext1));
        closeSoftKeyboard();
        onView(withId(R.id.btnLogin)).perform(click());

    }
}
