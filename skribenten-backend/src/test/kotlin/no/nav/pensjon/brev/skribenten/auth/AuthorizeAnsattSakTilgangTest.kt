package no.nav.pensjon.brev.skribenten.auth

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.basic
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import io.ktor.server.util.getOrFail
import no.nav.pensjon.brev.skribenten.MockPrincipal
import no.nav.pensjon.brev.skribenten.initADGroups
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brev.skribenten.model.Pdl
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brev.skribenten.serialize.Sakstype
import no.nav.pensjon.brev.skribenten.services.PdlService
import no.nav.pensjon.brev.skribenten.services.PdlServiceException
import no.nav.pensjon.brev.skribenten.services.PdlServiceStub
import no.nav.pensjon.brev.skribenten.services.PenService
import no.nav.pensjon.brev.skribenten.services.PenServiceStub
import no.nav.pensjon.brev.skribenten.services.notYetStubbed
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.Month

private val navIdent = NavIdent("månedens ansatt")
private val testSak = Pen.SakSelection(
    saksId = SaksId(1337),
    foedselsnr = "12345",
    foedselsdato = LocalDate.of(1990, 1, 1),
    navn = Pen.SakSelection.Navn("a", "b", "c"),
    sakType = Sakstype("Sakstype123"),
)
private val sakVikafossen = Pen.SakSelection(
    saksId = SaksId(7007),
    foedselsnr = "007",
    foedselsdato = LocalDate.of(1920, Month.NOVEMBER, 11),
    navn = Pen.SakSelection.Navn("a", "b", "c"),
    sakType = Sakstype("Sakstype123"),
)

private val generellSak0001 = Pen.SakSelection(
    saksId = SaksId(7008),
    foedselsnr = "12345",
    foedselsdato = LocalDate.of(1920, Month.NOVEMBER, 11),
    navn = Pen.SakSelection.Navn("a", "b", "c"),
    sakType = Sakstype("GENRL"),
)

private val generellSak0002 = Pen.SakSelection(
    saksId = SaksId(7009),
    foedselsnr = "12345",
    foedselsdato = LocalDate.of(1920, Month.NOVEMBER, 11),
    navn = Pen.SakSelection.Navn("a", "b", "c"),
    sakType = Sakstype("GENRL"),
)


class AuthorizeAnsattSakTilgangTest {
    init {
        initADGroups()
    }

    private val creds = BasicAuthCredentials("test", "123")

    private fun lagPdlService(adressebeskyttelser: Map<Pair<String, Pdl.Behandlingsnummer?>, List<Pdl.Gradering>> = mapOf()) = object : PdlServiceStub() {
        override suspend fun hentAdressebeskyttelse(fnr: String, behandlingsnummer: Pdl.Behandlingsnummer?) =
            adressebeskyttelser[Pair(fnr, behandlingsnummer)]
                ?: notYetStubbed("Mangler stub for adressebeskyttelse for fødselsnummer $fnr og behandlingsnummer $behandlingsnummer")

        override suspend fun hentBrukerContext(fnr: String, behandlingsnummer: Pdl.Behandlingsnummer?): Pdl.PersonContext =
            notYetStubbed("Mangler stub for hentBrukerContext")

    }

    private val defaultPdlService = lagPdlService(mapOf(Pair(testSak.foedselsnr, behandlingsnummer()) to emptyList()))

    private val defaultPenService = object : PenServiceStub() {
        private val saker = listOf(testSak, sakVikafossen, generellSak0001, generellSak0002).associateBy { it.saksId.toString() }
        override suspend fun hentSak(saksId: String): Pen.SakSelection? = saker[saksId]
    }

