package no.nav.pensjon.brev.skribenten.services

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.skribenten.MockPrincipal
import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.services.BrevdataDto.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

private const val EXPECTED_EXSTREAM_URL = "http://beste-exstream-brev"
private const val EXPECTED_DOKSYS_URL = "http://beste-doksys-brev"

private const val forventaJournalpostId = "1234"
private const val forventaDokumentId = "5678"

class LegacyBrevServiceTest {

    private val principalIdent = NavIdent("kulIdent1234")
    private val principal = MockPrincipal(principalIdent, "Kul saksbehandler")
    private val principalSinNAVEnhet = NAVAnsattEnhet("1111", "Nav Ozzzlo")


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
        brevsystem = BrevSystem.GAMMEL,
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
        brevsystem = BrevSystem.DOKSYS,
        brevgruppe = null,
    )

    private val brevmetadataService = FakeBrevmetadataService(
        maler = mapOf("exstream" to exstreamBrevMetadata, "doksys" to doksysBrevmetadata),
    )

    private val safService = object : SafServiceStub() {
        override suspend fun waitForJournalpostStatusUnderArbeid(journalpostId: String) = JournalpostLoadingResult.READY
        override suspend fun getFirstDocumentInJournal(journalpostId: String): ServiceResult<SafService.HentDokumenterResponse> {
            return ServiceResult.Ok(
                SafService.HentDokumenterResponse(
                    SafService.HentDokumenterResponse.Journalposter(
                        SafService.HentDokumenterResponse.Journalpost(
                            journalpostId, listOf(
                                SafService.HentDokumenterResponse.Dokument(forventaDokumentId)
                            )
                        )
                    ), null
                )
            )
        }
    }

    private val penService = object : PenServiceStub() {
        override suspend fun bestillDoksysBrev(
            request: Api.BestillDoksysBrevRequest,
            enhetsId: String,
            saksId: Long,
        ): ServiceResult<Pen.BestillDoksysBrevResponse> = ServiceResult.Ok(Pen.BestillDoksysBrevResponse(forventaJournalpostId))

        override suspend fun bestillExstreamBrev(bestillExstreamBrevRequest: Pen.BestillExstreamBrevRequest) =
            ServiceResult.Ok(Pen.BestillExstreamBrevResponse(forventaJournalpostId))

        override suspend fun redigerDoksysBrev(journalpostId: String, dokumentId: String) =
            if(journalpostId == forventaJournalpostId && dokumentId == forventaDokumentId) {
                ServiceResult.Ok(Pen.RedigerDokumentResponse(EXPECTED_DOKSYS_URL))
            } else {
                notYetStubbed("Mangler stub for redigerDoksysBrev med journalpostId: $journalpostId og dokumentId: $dokumentId")
            }

        override suspend fun redigerExstreamBrev(journalpostId: String) =
            if (journalpostId == forventaJournalpostId) {
                ServiceResult.Ok(Pen.RedigerDokumentResponse(EXPECTED_EXSTREAM_URL))
            } else {
                notYetStubbed("Mangler stub for redigerExstreamBrev med journalpostId: $journalpostId")
            }
    }

    private val navansattService = FakeNavansattService(
        harTilgangTilEnhet = mapOf(
            Pair(principalIdent.id, principalSinNAVEnhet.id) to true
        ),
        navansatte = mapOf(principalIdent.id to "verdens beste saksbehandler")
    )


    private val legacyBrevService = LegacyBrevService(
        brevmetadataService = brevmetadataService,
        safService = safService,
        penService = penService,
        navansattService = navansattService,
    )

    @Test
    fun `bestill exstream brev feiler ved manglende tilgang`() {
        runBlocking {
            val bestillBrevResult = withPrincipal(principal) {
                legacyBrevService.bestillOgRedigerExstreamBrev(
                    gjelderPid = "9999",
                    request = Api.BestillExstreamBrevRequest(
                        brevkode = "exstream",
                        spraak = SpraakKode.NB,
                        vedtaksId = null,
                        idTSSEkstern = null,
                        brevtittel = null,
                        enhetsId = "9999"
                    ),
                    saksId = 3333L
                )
            }
            assertThat(bestillBrevResult.failureType).isEqualTo(Api.BestillOgRedigerBrevResponse.FailureType.ENHET_UNAUTHORIZED)
        }
    }

    @Test
    fun `kan bestille exstream brev med riktig tilgang`() {
        runBlocking {
            val bestillBrevResult = withPrincipal(principal) {
                legacyBrevService.bestillOgRedigerExstreamBrev(
                    gjelderPid = "9999", request = Api.BestillExstreamBrevRequest(
                        brevkode = "exstream",
                        spraak = SpraakKode.NB,
                        vedtaksId = null,
                        idTSSEkstern = null,
                        brevtittel = null,
                        enhetsId = principalSinNAVEnhet.id
                    ),
                    saksId = 3333L
                )
            }
            assertThat(bestillBrevResult.failureType).isNull()
            assertThat(bestillBrevResult.url).isEqualTo(EXPECTED_EXSTREAM_URL)
        }
    }


    @Test
    fun `kan bestille exstream eblankett med riktig tilgang`() {
        runBlocking {
            val bestillBrevResult = withPrincipal(principal) {
                legacyBrevService.bestillOgRedigerEblankett(
                    gjelderPid = "9999", request = Api.BestillEblankettRequest(
                        brevkode = "exstream",
                        enhetsId = principalSinNAVEnhet.id,
                        mottakerText = "en tekst",
                        landkode = "NO",
                    ),
                    saksId = 3333L
                )
            }
            assertThat(bestillBrevResult.failureType).isNull()
            assertThat(bestillBrevResult.url).isEqualTo(EXPECTED_EXSTREAM_URL)
        }
    }

    @Test
    fun `bestill exstream eblankett feiler med manglende tilgang`() {
        runBlocking {
            val bestillBrevResult = withPrincipal(principal) {
                legacyBrevService.bestillOgRedigerEblankett(
                    gjelderPid = "9999", request = Api.BestillEblankettRequest(
                        brevkode = "exstream",
                        enhetsId = "9999",
                        mottakerText = "en tekst",
                        landkode = "NO",
                    ),
                    saksId = 3333L
                )
            }
            assertThat(bestillBrevResult.failureType).isEqualTo(Api.BestillOgRedigerBrevResponse.FailureType.ENHET_UNAUTHORIZED)
        }
    }

    @Test
    fun `kan bestille doksys brev med riktig tilgang`() {
        runBlocking {
            val bestillBrevResult = withPrincipal(principal) {
                legacyBrevService.bestillOgRedigerDoksysBrev(
                    Api.BestillDoksysBrevRequest(
                        brevkode = "doksys",
                        spraak = SpraakKode.NB,
                        vedtaksId = null,
                        enhetsId = principalSinNAVEnhet.id
                    ),
                    3333L
                )
            }
            assertThat(bestillBrevResult.failureType).isNull()
            assertThat(bestillBrevResult.url).isEqualTo(EXPECTED_DOKSYS_URL)
        }
    }

    @Test
    fun `bestill doksys brev feiler med manglende tilgang`() {
        runBlocking {
            val bestillBrevResult = withPrincipal(principal) {
                legacyBrevService.bestillOgRedigerDoksysBrev(
                    Api.BestillDoksysBrevRequest(
                        brevkode = "doksys",
                        spraak = SpraakKode.NB,
                        vedtaksId = null,
                        enhetsId = "9999"
                    ),
                    3333L
                )
            }
            assertThat(bestillBrevResult.failureType).isEqualTo(Api.BestillOgRedigerBrevResponse.FailureType.ENHET_UNAUTHORIZED)
        }
    }
}