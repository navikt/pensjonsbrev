package no.nav.pensjon.brev.skribenten.common.diff

import no.nav.pensjon.brev.skribenten.common.diff.EditOperation.Insert
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class EditScriptCursorTest {

    private sealed interface Token {
        data class A(val value: String) : Token
        data class B(val value: String) : Token
    }

    private fun <T : Token> insert(token: T, position: Int): Insert<T> = Insert(token, position)

    // constructor

    @Test
    fun `constructor throws on duplicate edit positions`() {
        assertThrows<IllegalArgumentException> {
            EditScriptCursor(listOf(Token.A("x"), Token.A("y")), listOf(insert(Token.A("x"), 0), insert(Token.A("y"), 0)))
        }
    }

    // hasNext and peek

    @Test
    fun `hasNext is false for empty token list`() {
        val cursor = EditScriptCursor<Token>(emptyList(), emptyList())
        assertFalse(cursor.hasNext)
    }

    @Test
    fun `hasNext is true when tokens remain`() {
        val cursor = EditScriptCursor(listOf(Token.A("x")), emptyList())
        assertTrue(cursor.hasNext)
    }

    @Test
    fun `hasNext is false after all tokens consumed`() {
        val cursor = EditScriptCursor(listOf(Token.A("x")), emptyList())
        cursor.consume()
        assertFalse(cursor.hasNext)
    }

    @Test
    fun `peek returns current token without advancing`() {
        val cursor = EditScriptCursor(listOf(Token.A("x"), Token.A("y")), emptyList())
        assertEquals(Token.A("x"), cursor.peek())
        assertEquals(Token.A("x"), cursor.peek())
        assertTrue(cursor.hasNext)
    }

    @Test
    fun `peek returns null on exhausted cursor`() {
        val cursor = EditScriptCursor<Token>(emptyList(), emptyList())
        assertNull(cursor.peek())
    }

    // consume

    @Test
    fun `consume returns token with null edit when no edit at position`() {
        val cursor = EditScriptCursor(listOf(Token.A("x")), emptyList())
        val (token, edit) = cursor.consume()
        assertEquals(Token.A("x"), token)
        assertNull(edit)
    }

    @Test
    fun `consume returns token with edit when edit exists at position`() {
        val token = Token.A("x")
        val op = insert(token, 0)
        val cursor = EditScriptCursor(listOf(token), listOf(op))
        val (consumed, edit) = cursor.consume()
        assertEquals(token, consumed)
        assertEquals(op, edit)
    }

    @Test
    fun `consume advances cursor`() {
        val cursor = EditScriptCursor(listOf(Token.A("x"), Token.A("y")), emptyList())
        cursor.consume()
        assertEquals(Token.A("y"), cursor.peek())
    }

    @Test
    fun `consume throws when edit value does not match token at position`() {
        val cursor = EditScriptCursor(
            listOf(Token.A("x")),
            listOf(insert(Token.A("different"), 0)),
        )
        assertThrows<IllegalArgumentException> { cursor.consume() }
    }

    @Test
    fun `consume returns null edit for token before the edited position`() {
        val tokens = listOf(Token.A("x"), Token.A("y"))
        val op = insert(Token.A("y"), 1)
        val cursor = EditScriptCursor(tokens, listOf(op))
        val (_, firstEdit) = cursor.consume()
        assertNull(firstEdit)
    }

    @Test
    fun `consume returns edit for token at its exact position`() {
        val tokens = listOf(Token.A("x"), Token.A("y"))
        val op = insert(Token.A("y"), 1)
        val cursor = EditScriptCursor(tokens, listOf(op))
        cursor.consume()
        val (_, secondEdit) = cursor.consume()
        assertEquals(op, secondEdit)
    }

    @Test
    fun `consume throws when edit value does not match token at non-zero position`() {
        val cursor = EditScriptCursor(
            listOf(Token.A("x"), Token.A("y")),
            listOf(insert(Token.A("different"), 1)),
        )
        cursor.consume()
        assertThrows<IllegalArgumentException> { cursor.consume() }
    }

    // requireAndComsume

    @Test
    fun `requireAndConsume returns typed token on type match`() {
        val cursor = EditScriptCursor(listOf(Token.A("x")), emptyList())
        val token: Token.A = cursor.requireAndConsume<Token.A>()
        assertEquals(Token.A("x"), token)
    }

    @Test
    fun `requireAndConsume throws on type mismatch`() {
        val cursor = EditScriptCursor<Token>(listOf(Token.A("x")), emptyList())
        assertThrows<IllegalArgumentException> { cursor.requireAndConsume<Token.B>() }
    }

    // consumeIf

    @Test
    fun `consumeIf returns null without advancing when type does not match`() {
        val cursor = EditScriptCursor<Token>(listOf(Token.A("x")), emptyList())
        val result = cursor.consumeIf<Token.B>()
        assertNull(result)
        assertTrue(cursor.hasNext)
    }

    @Test
    fun `consumeIf returns typed pair and advances on type match`() {
        val token = Token.A("x")
        val cursor = EditScriptCursor(listOf(token), emptyList())
        val (consumed, edit) = cursor.consumeIf<Token.A>()!!
        assertEquals(token, consumed)
        assertNull(edit)
        assertFalse(cursor.hasNext)
    }

    @Test
    fun `consumeIf passes edit when present`() {
        val token = Token.A("x")
        val op = insert(token, 0)
        val cursor = EditScriptCursor(listOf(token), listOf(op))
        val (_, edit) = cursor.consumeIf<Token.A>()!!
        assertEquals(op, edit)
    }

}

