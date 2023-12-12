package dev.astler.catlib.model

sealed class CatResource<T> {
    class Success<T>(val data: T) : CatResource<T>()
    class Failed<T>(val message: String) : CatResource<T>()
}