package no.nav.pensjon.brev.skribenten.auth

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.isNullOrEmptyString
import com.typesafe.config.ConfigValueFactory
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.skribenten.services.*
import no.nav.pensjon.brev.skribenten.services.PenService.SakSelection
import no.nav.pensjon.brev.skribenten.services.PenService.SakType.ALDER
import no.nav.pensjon.brev.skribenten.services.PenService.SakType.GENRL
import no.nav.pensjon.brev.skribenten.services.PenService.SakType.KRIGSP
import java.time.LocalDate
import java.time.Month
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

private const val NAVIdent = "månedens ansatt"
private val testSakEnhet = NAVEnhet("en veldig bra enhet", "NAVs beste!")
private val testSak = SakSelection(
    1337,
    "12345",
    LocalDate.of(1990, 1, 1),
    ALDER,
    "en veldig bra enhet"
)
private val sakVikafossen = SakSelection(
    7007,
    "007",
    LocalDate.of(1920, Month.NOVEMBER, 11),
    ALDER,
    "vikafossen"
)

private val generellSak0001 = SakSelection(
    7008,
    "12345",
    LocalDate.of(1920, Month.NOVEMBER, 11),
    PenService.SakType.GENRL,
    "0001"
)

private val generellSak0002 = SakSelection(
    7009,
    "12345",
    LocalDate.of(1920, Month.NOVEMBER, 11),
    PenService.SakType.GENRL,
    "0002"
)

class AuthorizeAnsattSakTilgangTest {
    init {
        ADGroups.init(
            ConfigValueFactory.fromMap(
                mapOf(
                    "pensjonUtland" to "ad gruppe id for Pensjon_Utland",
                    "pensjonSaksbehandler" to "ad gruppe id for PENSJON_SAKSBEHANDLER",
                    "fortroligAdresse" to "ad gruppe id for Fortrolig_Adresse",
                    "strengtFortroligAdresse" to "ad gruppe id for Strengt_Fortrolig_Adresse",
                    "strengtFortroligUtland" to "ad gruppe id for EndreStrengtFortroligUtland",
                )
            ).toConfig()
        )
    }

    private val creds = BasicAuthCredentials("test", "123")
    private val principalMock = mockk<UserPrincipal> {
        every { navIdent } returns NAVIdent
        every { isInGroup(any()) } returns false
    }
    private val navansattService = mockk<NavansattService> {
        coEvery { hentNavAnsattEnhetListe(any(), NAVIdent) } returns ServiceResult.Ok(listOf(testSakEnhet))
    }
    private val pdlService = mockk<PdlService> {
        coEvery { hentAdressebeskyttelse(any(), testSak.foedselsnr, ALDER.behandlingsnummer) } returns ServiceResult.Ok(emptyList())
    }
    private val penService = mockk<PenService> {
        coEvery { hentSak(any(), "${testSak.saksId}") } returns ServiceResult.Ok(testSak)
        coEvery { hentSak(any(), "${sakVikafossen.saksId}") } returns ServiceResult.Ok(sakVikafossen)
        coEvery { hentSak(any(), "${generellSak0001.saksId}") } returns ServiceResult.Ok(generellSak0001)
        coEvery { hentSak(any(), "${generellSak0002.saksId}") } returns ServiceResult.Ok(generellSak0002)
    }

    private val server = embeddedServer(Netty, port = 0) {
        install(Authentication) {
            basic("my domain") {
                validate {
                    if (it.name == creds.username && it.password == creds.password) {
                        principalMock
                    } else null
                }
            }
        }
        install(StatusPages) {
            exception<UnauthorizedException> { call, cause -> call.respond(HttpStatusCode.Unauthorized, cause.msg) }
        }
        routing {
            authenticate("my domain") {
                route("/sak") {
                    install(AuthorizeAnsattSakTilgang(navansattService, pdlService, penService))

                    get("/noSak/{noSak}") { call.respond("ingen sak") }
                    get("/sakFromPlugin/{saksId}") {
                        val sak = call.attributes[AuthorizeAnsattSakTilgang.sakKey]
                        call.respond(successResponse(sak.foedselsnr))
                    }
                    get("/{saksId}") {
                        val saksId = call.parameters.getOrFail("saksId")
                        call.respond(successResponse(saksId))
                    }
                }
            }
        }
    }.start()

