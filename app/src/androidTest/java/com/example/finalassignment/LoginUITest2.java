package com.example.finalassignment;

import android.support.test.rule.ActivityTestRule;

import com.example.finalassignment.activities.LoginActivity;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class LoginUITest2 {

    @Rule

    public ActivityTestRule<LoginActivity> testRule = new ActivityTestRule<>(LoginActivity.class );
    private String mtext = "ccasdgfh";
    private String mtext1 = "dddsdgfhg";


    @Test
    public void TestUi() throws Exception {
        onView(withId(R.id.etUsername)).perform(typeText(mtext));
        closeSoftKeyboard();
        onView(withId(R.id.etPassword)).perform(typeText(mtext1));
        closeSoftKeyboard();



        onView(withId(R.id.btnLogin)).perform(click());

    }
}
