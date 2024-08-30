package no.nav.pensjon.brev.skribenten

import com.typesafe.config.Config
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.auth.AzureADService
import no.nav.pensjon.brev.skribenten.auth.JwtConfig
import no.nav.pensjon.brev.skribenten.db.initDatabase
import no.nav.pensjon.brev.skribenten.routes.*
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.tjenestebussIntegrasjonRoute
import no.nav.pensjon.brev.skribenten.services.*

fun Application.configureRouting(authConfig: JwtConfig, skribentenConfig: Config) {
    val authService = AzureADService(authConfig)
    val servicesConfig = skribentenConfig.getConfig("services")
    initDatabase(servicesConfig)
    val safService = SafService(servicesConfig.getConfig("saf"), authService)
    val penService = PenService(servicesConfig.getConfig("pen"), authService)
    val pensjonPersonDataService = PensjonPersonDataService(servicesConfig.getConfig("pensjon_persondata"), authService)
    val pdlService = PdlService(servicesConfig.getConfig("pdl"), authService)
    val krrService = KrrService(servicesConfig.getConfig("krr"), authService)
    val brevbakerService = BrevbakerService(servicesConfig.getConfig("brevbaker"), authService)
    val brevmetadataService = BrevmetadataService(servicesConfig.getConfig("brevmetadata"))
    val tjenestebussIntegrasjonService =
        TjenestebussIntegrasjonService(
            servicesConfig.getConfig("tjenestebussintegrasjon"),
            servicesConfig.getConfig("samhandlerProxy"),
            authService
        )
    val navansattService = NavansattService(servicesConfig.getConfig("navansatt"), authService)
    val legacyBrevService = LegacyBrevService(brevmetadataService, safService, penService, navansattService)
    val brevmalService = BrevmalService(penService, brevmetadataService, brevbakerService)
    val brevredigeringService = BrevredigeringService(brevbakerService, penService, navansattService)


    routing {
        healthRoute()

        authenticate(authConfig.name) {
            setupServiceStatus(safService, penService, pensjonPersonDataService, pdlService, krrService, brevbakerService, brevmetadataService, tjenestebussIntegrasjonService, navansattService)

            landRoute()
            brevmal(brevbakerService)
            kodeverkRoute(penService)
            sakRoute(
                penService,
                legacyBrevService,
                pdlService,
                pensjonPersonDataService,
                krrService,
                brevmalService,
                brevredigeringService,
                safService,
            )
            brev(brevredigeringService)
            tjenestebussIntegrasjonRoute(tjenestebussIntegrasjonService)
            meRoute(navansattService)
        }
    }
}