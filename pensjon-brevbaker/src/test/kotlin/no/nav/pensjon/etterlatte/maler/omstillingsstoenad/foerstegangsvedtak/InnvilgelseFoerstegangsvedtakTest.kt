package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.foerstegangsvedtak

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
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test


@Tag(TestTags.INTEGRATION_TEST)
class OMSInnvilgelseFoerstegangsvedtakITest {

    @Test
    fun pdftest() {
        Letter(
            InnvilgelseFoerstegangsvedtak.template,
            Fixtures.create<OMSInnvilgelseFoerstegangsvedtakDTO>(),
            Language.Bokmal,
            Fixtures.felles
        ).let { PensjonLatexRenderer.render(it) }
            .let { runBlocking { LaTeXCompilerService(PDF_BUILDER_URL).producePDF(it, "test").base64PDF } }
            .also { writeTestPDF(EtterlatteBrevKode.OMS_INNVILGELSE_FOERSTEGANGSVEDTAK.name, it) }
    }

    @Test
    fun testHtml() {
        Letter(
            InnvilgelseFoerstegangsvedtak.template,
            Fixtures.create<OMSInnvilgelseFoerstegangsvedtakDTO>(),
            Language.Bokmal,
            Fixtures.felles
        ).let { PensjonHTMLRenderer.render(it) }
            .also { writeTestHTML(EtterlatteBrevKode.OMS_INNVILGELSE_FOERSTEGANGSVEDTAK.name, it) }
    }

}