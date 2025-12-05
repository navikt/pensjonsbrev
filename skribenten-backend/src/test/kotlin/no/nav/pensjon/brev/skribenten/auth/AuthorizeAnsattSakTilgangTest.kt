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
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brev.skribenten.model.Pdl
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.model.Pen.SakType.ALDER
import no.nav.pensjon.brev.skribenten.model.Pen.SakType.GENRL
import no.nav.pensjon.brev.skribenten.services.PdlService
import no.nav.pensjon.brev.skribenten.services.PdlServiceStub
import no.nav.pensjon.brev.skribenten.services.PenService
import no.nav.pensjon.brev.skribenten.services.PenServiceStub
import no.nav.pensjon.brev.skribenten.services.ServiceResult
import no.nav.pensjon.brev.skribenten.services.notYetStubbed
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.Month

private val navIdent = NavIdent("månedens ansatt")
private val testSak = Pen.SakSelection(
    1337,
    "12345",
    LocalDate.of(1990, 1, 1),
    Pen.SakSelection.Navn("a", "b", "c"),
    ALDER,
    "en veldig bra enhet"
)
private val sakVikafossen = Pen.SakSelection(
    7007,
    "007",
    LocalDate.of(1920, Month.NOVEMBER, 11),
    Pen.SakSelection.Navn("a", "b", "c"),
    ALDER,
    "vikafossen"
)

private val generellSak0001 = Pen.SakSelection(
    7008,
    "12345",
    LocalDate.of(1920, Month.NOVEMBER, 11),
    Pen.SakSelection.Navn("a", "b", "c"),
    GENRL,
    "0001"
)

private val generellSak0002 = Pen.SakSelection(
    7009,
    "12345",
    LocalDate.of(1920, Month.NOVEMBER, 11),
    Pen.SakSelection.Navn("a", "b", "c"),
    GENRL,
    "0002"
)


class AuthorizeAnsattSakTilgangTest {
    init {
        initADGroups()
    }

    private val creds = BasicAuthCredentials("test", "123")

    private fun lagPdlService(adressebeskyttelser: Map<Pair<String, Pdl.Behandlingsnummer?>, ServiceResult<List<Pdl.Gradering>>> = mapOf()) = object : PdlServiceStub() {
        override suspend fun hentAdressebeskyttelse(fnr: String, behandlingsnummer: Pdl.Behandlingsnummer?) =
            adressebeskyttelser[Pair(fnr, behandlingsnummer)]
                ?: notYetStubbed("Mangler stub for adressebeskyttelse for fødselsnummer $fnr og behandlingsnummer $behandlingsnummer")

        override suspend fun hentBrukerContext(fnr: String, behandlingsnummer: Pdl.Behandlingsnummer?): ServiceResult<Pdl.PersonContext> =
            notYetStubbed("Mangler stub for hentBrukerContext")

    }

    private val defaultPdlService = lagPdlService(mapOf(Pair(testSak.foedselsnr, ALDER.behandlingsnummer) to ServiceResult.Ok(emptyList())))

