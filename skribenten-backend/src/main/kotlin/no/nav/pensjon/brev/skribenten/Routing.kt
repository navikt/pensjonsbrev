package no.nav.pensjon.brev.skribenten

import com.typesafe.config.Config
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.auth.*
import no.nav.pensjon.brev.skribenten.brevbaker.BrevbakerServiceHttp
import no.nav.pensjon.brev.skribenten.brevbaker.RenderService
import no.nav.pensjon.brev.skribenten.brevredigering.application.BrevredigeringFacadeFactory
import no.nav.pensjon.brev.skribenten.common.Cache
import no.nav.pensjon.brev.skribenten.db.initDatabase
import no.nav.pensjon.brev.skribenten.fagsystem.BrevService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevdataService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.fagsystem.FagsakService
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.BrevmetadataServiceHttp
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.LegacyBrevServiceImpl
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.P1ServiceImpl
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.PentHttpClient
import no.nav.pensjon.brev.skribenten.routes.*
import no.nav.pensjon.brev.skribenten.routes.samhandler.samhandlerRoute
import no.nav.pensjon.brev.skribenten.services.*

fun Application.configureRouting(
    authConfig: JwtConfig,
    skribentenConfig: Config,
    cache: Cache
) {
    val authService = AzureADService(authConfig, cache = cache)
    val servicesConfig = skribentenConfig.getConfig("services")
    initDatabase(servicesConfig).also { db -> monitor.subscribe(ApplicationStopping) { db.close() } }
    val safService = SafServiceHttp(servicesConfig.getConfig("saf"), authService)
    val penClient = PentHttpClient(servicesConfig.getConfig("pen"), authService)
    val skjermingService = SkjermingServiceHttp(servicesConfig.getConfig("skjerming"), authService, cache)
    val pensjonPersonDataService = PensjonPersonDataService(servicesConfig.getConfig("pensjon_persondata"), authService)
    val pensjonRepresentasjonService = PensjonRepresentasjonService(servicesConfig.getConfig("pensjonRepresentasjon"), authService, cache)
    val pdlService = PdlServiceHttp(servicesConfig.getConfig("pdl"), authService)
    val krrService = KrrService(servicesConfig.getConfig("krr"), authService)
    val brevbakerService = BrevbakerServiceHttp(servicesConfig.getConfig("brevbaker"), authService, cache)
    val brevmetadataService = BrevmetadataServiceHttp(servicesConfig.getConfig("brevmetadata"))
    val samhandlerService = SamhandlerServiceHttp(servicesConfig.getConfig("samhandlerProxy"), authService, cache)
    val navansattService = NavansattServiceHttp(servicesConfig.getConfig("navansatt"), authService, cache)
    val legacyBrevService = LegacyBrevServiceImpl(brevmetadataService, safService, penClient, navansattService)
    val norg2Service = Norg2ServiceHttp(servicesConfig.getConfig("norg2"), cache)
    val p1Service = P1ServiceImpl(penClient)

    val brevService = BrevService(penClient, legacyBrevService)
    val brevdataService = BrevdataService(penClient, samhandlerService)
    val brevmalService = BrevmalService(brevbakerService, penClient, brevmetadataService)
    val fagsakService = FagsakService(penClient)
    val renderService = RenderService(brevbakerService)

    val dto2ApiService = Dto2ApiService(brevmalService, navansattService, norg2Service, samhandlerService)
    val brevredigeringFacade = BrevredigeringFacadeFactory.create(brevService, brevdataService, brevmalService, navansattService, p1Service, renderService)
    val externalAPIService = ExternalAPIService(servicesConfig.getConfig("externalApi"), brevredigeringFacade, brevmalService)

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
                penClient,
                pensjonPersonDataService,
                pdlService,
                krrService,
                brevbakerService,
                brevmetadataService,
                samhandlerService,
                navansattService
            )

            landRoute()
            brevmal(brevmalService)
            kodeverkRoute(fagsakService)
            sakRoute(
                brevService,
                brevmalService,
                krrService,
                pdlService,
                fagsakService,
                pensjonPersonDataService,
                safService,
                skjermingService,
                p1Service,
                pensjonRepresentasjonService,
                brevredigeringFacade,
                dto2ApiService,
            )
            brev(pdlService, fagsakService, brevredigeringFacade, dto2ApiService)
            samhandlerRoute(samhandlerService)
            meRoute(navansattService)

        }

        externalAPI(authConfig, externalAPIService)
    }
}