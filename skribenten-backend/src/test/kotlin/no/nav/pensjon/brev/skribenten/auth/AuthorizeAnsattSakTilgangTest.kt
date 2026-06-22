package no.nav.pensjon.brev.skribenten.auth

import io.ktor.client.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.skribenten.MockPrincipal
import no.nav.pensjon.brev.skribenten.Testbrevkoder
import no.nav.pensjon.brev.skribenten.brevredigering.application.HentBrevService
import no.nav.pensjon.brev.skribenten.fagsystem.Behandlingsnummer
import no.nav.pensjon.brev.skribenten.fagsystem.FagsakService
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.PenClient
import no.nav.pensjon.brev.skribenten.initADGroups
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Distribusjon
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brev.skribenten.model.Pdl
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brev.skribenten.model.Sakstype
import no.nav.pensjon.brev.skribenten.services.*
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Pid
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDate
import java.time.Month

private val navIdent = NavIdent("månedens ansatt")
private val testSak = Pen.SakSelection(
    saksId = SaksId(1337),
    foedselsdato = LocalDate.of(1990, 1, 1),
    navn = Pen.SakSelection.Navn("a", "b", "c"),
    sakType = Sakstype("Sakstype123"),
    pid = Pid("12345"),
    behandlingsnumre = listOf()
)
private val sakVikafossen = Pen.SakSelection(
    saksId = SaksId(7007),
    foedselsdato = LocalDate.of(1920, Month.NOVEMBER, 11),
    navn = Pen.SakSelection.Navn("a", "b", "c"),
    sakType = Sakstype("Sakstype123"),
    pid = Pid("007"),
    behandlingsnumre = listOf()
)

private val generellSak0001 = Pen.SakSelection(
    saksId = SaksId(7008),
    foedselsdato = LocalDate.of(1920, Month.NOVEMBER, 11),
    navn = Pen.SakSelection.Navn("a", "b", "c"),
    sakType = Sakstype("GENRL"),
    pid = Pid("12345"),
    behandlingsnumre = listOf()
)

private val generellSak0002 = Pen.SakSelection(
    saksId = SaksId(7009),
    foedselsdato = LocalDate.of(1920, Month.NOVEMBER, 11),
    navn = Pen.SakSelection.Navn("a", "b", "c"),
    sakType = Sakstype("GENRL"),
    pid = Pid("12345"),
    behandlingsnumre = listOf(),
)


private fun brevInfo(id: Long, saksId: SaksId) = Dto.BrevInfo(
    id = BrevId(id),
    saksId = saksId,
    vedtaksId = null,
    opprettetAv = navIdent,
    opprettet = Instant.now(),
    sistredigertAv = navIdent,
    sistredigert = Instant.now(),
    redigeresAv = null,
    sistReservert = null,
    brevkode = Testbrevkoder.INFORMASJONSBREV,
    laastForRedigering = false,
    distribusjonstype = Distribusjon.SENTRALPRINT,
    mottaker = null,
    avsenderEnhetId = EnhetId("0001"),
    spraak = LanguageCode.BOKMAL,
    journalpostId = null,
    attestertAv = null,
    status = Dto.BrevStatus.KLADD,
)

class AuthorizeAnsattSakTilgangTest {
    init {
        initADGroups()
    }

    private val creds = BasicAuthCredentials("test", "123")

    private fun lagPdlService(adressebeskyttelser: Map<Pair<Pid, List<Behandlingsnummer>>, List<Pdl.Gradering>> = mapOf()) = object : PdlServiceStub() {
        override suspend fun hentAdressebeskyttelse(ident: Pid, behandlingsnumre: List<Behandlingsnummer>) =
            adressebeskyttelser[Pair(ident, behandlingsnumre)]
                ?: notYetStubbed("Mangler stub for adressebeskyttelse for fødselsnummer $ident og behandlingsnummer $behandlingsnumre")

        override suspend fun hentBrukerContext(ident: Pid, behandlingsnumre: List<Behandlingsnummer>): Pdl.PersonContext =
            notYetStubbed("Mangler stub for hentBrukerContext")

    }

    private val defaultPdlService = lagPdlService(mapOf(Pair(testSak.pid, behandlingsnummer()) to emptyList()))

