package no.nav.pensjon.brev.brevoppskrift

import com.auth0.jwk.JwkProvider
import com.auth0.jwk.JwkProviderBuilder
import io.ktor.server.application.Application
import io.ktor.server.config.getAs
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.plugins.di.provide
import io.ktor.server.plugins.di.DependencyRegistry
import no.nav.pensjon.brev.brevoppskrift.application.DraftHandler
import no.nav.pensjon.brev.brevoppskrift.application.PrJobHandler
import no.nav.pensjon.brev.brevoppskrift.integrasjoner.*
import no.nav.pensjon.brev.brevoppskrift.katalog.BuildSourceCatalogue
import no.nav.pensjon.brev.brevoppskrift.kotlin.KotlinDraftGenerator
import no.nav.pensjon.brev.brevoppskrift.lagring.DraftRepository
import no.nav.pensjon.brev.brevoppskrift.lagring.openDatabase
import no.nav.pensjon.brev.brevoppskrift.lagring.Transactional
import no.nav.pensjon.brev.brevoppskrift.lagring.SourceCatalogueRepository
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import org.jetbrains.exposed.v1.jdbc.Database
import com.zaxxer.hikari.HikariDataSource
import no.nav.pensjon.brev.brevoppskrift.preview.HtmlDraftPreview
import java.net.URI

fun Application.configureDependencies() {
    val config = environment.config.config("brevoppskrift").getAs<BrevoppskriftConfig>()
    configureDependencies(config, JwkProviderBuilder(URI(config.azure.jwksUri).toURL()).timeouts(5_000, 5_000).build())
}

internal fun Application.configureDependencies(
    config: BrevoppskriftConfig,
    jwks: JwkProvider,
    prService: PrService? = null,
    authoringServices: DependencyRegistry.() -> Unit = {
        provide<BuildSourceCatalogue>(BuildSourceCatalogue::persistent)
        provide(KotlinDraftGenerator::class)
        provide(HtmlDraftPreview::class)
    },
) {
    dependencies {
        provide { config }
        provide { jwks }
        provide<PrometheusMeterRegistry> { createMetrics() }
        cleanup<PrometheusMeterRegistry> { it.close() }
        provide { config: BrevoppskriftConfig, metrics: MeterRegistry -> openDatabase(config.database, metrics) }
        provide { dataSource: HikariDataSource -> Database.connect(dataSource) }
        provide(Transactional::class)
        provide(DraftRepository::class)
        provide(SourceCatalogueRepository::class)
        provide(PrJobRepository::class)
        authoringServices()
        provide<AiService?> { null }
        provide<NotificationService?> { null }
        provide<PrService?> { prService }
        provide(AiSuggestions::class)
        provide<WorkshopUrl> { config.integrations.webUrl }
        provide(DraftNotifications::class)
        provide(PrJobWorker::class)
        provide(PrJobHandler::class)
        provide(DraftHandler::class)
    }
}
