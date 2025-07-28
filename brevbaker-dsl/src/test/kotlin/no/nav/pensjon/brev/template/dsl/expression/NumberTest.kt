package no.nav.pensjon.brev.template.dsl.expression

import no.nav.brev.brevbaker.FellesFactory
import no.nav.pensjon.brev.template.ExpressionScope
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.Year
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class NumberTest {

    @Test
    fun `kan plusse paa int`() {
        val expression = 10.expr() + 4
        val scope = ExpressionScope(2, FellesFactory.felles, Language.Bokmal)
        assertEquals(14, expression.eval(scope))
    }

    @Test
    fun `kan plusse paa aar`() {
        val expression = 2023.expr().toYear() + 1
        val scope = ExpressionScope(2, FellesFactory.felles, Language.Bokmal)
        assertEquals(Year(2024), expression.eval(scope))
    }

    @Test
    fun `kan plusse paa kroner`() {
        val expression = 1000.expr().toKroner() + 100
        val scope = ExpressionScope(2, FellesFactory.felles, Language.Bokmal)
        assertEquals(Kroner(1100), expression.eval(scope))
    }

    @Test
    fun `kan trekke fra int`() {
        val expression = 10.expr() - 4
        val scope = ExpressionScope(2, FellesFactory.felles, Language.Bokmal)
        assertEquals(6, expression.eval(scope))
    }

    @Test
    fun `kan trekke fra aar`() {
        val expression = 2023.expr().toYear() - 1
        val scope = ExpressionScope(2, FellesFactory.felles, Language.Bokmal)
        assertEquals(Year(2022), expression.eval(scope))
    }

    @Test
    fun `kan trekke fra kroner`() {
        val expression = 1000.expr().toKroner() - 100
        val scope = ExpressionScope(2, FellesFactory.felles, Language.Bokmal)
        assertEquals(Kroner(900), expression.eval(scope))
    }
}