    private val client = HttpClient(CIO) {
        defaultRequest {
            url("http://localhost:${runBlocking { server.resolvedConnectors().first().port }}/")
        }
        install(Auth) {
            basic {
                credentials { creds }
                sendWithoutRequest { true }
            }
        }
    }

    @Test
    fun `bruker faar tilgang til sak naar krav er oppfylt`() = runBlocking {
        val response = client.get("/sak/${testSak.saksId}")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(successResponse(testSak.saksId.toString()), response.bodyAsText())
    }

    @Test
    fun `krever saksId path param`() = runBlocking {
        val response = client.get("/sak/noSak/123")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `krever at ansatt har tilgang til sakens enhet`() = runBlocking {
        coEvery {
            navansattService.hentNavAnsattEnhetListe(any(), NAVIdent)
        } returns ServiceResult.Ok(listOf(NAVEnhet("annen enhet", "annen enhet")))

        val response = client.get("/sak/${testSak.saksId}")
        assertEquals(HttpStatusCode.Forbidden, response.status)
    }

    @Test
    fun `krever at ansatt har gruppe for FortroligAdresse`() = runBlocking {
        coEvery {
            pdlService.hentAdressebeskyttelse(any(), testSak.foedselsnr,  ALDER.behandlingsnummer)
        } returns ServiceResult.Ok(listOf(PdlService.Gradering.FORTROLIG))

        val response = client.get("/sak/${testSak.saksId}")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `krever at ansatt har gruppe for StrengtFortroligAdresse`() = runBlocking {
        coEvery {
            pdlService.hentAdressebeskyttelse(any(), testSak.foedselsnr,  ALDER.behandlingsnummer)
        } returns ServiceResult.Ok(listOf(PdlService.Gradering.STRENGT_FORTROLIG))

        val response = client.get("/sak/${testSak.saksId}")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `krever at ansatt har gruppe for StrengtFortroligUtland`() = runBlocking {
        coEvery {
            pdlService.hentAdressebeskyttelse(any(), testSak.foedselsnr,  ALDER.behandlingsnummer)
        } returns ServiceResult.Ok(listOf(PdlService.Gradering.STRENGT_FORTROLIG_UTLAND))

        val response = client.get("/sak/${testSak.saksId}")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `ansatt med gruppe for FortroligAdresse faar svar`() = runBlocking {
        every { principalMock.isInGroup(ADGroups.fortroligAdresse) } returns true
        coEvery {
            pdlService.hentAdressebeskyttelse(any(), testSak.foedselsnr,  ALDER.behandlingsnummer)
        } returns ServiceResult.Ok(listOf(PdlService.Gradering.FORTROLIG))

        val response = client.get("/sak/${testSak.saksId}")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(successResponse(testSak.saksId.toString()), response.bodyAsText())
    }

    @Test
    fun `ansatt med gruppe for StrengtFortroligAdresse faar svar`() = runBlocking {
        every { principalMock.isInGroup(ADGroups.strengtFortroligAdresse) } returns true
        coEvery {
            pdlService.hentAdressebeskyttelse(any(), testSak.foedselsnr,  ALDER.behandlingsnummer)
        } returns ServiceResult.Ok(listOf(PdlService.Gradering.STRENGT_FORTROLIG))

        val response = client.get("/sak/${testSak.saksId}")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(successResponse(testSak.saksId.toString()), response.bodyAsText())
    }

    @Test
    fun `ansatt med gruppe for StrengtFortroligUtland faar svar`() = runBlocking {
        every { principalMock.isInGroup(ADGroups.strengtFortroligUtland) } returns true
        coEvery {
            pdlService.hentAdressebeskyttelse(any(), testSak.foedselsnr,  ALDER.behandlingsnummer)
        } returns ServiceResult.Ok(listOf(PdlService.Gradering.STRENGT_FORTROLIG_UTLAND))

        val response = client.get("/sak/${testSak.saksId}")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(successResponse(testSak.saksId.toString()), response.bodyAsText())
    }

    @Test
    fun `svarer med feil fra hentSak`() = runBlocking {
        coEvery { penService.hentSak(any(), any()) } returns ServiceResult.Error("Sak finnes ikke", HttpStatusCode.NotFound)

        val response = client.get("/sak/${testSak.saksId}")
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals("Sak finnes ikke", response.bodyAsText())
    }

    @Test
    fun `svarer med internal server error om hentNavAnsattEnhetListe feiler`() = runBlocking {
        coEvery { navansattService.hentNavAnsattEnhetListe(any(), any()) } returns ServiceResult.Error("Ansatt finnes ikke", HttpStatusCode.NotFound)

        val response = client.get("/sak/${testSak.saksId}")
        assertEquals(HttpStatusCode.InternalServerError, response.status)
        assertEquals("En feil oppstod ved henting av NAVEnheter for ansatt: $NAVIdent", response.bodyAsText())
    }

    @Test
    fun `svarer med internal server error om hentAdressebeskyttelse feiler`() = runBlocking {
        coEvery { pdlService.hentAdressebeskyttelse(any(), testSak.foedselsnr,  ALDER.behandlingsnummer) } returns ServiceResult.Error("En feil", HttpStatusCode.InternalServerError)

        val response = client.get("/sak/${testSak.saksId}")
        assertEquals(HttpStatusCode.InternalServerError, response.status)
        assertEquals("En feil oppstod ved validering av tilgang til sak", response.bodyAsText())
    }

    @Test
    fun `plugin lagrer sak som attribute tilgjengelig i route scope`() = runBlocking {
        val response = client.get("/sak/sakFromPlugin/${testSak.saksId}")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(successResponse(testSak.foedselsnr), response.bodyAsText())
    }

    @Test
    fun `svarer med not found for graderte brukere selv om saksbehandler mangler enhet vikafossen`() = runBlocking {
        coEvery { pdlService.hentAdressebeskyttelse(any(), sakVikafossen.foedselsnr, ALDER.behandlingsnummer) } returns ServiceResult.Ok(listOf(PdlService.Gradering.STRENGT_FORTROLIG))

        val response = client.get("/sak/${sakVikafossen.saksId}")
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertThat(response.bodyAsText(), isNullOrEmptyString)
    }

    @Test
    fun `forbidden fra PDL resulterer i not found svar`() = runBlocking {
        coEvery { pdlService.hentAdressebeskyttelse(any(), sakVikafossen.foedselsnr, ALDER.behandlingsnummer) } returns ServiceResult.Error(
            "Ikke tilgang til person",
            HttpStatusCode.Forbidden
        )

        val response = client.get("/sak/${sakVikafossen.saksId}")
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertThat(response.bodyAsText(), isNullOrEmptyString)
    }

    @Test
    fun `unauthorized fra PDL resulterer i internal server error svar`() = runBlocking {
        coEvery { pdlService.hentAdressebeskyttelse(any(), sakVikafossen.foedselsnr, ALDER.behandlingsnummer) } returns ServiceResult.Error(
            "Ikke autentisert",
            HttpStatusCode.Unauthorized
        )

        val response = client.get("/sak/${sakVikafossen.saksId}")
        assertEquals(HttpStatusCode.InternalServerError, response.status)
    }
    @Test
    fun `ansatt uten gruppe for 0001 og sakstype er generell og tilhorer enhet 0001 faar svar`() = runBlocking {
        coEvery { pdlService.hentAdressebeskyttelse(any(), testSak.foedselsnr, GENRL.behandlingsnummer) } returns ServiceResult.Ok(emptyList())
        every { principalMock.isInGroup(ADGroups.strengtFortroligUtland) } returns false
        val response = client.get("/sak/${generellSak0001.saksId}")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(successResponse(generellSak0001.saksId.toString()), response.bodyAsText())
    }

    @Test
    fun `ansatt uten gruppe for 0001 og sakstype er generell og tilhorer ikke enhet 0001 faar ikke tilgang`() = runBlocking {
        coEvery { pdlService.hentAdressebeskyttelse(any(), testSak.foedselsnr, GENRL.behandlingsnummer) } returns ServiceResult.Ok(emptyList())
        every { principalMock.isInGroup(ADGroups.strengtFortroligUtland) } returns false
        val response = client.get("/sak/${generellSak0002.saksId}")
        assertEquals(HttpStatusCode.Forbidden, response.status)
        assertEquals("Mangler enhetstilgang til sak", response.bodyAsText())
    }

    private fun successResponse(saksId: String) =
        "Fikk tilgang til den strengt bevoktede saken: $saksId"
}
