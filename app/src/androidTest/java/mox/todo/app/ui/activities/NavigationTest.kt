package mox.todo.app.ui.activities


import android.view.Gravity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.google.firebase.auth.FirebaseAuth
import mox.todo.app.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setup() {
        if (FirebaseAuth.getInstance().currentUser?.email != "test@test.com") {
            FirebaseAuth.getInstance().signOut()
            Thread.sleep(2000)
            FirebaseAuth.getInstance().signInWithEmailAndPassword("test@test.com", "asd123")
            Thread.sleep(2000)
            runOnUiThread { mActivityTestRule.activity.recreate() }
        }

    }

    @Test
    fun mainContentIsDisplayed() {
        onView(withId(R.id.main_content)).check(matches(isDisplayed()))
    }

    @Test
    fun drawerCanBeOpened() {
        onView(withId(R.id.drawer_layout))
            .check(matches(isClosed(Gravity.LEFT)))
            .perform(DrawerActions.open())
        onView(withId(R.id.nav_view))
            .check(matches(isDisplayed()))
    }

    @Test
    fun todosDisplayed() {
        onView(withId(R.layout.fragment_todos))
            .check(matches(isDisplayed()))
    }

}
