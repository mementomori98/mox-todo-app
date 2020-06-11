package mox.todo.app.util

import androidx.lifecycle.LiveData

/**
 * An asserted non-null liveData class so there is no need to use the !! operator
 */
open class LiveData<TType> : LiveData<TType>() {
    override fun getValue(): TType {
        return super.getValue()!!
    }
}