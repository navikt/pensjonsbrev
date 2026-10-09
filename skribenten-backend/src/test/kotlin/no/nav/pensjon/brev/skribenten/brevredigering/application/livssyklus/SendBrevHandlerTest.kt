package no.nav.pensjon.brev.skribenten.brevredigering.application.livssyklus
import no.nav.pensjon.brev.skribenten.brevredigering.application.BrevredigeringHandlerTestBase

import no.nav.pensjon.brev.skribenten.Testbrevkoder
import kotlinx.coroutines.CancellationException
import no.nav.brev.BrevLandmodell.Landkode
import no.nav.pensjon.brev.skribenten.Features
import no.nav.pensjon.brev.skribenten.override
import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.brevredigering.domain.Adresselinje
import no.nav.pensjon.brev.skribenten.brevredigering.domain.Navn
import no.nav.pensjon.brev.skribenten.brevredigering.domain.Poststed
import no.nav.pensjon.brev.skribenten.brevredigering.domain.SendBrevPolicy
import no.nav.pensjon.brev.skribenten.brevredigering.domain.TssId
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.PenAdresseManglerException
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.PenServiceException
import no.nav.pensjon.brev.skribenten.isFailure
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.model.*
import no.nav.pensjon.brev.skribenten.routes.samhandler.dto.HentSamhandlerResponseDto
import no.nav.pensjon.brev.skribenten.services.SamhandlerService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.parallel.Isolated
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.ValueSource
import java.time.LocalDate

@Isolated
class SendBrevHandlerTest : BrevredigeringHandlerTestBase() {

    @BeforeEach
    fun nullstillSending() {
        // TODO: Fjern `@Isolated` når toggle fjernes.
        Features.override(Features.samhandlerOrgnummer, false)
        penService.utfoerteSendBrevKall.clear()
    }

    @AfterEach
    fun nullstillToggle() {
        Features.override(Features.samhandlerOrgnummer, false)
    }

    @ParameterizedTest
    @EnumSource(Distribusjon::class)
    suspend fun `sender orgnummer naar toggle er paa`(distribusjon: Distribusjon) {
        Features.override(Features.samhandlerOrgnummer, true)
        sendMedOppslag(distribusjon = distribusjon) { samhandlerService.hentSamhandler(it) }

        val (request, distribuer) = penService.utfoerteSendBrevKall.single()
        assertThat(request.mottaker).isEqualTo(
            Pen.SendRedigerbartBrevRequest.Mottaker(
                type = Pen.SendRedigerbartBrevRequest.Mottaker.Type.ORGNR,
                organisasjon = Pen.SendRedigerbartBrevRequest.Mottaker.Organisasjon(SAMHANDLER_ORGNR),
            )
        )
        assertThat(distribuer).isEqualTo(distribusjon == Distribusjon.SENTRALPRINT)
    }

    @Test
    suspend fun `toggle av bruker TSS_ID uten organisasjonsoppslag`() {
        sendMedOppslag { error("Skal ikke hente samhandler ved sending naar toggle er av") }
        assertTssMottaker()
    }

