package no.nav.pensjon.brev.skribenten

import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.Application
import io.ktor.server.config.getAs
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.plugins.di.provide
import kotlinx.coroutines.launch
import no.nav.pensjon.brev.skribenten.auth.ADGroups
import no.nav.pensjon.brev.skribenten.auth.AuthService
import no.nav.pensjon.brev.skribenten.auth.AzureADService
import no.nav.pensjon.brev.skribenten.brevbaker.BrevbakerServiceHttp
import no.nav.pensjon.brev.skribenten.brevbaker.RenderService
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.AttesterBrevHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.EndreDistribusjonstypeHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.EndreMottakerHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.EndreRedigertVedleggHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.DiffBrevHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.SendBrevHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.SlettBrevHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.HentEllerOpprettPdfHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.HentP1DataHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.LagreP1DataHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.HentBrevForAlleSakerHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.HentBrevForSakHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.HentBrevInfoHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.HentRedigerbareVedleggHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.HentRedigertVedleggHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.SlettRedigertVedleggHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.EndreValgteVedleggHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.FrigiReservasjonHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.GenererFoerstesideHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.TilbakestillBrevHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.HentAlltidValgbareVedleggHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.HentBrevAttesteringHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.HentBrevForAlleSakerService
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.HentBrevHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.OppdaterBrevHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.OpprettBrevHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.ReserverBrevHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.SetFoerstesideHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.VeksleKlarStatusHandler
import no.nav.pensjon.brev.skribenten.brevredigering.domain.AttesterBrevPolicy
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.brevredigering.domain.FerdigRedigertPolicy
import no.nav.pensjon.brev.skribenten.brevredigering.domain.OpprettBrevPolicy
import no.nav.pensjon.brev.skribenten.brevredigering.domain.SendBrevPolicy
import no.nav.pensjon.brev.skribenten.brevredigering.domain.RedigerBrevPolicy
import no.nav.pensjon.brev.skribenten.common.Cache
import no.nav.pensjon.brev.skribenten.common.cacheFactory
import no.nav.pensjon.brev.skribenten.db.dataSourceFactory
import no.nav.pensjon.brev.skribenten.db.databaseReady
import no.nav.pensjon.brev.skribenten.db.kryptering.KrypteringService
import no.nav.pensjon.brev.skribenten.eksterntApi.ExternalAPIService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevdataService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.fagsystem.FagsakService
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.BrevmetadataServiceHttp
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.LegacyBrevServiceImpl
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.PentHttpClient
import no.nav.pensjon.brev.skribenten.foerstesidegenerator.FoerstesidegeneratorClient
import no.nav.pensjon.brev.skribenten.services.Dto2ApiService
import no.nav.pensjon.brev.skribenten.services.KrrService
import no.nav.pensjon.brev.skribenten.services.NaisLeaderService
import no.nav.pensjon.brev.skribenten.services.NavansattServiceHttp
import no.nav.pensjon.brev.skribenten.services.Norg2ServiceHttp
import no.nav.pensjon.brev.skribenten.services.PdlServiceHttp
import no.nav.pensjon.brev.skribenten.services.PensjonPersonDataServiceImpl
import no.nav.pensjon.brev.skribenten.services.PensjonRepresentasjonService
import no.nav.pensjon.brev.skribenten.services.SafServiceHttp
import no.nav.pensjon.brev.skribenten.services.SamhandlerServiceHttp
import no.nav.pensjon.brev.skribenten.services.SkjermingServiceHttp
import org.jetbrains.exposed.v1.jdbc.Database

fun Application.configureDependencies() {
    val skribentenConfig = environment.config.config("skribenten").getAs<SkribentenConfig>()
    ADGroups.init(skribentenConfig.groups)
    KrypteringService.init(skribentenConfig.krypteringsnoekkel)

    dependencies {
        provide { skribentenConfig }

        provide<Cache>(::cacheFactory)
        provide<AuthService>(AzureADService::class)
        provide<HikariDataSource>(::dataSourceFactory)
        provide { datasource: HikariDataSource ->
            Database.connect(datasource).also { databaseReady.set(true) }
        }

        provide<FeatureToggleService>(UnleashService::class)

        provide(NaisLeaderService::class)

        provide(SafServiceHttp::class)
        provide(PentHttpClient::class)
        provide(SkjermingServiceHttp::class)
        provide(PensjonPersonDataServiceImpl::class)
        provide(PensjonRepresentasjonService::class)
        provide(PdlServiceHttp::class)
        provide(KrrService::class)
        provide(BrevbakerServiceHttp::class)
        provide(BrevmetadataServiceHttp::class)
        provide(FoerstesidegeneratorClient::class)
        provide(SamhandlerServiceHttp::class)
        provide(NavansattServiceHttp::class)
        provide(LegacyBrevServiceImpl::class)
        provide(Norg2ServiceHttp::class)

        provide(BrevService::class)
        provide(BrevdataService::class)
        provide(BrevmalService::class)
        provide(FagsakService::class)
        provide(RenderService::class)

        provide(Dto2ApiService::class)
        provide(ExternalAPIService::class)

        provide(AttesterBrevPolicy::class)
        provide(BrevreservasjonPolicy::class)
        provide(FerdigRedigertPolicy::class)
        provide(OpprettBrevPolicy::class)
        provide(RedigerBrevPolicy::class)
        provide(SendBrevPolicy::class)

        provide(AttesterBrevHandler::class)
        provide(DiffBrevHandler::class)
        provide(EndreDistribusjonstypeHandler::class)
        provide(EndreMottakerHandler::class)
        provide(EndreRedigertVedleggHandler::class)
        provide(EndreValgteVedleggHandler::class)
        provide(FjernFavorittHandler::class)
        provide(FrigiReservasjonHandler::class)
        provide(GenererFoerstesideHandler::class)
        provide(HentAlltidValgbareVedleggHandler::class)
        provide(HentBrevAttesteringHandler::class)
        provide<HentBrevForAlleSakerService>(HentBrevForAlleSakerHandler::class)
        provide(HentBrevForSakHandler::class)
        provide(HentBrevHandler::class)
        provide(HentBrevInfoHandler::class)
        provide(HentEllerOpprettPdfHandler::class)
        provide(HentFavoritterHandler::class)
        provide(HentP1DataHandler::class)
        provide(HentRedigerbareVedleggHandler::class)
        provide(HentRedigertVedleggHandler::class)
        provide(LagreP1DataHandler::class)
        provide(LeggTilFavorittHandler::class)
        provide(OppdaterBrevHandler::class)
        provide(OpprettBrevHandler::class)
        provide(ReserverBrevHandler::class)
        provide(SendBrevHandler::class)
        provide(SetFoerstesideHandler::class)
        provide(SlettBrevHandler::class)
        provide(SlettRedigertVedleggHandler::class)
        provide(TilbakestillBrevHandler::class)
        provide(VeksleKlarStatusHandler::class)
    }

    launch { Features.init(dependencies.resolve()) }
}