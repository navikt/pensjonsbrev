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

fun Application.configureRouting(authConfig: JwtConfig, skribentenConfig: Config) {
    val authService = AzureADService(authConfig)
    val servicesConfig = skribentenConfig.getConfig("services")
    initDatabase(servicesConfig).also { db -> monitor.subscribe(ApplicationStopping) { db.close() } }
    val safService = SafService(servicesConfig.getConfig("saf"), authService)
    val penService = PenService(servicesConfig.getConfig("pen"), authService)
    val pensjonPersonDataService = PensjonPersonDataService(servicesConfig.getConfig("pensjon_persondata"), authService)
    val pdlService = PdlService(servicesConfig.getConfig("pdl"), authService)
    val krrService = KrrService(servicesConfig.getConfig("krr"), authService)
    val brevbakerService = BrevbakerService(servicesConfig.getConfig("brevbaker"), authService)
    val brevmetadataService = BrevmetadataService(servicesConfig.getConfig("brevmetadata"))
    val samhandlerService = SamhandlerService(servicesConfig.getConfig("samhandlerProxy"), authService)
    val tjenestebussIntegrasjonService = TjenestebussIntegrasjonService(servicesConfig.getConfig("tjenestebussintegrasjon"), authService)
    val navansattService = NavansattService(servicesConfig.getConfig("navansatt"), authService)
    val legacyBrevService = LegacyBrevService(brevmetadataService, safService, penService, navansattService)
    val brevmalService = BrevmalService(penService, brevmetadataService, brevbakerService)
    val norg2Service = Norg2Service(servicesConfig.getConfig("norg2"))
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
                requireOneOf(
                    setOf(
                        ADGroups.pensjonSaksbehandler,
                        ADGroups.attestant,
                        ADGroups.veileder,
                        ADGroups.okonomi,
                        ADGroups.brukerhjelpA
                    )
                )
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

            externalAPI(externalAPIService)
        }
    }
}