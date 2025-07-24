package no.nav.pensjon.brev.model

import com.natpryce.hamkrest.assertion.assertThat
import no.nav.brev.brevbaker.FellesFactory
import com.natpryce.hamkrest.equalTo
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.template.ExpressionScope
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brevbaker.api.model.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.Telefonnummer
import org.junit.jupiter.api.Test

class GenerellFormattingTest {
    private val testExpressionScope = ExpressionScope(EmptyBrevdata, FellesFactory.felles, Language.Bokmal)

    @Test
    fun `kan formattere nullable kroneverdi som er satt`(){
        assertThat(Kroner(1234).expr().format().eval(testExpressionScope), equalTo("1\u00A0234"))
    }

    @Test
    fun `kan formattere nullable kroneverdi som ikke er satt`(){
        assertThat(null.expr<Kroner?>().format().eval(testExpressionScope), equalTo(null))
    }

    @Test
    fun `kan formattere nullable telefonnummer som er satt`(){
        assertThat(Telefonnummer("12345678").expr().format().eval(testExpressionScope), equalTo("12\u00A034\u00A056\u00A078"))
    }

    @Test
    fun `kan formattere nullable telefonnummer som ikke er satt`(){
        assertThat(null.expr<Telefonnummer?>().format().eval(testExpressionScope), equalTo(null))
    }

    @Test
    fun `kan formattere nullable foedselsnummer som er satt`(){
        assertThat(Foedselsnummer("12345678910").expr().format().eval(testExpressionScope), equalTo("123456 78910"))
    }

    @Test
    fun `kan formattere nullable foedselsnummer som ikke er satt`(){
        assertThat(null.expr<Foedselsnummer?>().format().eval(testExpressionScope), equalTo(null))
    }


}