package no.nav.pensjon.brev.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.api.TemplateResource
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.AllTemplates
import no.nav.pensjon.etterlatte.etterlatteRouting

fun Application.brevbakerRouting(authenticationNames: Array<String>, latexCompilerService: LaTeXCompilerService, brevProvider: AllTemplates) =
    routing {
        val autobrev = TemplateResource("autobrev", brevProvider.hentAutobrevmaler(), latexCompilerService)
        val redigerbareBrev = TemplateResource("redigerbar", brevProvider.hentRedigerbareMaler(), latexCompilerService)

        route("/templates") {
            templateRoutes(autobrev)
            templateRoutes(redigerbareBrev)
        }

        authenticate(*authenticationNames, optional = application.developmentMode) {
            route("/letter") {
                letterRoutes(autobrev, redigerbareBrev)
            }

            route("etterlatte") {
                etterlatteRouting(latexCompilerService)
            }
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
