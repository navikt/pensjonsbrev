package no.nav.pensjon.etterlatte.maler.barnepensjon

import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.renderTestHtml
import no.nav.pensjon.brev.renderTestPDF
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.Fixtures
import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.BarnepensjonAvslag
import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.BarnepensjonAvslagDTO
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.INTEGRATION_TEST)
class BarnepensjonAvslagTest {

    @Test
    fun pdftest() {
        val letter = Letter(
            BarnepensjonAvslag.template,
            Fixtures.create<BarnepensjonAvslagDTO>(),
            Language.Bokmal,
            Fixtures.felles,
        )
        letter.renderTestPDF(EtterlatteBrevKode.BP_AVSLAG.name)
    }

    @Test
    fun testHtml() {
        Letter(
            BarnepensjonAvslag.template,
            Fixtures.create<BarnepensjonAvslagDTO>(),
            Language.Bokmal,
            Fixtures.felles,
        ).renderTestHtml(EtterlatteBrevKode.BP_AVSLAG.name)
    }
}
