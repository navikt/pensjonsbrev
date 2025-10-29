package no.nav.pensjon.brev.maler.redigerbar

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.redigerbar.BekreftelsePaaUfoeretrygdDto
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class BekreftelsePaaUfoeretrygdTest {

    @Test
    fun testPdf() {
        LetterTestImpl(
            BekreftelsePaaUfoeretrygd.template,
            Fixtures.create<BekreftelsePaaUfoeretrygdDto>(),
            Language.Nynorsk,
            Fixtures.felles
        ).renderTestPDF(BekreftelsePaaUfoeretrygd.kode.name)
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            BekreftelsePaaUfoeretrygd.template,
            Fixtures.create<BekreftelsePaaUfoeretrygdDto>(),
            Language.English,
            Fixtures.felles
        ).renderTestHtml(BekreftelsePaaUfoeretrygd.kode.name)
    }
}