package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.JSR310DateTimeDeserializerBase
import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.typesafe.config.Config
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import no.nav.pensjon.brev.skribenten.auth.AzureADOnBehalfOfAuthorizedHttpClient
import no.nav.pensjon.brev.skribenten.auth.AzureADService
import java.time.LocalDate
import java.util.*

class PenService(config: Config, authService: AzureADService) {
    private val penUrl = config.getString("url")
    private val penScope = config.getString("scope")

    private val client = AzureADOnBehalfOfAuthorizedHttpClient(penScope, authService) {
        defaultRequest {
            url(penUrl)
        }
        install(ContentNegotiation) {
            jackson{
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
        val sakType: String,
    )


    suspend fun hentSak(call: ApplicationCall, sakId: Long): ServiceResult<Sak, Any> =
        client.get(call, "sak/$sakId").toServiceResult()

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


    suspend fun bestillExtreamBrev(
        call: ApplicationCall,
        sakId: Long,
        brevkode: String,
        spraak: SpraakKode,
    ): ServiceResult<String, Any> =
        client.post(call, "sak/$sakId/extreamBrev") {
            setBody(
                BestillExtreamBrevDto(
                    letterCode = brevkode, // TODO fill with actual value
                    language = spraak,  // TODO fill with actual value
                    vedtaksId = null, // TODO fill with actual value
                    gjelderPid = "09417320595", // TODO fill with actual value. Can't it allways be set from sakid?
                    mottakerPid = null, // TODO fill with actual value
                    saksbehanlderNavn = "Saksbehandler Saksbehandlerson",  // TODO fill with actual value
                    saksbehanlderId = "ZBLABLAHH",  // TODO fill with actual value
                    isSensitivt = false,  // TODO fill with actual value
                )
            )
            contentType(ContentType.Application.Json)
        }.toServiceResult()

    suspend fun bestillDoksysBrev(call: ApplicationCall, sakId: Long): ServiceResult<String, Any> =
        client.post(call, "sak/$sakId/extreamBrev") {
            setBody(
                BestillExtreamBrevDto(
                    letterCode = "PE_IY_05_300",
                    language = SpraakKode.NB,
                    vedtaksId = null,
                    gjelderPid = "09417320595",
                    mottakerPid = null,
                    saksbehanlderNavn = "Saksbehandler Saksbehandlerson",
                    saksbehanlderId = "ZBLABLAHH",
                    isSensitivt = false,
                )
            )
            contentType(ContentType.Application.Json)
        }.toServiceResult()

    ///{sakId}/redigerbrev
}

