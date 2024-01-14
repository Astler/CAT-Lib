package dev.astler.catlib.signin.data

enum class CatSignInMode(public val id: Int) {
    MANDATORY(0),
    OPTIONAL(1),
    OPTIONAL_JUMP(2),
    REGISTER(3);

    companion object {
        fun String.fromString() = valueOf(this)
    }
}