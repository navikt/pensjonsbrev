package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Percent
import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Year
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

private val scope = ExpressionScope(Unit, FellesFactory.felles, Bokmal)

class EqualsOperationTest {

    @Test
    fun `equals of comparable expression`() {
        val testExpr = "test".expr()
        val testExpr2 = "test".expr()
        val anotherExpr = "another".expr()
        val nullExpr: Expression<String?> = null.expr()
        val nullExpr2: Expression<String?> = null.expr()

        assertThat((testExpr equalTo testExpr2).eval(scope)).isTrue
        assertThat((testExpr equalTo anotherExpr).eval(scope)).isFalse
        assertThat((testExpr notEqualTo anotherExpr).eval(scope)).isTrue
        assertThat((testExpr notEqualTo testExpr2).eval(scope)).isFalse
        assertThat((testExpr equalTo nullExpr).eval(scope)).isFalse
        assertThat((testExpr notEqualTo nullExpr).eval(scope)).isTrue
        assertThat((nullExpr equalTo nullExpr2).eval(scope)).isTrue
        assertThat((nullExpr notEqualTo nullExpr2).eval(scope)).isFalse
    }

    @Test
    fun `equals of comparable literals`() {
        val testExpr = "test".expr()
        val nullExpr: Expression<String?> = null.expr()

        assertThat((testExpr equalTo "test").eval(scope)).isTrue
        assertThat((testExpr equalTo "another").eval(scope)).isFalse
        assertThat((testExpr notEqualTo "another").eval(scope)).isTrue
        assertThat((testExpr notEqualTo "test").eval(scope)).isFalse
        assertThat((testExpr equalTo nullExpr).eval(scope)).isFalse
        assertThat((testExpr notEqualTo nullExpr).eval(scope)).isTrue
        assertThat((nullExpr equalTo null).eval(scope)).isTrue
        assertThat((nullExpr notEqualTo null).eval(scope)).isFalse
    }

    @Test
    fun `equals of Kroner`() {
        val kroner5 = Kroner(5).expr()
        val kroner5Other = Kroner(5).expr()
        val kroner2 = Kroner(2).expr()
        val kronerNull: Expression<Kroner?> = null.expr()

        assertThat((kroner5 equalTo kroner5Other).eval(scope)).isTrue
        assertThat((kroner5 notEqualTo kroner5Other).eval(scope)).isFalse
        assertThat((kroner5 equalTo kroner2).eval(scope)).isFalse
        assertThat((kroner5 notEqualTo kroner2).eval(scope)).isTrue
        assertThat((kroner5 equalTo kronerNull).eval(scope)).isFalse
        assertThat((kroner5 notEqualTo kronerNull).eval(scope)).isTrue

        assertThat((kroner5 equalTo Kroner(5)).eval(scope)).isTrue
        assertThat((kroner5 notEqualTo Kroner(5)).eval(scope)).isFalse
        assertThat((kroner5 equalTo Kroner(2)).eval(scope)).isFalse
        assertThat((kroner5 notEqualTo Kroner(2)).eval(scope)).isTrue
    }

    @Test
    fun `equals of Year`() {
        val year2025 = Year(2025).expr()
        val year2025Other = Year(2025).expr()
        val year2022 = Year(2022).expr()
        val yearNull: Expression<Year?> = null.expr()

        assertThat((year2025 equalTo year2025Other).eval(scope)).isTrue
        assertThat((year2025 notEqualTo year2025Other).eval(scope)).isFalse
        assertThat((year2025 equalTo year2022).eval(scope)).isFalse
        assertThat((year2025 notEqualTo year2022).eval(scope)).isTrue
        assertThat((year2025 equalTo yearNull).eval(scope)).isFalse
        assertThat((year2025 notEqualTo yearNull).eval(scope)).isTrue

        assertThat((year2025 equalTo Year(2025)).eval(scope)).isTrue
        assertThat((year2025 notEqualTo Year(2025)).eval(scope)).isFalse
        assertThat((year2025 equalTo Year(2022)).eval(scope)).isFalse
        assertThat((year2025 notEqualTo Year(2022)).eval(scope)).isTrue
    }

    @Test
    fun `equals of Percent`() {
        val percent50 = Percent(50).expr()
        val percent50Other = Percent(50).expr()
        val percent20 = Percent(20).expr()
        val percentNull: Expression<Percent?> = null.expr()

        assertThat((percent50 equalTo percent50Other).eval(scope)).isTrue
        assertThat((percent50 notEqualTo percent50Other).eval(scope)).isFalse
        assertThat((percent50 equalTo percent20).eval(scope)).isFalse
        assertThat((percent50 notEqualTo percent20).eval(scope)).isTrue
        assertThat((percent50 equalTo percentNull).eval(scope)).isFalse
        assertThat((percent50 notEqualTo percentNull).eval(scope)).isTrue

        assertThat((percent50 equalTo Percent(50)).eval(scope)).isTrue
        assertThat((percent50 notEqualTo Percent(50)).eval(scope)).isFalse
        assertThat((percent50 equalTo Percent(20)).eval(scope)).isFalse
        assertThat((percent50 notEqualTo Percent(20)).eval(scope)).isTrue
    }

    @Test
    fun `equals of Language`() {
        val languageBokmal = Bokmal.expr()
        val languageEnglish = English.expr()
        val languageNynorsk = Nynorsk.expr()
        val languageNull: Expression<Language?> = null.expr()

        assertThat((languageBokmal equalTo languageBokmal).eval(scope)).isTrue
        assertThat((languageBokmal notEqualTo languageBokmal).eval(scope)).isFalse

        assertThat((languageBokmal notEqualTo languageEnglish).eval(scope)).isTrue
        assertThat((languageBokmal equalTo languageEnglish).eval(scope)).isFalse
        assertThat((languageBokmal notEqualTo languageNynorsk).eval(scope)).isTrue
        assertThat((languageBokmal equalTo languageNynorsk).eval(scope)).isFalse

        assertThat((languageBokmal equalTo languageNull).eval(scope)).isFalse
        assertThat((languageBokmal notEqualTo languageNull).eval(scope)).isTrue
    }

    @Test
    fun `equals of Language literals`() {
        val languageBokmal = Bokmal.expr()
        val languageNull: Expression<Language?> = null.expr()

        assertThat((languageBokmal equalTo Bokmal).eval(scope)).isTrue
        assertThat((languageBokmal notEqualTo Bokmal).eval(scope)).isFalse

        assertThat((languageBokmal notEqualTo English).eval(scope)).isTrue
        assertThat((languageBokmal equalTo English).eval(scope)).isFalse
        assertThat((languageBokmal notEqualTo Nynorsk).eval(scope)).isTrue
        assertThat((languageBokmal equalTo Nynorsk).eval(scope)).isFalse

        assertThat((languageNull equalTo null).eval(scope)).isTrue
        assertThat((languageNull notEqualTo null).eval(scope)).isFalse
        assertThat((languageNull equalTo Bokmal).eval(scope)).isFalse
        assertThat((languageNull notEqualTo Bokmal).eval(scope)).isTrue
    }

    @Test
    fun `equals of IntValue and literal`() {
        val kroner5 = Kroner(5).expr()

        assertThat((kroner5 equalTo 5).eval(scope)).isTrue
        assertThat((kroner5 notEqualTo 5).eval(scope)).isFalse
        assertThat((kroner5 equalTo 2).eval(scope)).isFalse
        assertThat((kroner5 notEqualTo 2).eval(scope)).isTrue
    }

}