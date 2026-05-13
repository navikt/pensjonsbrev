package no.nav.pensjon.brev.skribenten.common.diff

sealed interface Change<T> {
    data class Insert<T>(val new: T) : Change<T>
    data class Delete<T>(val old: T) : Change<T>
    data class Replace<T>(val old: T, val new: T) : Change<T>
}

fun <T, U> Change<T>.map(transform: (T) -> U): Change<U> = when (this) {
    is Change.Insert -> Change.Insert(transform(new))
    is Change.Delete -> Change.Delete(transform(old))
    is Change.Replace -> Change.Replace(transform(old), transform(new))
}
