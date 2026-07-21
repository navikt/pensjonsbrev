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
                insertCursor.requireAndConsume<E>()
                deleteCursor.requireAndConsume<E>()
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
            insertPeek != null && deletePeek != null -> {
                require(insertPeek.first == deletePeek.first) {
                    "Cursors desynchronized: Unchanged tokens must be equal, but got insert=${insertPeek.first} and delete=${deletePeek.first}"
                }
                insertCursor.requireAndConsume<E>()
                deleteCursor.requireAndConsume<E>()
                Unchanged(insertPeek.first)
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
                is Replace, is Unchanged -> {
                    insertIndex++
                    deleteIndex++
                }
            }
        }
    }

    /**
     * Like [forEachIndexed], but stabilizes the insert/delete index passed to [action] so that it always refers to
     * an index that has actually been produced/matched on that side.
     *
     * [forEachIndexed] advances insertIndex/deleteIndex only when an entry actually consumes that side, meaning a
     * pure [Delete] entry is invoked with the (unconsumed) insertIndex that would apply to a *future* insert-side
     * entry - even when no such entry exists (e.g. because the insert-side sequence is already exhausted). Any
     * insert-side content nested inside such an entry's processing (e.g. leftover words belonging to an already
     * matched/Unchanged marker) would then incorrectly be attributed to this non-existent "next" index, rather than
     * the last real one. This function instead reuses the last real index for the side that isn't consumed by the
     * current entry, so nested processing always sees a valid, already-established index.
     */
    inline fun <reified E : T> forEachIndexedStable(action: (insertIndex: Int, deleteIndex: Int, DiffEntry<E>) -> Unit) {
        var lastInsertIndex = 0
        var lastDeleteIndex = 0
        forEachIndexed<E> { insertIndex, deleteIndex, entry ->
            val stableInsertIndex = if (entry is Delete) lastInsertIndex else insertIndex
            val stableDeleteIndex = if (entry is Insert) lastDeleteIndex else deleteIndex
            action(stableInsertIndex, stableDeleteIndex, entry)
            if (entry !is Delete) lastInsertIndex = insertIndex
            if (entry !is Insert) lastDeleteIndex = deleteIndex
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
