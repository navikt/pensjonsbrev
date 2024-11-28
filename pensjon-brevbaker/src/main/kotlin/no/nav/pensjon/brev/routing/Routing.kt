package no.nav.pensjon.brev.routing

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.api.TemplateResource
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.AllTemplates
import no.nav.pensjon.etterlatte.EtterlatteMaler

fun Application.brevRouting(authenticationNames: Array<String>, latexCompilerService: LaTeXCompilerService, brevProvider: AllTemplates) =
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
                letterRoutes(
                    autobrev = TemplateResource("", EtterlatteMaler.hentAutobrevmaler(), latexCompilerService),
                    redigerbareBrev = TemplateResource("etterlatteRedigerbare", EtterlatteMaler.hentRedigerbareMaler(), latexCompilerService)
                )
            }
        }
    }
