package no.nav.pensjon.brev.template.dsl

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class QuoteTest {

    @Test
    fun `has proper marks in Bokmal`() {
        val result = bokmal { +"hei".quoted() }.second as LiteralOrExpressionBuilder.LiteralWrapper
        assertEquals("«hei»", result.str)
    }

    @Test
    fun `has proper marks in Nynorsk`() {
        val result = nynorsk { +"hei".quoted() }.second as LiteralOrExpressionBuilder.LiteralWrapper
        assertEquals("«hei»", result.str)
    }

    @Test
    fun `has proper marks in English`() {
        val result = english { +"hi".quoted() }.second as LiteralOrExpressionBuilder.LiteralWrapper
        assertEquals("'hi'", result.str)
    }

    @Test
    fun `can be used as a normal method`() {
        val resultBokmal = bokmal { +quoted("hei") }.second as LiteralOrExpressionBuilder.LiteralWrapper
        assertEquals("«hei»", resultBokmal.str)
    }
}