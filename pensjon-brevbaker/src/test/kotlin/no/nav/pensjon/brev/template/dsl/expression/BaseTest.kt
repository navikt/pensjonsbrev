package no.nav.pensjon.brev.template.dsl.expression

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.Felles
import no.nav.pensjon.brev.template.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.math.exp

class BaseTest {

    @Test
    fun `ifElse creates a BinaryInvoke with branches as a tuple`() {
        val actual = ifElse(true.expr(), "hei".expr(), "hade bra".expr())
        val expected = Expression.BinaryInvoke(
            first = true.expr(),
            second = Expression.BinaryInvoke("hei".expr(), "hade bra".expr(), BinaryOperation.Tuple()),
            operation = BinaryOperation.IfElse()
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `ifElse expression evaluates to correct branch`() {
        val expr = ifElse(Expression.FromScope(ExpressionScope<Int, *>::argument) equalTo 2, "hei", "hade bra")

        val scope = ExpressionScope(2, Fixtures.felles, Language.Bokmal)

        assertEquals("hei", expr.eval(scope))
        assertEquals("hade bra", expr.eval(ExpressionScope(3, scope.felles, scope.language)))
    }

}