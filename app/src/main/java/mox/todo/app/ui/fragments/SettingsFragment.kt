package mox.todo.app.ui.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import mox.todo.app.R
import mox.todo.app.ui.viewmodels.SettingsViewModel
import java.util.*

class SettingsFragment : FragmentBase<SettingsViewModel>() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initViewModel<SettingsViewModel>()
        val root = getRoot(R.layout.fragment_settings, container, inflater)
        setupViews(root)
        return root
    }

    private fun setupViews(root: View) {
        val theme = root.findViewById<Switch>(R.id.theme)
        theme.isChecked = getStringPreference("theme", "light") == "dark"
        theme.setOnCheckedChangeListener { _, checked -> setTheme(checked) }

        val language = root.findViewById<Spinner>(R.id.language)
        setupLanguage(language)

        val compact = root.findViewById<Switch>(R.id.compact)
        compact.isChecked = getBooleanPreference("compact")
        compact.setOnCheckedChangeListener { _, checked -> setBooleanPreference("compact", checked) }
    }

    private fun setupLanguage(language: Spinner) {
        language.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1, listOf("English", "Pirate")
        )
        val index = if (getStringPreference("lang", "en") == "en") 0 else 1
        language.setSelection(index)
        language.onItemSelectedListener = languageListener()
    }

    private fun setTheme(dark: Boolean) {
        setStringPreference("theme", if (dark) "dark" else "light")
        updateActivity()
    }

    private fun updateActivity() {
        activity?.intent?.putExtra("settings", true)
        activity?.recreate()
    }

    private fun languageListener() = object : AdapterView.OnItemSelectedListener {
        private var first = true

        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val lang = if (position == 0) "en" else "eo"
            setStringPreference("lang", lang)
            val locale = Locale(lang)
            Locale.setDefault(locale)
            val configuration = Configuration()
            configuration.locale = locale
            requireActivity().baseContext.resources.updateConfiguration(
                configuration,
                requireActivity().baseContext.resources.displayMetrics
            )
            if (!first)
                updateActivity()
            else first = false
        }
    }
}
