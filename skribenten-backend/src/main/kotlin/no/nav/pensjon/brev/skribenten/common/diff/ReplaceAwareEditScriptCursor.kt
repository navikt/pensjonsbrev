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
     * The insert/delete index for a single entry yielded by [forEachIndexedStable].
     *
     * [insertIndex] only advances when an entry genuinely, non-decoy-ly consumes the insert side (see
     * [forEachIndexedStable]'s `action` doc for what "decoy" means) - so a [Delete] entry (or a decoy) is paired
     * with the index that would apply to a *future*, genuinely-established insert-side entry, which may not exist
     * yet (or at all). This correctly distinguishes a leading delete (attributed to the position before the next
     * real entry) from a trailing delete (attributed to the position one past the last real entry), and is safe to
     * use both as a placement key (e.g. into a `deletedContent` map) and as an anchor for nested processing that
     * needs an already-established position, since it is never used to index into an actual array.
     *
     * [rawDeleteIndex]/[stableDeleteIndex] mirror the insert-side distinction from before decoy-correction was
     * needed, but only for the delete side, where no decoy concept applies: every element of the old sequence is
     * visited exactly once, so [rawDeleteIndex] (this entry's own position) is always a real, meaningful position for
     * an entry's own reporting, while [stableDeleteIndex] (the last real position) is what nested processing inside
     * an [Insert] entry (which doesn't itself consume any delete-side position) should anchor to instead.
     */
    class StableIndices(
        val insertIndex: Int,
        val rawDeleteIndex: Int,
        val stableDeleteIndex: Int,
    )

    /**
     * Like [forEachIndexed], but additionally provides a decoy-corrected insert index and a stabilized delete index
     * - see [StableIndices].
     *
     * [forEachIndexed] advances insertIndex/deleteIndex only when an entry actually consumes that side, meaning a
     * pure [Delete] entry is invoked with the (unconsumed) insertIndex that would apply to a *future* insert-side
     * entry - even when no such entry exists (e.g. because the insert-side sequence is already exhausted). Any
     * insert-side content nested inside such an entry's processing (e.g. leftover words belonging to an already
     * matched/Unchanged marker) would then incorrectly be attributed to this non-existent "next" index, rather than
     * the last real one.
     *
     * [action] returns whether the entry's insert-side pairing turned out to be a decoy despite its own [DiffEntry]
     * classification suggesting otherwise - e.g. an [Unchanged]/[Replace] entry that weak, content-blind equality
     * paired with an unrelated node, which nested analysis then reveals carries no real surviving content on the
     * insert side (see e.g. `EditLetterWordTokenizer.consumeTextContent`'s `isWholeNodeGone`). When `true`, this
     * entry is excluded from [StableIndices.insertIndex]'s bookkeeping (as if it were a [Delete]), so that later
     * entries aren't advanced past a position that was never genuinely established - which would otherwise
     * misattribute a later, genuinely leading delete to a spurious "next" position. [Delete] entries are always
     * excluded regardless of the returned value, since they never consume the insert side to begin with.
     */
    inline fun <reified E : T> forEachIndexedStable(action: (indices: StableIndices, entry: DiffEntry<E>) -> Boolean) {
        var insertIndex = 0
        var lastDeleteIndex = 0
        forEachIndexed<E> { _, deleteIndex, entry ->
            val stableDeleteIndex = if (entry is Insert) lastDeleteIndex else deleteIndex
            val insertSideWasDecoy = action(StableIndices(insertIndex, deleteIndex, stableDeleteIndex), entry)
            if (entry !is Delete && !insertSideWasDecoy) insertIndex++
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
