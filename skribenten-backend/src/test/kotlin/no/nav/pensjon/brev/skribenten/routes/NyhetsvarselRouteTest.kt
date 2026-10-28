package no.nav.pensjon.brev.skribenten.routes

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.request.get
import io.ktor.client.request.put
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
import no.nav.pensjon.brev.skribenten.BekreftNyhetsvarselHandler
import no.nav.pensjon.brev.skribenten.FjernFavorittHandler
import no.nav.pensjon.brev.skribenten.HentBekreftedeNyhetsvarslerHandler
import no.nav.pensjon.brev.skribenten.HentFavoritterHandler
import no.nav.pensjon.brev.skribenten.LeggTilFavorittHandler
import no.nav.pensjon.brev.skribenten.MockPrincipal
import no.nav.pensjon.brev.skribenten.SharedPostgres
import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.db.Transactional
import no.nav.pensjon.brev.skribenten.initADGroups
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brev.skribenten.services.FakeNavansattService
import no.nav.pensjon.brev.skribenten.services.NavansattService
import no.nav.pensjon.brev.skribenten.skribentenContenNegotiation
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicInteger

class NyhetsvarselRouteTest {
    init {
        initADGroups()
    }

    private val creds = BasicAuthCredentials("test", "123")
    private val identCounter = AtomicInteger(0)
    private fun nyNavIdent() = NavIdent("N${identCounter.incrementAndGet()}")

    @BeforeAll
    fun initDb() {
        SharedPostgres.subscribeAndEnsureDatabaseInitialized(this)
    }

    @AfterAll
    fun kansellerDbAvhengighet() {
        SharedPostgres.cancelSubscription(this)
    }

    private fun meTestApplication(
        navIdent: NavIdent,
        block: suspend ApplicationTestBuilder.(client: HttpClient) -> Unit,
    ): Unit = testApplication {
        application {
            skribentenContenNegotiation()

            install(Authentication) {
                basic("my domain") {
                    validate { if (it.name == creds.username && it.password == creds.password) MockPrincipal(navIdent, "Ansatt, Veldig Bra") else null }
                }
            }

            dependencies {
                provide { Transactional(SharedPostgres.database) }
                provide<NavansattService> { FakeNavansattService() }
                provide(HentFavoritterHandler::class)
                provide(LeggTilFavorittHandler::class)
                provide(FjernFavorittHandler::class)
                provide(HentBekreftedeNyhetsvarslerHandler::class)
                provide(BekreftNyhetsvarselHandler::class)
            }

            routing {
                authenticate("my domain") {
                    install(PrincipalInContext)
                    meRoute()
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
    fun `svarer med tom liste for bruker uten bekreftede nyhetsvarsler`() = meTestApplication(nyNavIdent()) { client ->
        val response = client.get("/me/bekreftedeNyhetsvarsler")

        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
        assertThat(response.bodyAsText()).isEqualTo("[]")
    }

    @Test
    fun `bekrefter nyhetsvarsel og henter det ut igjen som streng`() = meTestApplication(nyNavIdent()) { client ->
        val bekreft = client.put("/me/bekreftedeNyhetsvarsler/diff-toggles")

        assertThat(bekreft.status).isEqualTo(HttpStatusCode.NoContent)
        assertThat(client.get("/me/bekreftedeNyhetsvarsler").bodyAsText()).isEqualTo("""["diff-toggles"]""")
    }

    @Test
    fun `aa bekrefte samme nyhetsvarsel flere ganger gir fortsatt bare ett`() = meTestApplication(nyNavIdent()) { client ->
        assertThat(client.put("/me/bekreftedeNyhetsvarsler/diff-toggles").status).isEqualTo(HttpStatusCode.NoContent)
        assertThat(client.put("/me/bekreftedeNyhetsvarsler/diff-toggles").status).isEqualTo(HttpStatusCode.NoContent)

        assertThat(client.get("/me/bekreftedeNyhetsvarsler").bodyAsText()).isEqualTo("""["diff-toggles"]""")
    }

    @Test
    fun `brukere ser bare sine egne bekreftede nyhetsvarsler`() {
        meTestApplication(nyNavIdent()) { client ->
            client.put("/me/bekreftedeNyhetsvarsler/diff-toggles")
        }
        meTestApplication(nyNavIdent()) { client ->
            assertThat(client.get("/me/bekreftedeNyhetsvarsler").bodyAsText()).isEqualTo("[]")
        }
    }

    @Test
    fun `svarer med bad request for ugyldig nyhetsvarselId`() = meTestApplication(nyNavIdent()) { client ->
        assertThat(client.put("/me/bekreftedeNyhetsvarsler/Attestant_Diff").status).isEqualTo(HttpStatusCode.BadRequest)
        assertThat(client.put("/me/bekreftedeNyhetsvarsler/${"a".repeat(101)}").status).isEqualTo(HttpStatusCode.BadRequest)

        assertThat(client.get("/me/bekreftedeNyhetsvarsler").bodyAsText()).isEqualTo("[]")
    }

    @Test
    fun `svarer med unauthorized uten innlogget bruker`() = meTestApplication(nyNavIdent()) { _ ->
        val utenInnlogging = createClient { }

        assertThat(utenInnlogging.get("/me/bekreftedeNyhetsvarsler").status).isEqualTo(HttpStatusCode.Unauthorized)
        assertThat(utenInnlogging.put("/me/bekreftedeNyhetsvarsler/diff-toggles").status).isEqualTo(HttpStatusCode.Unauthorized)
    }
}
