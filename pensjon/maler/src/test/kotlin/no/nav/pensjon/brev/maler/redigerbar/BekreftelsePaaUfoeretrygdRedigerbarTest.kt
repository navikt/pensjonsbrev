package no.nav.pensjon.brev.maler.redigerbar

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.maler.ufore.diverse.BekreftelsePaaUfoeretrygdRedigerbar
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class BekreftelsePaaUfoeretrygdRedigerbarTest {

    @Test
    fun testPdf() {
        LetterTestImpl(
            BekreftelsePaaUfoeretrygdRedigerbar.template,
            Fixtures.create(BekreftelsePaaUfoeretrygdRedigerbar::class),
            Language.Nynorsk,
            Fixtures.felles
        ).renderTestPDF(BekreftelsePaaUfoeretrygdRedigerbar.kode.name)
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            BekreftelsePaaUfoeretrygdRedigerbar.template,
            Fixtures.create(BekreftelsePaaUfoeretrygdRedigerbar::class),
            Language.English,
            Fixtures.felles
        ).renderTestHtml(BekreftelsePaaUfoeretrygdRedigerbar.kode.name)
    }
}