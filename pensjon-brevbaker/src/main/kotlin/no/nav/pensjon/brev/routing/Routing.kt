package no.nav.pensjon.brev.routing

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.log
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import no.nav.brev.brevbaker.AllTemplates
import no.nav.brev.brevbaker.PDFByggerService
import no.nav.pensjon.brev.api.AutobrevTemplateResource
import no.nav.pensjon.brev.api.RedigerbarTemplateResource
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.BestillBrevRequestAsync
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.latex.PDFByggerAsync
import no.nav.pensjon.etterlatte.EtterlatteMaler

fun Application.brevRouting(
    authenticationNames: Array<String>?,
    pdfByggerService: PDFByggerService,
    brevProvider: AllTemplates,
    pDFByggerAsync: PDFByggerAsync?,
) =
    routing {
        val autobrev = AutobrevTemplateResource("autobrev", brevProvider.hentAutobrevmaler(), pdfByggerService, pDFByggerAsync)
        val redigerbareBrev = RedigerbarTemplateResource("redigerbar", brevProvider.hentRedigerbareMaler(), pdfByggerService)

        route("/templates") {
            templateRoutes(autobrev)
            templateRoutes(redigerbareBrev)
        }

        authenticate(authenticationNames) {
            route("/letter") {
                autobrevRoutes(autobrev)
                redigerbarRoutes(redigerbareBrev)
                if (pDFByggerAsync != null) {
                    log.info("registrert endepunkt for async kompilering av brev")
                    post<BestillBrevRequestAsync<Brevkode.Automatisk>>("/${autobrev.name}/pdfAsync") { brevbestillingAsync ->
                        installBrevkodeInCallContext(brevbestillingAsync.kode)
                        autobrev.renderPdfAsync(brevbestillingAsync)
                        autobrev.countLetter(brevbestillingAsync.kode)
                        call.respond(HttpStatusCode.OK)
                    }
                }
            }

            route("etterlatte") {
                val etterlatteResource = AutobrevTemplateResource(
                    "",
                    EtterlatteMaler.hentAutobrevmaler(),
                    pdfByggerService,
                    pDFByggerAsync
                )
                autobrevRoutes(etterlatteResource)

                post<BestillBrevRequest<Brevkode.Automatisk>>("/json/slate") { request ->
                    call.respond(etterlatteResource.renderJSON(request).let { EtterlatteMaler.somSlate(it) })
                }
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