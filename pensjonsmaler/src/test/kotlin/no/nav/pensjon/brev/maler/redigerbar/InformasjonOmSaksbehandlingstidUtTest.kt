package no.nav.pensjon.brev.maler.redigerbar

import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidUtDto
import no.nav.pensjon.brev.api.toLanguage
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brevbaker.api.model.LanguageCode.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class InformasjonOmSaksbehandlingstidUtTest {
    private val data =
        InformasjonOmSaksbehandlingstidUtDto(
            saksbehandlerValg =
                InformasjonOmSaksbehandlingstidUtDto.SaksbehandlerValg(
                    forlengetSaksbehandlingstid = false,
                ),
            pesysData = EmptyBrevdata,
        )

    @Test
    fun `uten forlengetSaksbehandlingstid`() {
        writeAllLanguages("uten-forlenget-saksbehandlingstid", data)
    }

    @Test
    fun `med forlengetSaksbehandlingstid`() {
        writeAllLanguages("med-forlengset-saksbehandlingstid", data.copy(saksbehandlerValg = data.saksbehandlerValg.copy(forlengetSaksbehandlingstid = true)))
    }

    private fun writeAllLanguages(
        testNavn: String,
        data: InformasjonOmSaksbehandlingstidUtDto,
    ) {
        listOf(BOKMAL, NYNORSK, ENGLISH).forEach { lang ->
            Letter(
                InformasjonOmSaksbehandlingstidUT.template,
                data,
                lang.toLanguage(),
                Fixtures.felles,
            ).renderTestPDF("000130-$testNavn-${lang.name}")
        }
    }
}
