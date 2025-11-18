package no.nav.pensjon.brev.maler.redigerbar

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakOmInnvilgelseAvOmsorgspoengDto
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class VedtakOmInnvilgelseAvOmsorgspoengTest {

    @Test
    fun testPdf() {
        LetterTestImpl(
            VedtakOmInnvilgelseAvOmsorgspoeng.template,
            Fixtures.create<VedtakOmInnvilgelseAvOmsorgspoengDto>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestPDF(VedtakOmInnvilgelseAvOmsorgspoeng.kode.name)
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            VedtakOmInnvilgelseAvOmsorgspoeng.template,
            Fixtures.create<VedtakOmInnvilgelseAvOmsorgspoengDto>(),
            Language.Nynorsk,
            Fixtures.felles
        ).renderTestHtml(VedtakOmInnvilgelseAvOmsorgspoeng.kode.name)
    }
}