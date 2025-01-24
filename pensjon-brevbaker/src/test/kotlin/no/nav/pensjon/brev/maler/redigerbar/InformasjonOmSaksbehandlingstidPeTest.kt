package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidPeDto
import no.nav.pensjon.brev.api.toLanguage
import no.nav.pensjon.brev.renderTestPDF
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brevbaker.api.model.LanguageCode.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class InformasjonOmSaksbehandlingstidPeTest {

    private val data = InformasjonOmSaksbehandlingstidPeDto(
        saksbehandlerValg = InformasjonOmSaksbehandlingstidPeDto.SaksbehandlerValg(
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
        writeAllLanguages("med-land", data.copy(saksbehandlerValg = data.saksbehandlerValg.copy(soeknadMottattFraUtland = false)))
    }

    @Test
    fun `med venter svar AFP`() {
        writeAllLanguages("med-venter-svar", data.copy(saksbehandlerValg = data.saksbehandlerValg.copy(venterPaaSvarAFP = true)))
    }

    private fun writeAllLanguages(testNavn: String, data: InformasjonOmSaksbehandlingstidPeDto) {
        listOf(BOKMAL, NYNORSK, ENGLISH).forEach { lang ->
            Letter(
                InformasjonOmSaksbehandlingstidPE.template,
                data,
                lang.toLanguage(),
                Fixtures.felles
            ).renderTestPDF("000130-$testNavn-${lang.name}")
        }
    }
}