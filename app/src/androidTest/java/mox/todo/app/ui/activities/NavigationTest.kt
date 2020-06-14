package mox.todo.app.ui.activities


import android.view.Gravity
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.NavigationViewActions.navigateTo
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import mox.todo.app.R
import mox.todo.app.authenticateTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    private fun navigate(@IdRes id: Int) {
        onView(withId(R.id.drawer_layout))
            .perform(DrawerActions.open())
        onView(withId(R.id.nav_view))
            .perform(navigateTo(id))
    }

    @Before
    fun setup() {
        authenticateTest(activityTestRule.activity)
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
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
    fun todosDisplayedByDefault() {
        onView(withId(R.id.todos_layout))
            .check(matches(isDisplayed()))
    }

    @Test
    fun navigateToSettings() {
        navigate(R.id.nav_settings)

        onView(withId(R.id.settings_layout))
            .check(matches(isDisplayed()))
    }

    @Test
    fun navigateToNewList() {
        navigate(R.id.nav_new_list)

        intended(hasComponent(CreateListActivity::class.java.name))
    }

}
