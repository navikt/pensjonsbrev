package no.nav.pensjon.brev.maler.redigerbar

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakAvslagPaaOmsorgsopptjeningDto
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test


@Tag(TestTags.MANUAL_TEST)
class VedtakAvslagPaaOmsorgsopptjeningTest {

    @Test
    fun testPdf() {
        LetterTestImpl(
            VedtakOmInnvilgelseAvOmsorgspoeng.template,
            Fixtures.create<VedtakAvslagPaaOmsorgsopptjeningDto>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestPDF(VedtakAvslagPaaOmsorgsopptjening.kode.name)
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            VedtakAvslagPaaOmsorgsopptjening.template,
            Fixtures.create<VedtakAvslagPaaOmsorgsopptjeningDto>(),
            Language.English,
            Fixtures.felles
        ).renderTestHtml(VedtakAvslagPaaOmsorgsopptjening.kode.name)
    }
}