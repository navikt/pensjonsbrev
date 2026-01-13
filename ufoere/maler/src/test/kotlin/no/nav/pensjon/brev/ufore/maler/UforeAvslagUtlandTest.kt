package no.nav.pensjon.brev.ufore.maler

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.ufore.Fixtures
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagUtlandDto
import no.nav.pensjon.brev.ufore.maler.uforeavslag.UforeAvslagMedlemskapUtland
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class UforeAvslagUtlandTest {
    @Test
    fun testHtml() {
        LetterTestImpl(
            UforeAvslagMedlemskapUtland.template,
            Fixtures.create<UforeAvslagUtlandDto>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestHtml(UforeAvslagMedlemskapUtland.kode.name)
    }
}