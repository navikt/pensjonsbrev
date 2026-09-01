package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.Testbrevkoder
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.VedtaksId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

class HentEllerOpprettAttesteringPdfHandlerTest : BrevredigeringHandlerTestBase() {

    private suspend fun klartVedtaksbrev(): Dto.Brevredigering =
        opprettBrev(brevkode = Testbrevkoder.VEDTAKSBREV, vedtaksId = VedtaksId(1234)).resultOrFail()
            .also { veksleKlarStatus(it, klar = true).resultOrFail() }

    @Test
    suspend fun `oppretter document med fersk dokumentdato`() {
        val brev = klartVedtaksbrev()

        assertThat(hentEllerOpprettAttesteringPdf(brev)).isSuccess {
            assertThat(it.document.dokumentDato).isEqualTo(LocalDate.now())
            assertThat(it.document.pdf).isEqualTo(stagetPDF)
            assertThat(it.document.redigertBrevHash).isEqualTo(brev.redigertBrevHash)
            assertThat(it.rendretBrevErEndret).isFalse()
        }
    }

    @Test
    suspend fun `varsler aldri om at rendret brev er endret`() {
        val brev = klartVedtaksbrev()

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

    @Test
    suspend fun `gjenbruker cachet document naar ingenting er endret`() {
        val brev = klartVedtaksbrev()

        val foerste = hentEllerOpprettAttesteringPdf(brev).resultOrFail()
        val andre = hentEllerOpprettAttesteringPdf(brev).resultOrFail()

        assertThat(andre.document).isEqualTo(foerste.document)
    }
}
