package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.BinaryOperation
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.UnaryOperation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate


class ExpressionTest {
    private data class SomeDto(val name: String)

    @Test
    fun `select creates a unaryinvoke with select`() {
        val argument = Expression.LetterProperty(Letter<SomeDto>::argument)
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
            second = Expression.LetterProperty(Letter<Nothing>::language),
            operation = BinaryOperation.LocalizedDateFormat
        )

        assertEquals(expected, expr.format())
    }
}