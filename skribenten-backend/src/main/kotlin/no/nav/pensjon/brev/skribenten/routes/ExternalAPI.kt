package no.nav.pensjon.brev.skribenten.routes

import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.auth.ADGroups
import no.nav.pensjon.brev.skribenten.auth.JwtConfig
import no.nav.pensjon.brev.skribenten.auth.PrincipalHasGroup
import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.services.ExternalAPIService

fun Route.externalAPI(authConfig: JwtConfig, externalAPIService: ExternalAPIService) =
    authenticate(authConfig.name) {
        install(PrincipalInContext)
        install(PrincipalHasGroup) {
            requireOneOf(ADGroups.alleBrukergrupper)
            onRejection { respond(emptyList<String>()) }
        }
        route("/external/api/v1") {
            get("/brev") {
                val saksIder = call.queryParameters.getAll("saksId")
                    ?.flatMap { it.split(",") }
                    ?.mapNotNull { it.toLongOrNull() }
                    ?: emptyList()

                call.respond(externalAPIService.hentAlleBrevForSaker(saksIder.toSet()))
            }
        }
    }
