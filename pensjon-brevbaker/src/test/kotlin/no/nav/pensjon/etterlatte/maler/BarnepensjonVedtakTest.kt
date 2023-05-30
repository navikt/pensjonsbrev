package no.nav.pensjon.etterlatte.maler

import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.render.*
import no.nav.pensjon.etterlatte.Fixtures
import no.nav.pensjon.etterlatte.maler.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test


@Tag(TestTags.PDF_BYGGER)
class BarnepensjonVedtakITest {

    @Test
    fun pdftest() {
        Letter(
            BarnepensjonVedtak.template,
            Fixtures.create<BarnepensjonVedtakDTO>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestPDF("BARNEPENSJON_VEDTAK")
    }

    @Test
    fun testHtml() {
        Letter(
            BarnepensjonVedtak.template,
            Fixtures.create<BarnepensjonVedtakDTO>(),
            Language.Bokmal,
            Fixtures.felles
        ).let { PensjonHTMLRenderer.render(it) }
            .also { writeTestHTML("BARNEPENSJON_VEDTAK", it) }
    }

}