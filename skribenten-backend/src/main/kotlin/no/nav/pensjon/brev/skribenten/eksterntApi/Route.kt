package no.nav.pensjon.brev.skribenten.eksterntApi

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import no.nav.pensjon.brev.skribenten.auth.ADGroups
import no.nav.pensjon.brev.skribenten.auth.JwtConfig
import no.nav.pensjon.brev.skribenten.auth.PrincipalHasGroup
import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.brevredigering.domain.OpprettBrevPolicy
import no.nav.pensjon.brev.skribenten.model.SaksId
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("ExternalApi")

fun Route.externalAPI(authConfig: JwtConfig, externalAPIService: ExternalAPIService) =
    authenticate(authConfig.name) {
        install(PrincipalInContext)
        install(PrincipalHasGroup) {
            requireOneOf(ADGroups.alleBrukergrupper)
            onRejection { respond(emptyList<String>()) }
        }
        route("/external/api/v1") {
            route("/brev") {
                get {
                    val saksIder = call.queryParameters.getAll("saksId")
                        ?.flatMap { it.split(",") }
                        ?.mapNotNull { it.toLongOrNull() }
                        ?.map { SaksId(it) }
                        ?: emptyList()

                    call.respond(externalAPIService.hentAlleBrevForSaker(saksIder.toSet()))
                }
                post<ExternalAPI.OpprettBrevRequest>("/brev") { request ->
                    externalAPIService.opprettBrev(request).onSuccess {
                        call.respond(HttpStatusCode.Created, it)
                    }.onError { error ->
                        when (error) {
                            is OpprettBrevPolicy.KanIkkeOppretteBrev.BrevmalKreverVedtaksId ->
                                call.respond(HttpStatusCode.BadRequest, "Brevmal krever vedtaksId: ${error.brevkode}")

                            is OpprettBrevPolicy.KanIkkeOppretteBrev.IkkeTilgangTilEnhet ->
                                call.respond(HttpStatusCode.BadRequest, "Ikke tilgang til enhet: ${error.enhetsId}")

                            else -> {
                                logger.warn("Feil ved opprettelse av brev: {}", error)
                                call.respond(HttpStatusCode.InternalServerError, "Feil ved opprettelse av brev")
                            }
                        }
                    }
                }
            }
        }
    }
