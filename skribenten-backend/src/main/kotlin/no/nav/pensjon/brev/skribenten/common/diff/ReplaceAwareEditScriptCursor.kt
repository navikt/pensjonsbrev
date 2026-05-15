package no.nav.pensjon.brev.skribenten.common.diff

class ReplaceAwareEditScriptCursor<T : Any>(
    @PublishedApi internal val insertCursor: EditScriptCursor<T>,
    @PublishedApi internal val deleteCursor: EditScriptCursor<T>,
) {
    val hasNext: Boolean get() = insertCursor.hasNext || deleteCursor.hasNext
    fun peek(): T? = insertCursor.peek() ?: deleteCursor.peek()

    inline fun <reified E : T> consumeIf(): Pair<E, Change<E>?>? {
        val insertPeek = insertCursor.peekBoth<E>()
        val deletePeek = deleteCursor.peekBoth<E>()

        return when {
            insertPeek?.second != null && deletePeek?.second != null && insertPeek.first::class == deletePeek.first::class -> {
                insertCursor.requireAndConsume<E>(); deleteCursor.requireAndConsume<E>()
                Pair(insertPeek.first, Change.Replace(deletePeek.first, insertPeek.first))
            }
            insertPeek?.second != null -> {
                insertCursor.requireAndConsume<E>()
                Pair(insertPeek.first, Change.Insert(insertPeek.first))
            }
            deletePeek?.second != null -> {
                deleteCursor.requireAndConsume<E>()
                Pair(deletePeek.first, Change.Delete(deletePeek.first))
            }
            insertPeek != null || deletePeek != null -> {
                if (insertPeek != null) insertCursor.requireAndConsume<E>()
                if (deletePeek != null) deleteCursor.requireAndConsume<E>()
                Pair((insertPeek ?: deletePeek)!!.first, null)
            }
            else -> null
        }
    }


    inline fun <reified E : T> requireAndConsume(): E {
        val result = consumeIf<E>()
        require(result != null) { "Expected ${E::class.simpleName} token, got: ${peek()}" }
        return result.first
    }

    inline fun <reified E : T> forEachIndexed(action: (insertIndex: Int, deleteIndex: Int, E, Change<E>?) -> Unit) {
        var insertIndex = 0
        var deleteIndex = 0
        while (true) {
            val (token, change) = consumeIf<E>() ?: break
            action(insertIndex, deleteIndex, token, change)
            when (change) {
                is Change.Insert -> insertIndex++
                is Change.Delete -> deleteIndex++
                else -> { insertIndex++; deleteIndex++ }
            }
        }
    }

    inline fun <reified E : T, R> fold(initial: R, action: (R, E, Change<E>?) -> R): R {
        var accumulator = initial
        while (true) {
            val (token, change) = consumeIf<E>() ?: break
            accumulator = action(accumulator, token, change)
        }
        return accumulator
    }
}
