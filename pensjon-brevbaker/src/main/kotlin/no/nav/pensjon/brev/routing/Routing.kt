package no.nav.pensjon.brev.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.api.AutobrevTemplateResource
import no.nav.pensjon.brev.latex.LaTeXCompilerHttpService
import no.nav.pensjon.brev.latex.LatexAsyncCompilerService
import no.nav.brev.brevbaker.AllTemplates
import no.nav.pensjon.brev.api.RedigerbarTemplateResource
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.BestillBrevRequestAsync
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.latex.LaTeXCompilerGrpcService
import no.nav.pensjon.etterlatte.EtterlatteMaler

fun Application.brevRouting(
    authenticationNames: Array<String>?,
    latexCompilerService: LaTeXCompilerHttpService,
    brevProvider: AllTemplates,
    latexAsyncCompilerService: LatexAsyncCompilerService?,
) =
    routing {
        val latexCompilerGrpcService = LaTeXCompilerGrpcService("pensjon-pdf-bygger-grpc-headless", System.getenv()["GRPC_PORT"]?.toInt() ?: 8080)
        val autobrev = AutobrevTemplateResource("autobrev", brevProvider.hentAutobrevmaler(), latexCompilerService, latexAsyncCompilerService, latexCompilerGrpcService)
        val redigerbareBrev = RedigerbarTemplateResource("redigerbar", brevProvider.hentRedigerbareMaler(), latexCompilerService)

        route("/templates") {
            templateRoutes(autobrev)
            templateRoutes(redigerbareBrev)
        }

        route("/letter") {
            post<BestillBrevRequest<Brevkode.Automatisk>>("/pdfGrpc-unauth") { brevbestilling ->
                installBrevkodeInCallContext(brevbestilling.kode)
                call.respond(autobrev.renderPDFGrpc(brevbestilling))
                autobrev.countLetter(brevbestilling.kode)
            }
        }

        authenticate(authenticationNames) {
            route("/letter") {
                autobrevRoutes(autobrev)
                redigerbarRoutes(redigerbareBrev)
                if (latexAsyncCompilerService != null) {
                    post<BestillBrevRequestAsync<Brevkode.Automatisk>>("/${autobrev.name}/pdfAsync") { brevbestillingAsync ->
                        installBrevkodeInCallContext(brevbestillingAsync.kode)
                        autobrev.renderPdfAsync(brevbestillingAsync)
                        autobrev.countLetter(brevbestillingAsync.kode)
                        call.respond(HttpStatusCode.OK)
                    }
                }
                post<BestillBrevRequest<Brevkode.Automatisk>>("/pdfGrpc") { brevbestilling ->
                    installBrevkodeInCallContext(brevbestilling.kode)
                    call.respond(autobrev.renderPDFGrpc(brevbestilling))
                    autobrev.countLetter(brevbestilling.kode)
                }
            }

            route("etterlatte") {
                autobrevRoutes(AutobrevTemplateResource("", EtterlatteMaler.hentAutobrevmaler(), latexCompilerService, latexAsyncCompilerService, latexCompilerGrpcService))
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