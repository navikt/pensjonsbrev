package no.nav.pensjon.brev.template

import no.nav.brev.BrevLandmodell
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
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

    @Nested
    @DisplayName("Landnavn-formatering")
    inner class LandnavnFormatering {

        private val landkodeNorge = BrevLandmodell.Landkode("NO")

        @Test
        fun `Landnavn returnerer riktig namn på bokmål`() {
            assertEquals("Norge", LocalizedFormatter.LandnavnFormat.apply(landkodeNorge, Language.Bokmal))
        }

        @Test
        fun `Landnavn returnerer riktig namn på nynorsk`() {
            assertEquals("Noreg", LocalizedFormatter.LandnavnFormat.apply(landkodeNorge, Language.Nynorsk))
        }

        @Test
        fun `Landnavn returnerer riktig namn på engelsk`() {
            assertEquals("Norway", LocalizedFormatter.LandnavnFormat.apply(landkodeNorge, Language.English))
        }

        @Test
        fun `Landnavn handterer lowercase landkode`() {
            assertEquals("Norge", LocalizedFormatter.LandnavnFormat.apply(landkodeNorge, Language.Bokmal))
            assertEquals("Noreg", LocalizedFormatter.LandnavnFormat.apply(landkodeNorge, Language.Nynorsk))
            assertEquals("Norway", LocalizedFormatter.LandnavnFormat.apply(landkodeNorge, Language.English))
        }

        @Test
        fun `format() på Landkode-expression brukar språket frå scope`() {
            val landkodeSverige = BrevLandmodell.Landkode("SE")
            val expr = landkodeSverige.expr().format()
            assertEquals("Sverige", expr.eval(ExpressionScope(landkodeSverige, FellesFactory.felles, Language.Bokmal)))
            assertEquals("Sverige", expr.eval(ExpressionScope(landkodeSverige, FellesFactory.felles, Language.Nynorsk)))
            assertEquals("Sweden", expr.eval(ExpressionScope(landkodeSverige, FellesFactory.felles, Language.English)))
        }
    }
}