package no.nav.pensjon.brev.maler.vedlegg.pdf

import no.nav.brev.brevbaker.FellesFactory
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.SamletMeldingOmPensjonsvedtakDto
import no.nav.pensjon.brev.fixtures.createP1Dto
import no.nav.pensjon.brev.template.ExpressionScope
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.expression.expr
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class P1SomDSLTest {

    @Test
    fun P1SomDSL() {
        val scope = ExpressionScope(EmptyBrevdata, FellesFactory.felles, Language.Bokmal)
        val somDSL = p1Vedlegg.createVedlegg(scope, createP1Dto().expr(), SamletMeldingOmPensjonsvedtakDto.SaksbehandlerValg(false).expr())
        assertEquals(8, somDSL.sider.size)
        assertFalse { somDSL.sider.map { it.felt }.any { it.isEmpty() } }
    }

}