package no.nav.pensjon.brev.template.dsl.expression

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.expression.ExpressionTestSelectors.SomeDtoSelectors.name
import no.nav.pensjon.brev.template.dsl.expression.ExpressionTestSelectors.SomeDtoSelectors.nameSelector
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate


class ExpressionTest {
    data class SomeDto(val name: String)

    @Suppress("unused")
    @TemplateModelHelpers
    object Helpers : HasModel<SomeDto>

    @Test
    fun `select creates a unaryinvoke with select`() {
        val argument = Expression.FromScope.Argument<SomeDto>()
        val expected = Expression.UnaryInvoke(
            value = argument,
            operation = UnaryOperation.Select(nameSelector)
        )

        assertEquals(expected, argument.name)
    }

    @Test
    fun `format creates a binaryinvoke with dateformatting by letter language`() {
        val expr = Expression.Literal(LocalDate.of(2020,1,1))
        val expected = Expression.BinaryInvoke(
            first = expr,
            second = Expression.FromScope.Language,
            operation = LocalizedFormatter.DateFormat
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