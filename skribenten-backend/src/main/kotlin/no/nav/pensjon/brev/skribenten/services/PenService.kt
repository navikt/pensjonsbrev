package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.typesafe.config.Config
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import no.nav.pensjon.brev.skribenten.OrderLetterRequest
import no.nav.pensjon.brev.skribenten.auth.AuthorizedHttpClientResult
import no.nav.pensjon.brev.skribenten.auth.AzureADOnBehalfOfAuthorizedHttpClient
import no.nav.pensjon.brev.skribenten.auth.AzureADService
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

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
            }
        }
    }

    // Demo request
    // TODO: Handle ResponseException and wrap in ServiceResult.Error. Design a type for possible errors.

    // gjelder fnr
    // nav enhet tilhørlighet(og egen valgt?)
    // sakstype
    // person navn
    // fødselsdato

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class PenPersonDto(
        val fodselsdato: LocalDate,
        val fnr: String,
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Sak(
        val sakId: Long,
        val penPerson: PenPersonDto,
        val sakType: SakType,
    )

    enum class SakType { AFP, AFP_PRIVAT, ALDER, BARNEP, FAM_PL, GAM_YRK, GENRL, GJENLEV, GRBL, KRIGSP, OMSORG, UFOREP, }
    data class SakSelection(
        val sakId: Long,
        val foedselsnr: String,
        val foedselsdato: String,
        val sakType: SakType,
    )


    private suspend fun fetchSak(call: ApplicationCall, sakId: String): ServiceResult<Sak, String> =
        client.get(call, "sak/$sakId").toServiceResult<Sak, String>()

    suspend fun hentSak(call: ApplicationCall, sakId: String) =
        fetchSak(call, sakId).map {
            SakSelection(
                sakId = it.sakId,
                foedselsnr = it.penPerson.fnr,
                foedselsdato = it.penPerson.fodselsdato.format(
                    DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
                        .withLocale(Locale.forLanguageTag("no"))
                ),
                sakType = it.sakType,
            )
    }

    private data class BestillBrevRequest(
        val sakId: Long? = null,
        val brevType: String? = null,
        val mottaker: String? = null,
        val saksbehandlerNavn: String? = null,
        val saksbehandlerIdent: String? = null,
        val journalfoerendeEnhet: String? = null,
        val sensitivePersonopplysninger: Boolean? = null,
        val sprakKode: String? = null,
        val automatiskBehandlet: Boolean? = null,
        val gjelder: String? = null,
        val vedtakId: Long? = null,
    )

    private data class BestillExtreamBrevDto(
        val letterCode: String,
        val language: SpraakKode,
        val vedtaksId: String?,
        val gjelderPid: String,
        val mottakerPid: String?,
        val saksbehanlderNavn: String,
        val saksbehanlderId: String,
        val isSensitivt: Boolean,
    )


    suspend fun bestillExtreamBrev(call: ApplicationCall, request: OrderLetterRequest): ServiceResult<String, String> =
        client.post(call, "brev/extream/${request.sakId}") {
            setBody(
                BestillExtreamBrevDto(
                    letterCode = request.brevkode,
                    language = request.spraak,
                    vedtaksId = null, // TODO fill with actual value
                    gjelderPid = request.gjelderPid, // TODO fill with actual value. Can't it allways be set from sakid?
                    mottakerPid = null, // TODO fill with actual value
                    saksbehanlderNavn = "Saksbehandler Saksbehandlerson",  // TODO fill with actual value
                    saksbehanlderId = "ZTest",  // TODO fill with actual value
                    isSensitivt = false,  // TODO fill with actual value
                )
            )
            contentType(ContentType.Application.Json)
        }.toServiceResult<String, String>()

//    suspend fun bestillDoksysBrev(call: ApplicationCall, sakId: Long): AuthorizedHttpClientResult =
//        client.post(call, "sak/$sakId/doksysBrev") {
//            setBody(
//                BestillExtreamBrevDto(
//                    letterCode = "PE_IY_05_300",
//                    language = SpraakKode.NB,
//                    vedtaksId = null,
//                    gjelderPid = "09417320595",
//                    mottakerPid = null,
//                    saksbehanlderNavn = "Saksbehandler Saksbehandlerson",
//                    saksbehanlderId = "ZBLABLAHH",
//                    isSensitivt = false,
//                )
//            )
//            contentType(ContentType.Application.Json)
//        }

    suspend fun redigerExtreamBrev(call: ApplicationCall, journalposId: String): ServiceResult<String, String> =
        client.get(call, "brev/rediger/extream/${journalposId}").toServiceResult()

    suspend fun redigerDoksysBrev(call: ApplicationCall, journalposId: String): AuthorizedHttpClientResult =
        client.get(call, "brev/rediger/doksys/${journalposId}")
    ///{sakId}/redigerbrev
}

