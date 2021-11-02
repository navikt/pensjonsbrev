package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate


class ExpressionTest {
    private data class SomeDto(val name: String)

    @Test
    fun `select creates a unaryinvoke with select`() {
        val argument = Expression.LetterProperty(ExpressionScope<SomeDto, *>::argument)
        val expected = Expression.UnaryInvoke(
            value = argument,
            operation = UnaryOperation.Select(SomeDto::name)
        )

        assertEquals(expected, argument.select(SomeDto::name))
    }

    @Test
    fun `str creates a unaryinvoke with tostring`() {
        val expr = Expression.Literal(22)
        val expected = Expression.UnaryInvoke(
            value = expr,
            operation = UnaryOperation.ToString()
        )

        assertEquals(expected, expr.str())
    }

    @Test
    fun `format creates a binaryinvoke with dateformatting by letter language`() {
        val expr = Expression.Literal(LocalDate.now())
        val expected = Expression.BinaryInvoke(
            first = expr,
            second = Expression.LetterProperty(ExpressionScope<Nothing, *>::language),
            operation = BinaryOperation.LocalizedDateFormat
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
}