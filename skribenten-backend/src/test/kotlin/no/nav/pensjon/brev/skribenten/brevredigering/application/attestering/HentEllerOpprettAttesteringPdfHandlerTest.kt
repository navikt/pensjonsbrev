package no.nav.pensjon.brev.skribenten.brevredigering.application.attestering

import no.nav.pensjon.brev.skribenten.Testbrevkoder
import no.nav.pensjon.brev.skribenten.brevredigering.application.BrevredigeringHandlerTestBase
import no.nav.pensjon.brev.skribenten.brevredigering.domain.MarkupVersjon
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.VedtaksId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import java.time.LocalDate

class HentEllerOpprettAttesteringPdfHandlerTest : BrevredigeringHandlerTestBase() {

    @BeforeEach
    fun stageV2() = stageMarkupV2()

    private suspend fun klartVedtaksbrev(versjon: MarkupVersjon): Dto.Brevredigering =
        opprettBrev(brevkode = Testbrevkoder.VEDTAKSBREV, vedtaksId = VedtaksId(1234), markupVersjon = versjon).resultOrFail()
            .also { veksleKlarStatus(it, klar = true).resultOrFail() }

    private fun renderPdfKall(versjon: MarkupVersjon): Int = when (versjon) {
        MarkupVersjon.V1 -> brevbakerService.renderPdfKall.size
        MarkupVersjon.V2 -> brevbakerService.renderPdfV2Kall.size
    }

    @ParameterizedTest
    @EnumSource(MarkupVersjon::class)
    suspend fun `oppretter document med fersk dokumentdato`(versjon: MarkupVersjon) {
        val brev = klartVedtaksbrev(versjon)
        val kallFoer = renderPdfKall(versjon)

        assertThat(hentEllerOpprettAttesteringPdf(brev)).isSuccess {
            assertThat(it.document.dokumentDato).isEqualTo(LocalDate.now())
            assertThat(it.document.pdf).isEqualTo(stagetPDF)
            assertThat(it.document.redigertBrevHash).isEqualTo(brev.redigertBrevHash)
            assertThat(it.rendretBrevErEndret).isFalse()
        }
        assertThat(renderPdfKall(versjon)).isEqualTo(kallFoer + 1)
    }

    @ParameterizedTest
    @EnumSource(MarkupVersjon::class)
    suspend fun `varsler aldri om at rendret brev er endret`(versjon: MarkupVersjon) {
        val brev = klartVedtaksbrev(versjon)

        stagePdf("min første pdf".encodeToByteArray())
        hentEllerOpprettAttesteringPdf(brev).resultOrFail()

        stagePdf("min andre pdf".encodeToByteArray())
        stagEndretMal()
        penService.pesysBrevdata = brevdataResponseData.copy(brevdata = Api.GeneriskBrevdata().also { it["a"] = "b" })

        assertThat(hentEllerOpprettAttesteringPdf(brev)).isSuccess {
            assertThat(it.document.pdf).isEqualTo("min andre pdf".encodeToByteArray())
            assertThat(it.rendretBrevErEndret).isFalse()
        }
    }

    @ParameterizedTest
    @EnumSource(MarkupVersjon::class)
    suspend fun `gjenbruker cachet document naar ingenting er endret`(versjon: MarkupVersjon) {
        val brev = klartVedtaksbrev(versjon)

        val foerste = hentEllerOpprettAttesteringPdf(brev).resultOrFail()
        val andre = hentEllerOpprettAttesteringPdf(brev).resultOrFail()

        assertThat(andre.document).isEqualTo(foerste.document)
    }
}
