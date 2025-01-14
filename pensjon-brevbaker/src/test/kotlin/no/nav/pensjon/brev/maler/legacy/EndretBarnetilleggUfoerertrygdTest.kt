package no.nav.pensjon.brev.maler.legacy

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.api.model.maler.legacy.EndretBarnetilleggUfoeretrygdDto
import no.nav.pensjon.brev.renderTestHtml
import no.nav.pensjon.brev.renderTestPDF
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test


@Tag(TestTags.MANUAL_TEST)
class EndretBarnetilleggUfoerertrygdTest {

    @Test
    fun testPdf() {
        renderTestPDF(
            EndretBarnetilleggUfoerertrygd.template,
            Fixtures.create<EndretBarnetilleggUfoeretrygdDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto,
            "UT_ENDRET_BARNETILLEGG"
        )
    }

    @Test
    fun testHtml() {
        renderTestHtml(
            EndretBarnetilleggUfoerertrygd.template,
            Fixtures.create<EndretBarnetilleggUfoeretrygdDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto,
            "UT_ENDRET_BARNETILLEGG"
        )
    }
}