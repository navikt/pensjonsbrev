package no.nav.pensjon.brev.skribenten.common.diff

class EditScriptCursor<T : Any>(private val tokens: List<T>, edits: List<EditOperation<T>>) {
    init {
        require(edits.distinctBy { it.position }.size == edits.size) { "Expected edits to only have unique position references" }
    }
    private val edits = edits.associateBy { it.position }
    private var currentIndex = 0
    val hasNext: Boolean get() = currentIndex < tokens.size

    fun peek(): T? = tokens.getOrNull(currentIndex)

    fun consume(): Pair<T, EditOperation<T>?> = Pair(tokens[currentIndex], edits[currentIndex++]).also {
        require(it.second == null || it.second?.value == it.first) {
            "Expected edit operation value to match tokens at position ${currentIndex - 1}, but was ${it.second?.value} and ${it.first}"
        }
    }

    inline fun <reified E : T> requireAndConsume(): E {
        val (token) = consume()
        require(token is E) { "Expected to consume ${E::class.simpleName}-token but found: $token" }
        return token
    }

    inline fun <reified E : T> consumeIf(): Pair<E, EditOperation<E>?>? {
        if (peek() !is E) return null
        val (token, edit) = consume()
        @Suppress("UNCHECKED_CAST")
        return Pair(token as E, edit as EditOperation<E>?)
    }

    inline fun <reified E : T, R> fold(initial: R, action: (R, E, EditOperation<E>?) -> R): R {
        var accumulator = initial
        while (true) {
            val (token, edit) = consumeIf<E>() ?: break
            accumulator = action(accumulator, token, edit)
        }
        return accumulator
    }

    inline fun <reified E : T, R> flatMapIndexed(action: (Int, E, EditOperation<E>?) -> List<R>): List<R> {
        val result = mutableListOf<R>()
        var index = 0
        while (true) {
            val (token, edit) = consumeIf<E>() ?: break
            result.addAll(action(index++, token, edit))
        }
        return result
    }
}
