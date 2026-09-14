package no.nav.pensjon.brev.ufore.maler.uforeavslag

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.ufore.Fixtures
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class UforeAvslagTestmalTest {

    @Test
    fun testHtml() {
        LetterTestImpl(
            UforeAvslagTestmal.template,
            Fixtures.create(UforeAvslagTestmal::class),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestHtml("UTAvslagTestmal")
    }
}