package no.nav.pensjon.brev.skribenten.usecase

import no.nav.pensjon.brev.skribenten.Testbrevkoder
import no.nav.pensjon.brev.skribenten.domain.SendBrevPolicy
import no.nav.pensjon.brev.skribenten.isFailure
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.Distribusjonstype
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.model.VedtaksId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

class SendBrevHandlerTest : BrevredigeringTest() {

    @Test
    suspend fun `kan ikke distribuere vedtaksbrev som ikke er attestert`() {
        val brev = opprettBrev(
            saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg", true) },
            brevkode = Testbrevkoder.VEDTAKSBREV,
            vedtaksId = VedtaksId(1),
        ).resultOrFail()

        assertThat(hentEllerOpprettPdf(brev)).isSuccess()
        assertThat(veksleKlarStatus(brev, true)).isSuccess()
        
        assertThat(sendBrev(brev)).isFailure<SendBrevPolicy.KanIkkeSende.VedtaksbrevIkkeAttestert, _, _>()
    }

    @Test
    suspend fun `kan distribuere vedtaksbrev som er attestert`() {
        brevbakerService.renderPdfKall.clear()

        val brev = opprettBrev(
            saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg", true) },
            brevkode = Testbrevkoder.VEDTAKSBREV,
            vedtaksId = VedtaksId(1),
        ).resultOrFail()

        assertThat(veksleKlarStatus(brev, true)).isSuccess()
        assertThat(attester(brev)).isSuccess()
        assertThat(hentEllerOpprettPdf(brev, principal = attestant1Principal)).isSuccess()

        assertThat(sendBrev(brev, principal = attestant1Principal)).isSuccess {
            assertThat(it.journalpostId?.id).isEqualTo(bestillBrevresponse.journalpostId?.id)
        }
    }

    @Test
    suspend fun `distribuerer sentralprint brev`() {
        val brev = opprettBrev().resultOrFail()

        assertThat(veksleKlarStatus(brev, true)).isSuccess()
        assertThat(hentEllerOpprettPdf(brev)).isSuccess()
        assertThat(sendBrev(brev)).isSuccess()

        penService.verifyHentPesysBrevdata(sak1.saksId, null, Testbrevkoder.INFORMASJONSBREV, PRINCIPAL_NAVENHET_ID)
        penService.verifySendBrev(
            Pen.SendRedigerbartBrevRequest(
                templateDescription = informasjonsbrev,
                dokumentDato = LocalDate.now(),
                saksId = sak1.saksId,
                brevkode = Testbrevkoder.INFORMASJONSBREV,
                enhetId = PRINCIPAL_NAVENHET_ID,
                pdf = stagetPDF,
                eksternReferanseId = "skribenten:${brev.info.id.id}",
                mottaker = null,
            ), true
        )
    }

    @Test
    suspend fun `distribuerer ikke lokalprint brev`() {
        val brev = opprettBrev().resultOrFail()

        assertThat(endreDistribusjonstype(brev.info.id, Distribusjonstype.LOKALPRINT)).isSuccess()
        assertThat(veksleKlarStatus(brev, true)).isSuccess()
        assertThat(hentEllerOpprettPdf(brev)).isSuccess()
        assertThat(sendBrev(brev)).isSuccess()

        penService.verifyHentPesysBrevdata(sak1.saksId, null, Testbrevkoder.INFORMASJONSBREV, PRINCIPAL_NAVENHET_ID)
        penService.verifySendBrev(
            Pen.SendRedigerbartBrevRequest(
                templateDescription = informasjonsbrev,
                dokumentDato = LocalDate.now(),
                saksId = sak1.saksId,
                brevkode = Testbrevkoder.INFORMASJONSBREV,
                enhetId = PRINCIPAL_NAVENHET_ID,
                pdf = stagetPDF,
                eksternReferanseId = "skribenten:${brev.info.id.id}",
                mottaker = null,
            ), false
        )
    }

    @Test
    suspend fun `kan ikke sende brev som ikke er markert klar til sending`() {
        val brev = opprettBrev().resultOrFail()
        assertThat(hentEllerOpprettPdf(brev)).isSuccess()

        assertThat(sendBrev(brev)).isFailure<SendBrevPolicy.KanIkkeSende.IkkeLaastForRedigering, _, _>()
    }

    @Test
    suspend fun `kan ikke sende brev hvor pdf har annen hash enn siste brevredigering`() {
        val brev = opprettBrev().resultOrFail()
        assertThat(hentEllerOpprettPdf(brev)).isSuccess()
        assertThat(oppdaterBrev(brevId = brev.info.id, nyttRedigertbrev = brev.redigertBrev.withSignaturSaksbehandler("en ny signatur"))).isSuccess()
        assertThat(veksleKlarStatus(brev, true)).isSuccess()

        assertThat(sendBrev(brev)).isFailure<SendBrevPolicy.KanIkkeSende.DocumentIkkeForGjeldendeRedigertBrev, _, _>()
    }

    @Test
    suspend fun `arkivert brev men ikke distribuert kan sendes`() {
        val brev = opprettBrev().resultOrFail()
        assertThat(arkiverBrev(brev)).isSuccess()
        assertThat(hentBrev(brev.info.id)).isNotNull()

        assertThat(sendBrev(brev)).isSuccess()
        assertThat(hentBrev(brev.info.id)).isNull()
    }

    @Test
    suspend fun `brev distribueres til annen mottaker`() {
        val mottaker = Dto.Mottaker.samhandler("987")
        val brev = opprettBrev(mottaker = mottaker).resultOrFail()
        assertThat(hentEllerOpprettPdf(brev)).isSuccess()
        assertThat(veksleKlarStatus(brev, true)).isSuccess()
        assertThat(sendBrev(brev)).isSuccess()

        penService.verifySendBrev(
            Pen.SendRedigerbartBrevRequest(
                templateDescription = informasjonsbrev,
                dokumentDato = LocalDate.now(),
                saksId = sak1.saksId,
                brevkode = Testbrevkoder.INFORMASJONSBREV,
                enhetId = PRINCIPAL_NAVENHET_ID,
                pdf = stagetPDF,
                eksternReferanseId = "skribenten:${brev.info.id.id}",
                mottaker = Pen.SendRedigerbartBrevRequest.Mottaker(
                    Pen.SendRedigerbartBrevRequest.Mottaker.Type.TSS_ID,
                    mottaker.tssId,
                    null,
                    null
                )
            ), true
        )
    }

    @Test
    suspend fun `status er ARKIVERT om brev har journalpost`() {
        val brev = opprettBrev().resultOrFail()
        assertThat(arkiverBrev(brev)).isSuccess()

        assertThat(hentBrev(brev.info.id)).isSuccess {
            assertThat(it.info.status).isEqualTo(Dto.BrevStatus.ARKIVERT)
        }
    }
}