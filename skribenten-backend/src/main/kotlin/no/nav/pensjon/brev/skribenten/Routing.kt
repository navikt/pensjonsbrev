package no.nav.pensjon.brev.skribenten

import com.typesafe.config.Config
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.auth.ADGroups
import no.nav.pensjon.brev.skribenten.auth.AzureADService
import no.nav.pensjon.brev.skribenten.auth.JwtConfig
import no.nav.pensjon.brev.skribenten.auth.PrincipalHasGroup
import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.db.initDatabase
import no.nav.pensjon.brev.skribenten.routes.*
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.tjenestebussIntegrasjonRoute
import no.nav.pensjon.brev.skribenten.services.*

fun Application.configureRouting(
    authConfig: JwtConfig,
    skribentenConfig: Config,
    cacheConfig: CacheImplementation<String, String>
) {
    val authService = AzureADService(authConfig, cacheConfig = cacheConfig)
    val servicesConfig = skribentenConfig.getConfig("services")
    initDatabase(servicesConfig).also { db -> monitor.subscribe(ApplicationStopPreparing) { db.close() } }
    val safService = SafServiceHttp(servicesConfig.getConfig("saf"), authService)
    val penService = PenServiceHttp(servicesConfig.getConfig("pen"), authService)
    val pensjonPersonDataService = PensjonPersonDataService(servicesConfig.getConfig("pensjon_persondata"), authService)
    val pdlService = PdlServiceHttp(servicesConfig.getConfig("pdl"), authService)
    val krrService = KrrService(servicesConfig.getConfig("krr"), authService)
    val brevbakerService = BrevbakerServiceHttp(servicesConfig.getConfig("brevbaker"), authService)
    val brevmetadataService = BrevmetadataServiceHttp(servicesConfig.getConfig("brevmetadata"))
    val samhandlerService = SamhandlerServiceHttp(servicesConfig.getConfig("samhandlerProxy"), authService)
    val tjenestebussIntegrasjonService = TjenestebussIntegrasjonService(servicesConfig.getConfig("tjenestebussintegrasjon"), authService)
    val navansattService = NavansattServiceHttp(servicesConfig.getConfig("navansatt"), authService)
    val legacyBrevService = LegacyBrevService(brevmetadataService, safService, penService, navansattService)
    val brevmalService = BrevmalService(penService, brevmetadataService, brevbakerService)
    val norg2Service = Norg2ServiceHttp(servicesConfig.getConfig("norg2"))
    val brevredigeringService =
        BrevredigeringService(brevbakerService, navansattService, penService)
    val dto2ApiService = Dto2ApiService(brevbakerService, navansattService, norg2Service, samhandlerService)
    val externalAPIService = ExternalAPIService(servicesConfig.getConfig("externalApi"), brevredigeringService, brevbakerService)

    Features.initUnleash(servicesConfig.getConfig("unleash"))

    routing {
        healthRoute()
        swaggerUI("/swagger", "openapi/external-api.yaml")

        authenticate(authConfig.name) {
            install(PrincipalInContext)
            install(PrincipalHasGroup) {
                requireOneOf(ADGroups.alleBrukergrupper)
            }

            setupServiceStatus(
                safService,
                penService,
                pensjonPersonDataService,
                pdlService,
                krrService,
                brevbakerService,
                brevmetadataService,
                samhandlerService,
                tjenestebussIntegrasjonService,
                navansattService
            )

            landRoute()
            brevmal(brevbakerService)
            kodeverkRoute(penService)
            sakRoute(
                dto2ApiService,
                brevmalService,
                brevredigeringService,
                krrService,
                legacyBrevService,
                pdlService,
                penService,
                pensjonPersonDataService,
                safService,
            )
            brev(brevredigeringService, dto2ApiService, pdlService, penService)
            tjenestebussIntegrasjonRoute(samhandlerService, tjenestebussIntegrasjonService)
            meRoute(navansattService)

        }

        externalAPI(authConfig, externalAPIService)
    }
}