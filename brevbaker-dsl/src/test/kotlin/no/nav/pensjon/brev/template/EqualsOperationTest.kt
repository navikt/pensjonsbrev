package no.nav.pensjon.brev.template

import com.natpryce.hamkrest.MatchResult
import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.Percent
import no.nav.pensjon.brevbaker.api.model.Year
import org.junit.jupiter.api.Test

class EvaluatesMatcher<in T>(private val matcher: Matcher<T>, private val scope: ExpressionScope<*>) :
    Matcher<Expression<T>> {
    override fun invoke(actual: Expression<T>): MatchResult = matcher(actual.eval(scope))
    override val description: String get() = "evaluates and ${matcher.description}"
}

fun <T> evalsTo(
    expected: T,
    scope: ExpressionScope<*> = ExpressionScope(Unit, FellesFactory.felles, Bokmal)
): EvaluatesMatcher<T> =
    EvaluatesMatcher(equalTo(expected), scope)


class EqualsOperationTest {

    @Test
    fun `equals of comparable expression`() {
        val testExpr = "test".expr()
        val testExpr2 = "test".expr()
        val anotherExpr = "another".expr()
        val nullExpr: Expression<String?> = null.expr()
        val nullExpr2: Expression<String?> = null.expr()

        assertThat(testExpr equalTo testExpr2, evalsTo(true))
        assertThat(testExpr equalTo anotherExpr, evalsTo(false))
        assertThat(testExpr notEqualTo anotherExpr, evalsTo(true))
        assertThat(testExpr notEqualTo testExpr2, evalsTo(false))
        assertThat(testExpr equalTo nullExpr, evalsTo(false) )
        assertThat(testExpr notEqualTo nullExpr, evalsTo(true) )
        assertThat(nullExpr equalTo nullExpr2, evalsTo(true))
        assertThat(nullExpr notEqualTo nullExpr2, evalsTo(false))
    }

    @Test
    fun `equals of comparable literals`() {
        val testExpr = "test".expr()
        val nullExpr: Expression<String?> = null.expr()

        assertThat(testExpr equalTo "test", evalsTo(true))
        assertThat(testExpr equalTo "another", evalsTo(false))
        assertThat(testExpr notEqualTo "another", evalsTo(true))
        assertThat(testExpr notEqualTo "test", evalsTo(false))
        assertThat(testExpr equalTo nullExpr, evalsTo(false) )
        assertThat(testExpr notEqualTo nullExpr, evalsTo(true) )
        assertThat(nullExpr equalTo null, evalsTo(true))
        assertThat(nullExpr notEqualTo null, evalsTo(false))
    }

    @Test
    fun `equals of Kroner`() {
        val kroner5 = Kroner(5).expr()
        val kroner5Other = Kroner(5).expr()
        val kroner2 = Kroner(2).expr()
        val kronerNull: Expression<Kroner?> = null.expr()

        assertThat(kroner5 equalTo kroner5Other, evalsTo(true))
        assertThat(kroner5 notEqualTo kroner5Other, evalsTo(false))
        assertThat(kroner5 equalTo kroner2, evalsTo(false))
        assertThat(kroner5 notEqualTo kroner2, evalsTo(true))
        assertThat(kroner5 equalTo kronerNull, evalsTo(false))
        assertThat(kroner5 notEqualTo kronerNull, evalsTo(true))

        assertThat(kroner5 equalTo Kroner(5), evalsTo(true))
        assertThat(kroner5 notEqualTo Kroner(5), evalsTo(false))
        assertThat(kroner5 equalTo Kroner(2), evalsTo(false))
        assertThat(kroner5 notEqualTo Kroner(2), evalsTo(true))
    }