    private fun basicAuthTestApplication(
        principal: MockPrincipal = MockPrincipal(navIdent, "Ansatt, Veldig Bra"),
        penService: PenService = defaultPenService,
        pdlService: PdlService = defaultPdlService,
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
            exception<PdlServiceException> { call, cause -> call.respond(status = cause.status, message = cause.message) }
        }
        routing {
            authenticate("my domain") {
                install(PrincipalInContext)

                route("/sak") {
                    install(AuthorizeAnsattSakTilgang) {
                        this.pdlService = pdlService
                        this.penService = penService
                    }

                    get("/noSak/{noSak}") { call.respond("ingen sak") }
                    get("/sakFromPlugin/{saksId}") {
                        val sak = call.attributes[SakKey]
                        call.respond(successResponse(sak.foedselsnr))
                    }
                    get("/{saksId}") {
                        val saksId = call.parameters.getOrFail("saksId")
                        call.respond(successResponse(saksId))
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
    fun `bruker faar tilgang til sak naar krav er oppfylt`() = basicAuthTestApplication(
        pdlService = lagPdlService(adressebeskyttelser = mapOf(Pair(testSak.foedselsnr, behandlingsnummer()) to emptyList()))
    ) { client ->
        val response = client.get("/sak/${testSak.saksId}")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(successResponse(testSak.saksId.toString()), response.bodyAsText())
    }

    @Test
    fun `krever saksId path param`() = basicAuthTestApplication { client ->
        val response = client.get("/sak/noSak/123")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `krever at ansatt har gruppe for FortroligAdresse`() = basicAuthTestApplication(
        pdlService = lagPdlService(adressebeskyttelser = mapOf(Pair(testSak.foedselsnr, behandlingsnummer()) to listOf(Pdl.Gradering.FORTROLIG)))
    ) { client ->
        val response = client.get("/sak/${testSak.saksId}")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `krever at ansatt har gruppe for StrengtFortroligAdresse`() = basicAuthTestApplication(
        pdlService = lagPdlService(adressebeskyttelser = mapOf(Pair(testSak.foedselsnr, behandlingsnummer()) to listOf(Pdl.Gradering.STRENGT_FORTROLIG)))
    ) { client ->
        val response = client.get("/sak/${testSak.saksId.id}")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `krever at ansatt har gruppe for StrengtFortrolig for utland`() = basicAuthTestApplication(
        pdlService = lagPdlService(adressebeskyttelser = mapOf(Pair(testSak.foedselsnr, behandlingsnummer()) to listOf(Pdl.Gradering.STRENGT_FORTROLIG_UTLAND)))
    ) { client ->
        val response = client.get("/sak/${testSak.saksId.id}")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `ansatt med gruppe for FortroligAdresse faar svar`() =
        basicAuthTestApplication(
            principal = MockPrincipal(navIdent, "Hemmelig ansatt", setOf(ADGroups.fortroligAdresse)),
            pdlService = lagPdlService(adressebeskyttelser = mapOf(Pair(testSak.foedselsnr, behandlingsnummer()) to listOf(Pdl.Gradering.FORTROLIG)))
        ) { client ->
            val response = client.get("/sak/${testSak.saksId.id}")
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals(successResponse(testSak.saksId.toString()), response.bodyAsText())
        }

    @Test
    fun `ansatt med gruppe for StrengtFortroligAdresse faar svar`() =
        basicAuthTestApplication(
            principal = MockPrincipal(navIdent, "Hemmelig ansatt", setOf(ADGroups.strengtFortroligAdresse)),
            pdlService = lagPdlService(adressebeskyttelser = mapOf(Pair(testSak.foedselsnr, behandlingsnummer()) to listOf(Pdl.Gradering.STRENGT_FORTROLIG)))
        ) { client ->
            val response = client.get("/sak/${testSak.saksId.id}")
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals(successResponse(testSak.saksId.toString()), response.bodyAsText())
        }

    @Test
    fun `ansatt med gruppe for StrengtFortroligUtland faar svar`() =
        basicAuthTestApplication(
            principal = MockPrincipal(navIdent, "Hemmelig ansatt", setOf(ADGroups.strengtFortroligAdresse)),
            pdlService = lagPdlService(adressebeskyttelser = mapOf(Pair(testSak.foedselsnr, behandlingsnummer()) to listOf(Pdl.Gradering.STRENGT_FORTROLIG_UTLAND)))
        ) { client ->
            val response = client.get("/sak/${testSak.saksId.id}")
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals(successResponse(testSak.saksId.toString()), response.bodyAsText())
        }

    @Test
    fun `svarer med feil fra hentSak`() = basicAuthTestApplication(penService = object : PenServiceStub() {
        override suspend fun hentSak(saksId: String) = null
    }) { client ->
        val response = client.get("/sak/${testSak.saksId.id}")
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals("Sak ikke funnet", response.bodyAsText())
    }

    @Test
    fun `svarer med internal server error om hentAdressebeskyttelse feiler`() = basicAuthTestApplication(
        pdlService = object : PdlServiceStub() {
            override suspend fun hentAdressebeskyttelse(fnr: String, behandlingsnummer: Pdl.Behandlingsnummer?) =
                throw PdlServiceException("En feil", HttpStatusCode.InternalServerError)
        }
    ) { client ->
        val response = client.get("/sak/${testSak.saksId.id}")
        assertEquals(HttpStatusCode.InternalServerError, response.status)
    }

    @Test
    fun `plugin lagrer sak som attribute tilgjengelig i route scope`() = basicAuthTestApplication(
        pdlService = lagPdlService(adressebeskyttelser = mapOf(Pair(testSak.foedselsnr, behandlingsnummer()) to emptyList()))
    ) { client ->
        val response = client.get("/sak/sakFromPlugin/${testSak.saksId.id}")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(successResponse(testSak.foedselsnr), response.bodyAsText())
    }

    @Test
    fun `svarer med not found for graderte brukere selv om saksbehandler mangler enhet vikafossen`() = basicAuthTestApplication(
        pdlService = lagPdlService(adressebeskyttelser = mapOf(Pair(sakVikafossen.foedselsnr, behandlingsnummer()) to listOf(Pdl.Gradering.STRENGT_FORTROLIG)))
    ) { client ->
        val response = client.get("/sak/${sakVikafossen.saksId.id}")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    private fun behandlingsnummer(): Pdl.Behandlingsnummer? = Pen.finnBehandlingsnummer(Sakstype("Sakstype123"))

    private fun successResponse(saksId: String) =
        "Fikk tilgang til den strengt bevoktede saken: $saksId"
}
