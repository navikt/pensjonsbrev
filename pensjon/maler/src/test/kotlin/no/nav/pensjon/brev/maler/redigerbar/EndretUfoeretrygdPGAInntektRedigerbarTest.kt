package no.nav.pensjon.brev.maler.redigerbar

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.maler.ufore.endring.EndretUfoeretrygdPGAInntektRedigerbar
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class EndretUfoeretrygdPGAInntektRedigerbarTest {

    @Test
    fun testPdf() {
        LetterTestImpl(
            EndretUfoeretrygdPGAInntektRedigerbar.template,
            Fixtures.create(EndretUfoeretrygdPGAInntektRedigerbar::class),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestPDF(EndretUfoeretrygdPGAInntektRedigerbar.kode.name)
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            EndretUfoeretrygdPGAInntektRedigerbar.template,
            Fixtures.create(EndretUfoeretrygdPGAInntektRedigerbar::class),
            Language.Nynorsk,
            Fixtures.felles
        ).renderTestHtml(EndretUfoeretrygdPGAInntektRedigerbar.kode.name)
    }
}
