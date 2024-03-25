package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.databind.DeserializationFeature
import com.typesafe.config.Config
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.slf4j.LoggerFactory

class BrevmetadataService(
    config: Config,
    clientEngine: HttpClientEngine = CIO.create(),
) : ServiceStatus {
    private val brevmetadataUrl = config.getString("url")
    private val logger = LoggerFactory.getLogger(BrevmetadataService::class.java)
    private val httpClient = HttpClient(clientEngine) {
        defaultRequest {
            url(brevmetadataUrl)
        }
        install(ContentNegotiation) {
            jackson {
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            }
        }
    }


    suspend fun hentMaler(sakType: PenService.SakType, includeEblanketter: Boolean): List<BrevdataDto> =
        coroutineScope {
            val eblanketterAsync: Deferred<List<BrevdataDto>> = async { if (includeEblanketter) getEblanketter() else emptyList() }
            val malerAsync: Deferred<List<BrevdataDto>> = async { getBrevmalerForSakstype(sakType) }
            return@coroutineScope eblanketterAsync.await().plus(malerAsync.await())
        }

    private suspend fun getBrevmalerForSakstype(sakstype: PenService.SakType): List<BrevdataDto> {
        val httpResponse = httpClient.get("/api/brevdata/brevdataForSaktype/$sakstype?includeXsd=false") {
            contentType(ContentType.Application.Json)
        }
        if (httpResponse.status.isSuccess()) {
            return httpResponse.body<List<BrevdataDto>>()
        } else {
            logger.error("Feil ved henting av brevmetadata. Status: ${httpResponse.status} Message: ${httpResponse.bodyAsText()}")
            return emptyList()
        }
    }

    private suspend fun getEblanketter(): List<BrevdataDto> {
        return httpClient.get("/api/brevdata/allBrev?includeXsd=false") {
            contentType(ContentType.Application.Json)
        }.body<List<BrevdataDto>>()
            .filter { it.dokumentkategori == BrevdataDto.DokumentkategoriCode.E_BLANKETT }
    }

    suspend fun getMal(brevkode: String): BrevdataDto {
        return httpClient.get("/api/brevdata/brevForBrevkode/${brevkode}") {
            contentType(ContentType.Application.Json)
        }.body<BrevdataDto>()
    }

    override val name = "Brevmetadata"
    override suspend fun ping(call: ApplicationCall): ServiceResult<Boolean> =
        httpClient.get("/api/internal/isReady").toServiceResult<String>().map { true }
}

data class BrevdataDto(
    val redigerbart: Boolean,
    val dekode: String,
    val brevkategori: BrevkategoriCode?,
    val dokType: DokumentType,
    val sprak: List<SpraakKode>?,
    val visIPselv: Boolean?,
    val utland: String?,
    val brevregeltype: BrevregeltypeCode?,
    val brevkravtype: String?,
    val brevkontekst: BrevkontekstCode?,
    val dokumentkategori: DokumentkategoriCode,
    val synligForVeileder: Boolean?,
    val prioritet: Int?,
    val brevkodeIBrevsystem: String,
    val brevsystem: BrevSystem,
    val brevgruppe: String?,
) {
    @Suppress("unused")
    enum class DokumentkategoriCode { B, E_BLANKETT, IB, SED, VB }

    @Suppress("unused")
    enum class BrevkategoriCode { BREV_MED_SKJEMA, INFORMASJON, INNHENTE_OPPL, NOTAT, OVRIG, VARSEL, VEDTAK }
    enum class BrevSystem { DOKSYS, GAMMEL /*EXSTREAM*/, }
    enum class BrevkontekstCode { ALLTID, SAK, VEDTAK }

    @Suppress("unused")
    enum class DokumentType {
        I, //Inngende dokument
        N, //Notat
        U, //Utgende dokument
    }

    fun isRedigerbarBrevtittel(): Boolean =
        brevkodeIBrevsystem == Brevkoder.FRITEKSTBREV_KODE
                || (dokType == DokumentType.N && brevkodeIBrevsystem !in Brevkoder.ikkeRedigerbarBrevtittel)

    enum class BrevregeltypeCode {
        GG,     //Gammelt regelverk
        GN,     //Nytt regelverk med gammel opptjening
        NN,     //Nytt regelverk
        ON,     //Overgangsordning med ny og gammel opptjening
        OVRIGE  //vrige brev, ikke knyttet til gammelt eller nytt regelverk.
    }

}


