package no.nav.pensjon.brev.maler.legacy

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDto
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class EndretUfoeretrygdPGAInntektTest {

    @Test
    fun testPdf() {
        LetterTestImpl(
            EndretUfoeretrygdPGAInntektLegacy.template,
            Fixtures.create<EndretUfoeretrygdPGAInntektDto>(),
            Language.Nynorsk,
            Fixtures.fellesAuto
        ).renderTestPDF("UT_ENDRET_PGA_INNTEKT")
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            EndretUfoeretrygdPGAInntektLegacy.template,
            Fixtures.create<EndretUfoeretrygdPGAInntektDto>(),
            Language.Nynorsk,
            Fixtures.fellesAuto
        ).renderTestHtml("UT_ENDRET_PGA_INNTEKT")
    }
}