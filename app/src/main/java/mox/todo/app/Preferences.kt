package mox.todo.app

class Preferences private constructor() {

    companion object {
        val instance: Preferences = Preferences()
    }

    fun getTheme(): Int {
        return R.style.AppTheme_Light
    }

}