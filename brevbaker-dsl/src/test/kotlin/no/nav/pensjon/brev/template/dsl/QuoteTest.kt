package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.ExpressionScope
import no.nav.pensjon.brev.template.Language
import no.nav.brev.brevbaker.FellesFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class QuoteTest {

    @Test
    fun `has proper marks in Bokmal`() {
        assertEquals("«hei»", "hei".quoted().eval(ExpressionScope(2, FellesFactory.felles, Language.Bokmal)))
    }

    @Test
    fun `has proper marks in Nynorsk`() {
        assertEquals("«hei»", "hei".quoted().eval(ExpressionScope(2, FellesFactory.felles, Language.Nynorsk)))
    }

    @Test
    fun `has proper marks in English`() {
        assertEquals("'hi'", "hi".quoted().eval(ExpressionScope(2, FellesFactory.felles, Language.English)))
    }

    @Test
    fun `can be used as a normal method`() {
        assertEquals("«hei»", quoted("hei").eval(ExpressionScope(2, FellesFactory.felles, Language.Bokmal)))
    }
}