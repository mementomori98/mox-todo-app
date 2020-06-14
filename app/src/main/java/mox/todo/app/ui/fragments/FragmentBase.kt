package mox.todo.app.ui.fragments

import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders

abstract class FragmentBase<TViewModel> : Fragment() where TViewModel : ViewModel {

    protected lateinit var viewModel: TViewModel

    protected inline fun <reified T> initViewModel() where T : TViewModel {
        viewModel = ViewModelProviders.of(this).get(T::class.java)
    }

    protected fun getRoot(
        @LayoutRes layout: Int,
        container: ViewGroup?,
        inflater: LayoutInflater
    ): View {
        val context = ContextThemeWrapper(activity, activity?.theme)

        val localInflater = inflater.cloneInContext(context)
        return localInflater.inflate(layout, container, false)
    }

    protected fun setBooleanPreference(name: String, value: Boolean) = getPreferences().edit()
        .putBoolean(name, value).apply()

    protected fun setStringPreference(name: String, value: String) = getPreferences().edit()
        .putString(name, value).apply()

    protected fun getBooleanPreference(name: String, default: Boolean = false) = getPreferences()
        .getBoolean(name, default)!!

    protected fun getStringPreference(name: String, default: String = "") = getPreferences()
        .getString(name, default)!!

    private fun getPreferences() = requireActivity()
        .getSharedPreferences("pref", 0)

}