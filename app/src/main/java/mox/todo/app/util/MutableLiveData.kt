package mox.todo.app.util

/**
 * Extends the mox.app.util.LiveData class so LiveData can be used as a super class
 */
class MutableLiveData<TType> : LiveData<TType>() {

    public override fun setValue(value: TType) {
        super.setValue(value)
    }

}