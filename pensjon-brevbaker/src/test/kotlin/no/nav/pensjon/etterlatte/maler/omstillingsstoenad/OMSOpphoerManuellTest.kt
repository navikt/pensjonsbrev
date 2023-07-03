package no.nav.pensjon.etterlatte.maler.omstillingsstoenad

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.PDF_BUILDER_URL
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.render.*
import no.nav.pensjon.brev.writeTestHTML
import no.nav.pensjon.brev.writeTestPDF
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.Fixtures
import no.nav.pensjon.etterlatte.maler.*
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.OMSInnvilgelseManuell
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.OMSOpphoerManuell
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test


@Tag(TestTags.INTEGRATION_TEST)
class OMSOpphoerManuellITest {

    @Test
    fun pdftest() {
        Letter(
            OMSOpphoerManuell.template,
            Fixtures.create<ManueltBrevDTO>(),
            Language.Bokmal,
            Fixtures.felles
        ).let { PensjonLatexRenderer.render(it) }
            .let { runBlocking { LaTeXCompilerService(PDF_BUILDER_URL).producePDF(it, "test").base64PDF } }
            .also { writeTestPDF(EtterlatteBrevKode.OMS_OPPHOER_MANUELL.name, it) }
    }

    @Test
    fun testHtml() {
        Letter(
            OMSOpphoerManuell.template,
            Fixtures.create<ManueltBrevDTO>(),
            Language.Bokmal,
            Fixtures.felles
        ).let { PensjonHTMLRenderer.render(it) }
            .also { writeTestHTML(EtterlatteBrevKode.OMS_OPPHOER_MANUELL.name, it) }
    }

}