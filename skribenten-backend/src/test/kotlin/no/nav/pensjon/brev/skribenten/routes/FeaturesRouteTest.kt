package no.nav.pensjon.brev.skribenten.routes

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.basic
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.routing.routing
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import no.nav.pensjon.brev.skribenten.FeatureToggleService
import no.nav.pensjon.brev.skribenten.MockPrincipal
import no.nav.pensjon.brev.skribenten.UnleashToggle
import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.initADGroups
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brev.skribenten.skribentenContenNegotiation
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FeaturesRouteTest {
    init {
        initADGroups()
    }

    private val navIdent = NavIdent("månedens ansatt")
    private val creds = BasicAuthCredentials("test", "123")

    // Stub som husker hvilket UnleashToggle den ble spurt om, slik at vi kan bevise at ruten
    // videresender path-parameteret uendret til FeatureToggleService (uten selv å legge på
    // pensjonsbrev.skribenten.-prefikset - det er UnleashServices ansvar).
    private class StubFeatureToggleService(private val enabledToggles: Set<String> = emptySet()) : FeatureToggleService {
        val spurteOm = mutableListOf<UnleashToggle>()

        override suspend fun isEnabled(toggle: UnleashToggle): Boolean {
            spurteOm.add(toggle)
            return toggle.name in enabledToggles
        }

        override fun close() {}
    }

    private fun featuresTestApplication(
        featureToggleService: StubFeatureToggleService = StubFeatureToggleService(),
        principal: MockPrincipal = MockPrincipal(navIdent, "Ansatt, Veldig Bra"),
        block: suspend ApplicationTestBuilder.(client: HttpClient) -> Unit,
    ): Unit = testApplication {
        application {
            skribentenContenNegotiation()

            install(Authentication) {
                basic("my domain") {
                    validate { if (it.name == creds.username && it.password == creds.password) principal else null }
                }
            }

            dependencies {
                provide<FeatureToggleService> { featureToggleService }
            }

            routing {
                authenticate("my domain") {
                    install(PrincipalInContext)
                    featureToggleRoute()
                }
            }
        }

        val client = createClient {
            install(Auth) {
                basic {
                    credentials { creds }
                    sendWithoutRequest { true }
                }
            }
        }

        block(client)
    }

    @Test
    fun `svarer med enabled true naar toggle er paa`() = featuresTestApplication(
        featureToggleService = StubFeatureToggleService(enabledToggles = setOf("minToggle"))
    ) { client ->
        val response: HttpResponse = client.get("/features/minToggle")

        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
        assertThat(response.bodyAsText()).isEqualTo("""{"enabled":true}""")
    }

    @Test
    fun `svarer med enabled false naar toggle er av`() = featuresTestApplication(
        featureToggleService = StubFeatureToggleService(enabledToggles = emptySet())
    ) { client ->
        val response: HttpResponse = client.get("/features/minToggle")

        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
        assertThat(response.bodyAsText()).isEqualTo("""{"enabled":false}""")
    }

    @Test
    fun `sender featureName uendret videre til FeatureToggleService uten prefiks`() {
        val stub = StubFeatureToggleService()
        featuresTestApplication(featureToggleService = stub) { client ->
            client.get("/features/minLokaleToggleNavn")

            assertThat(stub.spurteOm).containsExactly(UnleashToggle("minLokaleToggleNavn"))
        }
    }

    @Test
    fun `svarer med bad request naar featureName er tomt`() = featuresTestApplication { client ->
        val response = client.get("/features/%20")

        assertThat(response.status).isEqualTo(HttpStatusCode.BadRequest)
    }

    @Test
    fun `svarer med unauthorized uten gyldige credentials`() = featuresTestApplication(
        principal = MockPrincipal(navIdent, "Ansatt, Veldig Bra")
    ) { _ ->
        val client = createClient { }
        val response = client.get("/features/minToggle")

        assertThat(response.status).isEqualTo(HttpStatusCode.Unauthorized)
    }
}
