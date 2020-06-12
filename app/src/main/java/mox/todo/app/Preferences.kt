package mox.todo.app

import androidx.annotation.StyleRes

class Preferences private constructor() {

    companion object {
        val instance: Preferences = Preferences()
    }

    fun getTheme(): Int {
        return R.style.AppTheme_Dark
    }

}