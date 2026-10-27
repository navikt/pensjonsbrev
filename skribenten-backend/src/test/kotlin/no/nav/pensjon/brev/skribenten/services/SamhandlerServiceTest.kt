package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.pensjon.brev.skribenten.OboClientConfig
import no.nav.pensjon.brev.skribenten.auth.FakeAuthService
import no.nav.pensjon.brev.skribenten.brevredigering.domain.TssId
import no.nav.pensjon.brev.skribenten.common.InMemoryCache
import no.nav.pensjon.brev.skribenten.model.OrgNr
import no.nav.pensjon.brev.skribenten.routes.samhandler.dto.HentSamhandlerResponseDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SamhandlerServiceTest {
    private val config = OboClientConfig(url = "http://samhandler.test", scope = "test-scope")

    private val samhandler = SamhandlerServiceHttp.SamhandlerEnkel(
        navn = "Advokat Handler AS",
        samhandlerType = "ADVO",
        offentligId = "987654321",
        idType = "ORGNR",
    )

    @Test
    fun `normalisert organisasjonsnummer er kun tilgjengelig for gyldig norsk organisasjon`() {
        fun success(idType: String, offentligId: String) =
            HentSamhandlerResponseDto.Success(
                navn = "Advokat Handler AS",
                samhandlerType = "ADVO",
                offentligId = offentligId,
                idType = idType,
            )

        assertEquals(OrgNr("987654321"), success("ORG", "00987654321").organisasjonsnummer)
        assertEquals(OrgNr("987654321"), success("ORG", "987654321").organisasjonsnummer)
        assertEquals(null, success("ORG", "1234567890").organisasjonsnummer)
        assertEquals(null, success("ORGNR", "987654321").organisasjonsnummer)

        val json = jacksonObjectMapper().writeValueAsString(success("ORG", "00987654321"))
        assertEquals("987654321", jacksonObjectMapper().readTree(json)["organisasjonsnummer"].asText())
    }

    @Test
    fun `navn og type deler ett cachet kall mot samhandler-proxy`() {
        httpClientTest(samhandler) { engine ->
            val service = SamhandlerServiceHttp(
                config = config,
                authService = FakeAuthService,
                cache = InMemoryCache(),
                engine = engine,
            )

            assertEquals("Advokat Handler AS", service.hentSamhandlerNavn(TssId("80000123456")))
            assertEquals("ADVO", service.hentSamhandlerType(TssId("80000123456")))

            assertEquals(1, engine.requestHistory.count { it.url.encodedPath.contains("hentSamhandlerEnkel") })
        }
    }
}
