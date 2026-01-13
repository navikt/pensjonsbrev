package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LocalizedFormatterTest {

    private val testExpressionScope = ExpressionScope(EmptyAutobrevdata, FellesFactory.felles, Language.Bokmal)

    @Test
    fun `double formatteres som standard til 2 desimaler`() {
        assertThat(2.0.expr().format().eval(testExpressionScope)).isEqualTo("2,00")
    }

    @Test
    fun `double formatteres ikke til negative tall`() {
        assertThat(2.0.expr().format(-1).eval(testExpressionScope)).isEqualTo("2")
    }

    @Test
    fun `double formatteres ikke til mer enn 16 plasser`() {
        assertThat(2.0.expr().format(100).eval(testExpressionScope)).isEqualTo("2," + "0".repeat(16))
    }

    @Test
    fun `kan formattere double som nullable verdi`() {
        assertThat(null.expr<Double?>().format(0).eval(testExpressionScope)).isNull()
    }

    @Test
    fun `kan formattere integer som nullable verdi`() {
        assertThat(null.expr<Int?>().format().eval(testExpressionScope)).isNull()
    }

}