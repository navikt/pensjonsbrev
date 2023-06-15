package no.nav.pensjon.etterlatte.maler

import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.render.*
import no.nav.pensjon.etterlatte.Fixtures
import no.nav.pensjon.etterlatte.maler.*
import no.nav.pensjon.etterlatte.maler.barnepensjon.BarnepensjonInnvilgelse
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test


@Tag(TestTags.PDF_BYGGER)
class BarnepensjonInnvilgelseTest {

    @Test
    fun pdftest() {
        Letter(
            BarnepensjonInnvilgelse.template,
            Fixtures.create<BarnepensjonInnvilgelseDTO>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestPDF("BARNEPENSJON_INNVILGELSE")
    }

    @Test
    fun testHtml() {
        Letter(
            BarnepensjonInnvilgelse.template,
            Fixtures.create<BarnepensjonInnvilgelseDTO>(),
            Language.Bokmal,
            Fixtures.felles
        ).let { PensjonHTMLRenderer.render(it) }
            .also { writeTestHTML("BARNEPENSJON_INNVILGELSE", it) }
    }

}