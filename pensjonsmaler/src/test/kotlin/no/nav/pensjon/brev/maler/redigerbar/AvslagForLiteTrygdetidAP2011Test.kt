package no.nav.pensjon.brev.maler.redigerbar

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDto
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class AvslagForLiteTrygdetidAP2011Test {

    @Test
    fun testPdf() {
        LetterTestImpl(
            AvslagForLiteTrygdetidAP2011.template,
            Fixtures.create<AvslagForLiteTrygdetidAPDto>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestPDF(AvslagForLiteTrygdetidAP2011.kode.name)
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            AvslagForLiteTrygdetidAP2011.template,
            Fixtures.create<AvslagForLiteTrygdetidAPDto>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestHtml(AvslagForLiteTrygdetidAP2011.kode.name)
    }
}

