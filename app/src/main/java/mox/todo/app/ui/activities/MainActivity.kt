package mox.todo.app.ui.activities

import android.content.Intent
import android.graphics.BlendMode
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.content.res.AppCompatResources
import androidx.navigation.ui.AppBarConfiguration
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import mox.todo.app.R
import mox.todo.app.ui.fragments.SettingsFragment
import mox.todo.app.ui.fragments.TodosFragment
import mox.todo.app.ui.viewmodels.MainViewModel
import kotlin.reflect.KClass

class MainActivity : ActivityBase() {

    private val allTodosId = 1000000

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        setupNavigation()
        if (intent.extras?.getBoolean("settings") == true)
            loadFragment(SettingsFragment())
        else
            loadFragment(TodosFragment())
    }

    private fun loadFragment(fragment: Fragment) {
        title = "Todos"
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_content, fragment)
        transaction.commit()
    }

    private fun <T> loadActivity(activity: KClass<T>, bundle: Bundle? = null) where T : Any {
        val intent = Intent(
            applicationContext,
            activity.java
        )
        bundle?.let(intent::putExtras)
        startActivity(intent)
    }

    private fun setupNavigation() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_header_title, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected)

        observeDrawerListItems()
    }

    private fun observeDrawerListItems() = viewModel.todoLists().observe(this, Observer { lists ->
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        val menu = navigationView.menu
        menu.removeGroup(R.id.menu_todo_lists)
        menu.add(R.id.menu_todo_lists, allTodosId, Menu.NONE, resources.getString(R.string.all_todos))
        lists.forEach { list ->
            val item = menu.add(R.id.menu_todo_lists, list.key, list.key + 1, list.name)
            val icon = resources.getDrawable(mapIcon(list.color), null)
            item.icon = icon
        }
    })

    private fun onNavigationItemSelected(item: MenuItem): Boolean {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        viewModel.listId = null
        when (item.itemId) {
            allTodosId -> loadFragment(TodosFragment())
            R.id.nav_settings -> loadFragment(SettingsFragment())
            R.id.nav_new_list -> loadActivity(CreateListActivity::class)
            R.id.nav_logout -> logout()
            else -> loadAllTodos(item.itemId)
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun loadAllTodos(listId: Int) {
        viewModel.listId = listId
        loadFragment(TodosFragment(viewModel.listId) {
            viewModel.listId = null
            loadFragment(TodosFragment())
        })
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, StartupActivity::class.java))
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (viewModel.listId != null)
            menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_delete_list -> false
        else -> super.onOptionsItemSelected(item)
    }

    private fun mapIcon(listColor: Int) = when (listColor) {
        0 -> R.drawable.ic_list_blue
        1 -> R.drawable.ic_list_red
        2 -> R.drawable.ic_list_yellow
        3 -> R.drawable.ic_list_green
        else -> R.id.blue
    }

}
