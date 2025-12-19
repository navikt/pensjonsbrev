package no.nav.pensjon.brev.skribenten.usecase

sealed class Result<out T, out E> {
    data class Success<T>(val value: T) : Result<T, Nothing>()
    data class Failure<E>(val error: E) : Result<Nothing, E>()

    companion object {
        fun <T> success(value: T): Result<T, Nothing> = Success(value)
        fun <E> failure(error: E): Result<Nothing, E> = Failure(error)
    }

    val isSuccess: Boolean
        get() = this is Success

    fun <TResult> then(transform: (T) -> TResult): Result<TResult, E> =
        when (this) {
            is Success -> Success(transform(this.value))
            is Failure -> this
        }

    inline fun onSuccess(action: (T) -> Unit): Result<T, E> {
        if (this is Success) {
            action(this.value)
        }
        return this
    }

    inline fun onError(action: (E) -> Unit): Result<T, E> {
        if (this is Failure) {
            action(this.error)
        }
        return this
    }
}