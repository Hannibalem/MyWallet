package revolut.com.mywallet.utils.store

interface State {

    enum class Status {
        NONE,
        LOADING,
        READY,
        ERROR
    }
}
