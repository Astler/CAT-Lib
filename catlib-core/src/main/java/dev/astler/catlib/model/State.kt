package dev.astler.catlib.model

import dev.astler.catlib.utils.infoLog


sealed class State<T> {
    class Loading<T> : State<T>()

    data class Success<T>(val data: T) : State<T>()

    data class Error<T>(val message: String) : State<T>()

    fun isLoading(): Boolean = this is Loading

    fun isSuccessful(): Boolean = this is Success

    fun isFailed(): Boolean = this is Error

    companion object {

        fun <T> loading() = Loading<T>()

        fun <T> success(data: T) = Success(data)

        fun <T> error(message: String) = Error<T>(message)

        fun <T> fromResource(resource: Resource<T>): State<T> = when (resource) {
            is Resource.Success -> success(resource.data)
            is Resource.Failed -> {
                infoLog(resource.message)
                error(resource.message)
            }
        }
    }
}

sealed class Resource<T> {
    class Success<T>(val data: T) : Resource<T>()
    class Failed<T>(val message: String) : Resource<T>()
}