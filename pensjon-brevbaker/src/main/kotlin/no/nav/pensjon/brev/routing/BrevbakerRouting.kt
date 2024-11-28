package no.nav.pensjon.brev.routing

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.response.respondText
import io.ktor.server.routing.application
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

internal fun Application.brevbakerRouting(authenticationNames: Array<String>) {
    routing {
        authenticate(*authenticationNames, optional = application.developmentMode) {
            get("/ping_authorized") {
                val principal = call.authentication.principal<JWTPrincipal>()
                call.respondText("Authorized as: ${principal?.subject}")
            }
        }

        get("/isAlive") {
            call.respondText("Alive!", ContentType.Text.Plain, HttpStatusCode.OK)
        }

        get("/isReady") {
            call.respondText("Ready!", ContentType.Text.Plain, HttpStatusCode.OK)
        }

        swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml")
    }
}