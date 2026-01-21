package no.nav.pensjon.brev.skribenten.usecase

sealed class Outcome<out T, out E> {
    data class Success<T>(val value: T) : Outcome<T, Nothing>()
    data class Failure<E>(val error: E) : Outcome<Nothing, E>()

    companion object {
        fun <T> success(value: T): Outcome<T, Nothing> = Success(value)
        fun <E> failure(error: E): Outcome<Nothing, E> = Failure(error)
    }

    val isSuccess: Boolean
        get() = this is Success

    fun <TResult> then(transform: (T) -> TResult): Outcome<TResult, E> =
        when (this) {
            is Success -> Success(transform(this.value))
            is Failure -> this
        }

    inline fun onSuccess(action: (T) -> Unit): Outcome<T, E> {
        if (this is Success) {
            action(this.value)
        }
        return this
    }

    inline fun onError(action: (E) -> Unit): Outcome<T, E> {
        if (this is Failure) {
            action(this.error)
        }
        return this
    }

    inline fun onError(ignore: (E) -> Boolean, action: (E) -> Unit): Outcome<T, E> {
        if (this is Failure && !ignore(this.error)) {
            action(this.error)
        }
        return this
    }
}