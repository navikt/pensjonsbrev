package no.nav.pensjon.brev.skribenten.common.diff

sealed class DiffEntry<T> {
    data class Insert<T>(val new: T) : DiffEntry<T>()
    data class Delete<T>(val old: T) : DiffEntry<T>()
    data class Replace<T>(val old: T, val new: T) : DiffEntry<T>()
    data class Unchanged<T>(val value: T) : DiffEntry<T>()

    val token: T get() = when (this) {
        is Insert -> new
        is Delete -> old
        is Replace -> new
        is Unchanged -> value
    }

    fun <R> toChange(transform: (T) -> R): Change<R>? = when (this) {
        is Insert -> Change.Insert(transform(new))
        is Delete -> Change.Delete(transform(old))
        is Replace -> Change.Replace(transform(old), transform(new))
        is Unchanged -> null
    }

    inline fun <reified R : T> narrow(): DiffEntry<R> = map { it as R }

    fun <R> map(transform: (T) -> R): DiffEntry<R> = when (this) {
        is Insert -> Insert(transform(new))
        is Delete -> Delete(transform(old))
        is Replace -> Replace(transform(old), transform(new))
        is Unchanged -> Unchanged(transform(value))
    }
}
