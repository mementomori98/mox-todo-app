package mox.todo.app.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mox.todo.app.R
import mox.todo.app.ui.viewmodels.SettingsViewModel

class SettingsFragment : FragmentBase<SettingsViewModel>() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initViewModel<SettingsViewModel>()
        return getRoot(R.layout.fragment_settings, container, inflater)
    }

}
