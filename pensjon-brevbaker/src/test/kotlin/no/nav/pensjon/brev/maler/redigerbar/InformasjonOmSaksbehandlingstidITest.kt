package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidDto.SaksbehandlerValg.InkluderVenterSvarAFP
import no.nav.pensjon.brev.api.toLanguage
import no.nav.pensjon.brev.renderTestPDF
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brevbaker.api.model.LanguageCode.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.LocalDate

@Tag(TestTags.INTEGRATION_TEST)
class InformasjonOmSaksbehandlingstidITest {

    private val data = InformasjonOmSaksbehandlingstidDto(
        saksbehandlerValg = InformasjonOmSaksbehandlingstidDto.SaksbehandlerValg(LocalDate.now().minusWeeks(1), "AFP", null, null, 10),
        pesysData = EmptyBrevdata
    )

    @Test
    fun `uten land og venter svar AFP`() {
        writeAllLanguages("uten-land-og-venter-svar", data)
    }

    @Test
    fun `med land`() {
        writeAllLanguages("med-land", data.copy(saksbehandlerValg = data.saksbehandlerValg.copy(land = "Polen")))
    }

    @Test
    fun `med venter svar AFP`() {
        writeAllLanguages(
            "med-venter-svar",
            data.copy(
                saksbehandlerValg = data.saksbehandlerValg.copy(
                    inkluderVenterSvarAFP = InkluderVenterSvarAFP(
                        60,
                        LocalDate.now().plusMonths(2).withDayOfMonth(1)
                    )
                )
            )
        )
    }

    private fun writeAllLanguages(testNavn: String, data: InformasjonOmSaksbehandlingstidDto) {
        listOf(BOKMAL, NYNORSK, ENGLISH).forEach { lang ->
            Letter(
                InformasjonOmSaksbehandlingstid.template,
                data,
                lang.toLanguage(),
                Fixtures.felles
            ).renderTestPDF("000130-$testNavn-${lang.name}")
        }
    }

}