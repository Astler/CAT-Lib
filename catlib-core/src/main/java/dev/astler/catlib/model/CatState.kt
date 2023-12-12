package dev.astler.catlib.model

sealed class CatState<T>(val data: T? = null) {

    class Loading<T> : CatState<T>()

    data class Success<T>(val successData: T) : CatState<T>(successData)

    data class Error<T>(val message: String?) : CatState<T>()

    fun isLoading(): Boolean = this is Loading

    fun isSuccessful(): Boolean = this is Success

    fun isFailed(): Boolean = this is Error

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