package no.nav.pensjon.brev.skribenten.common.diff

class ReplaceAwareEditScriptCursor<T : Any>(
    @PublishedApi internal val insertCursor: EditScriptCursor<T>,
    @PublishedApi internal val deleteCursor: EditScriptCursor<T>,
) {
    val hasNext: Boolean get() = insertCursor.hasNext || deleteCursor.hasNext
    fun peek(): T? = insertCursor.peek() ?: deleteCursor.peek()

    inline fun <reified E : T> consumeIf(): Pair<E, Change<E>?>? {
        val insertIsE = insertCursor.peek() is E
        val deleteIsE = deleteCursor.peek() is E
        if (!insertIsE && !deleteIsE) return null

        return when {
            insertIsE && deleteIsE -> {
                val insertHasEdit = insertCursor.peekEdit() != null
                val deleteHasEdit = deleteCursor.peekEdit() != null
                when {
                    !insertHasEdit && !deleteHasEdit -> {
                        val (token, _) = insertCursor.consumeIf<E>()!!
                        deleteCursor.consumeIf<E>()!!
                        Pair(token, null)
                    }
                    insertHasEdit && deleteHasEdit ->
                        if (insertCursor.peek()!!::class == deleteCursor.peek()!!::class) {
                            val (newToken, _) = insertCursor.consumeIf<E>()!!
                            val (oldToken, _) = deleteCursor.consumeIf<E>()!!
                            Pair(newToken, Change.Replace(oldToken, newToken))
                        } else {
                            val (token, _) = insertCursor.consumeIf<E>()!!
                            Pair(token, Change.Insert(token))
                        }
                    insertHasEdit -> {
                        val (token, _) = insertCursor.consumeIf<E>()!!
                        Pair(token, Change.Insert(token))
                    }
                    else -> {
                        val (token, _) = deleteCursor.consumeIf<E>()!!
                        Pair(token, Change.Delete(token))
                    }
                }
            }
            insertIsE -> {
                val (token, edit) = insertCursor.consumeIf<E>()!!
                if (edit != null) Pair(token, Change.Insert(token)) else Pair(token, null)
            }
            else -> {
                val (token, edit) = deleteCursor.consumeIf<E>()!!
                if (edit != null) Pair(token, Change.Delete(token)) else Pair(token, null)
            }
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