    @ParameterizedTest
    @ValueSource(strings = ["FNR", "UTOR", "ORGNR", "UNKNOWN"])
    suspend fun `andre idTyper bruker TSS_ID`(idType: String) {
        Features.override(Features.samhandlerOrgnummer, true)
        sendMedOppslag { samhandlerService.hentSamhandler(it).let { response ->
            response.copy(success = response.success!!.copy(idType = idType))
        } }
        assertTssMottaker()
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "   "])
    suspend fun `tomt orgnummer bruker TSS_ID`(offentligId: String) {
        Features.override(Features.samhandlerOrgnummer, true)
        sendMedOppslag { samhandlerService.hentSamhandler(it).let { response ->
            response.copy(success = response.success!!.copy(offentligId = offentligId))
        } }
        assertTssMottaker()
    }

    @ParameterizedTest
    @EnumSource(HentSamhandlerResponseDto.FailureType::class)
    suspend fun `feilrespons fra samhandler bruker TSS_ID`(failure: HentSamhandlerResponseDto.FailureType) {
        Features.override(Features.samhandlerOrgnummer, true)
        sendMedOppslag { HentSamhandlerResponseDto(null, failure) }
        assertTssMottaker()
    }

    @Test
    suspend fun `kansellert oppslag sender ikke til PEN`() {
        Features.override(Features.samhandlerOrgnummer, true)
        assertThrows<CancellationException> {
            sendMedOppslag { throw CancellationException("Kansellert") }
        }
        assertThat(penService.utfoerteSendBrevKall).isEmpty()
    }

    @Test
    suspend fun `standardmottaker er uendret naar toggle er paa`() {
        Features.override(Features.samhandlerOrgnummer, true)
        sendMedOppslag(mottaker = null) { error("Standardmottaker skal ikke slaas opp") }
        assertThat(penService.utfoerteSendBrevKall.single().first.mottaker).isNull()
    }

    @ParameterizedTest
    @ValueSource(booleans = [false, true])
    suspend fun `manuelle adresser er uendret naar toggle er paa`(utenlandsk: Boolean) {
        Features.override(Features.samhandlerOrgnummer, true)
        val mottaker = if (utenlandsk) {
            Dto.Mottaker.utenlandskAdresse(
                navn = Navn("Mottaker"),
                adresselinje1 = Adresselinje("Foreign street 1"),
                adresselinje2 = Adresselinje("Postal town"),
                adresselinje3 = null,
                landkode = Landkode("SE"),
                manueltAdressertTil = Dto.Mottaker.ManueltAdressertTil.ANNEN,
            )
        } else {
            Dto.Mottaker.norskAdresse(
                navn = Navn("Mottaker"),
                postnummer = NorskPostnummer("0001"),
                poststed = Poststed("Oslo"),
                adresselinje1 = Adresselinje("Postboks 1"),
                adresselinje2 = null,
                adresselinje3 = null,
                manueltAdressertTil = Dto.Mottaker.ManueltAdressertTil.ANNEN,
            )
        }
        sendMedOppslag(mottaker = mottaker) { error("Manuell adresse skal ikke slaas opp") }
        val forventet = if (utenlandsk) {
            Pen.SendRedigerbartBrevRequest.Mottaker(
                type = Pen.SendRedigerbartBrevRequest.Mottaker.Type.UTENLANDSK_ADRESSE,
                utenlandskAdresse = Pen.SendRedigerbartBrevRequest.Mottaker.UtenlandsAdresse(
                    navn = Navn("Mottaker"),
                    landkode = Landkode("SE"),
                    adresselinje1 = Adresselinje("Foreign street 1"),
                    adresselinje2 = Adresselinje("Postal town"),
                    adresselinje3 = null,
                ),
            )
        } else {
            Pen.SendRedigerbartBrevRequest.Mottaker(
                type = Pen.SendRedigerbartBrevRequest.Mottaker.Type.NORSK_ADRESSE,
                norskAdresse = Pen.SendRedigerbartBrevRequest.Mottaker.NorskAdresse(
                    navn = Navn("Mottaker"),
                    postnummer = NorskPostnummer("0001"),
                    poststed = Poststed("Oslo"),
                    adresselinje1 = Adresselinje("Postboks 1"),
                    adresselinje2 = null,
                    adresselinje3 = null,
                ),
            )
        }
        assertThat(penService.utfoerteSendBrevKall.single().first.mottaker).isEqualTo(forventet)
    }

    @Test
    suspend fun `PEN-feil ved organisasjon gir ikke nytt sendeforsoek med TSS_ID`() {
        Features.override(Features.samhandlerOrgnummer, true)
        penService.sendBrevException = PenServiceException("PEN er utilgjengelig")

        assertThrows<PenServiceException> {
            sendMedOppslag { samhandlerService.hentSamhandler(it) }
        }
        assertThat(penService.utfoerteSendBrevKall).hasSize(1)
        assertThat(penService.utfoerteSendBrevKall.single().first.mottaker?.type)
            .isEqualTo(Pen.SendRedigerbartBrevRequest.Mottaker.Type.ORGNR)
    }

    private fun assertTssMottaker() {
        assertThat(penService.utfoerteSendBrevKall.single().first.mottaker).isEqualTo(
            Pen.SendRedigerbartBrevRequest.Mottaker(
                type = Pen.SendRedigerbartBrevRequest.Mottaker.Type.TSS_ID,
                tssId = SAMHANDLER_TSS_ID,
            )
        )
    }

    private suspend fun sendMedOppslag(
        mottaker: Dto.Mottaker? = Dto.Mottaker.samhandler(SAMHANDLER_TSS_ID),
        distribusjon: Distribusjon = Distribusjon.SENTRALPRINT,
        oppslag: suspend (TssId) -> HentSamhandlerResponseDto,
    ) {
        val brev = opprettBrev(mottaker = mottaker).resultOrFail()
        if (distribusjon != Distribusjon.SENTRALPRINT) {
            assertThat(endreDistribusjonstype(brev.info.id, distribusjon)).isSuccess()
        }
        assertThat(hentEllerOpprettPdf(brev)).isSuccess()
        assertThat(veksleKlarStatus(brev, true)).isSuccess()
        val service = object : SamhandlerService by samhandlerService {
            override suspend fun hentSamhandler(idTSSEkstern: TssId) = oppslag(idTSSEkstern)
        }
        val handler = SendBrevHandler(brevtilgang, brevService, brevmalService, sendtBrevMetrikker, service)
        assertThat(withPrincipal(saksbehandler1Principal) {
            handler(SendBrevHandler.Request(brev.info.id, sak1.saksId))
        }).isSuccess()
    }

    @Test
    suspend fun `kan ikke distribuere vedtaksbrev som ikke er attestert`() {
        val brev = opprettBrev(
            saksbehandlerValg = SaksbehandlervalgMap().apply { put("valg1", SaksbehandlervalgVerdi.Boolean(true)) },
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
            saksbehandlerValg = SaksbehandlervalgMap().apply { put("valg1", SaksbehandlervalgVerdi.Boolean(true)) },
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
                enhetsId = PRINCIPAL_NAVENHET_ID,
                pdf = stagetPDF,
                eksternReferanseId = "skribenten:${brev.info.id.id}",
                mottaker = null,
            ), true
        )
    }

    @Test
    suspend fun `distribuerer ikke lokalprint brev`() {
        val brev = opprettBrev().resultOrFail()

        assertThat(endreDistribusjonstype(brev.info.id, Distribusjon.LOKALPRINT)).isSuccess()
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
                enhetsId = PRINCIPAL_NAVENHET_ID,
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
        val mottaker = Dto.Mottaker.samhandler(TssId("987"))
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
                enhetsId = PRINCIPAL_NAVENHET_ID,
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

    @Test
    suspend fun `journalpostId fra feilende PEN-kall lagres paa brevet`() {
        val brev = klartBrev()
        penService.sendBrevException = PenAdresseManglerException(JournalpostId(456))

        assertThrows<PenAdresseManglerException> { sendBrev(brev) }

        assertThat(hentBrev(brev.info.id)).isSuccess {
            assertThat(it.info.journalpostId).isEqualTo(JournalpostId(456))
            assertThat(it.info.status).isEqualTo(Dto.BrevStatus.ARKIVERT)
        }
    }

    @Test
    suspend fun `brev er uendret naar PEN feiler uten journalpostId`() {
        val brev = klartBrev()
        penService.sendBrevException = PenServiceException("Noe gikk galt")

        assertThrows<PenServiceException> { sendBrev(brev) }

        assertThat(hentBrev(brev.info.id)).isSuccess {
            assertThat(it.info.journalpostId).isNull()
        }
    }

    @Test
    suspend fun `brev som feilet sending kan slettes naar mottaker har doedsdato`() {
        val brev = klartBrev()
        penService.sendBrevException = PenAdresseManglerException(JournalpostId(456))

        assertThrows<PenAdresseManglerException> { sendBrev(brev) }

        pdlService.brukerContext = Pdl.PersonContext(adressebeskyttelse = false, doedsdato = LocalDate.now())
        assertThat(slettBrev(brev)).isSuccess()
        assertThat(hentBrev(brev.info.id)).isNull()
    }

    private suspend fun klartBrev(): Dto.Brevredigering {
        val brev = opprettBrev().resultOrFail()
        assertThat(hentEllerOpprettPdf(brev)).isSuccess()
        assertThat(veksleKlarStatus(brev, true)).isSuccess()
        return brev
    }
}