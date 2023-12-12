package dev.astler.catlib.model

sealed class CatState<T>(val data: T? = null) {

    class Loading<T> : CatState<T>()

    data class Success<T>(val successData: T) : CatState<T>(successData)

    data class Error<T>(val message: String?) : CatState<T>()

    fun isLoading(actionIfTrue: (() -> Unit)? = null): Boolean {
        return (this is Loading).also {
            if (it) {
                actionIfTrue?.invoke()
            }
        }
    }

    fun isSuccessful(actionIfTrue: ((T) -> Unit)? = null): Boolean {
        return (this is Success).also {
            if (it) {
                actionIfTrue?.invoke((this as Success<T>).successData)
            }
        }
    }

    fun isFailed(actionIfTrue: ((String?) -> Unit)? = null): Boolean {
        return (this is Error).also {
            if (it) {
                actionIfTrue?.invoke((this as Error<T>).message)
            }
        }
    }

    companion object {

        fun <T> loading() = Loading<T>()

        fun <T> success(data: T) = Success(data)

        fun <T> error(message: String?) = Error<T>(message)

        fun <T> fromResource(resource: CatResource<T>): CatState<T> = when (resource) {
            is CatResource.Success -> success(resource.data)
            is CatResource.Failed -> {
                error(resource.message)
            }
        }
    }
}