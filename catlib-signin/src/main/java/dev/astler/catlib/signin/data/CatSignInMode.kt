package dev.astler.catlib.signin.data

enum class CatSignInMode {
    MANDATORY,
    OPTIONAL,
    OPTIONAL_JUMP,
    REGISTER;

    companion object {
        fun String.fromString() = valueOf(this)
    }
}