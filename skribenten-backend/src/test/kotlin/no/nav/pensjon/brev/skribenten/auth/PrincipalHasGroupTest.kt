package no.nav.pensjon.brev.skribenten.auth

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.request.*
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.basic
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.testing.*
import no.nav.pensjon.brev.skribenten.MockPrincipal
import no.nav.pensjon.brev.skribenten.initADGroups
import no.nav.pensjon.brev.skribenten.model.NavIdent
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.jupiter.api.Test

class PrincipalHasGroupTest {
    init { initADGroups() }

    private val navIdent = NavIdent("månedens ansatt")
    private val creds = BasicAuthCredentials("test", "123")

    private fun basicAuthTestApplication(
        principal: MockPrincipal = MockPrincipal(navIdent, "Ansatt, Veldig Bra"),
        block: suspend ApplicationTestBuilder.(client: HttpClient) -> Unit,
    ): Unit = testApplication {
        install(Authentication) {
            basic("my domain") {
                validate {
                    if (it.name == creds.username && it.password == creds.password) {
                        principal
                    } else null
                }
            }
        }
        install(StatusPages) {
            exception<UnauthorizedException> { call, cause -> call.respond(HttpStatusCode.Unauthorized, cause.msg) }
        }
        routing {
            authenticate("my domain") {
                install(PrincipalInContext)

                route("/singleGroupSet") {
                    install(PrincipalHasGroup) {
                        requireOneOf(setOf(ADGroups.pensjonSaksbehandler, ADGroups.attestant))
                    }
                    get {
                        call.respond(HttpStatusCode.OK, "Single group set passed")
                    }
                }

                route("/multipleGroupSets") {
                    install(PrincipalHasGroup) {
                        requireOneOf(setOf(ADGroups.pensjonSaksbehandler, ADGroups.attestant))
                        requireOneOf(setOf(ADGroups.pensjonUtland))
                    }
                    get {
                        call.respond(HttpStatusCode.OK, "Multiple group sets passed")
                    }
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
    fun `faar tilgang til singleGroupSet med en av gruppene`() = basicAuthTestApplication(
        MockPrincipal(
            navIdent,
            "Ansatt, Veldig Bra",
            setOf(ADGroups.pensjonSaksbehandler)
        )
    ) { client ->
        val result = client.get("/singleGroupSet")

        assertThat(result.status, equalTo(HttpStatusCode.OK))
    }

    @Test
    fun `faar tilgang til singleGroupSet med begge gruppene`() = basicAuthTestApplication(
        MockPrincipal(
            navIdent,
            "Ansatt, Veldig Bra",
            setOf(ADGroups.pensjonSaksbehandler, ADGroups.attestant)
        )
    ) { client ->
        val result = client.get("/singleGroupSet")

        assertThat(result.status, equalTo(HttpStatusCode.OK))
    }

    @Test
    fun `faar ikke tilgang til singleGroupSet uten noen av gruppene`() = basicAuthTestApplication(
        MockPrincipal(navIdent, "Ansatt, Veldig Bra")
    ) { client ->
        val result = client.get("/singleGroupSet")

        assertThat(result.status, equalTo(HttpStatusCode.Forbidden))
    }

    @Test
    fun `faar tilgang til multipleGroupSets med en av gruppene fra hvert sett`() = basicAuthTestApplication(
        MockPrincipal(
            navIdent,
            "Ansatt, Veldig Bra",
            setOf(ADGroups.pensjonSaksbehandler, ADGroups.pensjonUtland)
        )
    ) { client ->
        val result = client.get("/multipleGroupSets")

        assertThat(result.status, equalTo(HttpStatusCode.OK))
    }

    @Test
    fun `faar ikke tilgang til multipleGroupSets med kun en gruppe fra ett sett`() = basicAuthTestApplication(
        MockPrincipal(navIdent, "Ansatt, Veldig Bra", setOf(ADGroups.pensjonSaksbehandler))
    ) { client ->
        val result = client.get("/multipleGroupSets")

        assertThat(result.status, equalTo(HttpStatusCode.Forbidden))
    }
}