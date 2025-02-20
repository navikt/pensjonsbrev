package no.nav.pensjon.brev.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.api.AutobrevTemplateResource
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.latex.LatexAsyncCompilerService
import no.nav.brev.brevbaker.AllTemplates
import no.nav.pensjon.brev.api.RedigerbarTemplateResource
import no.nav.pensjon.brev.api.model.BestillBrevRequestAsync
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.etterlatte.EtterlatteMaler

fun Application.brevRouting(
    authenticationNames: Array<String>?,
    latexCompilerService: LaTeXCompilerService,
    brevProvider: AllTemplates,
    latexAsyncCompilerService: LatexAsyncCompilerService?,
) =
    routing {
        val autobrev = AutobrevTemplateResource("autobrev", brevProvider.hentAutobrevmaler(), latexCompilerService, latexAsyncCompilerService)
        val redigerbareBrev = RedigerbarTemplateResource("redigerbar", brevProvider.hentRedigerbareMaler(), latexCompilerService, latexAsyncCompilerService)

        route("/templates") {
            templateRoutes(autobrev)
            templateRoutes(redigerbareBrev)
        }

        authenticate(authenticationNames) {
            route("/letter") {
                autobrevRoutes(autobrev)
                redigerbarRoutes(redigerbareBrev)
                if (latexAsyncCompilerService != null) {
                    post<BestillBrevRequestAsync<Brevkode.Automatisk>>("/${autobrev.name}/pdfAsync") { brevbestillingAsync ->
                        val brevbestilling = brevbestillingAsync.brevRequest
                        installBrevkodeInCallContext(brevbestilling.kode)
                        autobrev.renderPdfAsync(brevbestillingAsync)
                        autobrev.countLetter(brevbestilling.kode)
                        call.respond(HttpStatusCode.OK)
                    }
                }
            }

            route("etterlatte") {
                autobrevRoutes(AutobrevTemplateResource("", EtterlatteMaler.hentAutobrevmaler(), latexCompilerService, latexAsyncCompilerService))
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

fun Routing.authenticate(names: Array<String>?, build: Route.() -> Unit) {
    if (names != null) {
        authenticate(*names, optional = false, build = build)
    } else {
        build()
    }
}