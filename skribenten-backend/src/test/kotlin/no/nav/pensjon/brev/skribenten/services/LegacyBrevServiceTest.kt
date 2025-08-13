package no.nav.pensjon.brev.skribenten.services

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.skribenten.MockPrincipal
import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brev.skribenten.services.BrevdataDto.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

private const val EXPECTED_EXSTREAM_URL = "http://beste-exstream-brev"
private const val EXPECTED_DOKSYS_URL = "http://beste-doksys-brev"

private const val journalpostId = "1234"
private const val dokumentId = "5678"

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

    private val brevmetadataService = FakeBrevmetadataService(
        maler = mapOf("exstream" to exstreamBrevMetadata, "doksys" to doksysBrevmetadata),
    )

    private val safService = FakeSafService(Pair(journalpostId, listOf(dokumentId)))

    private val penService = FakePenService(
        journalpostId = journalpostId,
        redigerDoksys = mapOf(Pair(journalpostId, dokumentId) to EXPECTED_DOKSYS_URL),
        redigerExstream = mapOf(journalpostId to EXPECTED_EXSTREAM_URL),
    )

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
                        isSensitive = false,
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
                        isSensitive = false,
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
                        isSensitive = false,
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
                        isSensitive = false,
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