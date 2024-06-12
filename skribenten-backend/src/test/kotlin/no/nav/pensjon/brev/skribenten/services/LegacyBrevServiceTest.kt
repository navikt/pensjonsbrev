package no.nav.pensjon.brev.skribenten.services

import io.ktor.server.application.*
import io.ktor.server.plugins.callid.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.principal
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.BestillExstreamBrevResponseDto
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.RedigerDoksysDokumentResponseDto
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.RedigerExstreamDokumentResponseDto
import no.nav.pensjon.brev.skribenten.services.BrevdataDto.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

private const val EXPECTED_EXSTREAM_URL = "http://beste-exstream-brev"
private const val EXPECTED_DOKSYS_URL = "http://beste-doksys-brev"

class LegacyBrevServiceTest {

    private val principal = mockk<UserPrincipal> {
        every {
            navIdent
        } returns "kulIdent1234"
    }
    private val mockCall = mockk<ApplicationCall> {
        every { callId } returns "utrolig kul callId"
        every { principal() } returns principal
    }


    private val tjenestebussIntegrasjonService = mockk<TjenestebussIntegrasjonService> {
        coEvery {
            bestillExstreamBrev(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any())
        } returns ServiceResult.Ok(BestillExstreamBrevResponseDto("1234", null))

        coEvery {
            redigerDoksysBrev(any(), any(), any())
        } returns ServiceResult.Ok(RedigerDoksysDokumentResponseDto(EXPECTED_DOKSYS_URL, null))

        coEvery {
            redigerExstreamBrev(any(), any())
        } returns ServiceResult.Ok(RedigerExstreamDokumentResponseDto(EXPECTED_EXSTREAM_URL, null))
    }
    private val exstreamBrevMetadata = BrevdataDto(
        redigerbart = true,
        dekode = "exstream",
        brevkategori = BrevkategoriCode.INFORMASJON,
        dokType = DokumentType.U,
        sprak = listOf(SpraakKode.NB),
        visIPselv = true,
        utland = "utland",
        brevregeltype = BrevregeltypeCode.OVRIGE,
        brevkravtype = "brevkravtype",
        brevkontekst = BrevkontekstCode.ALLTID,
        dokumentkategori = DokumentkategoriCode.IB,
        synligForVeileder = true,
        prioritet = 1234,
        brevkodeIBrevsystem = "exstream",
        brevsystem = BrevdataDto.BrevSystem.GAMMEL,
        brevgruppe = "brevgruppe",
    )

    private val doksysBrevmetadata = BrevdataDto(
        redigerbart = true,
        dekode = "doksys",
        brevkategori = BrevkategoriCode.INFORMASJON,
        dokType = DokumentType.U,
        sprak = listOf(SpraakKode.NB),
        visIPselv = true,
        utland = "utland",
        brevregeltype = BrevregeltypeCode.OVRIGE,
        brevkravtype = "brevkravtype",
        brevkontekst = BrevkontekstCode.ALLTID,
        dokumentkategori = DokumentkategoriCode.IB,
        synligForVeileder = true,
        prioritet = 1234,
        brevkodeIBrevsystem = "doksys",
        brevsystem = BrevdataDto.BrevSystem.DOKSYS,
        brevgruppe = null,
    )

    private val brevmetadataService = mockk<BrevmetadataService> {
        coEvery {
            getMal("exstream")
        } returns exstreamBrevMetadata

        coEvery {
            getMal("doksys")
        } returns doksysBrevmetadata
    }
    private val safService = mockk<SafService> {
        coEvery {
            waitForJournalpostStatusUnderArbeid(any(), any())
        } returns JournalpostLoadingResult.READY

        coEvery {
            getFirstDocumentInJournal(any(), any())
        } returns ServiceResult.Ok(
            SafService.HentDokumenterResponse(
                SafService.HentDokumenterResponse.Journalposter(
                    SafService.HentDokumenterResponse.Journalpost(
                        "1234", listOf(
                            SafService.HentDokumenterResponse.Dokument("1234")
                        )
                    )
                ), null
            )
        )
    }
    private val penService = mockk<PenService> {
        coEvery {
            bestillDoksysBrev(any(), any(), any(), any())
        } returns ServiceResult.Ok(Pen.BestillDoksysBrevResponse(EXPECTED_DOKSYS_URL, null))
    }
    private val navansattService = mockk<NavansattService> {
        coEvery {
            hentNavansatt(any(), any())
        } returns ServiceResult.Ok(Navansatt(emptyList(), "verdens", "beste", "saksbehandler"))
    }

    val legacyBrevService = LegacyBrevService(
        tjenestebussIntegrasjonService = tjenestebussIntegrasjonService,
        brevmetadataService = brevmetadataService,
        safService = safService,
        penService = penService,
        navansattService = navansattService,
    )

    @Test
    fun `feiler ved manglende tilgang`() {
        runBlocking {
            val bestillBrevResult = legacyBrevService.bestillOgRedigerExstreamBrev(
                call = mockCall, gjelderPid = "9999",
                request = Api.BestillExstreamBrevRequest(
                    brevkode = "exstream",
                    spraak = SpraakKode.NB,
                    isSensitive = false,
                    vedtaksId = null,
                    idTSSEkstern = null,
                    brevtittel = null,
                    enhetsId = "9999"
                ), saksId = 3333L,
                enhetsTilganger = listOf(NAVEnhet("1111", "NAV Ozzzlo"))
            )
            assertThat(bestillBrevResult.failureType).isEqualTo(Api.BestillOgRedigerBrevResponse.FailureType.ENHET_UNAUTHORIZED)
        }
    }

    @Test
    fun `kan bestille exstream brev med riktig tilgang`() {
        runBlocking {
            val bestillBrevResult = legacyBrevService.bestillOgRedigerExstreamBrev(
                call = mockCall, gjelderPid = "9999",
                request = Api.BestillExstreamBrevRequest(
                    brevkode = "exstream",
                    spraak = SpraakKode.NB,
                    isSensitive = false,
                    vedtaksId = null,
                    idTSSEkstern = null,
                    brevtittel = null,
                    enhetsId = "1111"
                ), saksId = 3333L, enhetsTilganger = listOf(NAVEnhet("1111", "NAV Ozzzlo"))
            )
            assertThat(bestillBrevResult.failureType).isNull()
            assertThat(bestillBrevResult.url).isEqualTo(EXPECTED_EXSTREAM_URL)
        }
    }

    @Test
    fun `kan bestille doksys brev med riktig tilgang`() {
        runBlocking {
            val bestillBrevResult = legacyBrevService.bestillOgRedigerDoksysBrev(
                mockCall,
                Api.BestillDoksysBrevRequest(
                    brevkode = "doksys",
                    spraak = SpraakKode.NB,
                    vedtaksId = null,
                    enhetsId = "1111"
                ), 3333L, enhetsTilganger = listOf(NAVEnhet("1111", "NAV Ozzzlo"))
            )
            assertThat(bestillBrevResult.failureType).isNull()
            assertThat(bestillBrevResult.url).isEqualTo(EXPECTED_DOKSYS_URL)
        }
    }
}