package no.nav.pensjon.brev.skribenten

import com.typesafe.config.Config
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.auth.AzureADService
import no.nav.pensjon.brev.skribenten.auth.JwtConfig
import no.nav.pensjon.brev.skribenten.routes.bestillBrevRoute
import no.nav.pensjon.brev.skribenten.routes.brevbakerRoute
import no.nav.pensjon.brev.skribenten.routes.brevmalerRoute
import no.nav.pensjon.brev.skribenten.routes.healthRoute
import no.nav.pensjon.brev.skribenten.routes.kodeverkRoute
import no.nav.pensjon.brev.skribenten.routes.meRoute
import no.nav.pensjon.brev.skribenten.routes.sakRoute
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.tjenestebussIntegrasjonRoute
import no.nav.pensjon.brev.skribenten.services.BrevbakerService
import no.nav.pensjon.brev.skribenten.services.BrevmetadataService
import no.nav.pensjon.brev.skribenten.services.KrrService
import no.nav.pensjon.brev.skribenten.services.LegacyBrevService
import no.nav.pensjon.brev.skribenten.services.NavansattService
import no.nav.pensjon.brev.skribenten.services.PdlService
import no.nav.pensjon.brev.skribenten.services.PenService
import no.nav.pensjon.brev.skribenten.services.PensjonPersonDataService
import no.nav.pensjon.brev.skribenten.services.SafService
import no.nav.pensjon.brev.skribenten.services.TjenestebussIntegrasjonService
import no.nav.pensjon.brev.skribenten.services.initDatabase

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
        TjenestebussIntegrasjonService(servicesConfig.getConfig("tjenestebussintegrasjon"), authService)
    val navansattService = NavansattService(servicesConfig.getConfig("navansatt"), authService)
    val legacyBrevService = LegacyBrevService(tjenestebussIntegrasjonService, brevmetadataService, safService, penService, navansattService)

    routing {
        healthRoute()

        authenticate(authConfig.name) {
            brevmalerRoute(brevmetadataService, skribentenConfig.getConfig("groups"))
            brevbakerRoute(brevbakerService)
            bestillBrevRoute(legacyBrevService)
            kodeverkRoute(penService)
            sakRoute(
                penService, navansattService,
                legacyBrevService,
                pdlService,
                pensjonPersonDataService,
                krrService,
            )
            tjenestebussIntegrasjonRoute(tjenestebussIntegrasjonService)
            meRoute(servicesConfig.getConfig("navansatt"), authService)
        }
    }
}