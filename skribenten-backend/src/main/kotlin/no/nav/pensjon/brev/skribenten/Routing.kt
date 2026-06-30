package no.nav.pensjon.brev.skribenten

import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.http.*
import io.ktor.openapi.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*
import io.ktor.server.routing.openapi.*
import no.nav.pensjon.brev.skribenten.auth.*
import no.nav.pensjon.brev.skribenten.brevbaker.BrevbakerServiceHttp
import no.nav.pensjon.brev.skribenten.brevbaker.RenderService
import no.nav.pensjon.brev.skribenten.brevredigering.application.BrevredigeringFacadeFactory
import no.nav.pensjon.brev.skribenten.common.Cache
import no.nav.pensjon.brev.skribenten.eksterntApi.ExternalAPIService
import no.nav.pensjon.brev.skribenten.eksterntApi.externalAPI
import no.nav.pensjon.brev.skribenten.fagsystem.BrevService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevdataService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.fagsystem.FagsakService
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.BrevmetadataServiceHttp
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.LegacyBrevServiceImpl
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.P1ServiceImpl
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.PentHttpClient
import no.nav.pensjon.brev.skribenten.openapi.JacksonReflectionJsonSchemaInference
import no.nav.pensjon.brev.skribenten.openapi.JacksonSchemaReflectionAdapter
import no.nav.pensjon.brev.skribenten.routes.*
import no.nav.pensjon.brev.skribenten.routes.samhandler.samhandlerRoute
import no.nav.pensjon.brev.skribenten.services.*

fun Application.configureRouting() {
    val cache: Cache by dependencies
    val authService: AuthService by dependencies
    val skribentenConfig: SkribentenConfig by dependencies
    val services = skribentenConfig.services

    val safService = SafServiceHttp(services.saf, authService)
    val penClient = PentHttpClient(services.pen, authService)
    val skjermingService = SkjermingServiceHttp(services.skjerming, authService, cache)
    val pensjonPersonDataService = PensjonPersonDataServiceImpl(services.pensjonPersondata, authService, cache = cache)
    val pensjonRepresentasjonService = PensjonRepresentasjonService(services.pensjonRepresentasjon, authService, cache)
    val pdlService = PdlServiceHttp(services.pdl, authService)
    val krrService = KrrService(services.krr, authService)
    val brevbakerService = BrevbakerServiceHttp(services.brevbaker, authService, cache)
    val brevmetadataService = BrevmetadataServiceHttp(services.brevmetadata)
    val samhandlerService = SamhandlerServiceHttp(services.samhandlerProxy, authService, cache)
    val navansattService = NavansattServiceHttp(services.navansatt, authService, cache)
    val legacyBrevService = LegacyBrevServiceImpl(brevmetadataService, safService, penClient, navansattService, pensjonPersonDataService)
    val norg2Service = Norg2ServiceHttp(services.norg2, cache)
    val p1Service = P1ServiceImpl(penClient)

    val brevService = BrevService(penClient, legacyBrevService)
    val brevdataService = BrevdataService(penClient, samhandlerService)
    val brevmalService = BrevmalService(brevbakerService, penClient, brevmetadataService)
    val fagsakService = FagsakService(penClient)
    val renderService = RenderService(brevbakerService)

    val dto2ApiService = Dto2ApiService(brevmalService, navansattService, norg2Service, samhandlerService)
    val brevredigeringFacade = BrevredigeringFacadeFactory.create(brevService, brevdataService, brevmalService, navansattService, p1Service, renderService)
    val externalAPIService = ExternalAPIService(services.externalApi, brevredigeringFacade, brevmalService, brevredigeringFacade)

    routing {
        healthRoute()

        swaggerUI("/swagger", "openapi/external-api.yaml")
        swaggerUI("/swagger-internal") {
            info = OpenApiInfo("Skribenten Internal API", "1.0")
            source = OpenApiDocSource.Routing(
                contentType = ContentType.Application.Json,
                schemaInference = JacksonReflectionJsonSchemaInference(
                    JacksonSchemaReflectionAdapter(ObjectMapper().skribentenServerJackson())
                ),
                routes = {
                    val excludedRoutePrefixes = listOf("/external/", "/swagger", "/isAlive", "/isReady")
                    routingRoot.descendants()
                        .filter { route ->
                            val path = route.path()
                            excludedRoutePrefixes.none { path.startsWith(it) }
                        }
                },
            )
            remotePath = "documentation.json"
        }

        authenticate(AUTHENTICATION_REALM_NAME) {
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

        externalAPI(externalAPIService, pdlService, fagsakService)
    }
}