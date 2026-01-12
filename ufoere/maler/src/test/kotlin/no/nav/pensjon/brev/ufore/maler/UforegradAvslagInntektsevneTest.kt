package no.nav.pensjon.brev.ufore.maler

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.ufore.Fixtures
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDto
import no.nav.pensjon.brev.ufore.maler.uforeavslag.UforegradAvslagInntektsevne
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class UforegradAvslagInntektsevneTest {
    @Test
    fun testHtml() {
        LetterTestImpl(
            UforegradAvslagInntektsevne.template,
            Fixtures.create<UforeAvslagInntektDto>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestHtml(UforegradAvslagInntektsevne.kode.name)
    }
}