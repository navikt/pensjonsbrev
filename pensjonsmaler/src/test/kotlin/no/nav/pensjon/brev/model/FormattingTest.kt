package no.nav.pensjon.brev.model

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.template.ExpressionScope
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Test
import no.nav.brev.brevbaker.FellesFactory
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.template.dsl.expression.expr


class FormattingTest {
    private val testExpressionScope = ExpressionScope(EmptyBrevdata, FellesFactory.felles, Language.Bokmal)

    @Test
    fun `kan formattere sakstype ufoeretrygd`() {
        assert(Sakstype.UFOREP.expr().format().eval(testExpressionScope) == "uføretrygd")
    }

    @Test
    fun `sakstype som ikke er satt formatteres til null`() {
        assert(null.expr<Sakstype?>().format().eval(testExpressionScope) == null)
    }

    @Test
    fun `sakstype generell formatteres til null fordi den krever fritekst`() {
        assert(Sakstype.GENRL.expr().format().eval(testExpressionScope) == null)
    }

}