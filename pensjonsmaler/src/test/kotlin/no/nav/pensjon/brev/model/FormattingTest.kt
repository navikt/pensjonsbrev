package no.nav.pensjon.brev.model

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.template.ExpressionScope
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.expression.expr
import org.junit.jupiter.api.Test


class FormattingTest {
    private val testExpressionScope = ExpressionScope(EmptyAutobrevdata, Fixtures.felles, Language.Bokmal)

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