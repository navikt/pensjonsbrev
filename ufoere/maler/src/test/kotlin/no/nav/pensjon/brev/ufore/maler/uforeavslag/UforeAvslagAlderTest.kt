package no.nav.pensjon.brev.ufore.maler.uforeavslag

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.ufore.Fixtures
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDto
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class UforeAvslagAlderTest {

    @Test
    fun testHtml() {
        LetterTestImpl(
            UforeAvslagAlder.template,
            Fixtures.create<UforeAvslagEnkelDto>(),
            Language.Nynorsk,
            Fixtures.felles
        ).renderTestHtml("UTAvslagAlder")
    }
}