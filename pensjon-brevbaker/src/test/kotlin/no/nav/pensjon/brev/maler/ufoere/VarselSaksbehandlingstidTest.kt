package no.nav.pensjon.brev.maler.ufoere

import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.VarselSaksbehandlingstidAutoDto
import no.nav.pensjon.brev.latex.*
import no.nav.pensjon.brev.maler.ufoereBrev.VarselSaksbehandlingstidAuto
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.render.*
import org.junit.jupiter.api.*

@Tag(TestTags.MANUAL_TEST)
class VarselSaksbehandlingstidTest {

    @Test
    fun pdftest() {
        Letter(
            VarselSaksbehandlingstidAuto.template,
            Fixtures.create<VarselSaksbehandlingstidAutoDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF("UT_VARSEL_SAKSBEHANDLINGSTID_AUTO")
    }

    @Test
    fun testHtml() {
        Letter(
            VarselSaksbehandlingstidAuto.template,
            Fixtures.create<VarselSaksbehandlingstidAutoDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestHtml("UT_VARSEL_SAKSBEHANDLINGSTID_AUTO_BOKMAL")
    }

}