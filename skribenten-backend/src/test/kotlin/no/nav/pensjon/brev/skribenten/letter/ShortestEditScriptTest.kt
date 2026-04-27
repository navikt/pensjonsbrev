package no.nav.pensjon.brev.skribenten.letter

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ShortestEditScriptTest {

    @Test
    fun `old and new fields reflect the input lists`() {
        val old = listOf("a", "b")
        val new = listOf("b", "c")
        val script = shortestEditScript(old, new)
        assertEquals(old, script.old)
        assertEquals(new, script.new)
    }

    @Test
    fun `inserts and deletes partition all`() {
        val script = shortestEditScript(listOf("a", "b"), listOf("b", "c"))
        assertEquals(
            script.all.toSet(),
            (script.inserts + script.deletes).toSet(),
        )
    }

    // No-change cases

    @Test
    fun `identical lists produce no operations`() {
        val script = shortestEditScript(listOf("a", "b", "c"), listOf("a", "b", "c"))
        assertEquals(emptyList<EditOperation<String>>(), script.all)
    }

    @Test
    fun `both lists empty produces no operations`() {
        val script = shortestEditScript(emptyList<String>(), emptyList())
        assertEquals(emptyList<EditOperation<String>>(), script.all)
    }

    // Only inserts

    @Test
    fun `empty old produces only inserts`() {
        val new = listOf("a", "b", "c")
        val script = shortestEditScript(emptyList(), new)
        assertTrue(script.deletes.isEmpty())
        assertEquals(new.size, script.inserts.size)
        assertCorrect(script)
    }

    @Test
    fun `single insert at beginning`() {
        val script = shortestEditScript(listOf("b", "c"), listOf("a", "b", "c"))
        assertEquals(0, script.deletes.size)
        assertEquals(1, script.inserts.size)
        assertCorrect(script)
    }

    @Test
    fun `single insert in middle`() {
        val script = shortestEditScript(listOf("a", "c"), listOf("a", "b", "c"))
        assertEquals(0, script.deletes.size)
        assertEquals(1, script.inserts.size)
        assertCorrect(script)
    }

    @Test
    fun `single insert at end`() {
        val script = shortestEditScript(listOf("a", "b"), listOf("a", "b", "c"))
        assertEquals(0, script.deletes.size)
        assertEquals(1, script.inserts.size)
        assertCorrect(script)
    }

    // Only deletes

    @Test
    fun `empty new produces only deletes`() {
        val old = listOf("a", "b", "c")
        val script = shortestEditScript(old, emptyList())
        assertTrue(script.inserts.isEmpty())
        assertEquals(old.size, script.deletes.size)
        assertCorrect(script)
    }

    @Test
    fun `single delete at beginning`() {
        val script = shortestEditScript(listOf("a", "b", "c"), listOf("b", "c"))
        assertEquals(1, script.deletes.size)
        assertEquals(0, script.inserts.size)
        assertCorrect(script)
    }

    @Test
    fun `single delete in middle`() {
        val script = shortestEditScript(listOf("a", "b", "c"), listOf("a", "c"))
        assertEquals(1, script.deletes.size)
        assertEquals(0, script.inserts.size)
        assertCorrect(script)
    }

    @Test
    fun `single delete at end`() {
        val script = shortestEditScript(listOf("a", "b", "c"), listOf("a", "b"))
        assertEquals(1, script.deletes.size)
        assertEquals(0, script.inserts.size)
        assertCorrect(script)
    }

    // Inserts and deletes combined

    @Test
    fun `replace single element requires one insert and one delete`() {
        val script = shortestEditScript(listOf("a", "b", "c"), listOf("a", "x", "c"))
        assertEquals(1, script.deletes.size)
        assertEquals(1, script.inserts.size)
        assertCorrect(script)
    }

    @Test
    fun `replace all elements`() {
        val old = listOf("a", "b", "c")
        val new = listOf("x", "y", "z")
        val script = shortestEditScript(old, new)
        assertEquals(old.size, script.deletes.size)
        assertEquals(new.size, script.inserts.size)
        assertCorrect(script)
    }

    @Test
    fun `non-contiguous changes across the list`() {
        val script = shortestEditScript(
            old = listOf("a", "b", "c", "d", "e"),
            new = listOf("a", "x", "c", "y", "e"),
        )
        assertEquals(2, script.deletes.size)
        assertEquals(2, script.inserts.size)
        assertCorrect(script)
    }

    @Test
    fun `prepend and append simultaneously`() {
        val script = shortestEditScript(
            old = listOf("b", "c"),
            new = listOf("a", "b", "c", "d"),
        )
        assertEquals(0, script.deletes.size)
        assertEquals(2, script.inserts.size)
        assertCorrect(script)
    }

    // Operation value correctness

    @Test
    fun `delete values match elements at their positions in old`() {
        val old = listOf("a", "b", "c", "d")
        val script = shortestEditScript(old, listOf("b", "d"))
        script.deletes.forEach { delete ->
            assertEquals(old[delete.position], delete.delete)
        }
    }

    @Test
    fun `insert values match elements at their positions in new`() {
        val new = listOf("a", "b", "c", "d")
        val script = shortestEditScript(listOf("b", "d"), new)
        script.inserts.forEach { insert ->
            assertEquals(new[insert.position], insert.insert)
        }
    }

    // Minimality

    @Test
    fun `script is minimal — does not delete and reinsert unchanged elements`() {
        // "a" and "c" are shared; only "b"↔"x" differs — minimum is 2 operations
        val script = shortestEditScript(listOf("a", "b", "c"), listOf("a", "x", "c"))
        assertEquals(2, script.all.size)
    }

    @Test
    fun `script is minimal for prefix match`() {
        // Shared prefix "a", "b"; only "c" is new — minimum is 1 operation
        val script = shortestEditScript(listOf("a", "b"), listOf("a", "b", "c"))
        assertEquals(1, script.all.size)
    }

    /**
     * Verifies the structural correctness of an edit script without applying it.
     *
     * The common subsequence (elements untouched by the script) must be identical
     * when derived from old (by removing deleted positions) and from new (by removing
     * inserted positions). Each operation's value must also match the element at its
     * declared position in the corresponding list.
     */
    private fun <T : Any> assertCorrect(script: EditScript<T>) {
        val deletePositions = script.deletes.map { it.position }.toSet()
        val insertPositions = script.inserts.map { it.position }.toSet()

        val commonFromOld = script.old.filterIndexed { i, _ -> i !in deletePositions }
        val commonFromNew = script.new.filterIndexed { i, _ -> i !in insertPositions }
        assertEquals(commonFromOld, commonFromNew, "Unchanged elements must form the same subsequence in old and new")

        script.deletes.forEach { assertEquals(script.old[it.position], it.delete, "Delete value must match old[${it.position}]") }
        script.inserts.forEach { assertEquals(script.new[it.position], it.insert, "Insert value must match new[${it.position}]") }
    }
}
