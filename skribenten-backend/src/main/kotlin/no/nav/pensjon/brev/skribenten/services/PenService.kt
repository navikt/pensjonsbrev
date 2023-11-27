package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.typesafe.config.Config
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import no.nav.pensjon.brev.skribenten.OrderLetterRequest
import no.nav.pensjon.brev.skribenten.auth.AzureADOnBehalfOfAuthorizedHttpClient
import no.nav.pensjon.brev.skribenten.auth.AzureADService
import java.time.LocalDate

class PenService(config: Config, authService: AzureADService) {
    private val penUrl = config.getString("url")
    private val penScope = config.getString("scope")

    private val client = AzureADOnBehalfOfAuthorizedHttpClient(penScope, authService) {
        defaultRequest {
            url(penUrl)
        }
        install(ContentNegotiation) {
            jackson {
                registerModule(JavaTimeModule())
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            }
        }
    }

    data class PenError(val feilmelding: String)

    data class PenPersonDto(
        val fodselsdato: LocalDate,
        val fnr: String,
    )
    data class Sak(
        val sakId: Long,
        val foedselsnr: String,
        val foedselsdato: LocalDate,
        val sakType: SakType,
    )

    enum class SakType { AFP, AFP_PRIVAT, ALDER, BARNEP, FAM_PL, GAM_YRK, GENRL, GJENLEV, GRBL, KRIGSP, OMSORG, UFOREP, }
    data class SakSelection(
        val sakId: Long,
        val foedselsnr: String,
        val foedselsdato: LocalDate,
        val sakType: SakType,
    )


    private suspend fun fetchSak(call: ApplicationCall, sakId: String): ServiceResult<Sak, PenError> =
        client.get(call, "brev/skribenten/sak/$sakId").toServiceResult<Sak, PenError>()

    suspend fun hentSak(call: ApplicationCall, sakId: String): ServiceResult<SakSelection, PenError> =
        fetchSak(call, sakId).map {
            SakSelection(
                sakId = it.sakId,
                foedselsnr = it.foedselsnr,
                foedselsdato = it.foedselsdato,
                sakType = it.sakType,
            )
        }

    private data class BestillExtreamBrevDto(
        val letterCode: String,
        val language: SpraakKode,
        val vedtaksId: String?,
        val gjelderPid: String,
        val mottakerPid: String?,
        val saksbehanlderNavn: String,
        val saksbehanlderId: String,
        val isSensitivt: Boolean,
        val eblankett: ElankettBestilling? = null
    ) {
        data class ElankettBestilling(val landkode: String, val mottakerText: String)
    }

    suspend fun bestillExtreamBrev(
        call: ApplicationCall,
        request: OrderLetterRequest,
        saksbehandlerNavn: String,
        saksbehandlerBrukernavn: String
    ): ServiceResult<String, PenError> =
        client.post(call, "brev/extream/${request.sakId}") {
            setBody(
                BestillExtreamBrevDto(
                    letterCode = request.brevkode,
                    language = request.spraak,
                    vedtaksId = null, // TODO fill with actual value. When do we do this? check psak
                    gjelderPid = request.gjelderPid,
                    mottakerPid = null, // TODO fill with actual value when chosen.
                    saksbehanlderNavn = saksbehandlerNavn,
                    saksbehanlderId = saksbehandlerBrukernavn,
                    isSensitivt = false,  // TODO add choice to relevant letters and fill in.
                    eblankett = if (request.landkode != null && request.mottakerText != null) {
                        BestillExtreamBrevDto.ElankettBestilling(request.landkode, request.mottakerText)
                    } else null
                )
            )
            contentType(ContentType.Application.Json)
        }.toServiceResult<String, PenError>()


    private data class BestillDoksysBrevRequest(
        val sakId: Long,
        val brevkode: String,
        val mottaker: String?,
        val saksbehandlerNavn: String?,
        val saksbehandlerIdent: String?,
        val journalfoerendeEnhet: String?,
        val sensitivePersonopplysninger: Boolean?,
        val sprakKode: String?,
        val gjelder: String?,
        val vedtakId: Long?,
        val adresse: Adresse,
    ) {
        data class Adresse(
            val adresselinje1: String?,
            val adresselinje2: String?,
            val adresselinje3: String?,
            val poststed: String?,
            val postnummer: String?,
            val land: String,
            val landkode: String,
        )
    }

    suspend fun bestillDoksysBrev(
        call: ApplicationCall,
        request: OrderLetterRequest,
        saksbehandlerNavn: String,
        saksbehandlerBrukernavn: String,
    ): ServiceResult<String, PenError> =
        client.post(call, "brev/skribenten/doksys/sak/${request.sakId}") {
            setBody(
                BestillDoksysBrevRequest(
                    sakId = request.sakId,
                    brevkode = request.brevkode,
                    mottaker = null, // TODO
                    saksbehandlerNavn = saksbehandlerNavn,
                    saksbehandlerIdent = saksbehandlerBrukernavn,
                    journalfoerendeEnhet = "4849", // TODO
                    sensitivePersonopplysninger = false, // TODO
                    sprakKode = request.spraak.name,
                    gjelder = request.gjelderPid,
                    vedtakId = 42806043, //TODO fyll inn fra query param / lag select?
                    adresse = BestillDoksysBrevRequest.Adresse(
                        //TODO konverter adresse til doksys format ved bestilling av brev
                        adresselinje1 = "Adresselinje 0",
                        adresselinje2 = "Adresselinje 1",
                        adresselinje3 = "Adresselinje 2",
                        poststed = "Oslo",
                        postnummer = "0600",
                        land = "NORGE",
                        landkode = "NO",
                    )
                )
            )
            contentType(ContentType.Application.Json)
        }.toServiceResult<String, PenError>()

    suspend fun redigerExtreamBrev(call: ApplicationCall, journalpostId: String): ServiceResult<String, String> =
        client.get(call, "brev/rediger/extream/${journalpostId}").toServiceResult()

    data class Avtaleland(val navn: String, val kode: String)

    suspend fun hentAvtaleland(call: ApplicationCall): ServiceResult<List<Avtaleland>, String> =
        client.get(call, "brev/skribenten/avtaleland").toServiceResult<List<Avtaleland>, String>()
}

