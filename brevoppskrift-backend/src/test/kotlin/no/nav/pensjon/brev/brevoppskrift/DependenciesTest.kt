package no.nav.pensjon.brev.brevoppskrift

import no.nav.pensjon.brev.brevoppskrift.lagring.SourceCatalogueTable
import org.jetbrains.exposed.v1.jdbc.selectAll
import com.auth0.jwk.JwkProvider
import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigResolveOptions
import com.zaxxer.hikari.HikariDataSource
import no.nav.pensjon.brev.brevoppskrift.application.DraftHandler
import no.nav.pensjon.brev.brevoppskrift.application.PrJobHandler
import no.nav.pensjon.brev.brevoppskrift.lagring.Transactional
import org.jetbrains.exposed.v1.jdbc.Database
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.config.HoconApplicationConfig
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.testing.testApplication
import no.nav.pensjon.brev.brevoppskrift.api.DraftGenerator
import no.nav.pensjon.brev.brevoppskrift.api.DraftPreview
import no.nav.pensjon.brev.brevoppskrift.api.SourceCatalogue
import no.nav.pensjon.brev.brevoppskrift.auth.AzureSettings
import no.nav.pensjon.brev.brevoppskrift.lagring.DatabaseSettings
import no.nav.pensjon.brev.brevoppskrift.katalog.BuildSourceCatalogue
import no.nav.pensjon.brev.brevoppskrift.kotlin.KotlinDraftGenerator
import no.nav.pensjon.brev.brevoppskrift.preview.HtmlDraftPreview
import no.nav.pensjon.brev.brevoppskrift.integrasjoner.*
import no.nav.pensjon.brev.brevoppskrift.lagring.DraftRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.parallel.Isolated

@Isolated
class DependenciesTest {
    @Test
    fun `production module resolves its graph and closes resources on shutdown`() {
        TestPostgres.standalone().use { postgres ->
            postgres.start()
            lateinit var dataSource: HikariDataSource
            lateinit var notifications: DraftNotifications
            lateinit var prWorker: PrJobWorker
            lateinit var metrics: PrometheusMeterRegistry
            testApplication {
                environment {
                    config = HoconApplicationConfig(
                        ConfigFactory.parseResources("application.conf")
                            .withFallback(ConfigFactory.parseMap(mapOf(
                                "AZURE_OPENID_CONFIG_ISSUER" to "https://issuer.invalid",
                                "AZURE_APP_CLIENT_ID" to "backend",
                                "AZURE_OPENID_CONFIG_JWKS_URI" to "https://issuer.invalid/jwks",
                                "AZURE_APP_PRE_AUTHORIZED_APPS" to """[{"name":"dev-gcp:pensjonsbrev:brevoppskrift-web","clientId":"bff"}]""",
                                "EDITOR_GROUP_ID" to "editors",
                                "DB_JDBC_URL" to postgres.jdbcUrl,
                                "DB_USERNAME" to postgres.username,
                                "DB_PASSWORD" to postgres.password,
                            )))
                            .resolve(ConfigResolveOptions.defaults().setUseSystemEnvironment(false)),
                    )
                }
                lateinit var app: Application
                application { app = this }
                startApplication()
                dataSource = app.dependencies.resolve()
                metrics = app.dependencies.resolve()
                assertThat(metrics.find("hikaricp.connections.max").tag("pool", "brevoppskrift").gauge()).isNotNull()
                assertThat(app.dependencies.resolve<Database>()).isNotNull()
                assertThat(app.dependencies.resolve<Transactional>()).isNotNull()
                assertThat(app.dependencies.resolve<Transactional>().transaction { SourceCatalogueTable.selectAll().count() }).isGreaterThan(0)
                assertThat(app.dependencies.resolve<DraftHandler>()).isNotNull()
                prWorker = app.dependencies.resolve()
                assertThat(app.dependencies.resolve<PrJobRepository>()).isNotNull()
                assertThat(app.dependencies.resolve<PrJobHandler>().enabled).isFalse()
                assertThat(app.dependencies.resolve<PrService?>()).isNull()
                val config = app.dependencies.resolve<BrevoppskriftConfig>()
                assertThat(app.dependencies.resolve<AzureSettings>()).isSameAs(config)
                assertThat(app.dependencies.resolve<DatabaseSettings>()).isSameAs(config)
                assertThat(app.dependencies.resolve<NotificationSettings>()).isSameAs(config)
                assertThat(config.azure.preAuthApps.single().clientId.value).isEqualTo("bff")
                assertThat(app.dependencies.resolve<JwkProvider>()).isNotNull()
                assertThat(app.dependencies.resolve<DraftRepository>().ready()).isTrue()
                val catalogue = app.dependencies.resolve<SourceCatalogue>()
                assertThat(catalogue).isSameAs(app.dependencies.resolve<BuildSourceCatalogue>())
                assertThat(catalogue.list()).isNotEmpty()
                assertThat(app.dependencies.resolve<DraftGenerator>()).isSameAs(app.dependencies.resolve<KotlinDraftGenerator>())
                assertThat(app.dependencies.resolve<DraftPreview>()).isSameAs(app.dependencies.resolve<HtmlDraftPreview>())
                notifications = app.dependencies.resolve()
                assertThat(app.dependencies.resolve<AiService?>()).isNull()
                assertThat(app.dependencies.resolve<NotificationService?>()).isNull()
                assertThat(app.dependencies.resolve<AiSuggestions>().enabled).isFalse()
                assertThat(notifications.enabled).isFalse()
                assertThat(client.get("/isReady").status).isEqualTo(HttpStatusCode.OK)
                assertThat(client.get("/api/v1/meg").status).isEqualTo(HttpStatusCode.Unauthorized)
            }
            assertThat(dataSource.isClosed).isTrue()
            assertThat(notifications.isActive).isFalse()
            assertThat(prWorker.isActive).isFalse()
            assertThat(metrics.isClosed).isTrue()
        }
    }
}