    @Test
    fun `equals of Year`() {
        val year2025 = Year(2025).expr()
        val year2025Other = Year(2025).expr()
        val year2022 = Year(2022).expr()
        val yearNull: Expression<Year?> = null.expr()

        assertThat(year2025 equalTo year2025Other, evalsTo(true))
        assertThat(year2025 notEqualTo year2025Other, evalsTo(false))
        assertThat(year2025 equalTo year2022, evalsTo(false))
        assertThat(year2025 notEqualTo year2022, evalsTo(true))
        assertThat(year2025 equalTo yearNull, evalsTo(false))
        assertThat(year2025 notEqualTo yearNull, evalsTo(true))

        assertThat(year2025 equalTo Year(2025), evalsTo(true))
        assertThat(year2025 notEqualTo Year(2025), evalsTo(false))
        assertThat(year2025 equalTo Year(2022), evalsTo(false))
        assertThat(year2025 notEqualTo Year(2022), evalsTo(true))
    }

    @Test
    fun `equals of Percent`() {
        val percent50 = Percent(50).expr()
        val percent50Other = Percent(50).expr()
        val percent20 = Percent(20).expr()
        val percentNull: Expression<Percent?> = null.expr()

        assertThat(percent50 equalTo percent50Other, evalsTo(true))
        assertThat(percent50 notEqualTo percent50Other, evalsTo(false))
        assertThat(percent50 equalTo percent20, evalsTo(false))
        assertThat(percent50 notEqualTo percent20, evalsTo(true))
        assertThat(percent50 equalTo percentNull, evalsTo(false))
        assertThat(percent50 notEqualTo percentNull, evalsTo(true))

        assertThat(percent50 equalTo Percent(50), evalsTo(true))
        assertThat(percent50 notEqualTo Percent(50), evalsTo(false))
        assertThat(percent50 equalTo Percent(20), evalsTo(false))
        assertThat(percent50 notEqualTo Percent(20), evalsTo(true))
    }

    @Test
    fun `equals of Language`() {
        val languageBokmal = Bokmal.expr()
        val languageEnglish = English.expr()
        val languageNynorsk = Nynorsk.expr()
        val languageNull: Expression<Language?> = null.expr()

        assertThat(languageBokmal equalTo languageBokmal, evalsTo(true))
        assertThat(languageBokmal notEqualTo languageBokmal, evalsTo(false))

        assertThat(languageBokmal notEqualTo languageEnglish, evalsTo(true))
        assertThat(languageBokmal equalTo languageEnglish, evalsTo(false))
        assertThat(languageBokmal notEqualTo languageNynorsk, evalsTo(true))
        assertThat(languageBokmal equalTo languageNynorsk, evalsTo(false))

        assertThat(languageBokmal equalTo languageNull, evalsTo(false))
        assertThat(languageBokmal notEqualTo languageNull, evalsTo(true))
    }

    @Test
    fun `equals of Language literals`() {
        val languageBokmal = Bokmal.expr()
        val languageNull: Expression<Language?> = null.expr()

        assertThat(languageBokmal equalTo Bokmal, evalsTo(true))
        assertThat(languageBokmal notEqualTo Bokmal, evalsTo(false))

        assertThat(languageBokmal notEqualTo English, evalsTo(true))
        assertThat(languageBokmal equalTo English, evalsTo(false))
        assertThat(languageBokmal notEqualTo Nynorsk, evalsTo(true))
        assertThat(languageBokmal equalTo Nynorsk, evalsTo(false))

        assertThat(languageNull equalTo null, evalsTo(true))
        assertThat(languageNull notEqualTo null, evalsTo(false))
        assertThat(languageNull equalTo Bokmal, evalsTo(false))
        assertThat(languageNull notEqualTo Bokmal, evalsTo(true))
    }

    @Test
    fun `equals of IntValue and literal`() {
        val kroner5 = Kroner(5).expr()

        assertThat(kroner5 equalTo 5, evalsTo(true))
        assertThat(kroner5 notEqualTo 5, evalsTo(false))
        assertThat(kroner5 equalTo 2, evalsTo(false))
        assertThat(kroner5 notEqualTo 2, evalsTo(true))
    }

}