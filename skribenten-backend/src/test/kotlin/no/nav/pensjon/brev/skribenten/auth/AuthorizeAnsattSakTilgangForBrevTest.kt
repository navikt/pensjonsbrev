package no.nav.pensjon.brev.skribenten.auth

import io.ktor.client.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.di.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import no.nav.pensjon.brev.skribenten.MockPrincipal
import no.nav.pensjon.brev.skribenten.SharedPostgres
import no.nav.pensjon.brev.skribenten.brevredigering.application.oppslag.HentBrevInfoHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.testBrevtilgang
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.common.Cache
import no.nav.pensjon.brev.skribenten.common.InMemoryCache
import no.nav.pensjon.brev.skribenten.fagsystem.Behandlingsnummer
import no.nav.pensjon.brev.skribenten.fagsystem.FagsakService
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.PenClient
import no.nav.pensjon.brev.skribenten.initADGroups
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brev.skribenten.model.Pdl
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brev.skribenten.routes.brevId
import no.nav.pensjon.brev.skribenten.services.PdlService
import no.nav.pensjon.brev.skribenten.services.PdlServiceStub
import no.nav.pensjon.brev.skribenten.services.PenClientStub
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Pid
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class AuthorizeAnsattSakTilgangForBrevTest {
    init {
        initADGroups()
    }

    @BeforeAll
    fun startDbOnce() {
        SharedPostgres.subscribeAndEnsureDatabaseInitialized(this)
    }

    @AfterAll
    fun kansellerDbAvhengighet() {
        SharedPostgres.cancelSubscription(this)
    }

    private val creds = BasicAuthCredentials("test", "123")
    private val principal = MockPrincipal(NavIdent("månedens ansatt"), "Ansatt, Veldig Bra")

    private val pdlService: PdlService = object : PdlServiceStub() {
        override suspend fun hentAdressebeskyttelse(ident: Pid, behandlingsnumre: List<Behandlingsnummer>): List<Pdl.Gradering> =
            emptyList()

        override suspend fun hentBrukerContext(ident: Pid, behandlingsnumre: List<Behandlingsnummer>): Pdl.PersonContext =
            throw NotImplementedError("Ikke i bruk i denne testen")
    }

    private val penClient: PenClient = object : PenClientStub() {
        override suspend fun hentSak(saksId: SaksId) = null
    }

    @Test
    fun `svarer med not found naar brevet ikke finnes`() = testApplication {
        application {
            install(Authentication) {
                basic("my domain") {
                    validate { if (it.name == creds.username && it.password == creds.password) principal else null }
                }
            }
            install(StatusPages) {
                exception<UnauthorizedException> { call, cause -> call.respond(HttpStatusCode.Unauthorized, cause.msg) }
            }
            dependencies {
                provide<PdlService> { pdlService }
                provide<PenClient> { penClient }
                provide(FagsakService::class)
                provide<Cache> { InMemoryCache() }
                provide<HentBrevInfoHandler> { HentBrevInfoHandler(testBrevtilgang()) }
            }

            routing {
                authenticate("my domain") {
                    install(PrincipalInContext)

                    route("/brev/{brevId}") {
                        install(AuthorizeAnsattSakTilgangForBrev)

                        get("/info") {
                            call.respond("brevId ${call.parameters.brevId().id} og sak ${call.attributes[SakKey].saksId.id}")
                        }
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

        val response = client.get("/brev/1337/info")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }
}
