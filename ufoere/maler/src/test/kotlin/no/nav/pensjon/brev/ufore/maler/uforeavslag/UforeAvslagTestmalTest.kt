package no.nav.pensjon.brev.ufore.maler.uforeavslag

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.ufore.Fixtures
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagTestmalDto
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class UforeAvslagTestmalTest {

    @Test
    fun testHtml() {
        LetterTestImpl(
            UforeAvslagTestmal.template,
            Fixtures.create< UforeAvslagTestmalDto>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestHtml("UTAvslagTestmal")
    }
}