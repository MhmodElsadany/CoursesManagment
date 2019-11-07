package cources.example.com.couresmanger;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import cources.example.com.couresmanger.LoginNdSignup.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Google       Company on 14/03/2018.
 */

@RunWith(AndroidJUnit4.class)
public class TestDemo {
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityActivityTestRule() {


        onView(withId(R.id.textor)).check(matches(withText("OR")));

    }
}
