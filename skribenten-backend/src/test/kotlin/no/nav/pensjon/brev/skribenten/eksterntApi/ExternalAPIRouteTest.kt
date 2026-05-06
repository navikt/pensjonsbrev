package no.nav.pensjon.brev.skribenten.eksterntApi

import com.typesafe.config.ConfigValueFactory
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.basic
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import no.nav.pensjon.brev.skribenten.MockPrincipal
import no.nav.pensjon.brev.skribenten.Testbrevkoder
import no.nav.pensjon.brev.skribenten.auth.ADGroups
import no.nav.pensjon.brev.skribenten.auth.JwtConfig
import no.nav.pensjon.brev.skribenten.brevredigering.application.HentBrevService
import no.nav.pensjon.brev.skribenten.brevredigering.application.OpprettBrevService
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.OpprettBrevHandlerImpl
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevmalFinnesIkke
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.brevredigering.domain.OpprettBrevPolicy
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.db.Hash
import no.nav.pensjon.brev.skribenten.fagsystem.Behandlingsnummer
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.fagsystem.FagsakService
import no.nav.pensjon.brev.skribenten.initADGroups
import no.nav.pensjon.brev.skribenten.letter.editedLetter
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Distribusjonstype
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brev.skribenten.serialize.Sakstype
import no.nav.pensjon.brev.skribenten.services.EnhetId
import no.nav.pensjon.brev.skribenten.services.FakeBrevbakerService
import no.nav.pensjon.brev.skribenten.services.FakeBrevmetadataService
import no.nav.pensjon.brev.skribenten.services.PdlServiceStub
import no.nav.pensjon.brev.skribenten.services.PenClientStub
import no.nav.pensjon.brev.skribenten.skribentenContenNegotiation
import no.nav.pensjon.brevbaker.api.model.BrevbakerType
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDate

class ExternalAPIRouteTest {

   @BeforeAll
   fun initAdGroups() = initADGroups()

    private val creds = BasicAuthCredentials("test", "123")
    private val navIdent = NavIdent("testSaksbehandler")
    private val authConfigName = "test-auth"
    private val jwtConfig = JwtConfig(
        name = authConfigName,
        issuer = "issuer",
        jwksUrl = "https://example.com/jwks",
        clientId = "clientId",
        tokenUri = "https://example.com/token",
        clientSecret = "secret",
        preAuthorizedApps = emptyList(),
        requireAzureAdClaims = false
    )

    private val brevInfo = Dto.BrevInfo(
        id = BrevId(42L),
        saksId = SaksId(1L),
        vedtaksId = null,
        opprettetAv = navIdent,
        opprettet = Instant.now(),
        sistredigertAv = navIdent,
        sistredigert = Instant.now(),
        redigeresAv = null,
        sistReservert = null,
        brevkode = Testbrevkoder.INFORMASJONSBREV,
        laastForRedigering = false,
        distribusjonstype = Distribusjonstype.SENTRALPRINT,
        mottaker = null,
        avsenderEnhetId = EnhetId("0001"),
        spraak = LanguageCode.BOKMAL,
        journalpostId = null,
        attestertAv = null,
        status = Dto.BrevStatus.KLADD
    )

    private val successBrevredigering = Dto.Brevredigering(
        info = brevInfo,
        redigertBrev = editedLetter(),
        redigertBrevHash = Hash("abc"),
        saksbehandlerValg = Api.GeneriskBrevdata(),
        propertyUsage = null,
        valgteVedlegg = null
    )

    private val opprettBrevRequestJson = """
        {
            "saksId": 1,
            "brevkode": "INFORMASJONSBREV",
            "spraak": "NB",
            "avsenderEnhetsId": "0001"
        }
    """.trimIndent()

    private val hentBrevService = object : HentBrevService {
        override fun hentBrevForAlleSaker(saksIder: Set<SaksId>) = listOf(brevInfo)
        override fun hentBrevInfo(brevId: BrevId) = brevInfo
    }

    private fun lagExternalAPIService(
        opprettBrevResult: Outcome<Dto.Brevredigering, BrevredigeringError> = Outcome.success(successBrevredigering)
    ) = ExternalAPIService(
        config = ConfigValueFactory.fromMap(mapOf("skribentenWebUrl" to "https://example.com")).toConfig(),
        hentBrevService = hentBrevService,
        brevmalService = BrevmalService(
            brevbakerService = FakeBrevbakerService(),
            penClient = PenClientStub(),
            brevmetadataService = FakeBrevmetadataService(),
        ),
        opprettBrevService = object : OpprettBrevService {
            override suspend fun opprettBrev(request: OpprettBrevHandlerImpl.Request) = opprettBrevResult
        }
    )

    private fun routeTestApplication(
        principal: MockPrincipal = MockPrincipal(navIdent, "Test Testesen", setOf(ADGroups.pensjonSaksbehandler)),
        externalAPIService: ExternalAPIService = lagExternalAPIService(),
        block: suspend ApplicationTestBuilder.(client: HttpClient) -> Unit,
    ): Unit = testApplication {
        install(Authentication) {
            basic(authConfigName) {
                validate {
                    if (it.name == creds.username && it.password == creds.password) principal else null
                }
            }
        }
        application {
            skribentenContenNegotiation()
        }
        routing {
            externalAPI(jwtConfig, externalAPIService, object : PdlServiceStub() {
                override suspend fun hentAdressebeskyttelse(ident: BrevbakerType.Pid, behandlingsnummer: Behandlingsnummer?) = null
            }, FagsakService(object : PenClientStub() {
                override suspend fun hentSak(saksId: SaksId) = Pen.SakSelection(saksId, LocalDate.now(), Pen.SakSelection.Navn("fornavn1", mellomnavn = null, "etternavn2"), Sakstype("hei"), BrevbakerType.Pid("123"))
            }))
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
    fun `opprettBrev returnerer 201 med brevId og sakId ved suksess`() = routeTestApplication { client ->
        val response = client.post("/external/api/v1/brev") {
            contentType(ContentType.Application.Json)
            setBody(opprettBrevRequestJson)
        }

        assertThat(response.status).isEqualTo(HttpStatusCode.Created)
        assertThat(response.bodyAsText()).isEqualTo("{\"brevId\":42,\"sakId\":1}")
    }

    @Test
    fun `opprettBrev returnerer 400 når brevmal krever vedtaksId`() = routeTestApplication(
        externalAPIService = lagExternalAPIService(
            Outcome.failure(OpprettBrevPolicy.KanIkkeOppretteBrev.BrevmalKreverVedtaksId(Testbrevkoder.INFORMASJONSBREV))
        )
    ) { client ->
        val response = client.post("/external/api/v1/brev") {
            contentType(ContentType.Application.Json)
            setBody(opprettBrevRequestJson)
        }

        assertThat(response.status).isEqualTo(HttpStatusCode.BadRequest)
    }

    @Test
    fun `opprettBrev returnerer 500 ved ukjent feil`() = routeTestApplication(
        externalAPIService = lagExternalAPIService(
            Outcome.failure(BrevmalFinnesIkke(Testbrevkoder.INFORMASJONSBREV))
        )
    ) { client ->
        val response = client.post("/external/api/v1/brev") {
            contentType(ContentType.Application.Json)
            setBody(opprettBrevRequestJson)
        }

        assertThat(response.status).isEqualTo(HttpStatusCode.InternalServerError)
    }
}