    // TODO: Endre til å stubbe FagsakService
    private val defaultPenService = object : PenClientStub() {
        private val saker = listOf(testSak, sakVikafossen, generellSak0001, generellSak0002).associateBy { it.saksId }
        override suspend fun hentSak(saksId: SaksId): Pen.SakSelection? = saker[saksId]
    }

    private fun lagHentBrevService(brevInfoer: List<Dto.BrevInfo> = emptyList()) = object : HentBrevService {
        private val brev = brevInfoer.associateBy { it.id }
        override fun hentBrevForAlleSaker(saksIder: Set<SaksId>) = brevInfoer.filter { it.saksId in saksIder }
        override fun hentBrevInfo(brevId: BrevId) = brev[brevId]
    }

    private val defaultHentBrevService = lagHentBrevService()

    private fun basicAuthTestApplication(
        principal: MockPrincipal = MockPrincipal(navIdent, "Ansatt, Veldig Bra"),
        penClient: PenClient = defaultPenService,
        pdlService: PdlService = defaultPdlService,
        hentBrevService: HentBrevService = defaultHentBrevService,
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
                        this.fagsakService = FagsakService(penClient)
                    }

                    get("/noSak/{noSak}") { call.respond("ingen sak") }
                    get("/sakFromPlugin/{saksId}") {
                        val sak = call.attributes[SakKey]
                        call.respond(successResponse(sak.pid.value))
                    }
                    get("/{saksId}") {
                        val saksId = call.parameters.getOrFail("saksId")
                        call.respond(successResponse(saksId))
                    }
                    route("/{saksId}/brev/{brevId}") {
                        install(AuthorizeBrevTilhoererSak) {
                            this.hentBrevService = hentBrevService
                        }
                        get {
                            val brevId = call.parameters.getOrFail("brevId")
                            call.respond(successResponse(brevId))
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

        block(client)
    }

    @Test
    fun `bruker faar tilgang til sak naar krav er oppfylt`() = basicAuthTestApplication(
        pdlService = lagPdlService(adressebeskyttelser = mapOf(Pair(testSak.pid, behandlingsnummer()) to emptyList()))
    ) { client ->
        val response = client.get("/sak/${testSak.saksId.id}")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(successResponse(testSak.saksId.id.toString()), response.bodyAsText())
    }

    @Test
    fun `krever saksId path param`() = basicAuthTestApplication { client ->
        val response = client.get("/sak/noSak/123")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `gir tilgang til brev som tilhoerer saken`() = basicAuthTestApplication(
        hentBrevService = lagHentBrevService(listOf(brevInfo(id = 99, saksId = testSak.saksId)))
    ) { client ->
        val response = client.get("/sak/${testSak.saksId.id}/brev/99")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(successResponse("99"), response.bodyAsText())
    }

    @Test
    fun `avviser brev som tilhoerer en annen sak`() = basicAuthTestApplication(
        hentBrevService = lagHentBrevService(listOf(brevInfo(id = 99, saksId = sakVikafossen.saksId)))
    ) { client ->
        val response = client.get("/sak/${testSak.saksId.id}/brev/99")
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals("Fant ikke brev", response.bodyAsText())
    }

    @Test
    fun `avviser brev som ikke finnes`() = basicAuthTestApplication(
        hentBrevService = lagHentBrevService(emptyList())
    ) { client ->
        val response = client.get("/sak/${testSak.saksId.id}/brev/99")
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals("Fant ikke brev", response.bodyAsText())
    }

    @Test
    fun `krever at ansatt har gruppe for FortroligAdresse`() = basicAuthTestApplication(
        pdlService = lagPdlService(adressebeskyttelser = mapOf(Pair(testSak.pid, behandlingsnummer()) to listOf(Pdl.Gradering.FORTROLIG)))
    ) { client ->
        val response = client.get("/sak/${testSak.saksId.id}")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `krever at ansatt har gruppe for StrengtFortroligAdresse`() = basicAuthTestApplication(
        pdlService = lagPdlService(adressebeskyttelser = mapOf(Pair(testSak.pid, behandlingsnummer()) to listOf(Pdl.Gradering.STRENGT_FORTROLIG)))
    ) { client ->
        val response = client.get("/sak/${testSak.saksId.id}")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `krever at ansatt har gruppe for StrengtFortrolig for utland`() = basicAuthTestApplication(
        pdlService = lagPdlService(adressebeskyttelser = mapOf(Pair(testSak.pid, behandlingsnummer()) to listOf(Pdl.Gradering.STRENGT_FORTROLIG_UTLAND)))
    ) { client ->
        val response = client.get("/sak/${testSak.saksId.id}")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `ansatt med gruppe for FortroligAdresse faar svar`() =
        basicAuthTestApplication(
            principal = MockPrincipal(navIdent, "Hemmelig ansatt", setOf(ADGroups.fortroligAdresse)),
            pdlService = lagPdlService(adressebeskyttelser = mapOf(Pair(testSak.pid, behandlingsnummer()) to listOf(Pdl.Gradering.FORTROLIG)))
        ) { client ->
            val response = client.get("/sak/${testSak.saksId.id}")
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals(successResponse(testSak.saksId.id.toString()), response.bodyAsText())
        }

    @Test
    fun `ansatt med gruppe for StrengtFortroligAdresse faar svar`() =
        basicAuthTestApplication(
            principal = MockPrincipal(navIdent, "Hemmelig ansatt", setOf(ADGroups.strengtFortroligAdresse)),
            pdlService = lagPdlService(adressebeskyttelser = mapOf(Pair(testSak.pid, behandlingsnummer()) to listOf(Pdl.Gradering.STRENGT_FORTROLIG)))
        ) { client ->
            val response = client.get("/sak/${testSak.saksId.id}")
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals(successResponse(testSak.saksId.id.toString()), response.bodyAsText())
        }

    @Test
    fun `ansatt med gruppe for StrengtFortroligUtland faar svar`() =
        basicAuthTestApplication(
            principal = MockPrincipal(navIdent, "Hemmelig ansatt", setOf(ADGroups.strengtFortroligAdresse)),
            pdlService = lagPdlService(adressebeskyttelser = mapOf(Pair(testSak.pid, behandlingsnummer()) to listOf(Pdl.Gradering.STRENGT_FORTROLIG_UTLAND)))
        ) { client ->
            val response = client.get("/sak/${testSak.saksId.id}")
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals(successResponse(testSak.saksId.id.toString()), response.bodyAsText())
        }

    @Test
    fun `svarer med feil fra hentSak`() = basicAuthTestApplication(penClient = object : PenClientStub() {
        override suspend fun hentSak(saksId: SaksId) = null
    }) { client ->
        val response = client.get("/sak/${testSak.saksId.id}")
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals("Sak ikke funnet", response.bodyAsText())
    }

    @Test
    fun `svarer med internal server error om hentAdressebeskyttelse feiler`() = basicAuthTestApplication(
        pdlService = object : PdlServiceStub() {
            override suspend fun hentAdressebeskyttelse(ident: Pid, behandlingsnumre: List<Behandlingsnummer>) =
                throw PdlServiceException("En feil", HttpStatusCode.InternalServerError)
        }
    ) { client ->
        val response = client.get("/sak/${testSak.saksId.id}")
        assertEquals(HttpStatusCode.InternalServerError, response.status)
    }

    @Test
    fun `plugin lagrer sak som attribute tilgjengelig i route scope`() = basicAuthTestApplication(
        pdlService = lagPdlService(adressebeskyttelser = mapOf(Pair(testSak.pid, behandlingsnummer()) to emptyList()))
    ) { client ->
        val response = client.get("/sak/sakFromPlugin/${testSak.saksId.id}")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(successResponse(testSak.pid.value), response.bodyAsText())
    }

    @Test
    fun `svarer med not found for graderte brukere selv om saksbehandler mangler enhet vikafossen`() = basicAuthTestApplication(
        pdlService = lagPdlService(adressebeskyttelser = mapOf(Pair(sakVikafossen.pid, behandlingsnummer()) to listOf(Pdl.Gradering.STRENGT_FORTROLIG)))
    ) { client ->
        val response = client.get("/sak/${sakVikafossen.saksId.id}")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    private fun behandlingsnummer(): List<Behandlingsnummer> = listOf()

    private fun successResponse(saksId: String) =
        "Fikk tilgang til den strengt bevoktede saken: $saksId"
}
