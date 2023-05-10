package no.nav.pensjon.brev.maler.redigerbar

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidDto.InkluderVenterSvarAFP
import no.nav.pensjon.brev.api.toLanguage
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.render.*
import no.nav.pensjon.brevbaker.api.model.LanguageCode.*
import org.junit.jupiter.api.*
import java.time.LocalDate

@Tag(TestTags.PDF_BYGGER)
class InformasjonOmSaksbehandlingstidITest {

    private val data = InformasjonOmSaksbehandlingstidDto(LocalDate.now().minusWeeks(1), "AFP", null, null, 10)

    @Test
    fun `uten land og venter svar AFP`() {
        writeAllLanguages("uten-land-og-venter-svar", data)
    }

    @Test
    fun `med land`() {
        writeAllLanguages("med-land", data.copy(land = "Polen"))
    }

    @Test
    fun `med venter svar AFP`() {
        writeAllLanguages("med-venter-svar", data.copy(inkluderVenterSvarAFP = InkluderVenterSvarAFP(60, LocalDate.now().plusMonths(2).withDayOfMonth(1))))
    }

    private fun writeAllLanguages(testNavn: String, data: InformasjonOmSaksbehandlingstidDto) {
        listOf(BOKMAL, NYNORSK, ENGLISH).forEach { lang ->
            Letter(
                InformasjonOmSaksbehandlingstid.template,
                data,
                lang.toLanguage(),
                Fixtures.felles
            )
                .let { PensjonLatexRenderer.render(it) }
                .let { runBlocking { LaTeXCompilerService(PDF_BUILDER_URL).producePDF(it, "test").base64PDF } }
                .also { writeTestPDF("000130-$testNavn-${lang.name}", it) }
        }
    }

}