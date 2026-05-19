package no.nav.pensjon.brev.skribenten.common.diff

import no.nav.pensjon.brev.skribenten.common.diff.DiffEntry.Delete
import no.nav.pensjon.brev.skribenten.common.diff.DiffEntry.Insert
import no.nav.pensjon.brev.skribenten.common.diff.DiffEntry.Replace
import no.nav.pensjon.brev.skribenten.common.diff.DiffEntry.Unchanged

class ReplaceAwareEditScriptCursor<T : Any>(editScript: EditScript<T>) {
    @PublishedApi internal val insertCursor = EditScriptCursor(editScript.new, editScript.inserts)
    @PublishedApi internal val deleteCursor = EditScriptCursor(editScript.old, editScript.deletes)

    val hasNext: Boolean get() = insertCursor.hasNext || deleteCursor.hasNext
    fun peek(): T? = insertCursor.peek() ?: deleteCursor.peek()

    inline fun <reified E : T> consumeIf(): DiffEntry<E>? {
        val insertPeek = insertCursor.peekBoth<E>()
        val deletePeek = deleteCursor.peekBoth<E>()

        return when {
            insertPeek?.second != null && deletePeek?.second != null && insertPeek.first::class == deletePeek.first::class -> {
                insertCursor.requireAndConsume<E>(); deleteCursor.requireAndConsume<E>()
                Replace(deletePeek.first, insertPeek.first)
            }
            insertPeek?.second != null -> {
                insertCursor.requireAndConsume<E>()
                Insert(insertPeek.first)
            }
            deletePeek?.second != null -> {
                deleteCursor.requireAndConsume<E>()
                Delete(deletePeek.first)
            }
            insertPeek != null || deletePeek != null -> {
                if (insertPeek != null) insertCursor.requireAndConsume<E>()
                if (deletePeek != null) deleteCursor.requireAndConsume<E>()
                Unchanged((insertPeek ?: deletePeek)!!.first)
            }
            else -> null
        }
    }

    inline fun <reified E : T> requireAndConsume(): E {
        val result = consumeIf<E>()
        require(result != null) { "Expected ${E::class.simpleName} token, got: ${peek()}" }
        return result.token
    }

    inline fun <reified E : T> forEachIndexed(action: (insertIndex: Int, deleteIndex: Int, DiffEntry<E>) -> Unit) {
        var insertIndex = 0
        var deleteIndex = 0
        while (true) {
            val entry = consumeIf<E>() ?: break
            action(insertIndex, deleteIndex, entry)
            when (entry) {
                is Insert -> insertIndex++
                is Delete -> deleteIndex++
                is Replace, is Unchanged -> { insertIndex++; deleteIndex++ }
            }
        }
    }

    inline fun <reified E : T, R> fold(initial: R, action: (R, DiffEntry<E>) -> R): R {
        var accumulator = initial
        while (true) {
            val entry = consumeIf<E>() ?: break
            accumulator = action(accumulator, entry)
        }
        return accumulator
    }
}
