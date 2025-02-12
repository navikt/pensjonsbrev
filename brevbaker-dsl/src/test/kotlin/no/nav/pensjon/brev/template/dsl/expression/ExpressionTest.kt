package no.nav.pensjon.brev.template.dsl.expression

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brevbaker.api.model.Kroner
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate

class ExpressionTest {
    @Test
    fun `format creates a binaryinvoke with dateformatting by letter language`() {
        val expr = Expression.Literal(LocalDate.of(2020, 1, 1))
        val expected =
            Expression.BinaryInvoke(
                first = expr,
                second = Expression.FromScope.Language,
                operation = LocalizedFormatter.DateFormat,
            )

        assertEquals(expected, expr.format())
    }

    @Test
    fun `expr wraps value in ExpressionLiteral`() {
        val value = "hei"
        assertEquals(Expression.Literal(value), value.expr())
    }

    @Test
    fun `plus creates a concat expression of expression operands`() {
        val op1 = "hei".expr()
        val op2 = " du".expr()
        val expected = Expression.BinaryInvoke(op1, op2, BinaryOperation.Concat)

        assertEquals(expected, op1 + op2)
    }

    @Test
    fun `plus creates a concat expression of expression plus string`() {
        val op1 = "hei".expr()
        val op2 = " du"
        val expected = Expression.BinaryInvoke(op1, Expression.Literal(op2), BinaryOperation.Concat)

        assertEquals(expected, op1 + op2)
    }

    @Test
    fun `stableHashcode of Literal String is based on value hashCode`() {
        val e = "burde få hash herfra"
        assertEquals(e.hashCode(), e.expr().stableHashCode())
    }

    private enum class MyEnum { A, B }

    @Test
    fun `stableHashCode of Literal enum value uses name hashCode`() {
        val e = MyEnum.A
        assertNotEquals(e.hashCode(), e.expr().stableHashCode())
        assertEquals(e.name.hashCode(), e.expr().stableHashCode())
    }

    @Test
    fun `stableHashCode of Literal collection of enum values uses hashCodes of names`() {
        val e = listOf(MyEnum.A)
        assertNotEquals(e.hashCode(), e.expr().stableHashCode())
        assertEquals(e.map { it.name }.hashCode(), e.expr().stableHashCode())
    }

    @Test
    fun `stableHashCode of Literal Number is based on value hashCode`() {
        val e = 31
        assertEquals(e.hashCode(), e.expr().stableHashCode())
    }

    @Test
    fun `stableHashCode of Literal IntValue is based on value hashCode`() {
        val e = Kroner(31)
        assertEquals(e.value.hashCode(), e.expr().stableHashCode())
    }

    @Test
    fun `stableHashCode of Literal Boolean is based on value hashCode`() {
        val e = true
        assertEquals(e.hashCode(), e.expr().stableHashCode())
    }

    @Test
    fun `stableHashCode of Literal collection is based on items stableHashCode`() {
        val e = listOf(MyEnum.A, MyEnum.B, MyEnum.B, MyEnum.A)
        assertEquals(StableHash.of(e.map { StableHash.of(it) }).stableHashCode(), e.expr().stableHashCode())
    }

    @Test
    fun `stableHashCode of Pair uses field hashing`() {
        val e = "a string" to 42
        assertEquals(e.hashCode(), e.expr().stableHashCode())
    }

    @Test
    fun `stableHashCode of Unit uses qualified name`() {
        assertEquals("kotlin.Unit".hashCode(), Unit.expr().stableHashCode())
    }

    @Test
    fun `stableHashCode of null is 0`() {
        assertEquals(0, null.expr().stableHashCode())
    }

    @Test
    fun `Assigned expression has a stableHashCode`() {
        assertEquals(Expression.FromScope.Assigned<String>(listOf(1, 2, 3).hashCode()).stableHashCode(), Expression.FromScope.Assigned<String>(listOf(1, 2, 3).hashCode()).stableHashCode())
    }
}
