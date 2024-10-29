package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.redigerbar.PaaminnelseLeveattestDto
import no.nav.pensjon.brev.renderTestHtml
import no.nav.pensjon.brev.renderTestPDF
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class PaaminnelseLeveattestTest {

    @Test
    fun testPdf() {
        Letter(
            PaaminnelseLeveattest.template,
            Fixtures.create<EmptyBrevdata>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF(PaaminnelseLeveattest.kode.name)
    }

    @Test
    fun testHtml() {
        Letter(
            PaaminnelseLeveattest.template,
            Fixtures.create<PaaminnelseLeveattestDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestHtml(PaaminnelseLeveattest.kode.name)
    }
}