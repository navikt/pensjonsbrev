package no.nav.pensjon.brev.brevoppskrift.integrasjoner

import com.auth0.jwk.JwkProvider
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.server.application.Application
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.testing.testApplication
import kotlinx.coroutines.*
import no.nav.pensjon.brev.brevoppskrift.*
import no.nav.pensjon.brev.brevoppskrift.auth.*
import no.nav.pensjon.brev.brevoppskrift.model.DraftLanguage
import no.nav.pensjon.brev.brevoppskrift.model.DraftId
import no.nav.pensjon.brev.brevoppskrift.model.DraftRevision
import no.nav.pensjon.brev.brevoppskrift.model.exampleDraft
import no.nav.pensjon.brev.brevoppskrift.lagring.DatabaseConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.parallel.Isolated
import java.util.UUID

@Isolated
class IntegrationLifecycleTest {
    @Test
    fun `optional adapters activate independently and DI closes services but not the injected engine`(): Unit = runBlocking {
        TestPostgres.standalone().use { postgres ->
            postgres.start()
            var calls = 0
            MockEngine { calls++; respond("ok") }.use { engine ->
                for (config in listOf(
                    IntegrationsConfig(),
                    IntegrationsConfig(vertex = ExternalServicesTest.vertex),
                    IntegrationsConfig(slackWebhookUrl = ExternalServicesTest.webhook),
                    IntegrationsConfig(vertex = ExternalServicesTest.vertex, slackWebhookUrl = ExternalServicesTest.webhook),
                    IntegrationsConfig(github = GitHubPrTest.appConfig),
                    IntegrationsConfig(vertex = ExternalServicesTest.vertex, slackWebhookUrl = ExternalServicesTest.webhook, github = GitHubPrTest.appConfig),
                )) {
                    var ai: AiService? = null
                    var slack: NotificationService? = null
                    var github: PrService? = null
                    var ownedEngine: HttpClientEngine? = null
                    testApplication {
                        lateinit var app: Application
                        application {
                            app = this
                            testModule(
                                AzureConfig("https://issuer.invalid", EntraAudience("backend"), "https://issuer.invalid/jwks", emptyList(), EntraGroupId("editors")),
                                JwkProvider { error("No authentication in lifecycle test") },
                                DatabaseConfig(postgres.jdbcUrl, postgres.username, postgres.password),
                                integrations = config,
                                integrationEngine = engine,
                            )
                        }
                        startApplication()
                        ai = app.dependencies.resolve<AiService?>()
                        slack = app.dependencies.resolve<NotificationService?>()
                        assertThat(ai != null).isEqualTo(config.vertex.projectId != null)
                        assertThat(slack != null).isEqualTo(config.slackWebhookUrl != null)
                        assertThat(app.dependencies.resolve<AiSuggestions>().enabled).isEqualTo(ai != null)
                        assertThat(app.dependencies.resolve<DraftNotifications>().enabled).isEqualTo(slack != null)
                        github = app.dependencies.resolve<PrService?>()
                        assertThat(github != null).isEqualTo(config.github.enabled)
                        if (ai != null && slack != null) ownedEngine = app.dependencies.resolve<HttpClientEngine>()
                        assertThat(calls).isZero()
                    }
                    ai?.let { assertThat(runCatching { it.suggest(AiRequest("synthetic", DraftLanguage.NB), false) }.exceptionOrNull()).isInstanceOf(CancellationException::class.java) }
                    slack?.let { assertThat(runCatching { it.send(Notification("synthetic", "synthetic")) }.exceptionOrNull()).isInstanceOf(CancellationException::class.java) }
                    github?.let {
                        val ticket = PrTicket(PrJobId(UUID.randomUUID()), DraftId(UUID.randomUUID()),
                            DraftRevision(1), exampleDraft(), PrLeaseToken(UUID.randomUUID()), 1)
                        assertThat(runCatching { it.create(ticket) }.exceptionOrNull()).isInstanceOf(CancellationException::class.java)
                    }
                    assertThat(calls).isZero()
                    assertThat(engine.coroutineContext[Job]?.isActive).isTrue()
                    ownedEngine?.let { assertThat(it.coroutineContext[Job]?.isActive).isFalse() }
                }
            }
        }
    }
}
