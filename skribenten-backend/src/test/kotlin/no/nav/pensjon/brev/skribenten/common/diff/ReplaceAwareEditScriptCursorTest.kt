package no.nav.pensjon.brev.skribenten.common.diff

import no.nav.pensjon.brev.skribenten.common.diff.EditOperation.Delete
import no.nav.pensjon.brev.skribenten.common.diff.EditOperation.Insert
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ReplaceAwareEditScriptCursorTest {

    private sealed interface Token {
        data class A(val value: String) : Token
        data class B(val value: String) : Token
    }

    private fun editScript(
        old: List<Token>,
        new: List<Token>,
        inserts: List<Insert<Token>> = emptyList(),
        deletes: List<Delete<Token>> = emptyList(),
    ) = EditScript(old, new, inserts + deletes)

    // --- consumeIf: basic behavior ---

    @Test
    fun `consumeIf returns Insert when only insert cursor has token with edit marker`() {
        val cursor = ReplaceAwareEditScriptCursor(
            editScript(
                old = listOf(Token.B("y")),
                new = listOf(Token.A("x"), Token.B("y")),
                inserts = listOf(Insert(Token.A("x"), 0)),
            )
        )
        assertEquals(DiffEntry.Insert(Token.A("x")), cursor.consumeIf<Token.A>())
    }

    @Test
    fun `consumeIf returns Delete when only delete cursor has token with edit marker`() {
        val cursor = ReplaceAwareEditScriptCursor(
            editScript(
                old = listOf(Token.A("x"), Token.B("z")),
                new = listOf(Token.B("z")),
                deletes = listOf(Delete(Token.A("x"), 0)),
            )
        )
        assertEquals(DiffEntry.Delete(Token.A("x")), cursor.consumeIf<Token.A>())
    }

    @Test
    fun `consumeIf returns Unchanged when both cursors have matching token without edits`() {
        val cursor = ReplaceAwareEditScriptCursor(
            editScript(
                old = listOf(Token.A("x")),
                new = listOf(Token.A("x")),
            )
        )
        assertEquals(DiffEntry.Unchanged(Token.A("x")), cursor.consumeIf<Token.A>())
    }

    @Test
    fun `consumeIf returns Replace when both cursors have same type with edit markers`() {
        // old=[A("old")], new=[A("new")], delete at 0, insert at 0
        // Both sides have A with edit markers and same class → Replace(old, new)
        val cursor = ReplaceAwareEditScriptCursor(
            editScript(
                old = listOf(Token.A("old")),
                new = listOf(Token.A("new")),
                inserts = listOf(Insert(Token.A("new"), 0)),
                deletes = listOf(Delete(Token.A("old"), 0)),
            )
        )
        assertEquals(DiffEntry.Replace(Token.A("old"), Token.A("new")), cursor.consumeIf<Token.A>())
        assertFalse(cursor.hasNext)
    }

    @Test
    fun `consumeIf returns null when neither cursor has token of requested type`() {
        val cursor = ReplaceAwareEditScriptCursor(
            editScript(
                old = listOf(Token.B("x")),
                new = listOf(Token.B("x")),
            )
        )
        assertNull(cursor.consumeIf<Token.A>())
    }

    // --- consumeIf: single-sided tokens ---

    @Test
    fun `consumeIf returns null when only delete cursor has token of type E without edit`() {
        // Valid script: old=[A("x"), B("done")], new=[B("consume first"), A("x"), B("done")]
        // Insert B("consume first") at position 0. After consuming it, insert is at A("x"), delete at A("x").
        // But we want to test what happens BEFORE consuming B: at that point insert is at B, delete at A.
        // consumeIf<A>: insert has B (not A) → insertPeek=null, delete has A("x") without edit → returns null
        val cursor = ReplaceAwareEditScriptCursor(
            editScript(
                old = listOf(Token.A("x"), Token.B("done")),
                new = listOf(Token.B("consume first"), Token.A("x"), Token.B("done")),
                inserts = listOf(Insert(Token.B("consume first"), 0)),
            )
        )
        // Insert cursor is at B("consume first"), delete cursor is at A("x")
        // consumeIf<A>: insert has B (not A), delete has A without edit → null
        assertNull(cursor.consumeIf<Token.A>())
        // Cursors remain synchronized — neither advanced
        assertTrue(cursor.hasNext)
    }

    @Test
    fun `consumeIf returns null when only insert cursor has token of type E without edit`() {
        // Mirror: old=[B("consume first"), A("x"), B("done")], new=[A("x"), B("done")]
        // Delete B("consume first") at position 0. Before consuming it:
        // insert cursor at A("x"), delete cursor at B("consume first")
        // consumeIf<A>: insert has A("x") without edit, delete has B (not A) → null
        val cursor = ReplaceAwareEditScriptCursor(
            editScript(
                old = listOf(Token.B("consume first"), Token.A("x"), Token.B("done")),
                new = listOf(Token.A("x"), Token.B("done")),
                deletes = listOf(Delete(Token.B("consume first"), 0)),
            )
        )
        // Insert cursor is at A("x"), delete cursor is at B("consume first")
        // consumeIf<A>: delete has B (not A), insert has A without edit → null
        assertNull(cursor.consumeIf<Token.A>())
        assertTrue(cursor.hasNext)
    }

    @Test
    fun `consumeIf requires equal tokens when both sides have type E without edits`() {
        // Both sides have type A at current position but different values.
        // This can't happen with a correctly computed SES (unchanged means equal), but the
        // require catches it as a safety invariant.
        val cursor = ReplaceAwareEditScriptCursor(
            editScript(
                old = listOf(Token.A("x")),
                new = listOf(Token.A("y")),
            )
        )
        val exception = org.junit.jupiter.api.assertThrows<IllegalArgumentException> {
            cursor.consumeIf<Token.A>()
        }
        assertTrue(exception.message!!.contains("Unchanged tokens must be equal"))
    }
}
