package no.nav.pensjon.brev.maler.redigerbar

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidDto
import no.nav.pensjon.brev.api.toLanguage
import no.nav.pensjon.brevbaker.api.model.LanguageCode.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class InformasjonOmSaksbehandlingstidITest {

    private val data = InformasjonOmSaksbehandlingstidDto(
        saksbehandlerValg = InformasjonOmSaksbehandlingstidDto.SaksbehandlerValg(
            soeknadMottattFraUtland = false,
            venterPaaSvarAFP = false,
        ),
        pesysData = EmptyBrevdata
    )

    @Test
    fun `uten land og venter svar AFP`() {
        writeAllLanguages("uten-land-og-venter-svar", data)
    }

    @Test
    fun `med land`() {
        writeAllLanguages("med-land", data.copy(saksbehandlerValg = data.saksbehandlerValg.copy(soeknadMottattFraUtland = true)))
    }

    @Test
    fun `med venter svar AFP`() {
        writeAllLanguages(
            "med-venter-svar",
            data.copy(saksbehandlerValg = data.saksbehandlerValg.copy(venterPaaSvarAFP = true))
        )
    }

    private fun writeAllLanguages(testNavn: String, data: InformasjonOmSaksbehandlingstidDto) {
        listOf(BOKMAL, NYNORSK, ENGLISH).forEach { lang ->
            LetterTestImpl(
                InformasjonOmSaksbehandlingstid.template,
                data,
                lang.toLanguage(),
                Fixtures.felles
            ).renderTestPDF("000130-$testNavn-${lang.name}")
        }
    }

}