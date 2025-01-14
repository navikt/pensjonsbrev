package no.nav.pensjon.brev.maler.legacy

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.api.model.maler.legacy.EndretUfoeretrygdPGAInntektDto
import no.nav.pensjon.brev.renderTestHtml
import no.nav.pensjon.brev.renderTestPDF
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class EndretUfoeretrygdPGAInntektTest {

    @Test
    fun testPdf() {
        renderTestPDF(
            EndretUfoeretrygdPGAInntektLegacy.template,
            Fixtures.create<EndretUfoeretrygdPGAInntektDto>(),
            Language.Nynorsk,
            Fixtures.fellesAuto,
            "UT_ENDRET_PGA_INNTEKT"
        )
    }

    @Test
    fun testHtml() {
        renderTestHtml(
            EndretUfoeretrygdPGAInntektLegacy.template,
            Fixtures.create<EndretUfoeretrygdPGAInntektDto>(),
            Language.Nynorsk,
            Fixtures.fellesAuto,
            "UT_ENDRET_PGA_INNTEKT"
        )
    }
}