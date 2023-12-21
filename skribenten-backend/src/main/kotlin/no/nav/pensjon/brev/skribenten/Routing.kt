package no.nav.pensjon.brev.skribenten

import com.typesafe.config.Config
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.skribenten.auth.AzureADService
import no.nav.pensjon.brev.skribenten.auth.JwtConfig
import no.nav.pensjon.brev.skribenten.routes.brevbakerRoute
import no.nav.pensjon.brev.skribenten.routes.favoritesRoute
import no.nav.pensjon.brev.skribenten.routes.healthRoute
import no.nav.pensjon.brev.skribenten.routes.kodeverkRoute
import no.nav.pensjon.brev.skribenten.routes.penRoute
import no.nav.pensjon.brev.skribenten.routes.personRoute
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.tjenestebussIntegrasjonRoute
import no.nav.pensjon.brev.skribenten.services.BrevbakerService
import no.nav.pensjon.brev.skribenten.services.BrevmetadataService
import no.nav.pensjon.brev.skribenten.services.KodeverkService
import no.nav.pensjon.brev.skribenten.services.KrrService
import no.nav.pensjon.brev.skribenten.services.LetterCategory
import no.nav.pensjon.brev.skribenten.services.LetterMetadata
import no.nav.pensjon.brev.skribenten.services.PdlService
import no.nav.pensjon.brev.skribenten.services.PenService
import no.nav.pensjon.brev.skribenten.services.PensjonPersonDataService
import no.nav.pensjon.brev.skribenten.services.SafService
import no.nav.pensjon.brev.skribenten.services.TjenestebussIntegrasjonService
import no.nav.pensjon.brev.skribenten.services.respondWithResult

fun Application.configureRouting(authConfig: JwtConfig, skribentenConfig: Config) {
    val authService = AzureADService(authConfig)
    val safService = SafService(skribentenConfig.getConfig("services.saf"), authService)
    val penService = PenService(skribentenConfig.getConfig("services.pen"), authService)
    val pensjonPersonDataService =
        PensjonPersonDataService(skribentenConfig.getConfig("services.pensjon_persondata"), authService)
    val kodeverkService = KodeverkService(skribentenConfig.getConfig("services.kodeverk"))
    val pdlService = PdlService(skribentenConfig.getConfig("services.pdl"), authService)
    val krrService = KrrService(skribentenConfig.getConfig("services.krr"), authService)
    val brevbakerService = BrevbakerService(skribentenConfig.getConfig("services.brevbaker"), authService)
    val brevmetadataService = BrevmetadataService(skribentenConfig.getConfig("services.brevmetadata"))
    val tjenestebussIntegrasjonService =
        TjenestebussIntegrasjonService(skribentenConfig.getConfig("services.tjenestebussintegrasjon"), authService)

    routing {
        healthRoute()

        authenticate(authConfig.name) {
            post("/test/pen") {
                respondWithResult(safService.getStatus(call, "453840176"))
            }

            data class LetterTemplatesResponse(
                val kategorier: List<LetterCategory>,
                val eblanketter: List<LetterMetadata>
            )
            get("/lettertemplates/{sakType}") {
                val sakType = call.parameters.getOrFail("sakType")
                call.respond(
                    LetterTemplatesResponse(
                        brevmetadataService.getRedigerbareBrevKategorier(sakType),
                        //TODO figure out who has access to e-blanketter and filter them out. then only display eblanketter when you get the metadata back.
                        brevmetadataService.getEblanketter()
                    )
                )
            }
            brevbakerRoute(brevbakerService)
            favoritesRoute()
            kodeverkRoute(kodeverkService, penService)
            penRoute(penService, safService)
            personRoute(pdlService, pensjonPersonDataService, krrService)
            tjenestebussIntegrasjonRoute(tjenestebussIntegrasjonService)
        }
    }
}