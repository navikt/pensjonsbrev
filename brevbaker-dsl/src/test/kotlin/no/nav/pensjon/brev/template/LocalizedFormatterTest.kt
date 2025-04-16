package no.nav.pensjon.brev.template

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class LocalizedFormatterTest {

    private val testExpressionScope = ExpressionScope(EmptyBrevdata, Fixtures.felles, Language.Bokmal)

    @Test
    fun `double formatteres som standard til 2 desimaler`() {
        assertThat(2.0.expr().format().eval(testExpressionScope), equalTo("2,00"))
    }

    @Test
    fun `double formatteres ikke til negative tall`() {
        assertThat(2.0.expr().format(-1).eval(testExpressionScope), equalTo("2"))
    }

    @Test
    fun `double formatteres ikke til mer enn 16 plasser`() {
        assertThat(2.0.expr().format(100).eval(testExpressionScope), equalTo("2," + "0".repeat(16)))
    }

    @Test
    fun `bigDecimal formatteres som standard til 2 desimaler`() {
        assertThat(BigDecimal(2).expr().format().eval(testExpressionScope), equalTo("2,00"))
    }

    @Test
    fun `bigDecimal formatteres ikke til negative tall`() {
        assertThat(BigDecimal(2).expr().format(-1).eval(testExpressionScope), equalTo("2"))
    }
}