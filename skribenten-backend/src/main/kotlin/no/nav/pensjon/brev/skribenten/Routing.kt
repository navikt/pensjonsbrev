package no.nav.pensjon.brev.skribenten

import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.http.*
import io.ktor.openapi.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*
import io.ktor.server.routing.openapi.*
import no.nav.pensjon.brev.skribenten.auth.*
import no.nav.pensjon.brev.skribenten.eksterntApi.externalAPI
import no.nav.pensjon.brev.skribenten.openapi.*
import no.nav.pensjon.brev.skribenten.routes.*
import no.nav.pensjon.brev.skribenten.routes.samhandler.samhandlerRoute

fun Application.configureRouting() {
    routing {
        healthRoute()

        swaggerUI("/swagger", "openapi/external-api.yaml")
        swaggerUI("/swagger-internal") {
            info = OpenApiInfo("Skribenten Internal API", "1.0")
            source = OpenApiDocSource.Routing(
                contentType = ContentType.Application.Json,
                schemaInference = JacksonReflectionJsonSchemaInference(
                    JacksonSchemaReflectionAdapter(ObjectMapper().skribentenServerJackson())
                ),
                routes = {
                    val excludedRoutePrefixes = listOf("/external/", "/swagger", "/isAlive", "/isReady")
                    routingRoot.descendants()
                        .filter { route ->
                            val path = route.path()
                            excludedRoutePrefixes.none { path.startsWith(it) }
                        }
                },
            )
            remotePath = "documentation.json"
        }

        authenticate(AUTHENTICATION_REALM_NAME) {
            install(PrincipalInContext)
            install(PrincipalHasGroup) {
                requireOneOf(ADGroups.alleBrukergrupper)
            }

            landRoute()
            brevmal()
            kodeverkRoute()
            sakRoute()
            brev()
            samhandlerRoute()
            meRoute()

        }

        externalAPI()
    }
}