    private val defaultPenService = object : PenServiceStub() {
        override suspend fun hentSak(saksId: String): ServiceResult<Pen.SakSelection> =
            mapOf(
                "${testSak.saksId}" to ServiceResult.Ok(testSak),
                "${sakVikafossen.saksId}" to ServiceResult.Ok(sakVikafossen),
                "${generellSak0001.saksId}" to ServiceResult.Ok(generellSak0001),
                "${generellSak0002.saksId}" to ServiceResult.Ok(generellSak0002)
            )[saksId] ?: ServiceResult.Error("Sak finnes ikke", HttpStatusCode.NotFound)

        override suspend fun hentP1VedleggData(
            saksId: Long,
            spraak: LanguageCode
        ): ServiceResult<Api.GeneriskBrevdata> {
            notYetStubbed()
        }
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
        pdlService = lagPdlService(adressebeskyttelser = mapOf(Pair(testSak.foedselsnr, ALDER.behandlingsnummer) to ServiceResult.Ok(emptyList())))
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
        pdlService = lagPdlService(adressebeskyttelser = mapOf(Pair(testSak.foedselsnr, ALDER.behandlingsnummer) to ServiceResult.Ok(listOf(Pdl.Gradering.FORTROLIG))))
    ) { client ->
        val response = client.get("/sak/${testSak.saksId}")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `krever at ansatt har gruppe for StrengtFortroligAdresse`() = basicAuthTestApplication(
        pdlService = lagPdlService(adressebeskyttelser = mapOf(Pair(testSak.foedselsnr, ALDER.behandlingsnummer) to ServiceResult.Ok(listOf(Pdl.Gradering.STRENGT_FORTROLIG))))
    ) { client ->
        val response = client.get("/sak/${testSak.saksId}")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `krever at ansatt har gruppe for StrengtFortrolig for utland`() = basicAuthTestApplication(
        pdlService = lagPdlService(adressebeskyttelser = mapOf(Pair(testSak.foedselsnr, ALDER.behandlingsnummer) to ServiceResult.Ok(listOf(Pdl.Gradering.STRENGT_FORTROLIG_UTLAND))))
    ) { client ->
        val response = client.get("/sak/${testSak.saksId}")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `ansatt med gruppe for FortroligAdresse faar svar`() =
        basicAuthTestApplication(
            principal = MockPrincipal(navIdent, "Hemmelig ansatt", setOf(ADGroups.fortroligAdresse)),
            pdlService = lagPdlService(adressebeskyttelser = mapOf(Pair(testSak.foedselsnr, ALDER.behandlingsnummer) to ServiceResult.Ok(listOf(Pdl.Gradering.FORTROLIG))))
        ) { client ->
            val response = client.get("/sak/${testSak.saksId}")
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals(successResponse(testSak.saksId.toString()), response.bodyAsText())
        }

    @Test
    fun `ansatt med gruppe for StrengtFortroligAdresse faar svar`() =
        basicAuthTestApplication(
            principal = MockPrincipal(navIdent, "Hemmelig ansatt", setOf(ADGroups.strengtFortroligAdresse)),
            pdlService = lagPdlService(adressebeskyttelser = mapOf(Pair(testSak.foedselsnr, ALDER.behandlingsnummer) to ServiceResult.Ok(listOf(Pdl.Gradering.STRENGT_FORTROLIG))))
        ) { client ->
            val response = client.get("/sak/${testSak.saksId}")
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals(successResponse(testSak.saksId.toString()), response.bodyAsText())
        }

    @Test
    fun `ansatt med gruppe for StrengtFortroligUtland faar svar`() =
        basicAuthTestApplication(
            principal = MockPrincipal(navIdent, "Hemmelig ansatt", setOf(ADGroups.strengtFortroligAdresse)),
            pdlService = lagPdlService(adressebeskyttelser = mapOf(Pair(testSak.foedselsnr, ALDER.behandlingsnummer) to ServiceResult.Ok(listOf(Pdl.Gradering.STRENGT_FORTROLIG_UTLAND))))
        ) { client ->
            val response = client.get("/sak/${testSak.saksId}")
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals(successResponse(testSak.saksId.toString()), response.bodyAsText())
        }

    @Test
    fun `svarer med feil fra hentSak`() = basicAuthTestApplication(penService = object : PenServiceStub() {
        override suspend fun hentSak(saksId: String) = ServiceResult.Error<Pen.SakSelection>("Sak finnes ikke", HttpStatusCode.NotFound)
    }) { client ->
        val response = client.get("/sak/${testSak.saksId}")
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals("Sak finnes ikke", response.bodyAsText())
    }

    @Test
    fun `svarer med internal server error om hentAdressebeskyttelse feiler`() = basicAuthTestApplication(
        pdlService = lagPdlService(adressebeskyttelser = mapOf(Pair(testSak.foedselsnr, ALDER.behandlingsnummer) to ServiceResult.Error(
            "En feil",
            HttpStatusCode.InternalServerError
        )))
    ) { client ->
        val response = client.get("/sak/${testSak.saksId}")
        assertEquals(HttpStatusCode.InternalServerError, response.status)
        assertEquals("En feil oppstod ved validering av tilgang til sak", response.bodyAsText())
    }

    @Test
    fun `plugin lagrer sak som attribute tilgjengelig i route scope`() = basicAuthTestApplication(
        pdlService = lagPdlService(adressebeskyttelser = mapOf(Pair(testSak.foedselsnr, ALDER.behandlingsnummer) to ServiceResult.Ok(emptyList())))
    ) { client ->
        val response = client.get("/sak/sakFromPlugin/${testSak.saksId}")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(successResponse(testSak.foedselsnr), response.bodyAsText())
    }

    @Test
    fun `svarer med not found for graderte brukere selv om saksbehandler mangler enhet vikafossen`() = basicAuthTestApplication(
        pdlService = lagPdlService(adressebeskyttelser = mapOf(Pair(sakVikafossen.foedselsnr, ALDER.behandlingsnummer) to ServiceResult.Ok(listOf(Pdl.Gradering.STRENGT_FORTROLIG))))
    ) { client ->
        val response = client.get("/sak/${sakVikafossen.saksId}")
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertThat(response.bodyAsText()).isNullOrEmpty()
    }

    @Test
    fun `forbidden fra PDL resulterer i not found svar`() = basicAuthTestApplication(
        pdlService = lagPdlService(adressebeskyttelser = mapOf(Pair(sakVikafossen.foedselsnr, ALDER.behandlingsnummer) to ServiceResult.Error(
            "Ikke tilgang til person",
            HttpStatusCode.Forbidden
        )))
    ) { client ->
        val response = client.get("/sak/${sakVikafossen.saksId}")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `unauthorized fra PDL resulterer i internal server error svar`() = basicAuthTestApplication(
        pdlService = lagPdlService(adressebeskyttelser = mapOf(Pair(sakVikafossen.foedselsnr, ALDER.behandlingsnummer) to ServiceResult.Error(
            "Ikke autentisert",
            HttpStatusCode.Unauthorized
        )))
    ) { client ->
        val response = client.get("/sak/${sakVikafossen.saksId}")
        assertEquals(HttpStatusCode.InternalServerError, response.status)
    }

    private fun successResponse(saksId: String) =
        "Fikk tilgang til den strengt bevoktede saken: $saksId"
}
