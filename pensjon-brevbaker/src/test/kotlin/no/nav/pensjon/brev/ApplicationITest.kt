package no.nav.pensjon.brev

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.containsSubstring
import com.natpryce.hamkrest.equalTo
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking
import no.nav.brev.brevbaker.BREVBAKER_URL
import no.nav.brev.brevbaker.Fixtures
import no.nav.brev.brevbaker.TestTags
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.fixtures.createLetterExampleDto
import no.nav.pensjon.brev.maler.example.LetterExample
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

class ApplicationITest {
    @Test
    fun `ping running brevbaker`() {
        runBlocking {
            try {
                testBrevbakerApp {
                    client.get("isAlive")
                }
            } catch (e: Exception) {
                throw Exception("Failed to ping brevbaker at: $BREVBAKER_URL", e)
            }
        }
    }

    @Tag(TestTags.MANUAL_TEST)
    @Test
    fun `deserialiser value class`() = testBrevbakerApp { client ->
        val response = client.post("/letter/autobrev/pdf") {
            contentType(ContentType.Application.Json)
            setBody(reqValue)
        }
        assertThat(response.bodyAsText(), containsSubstring("file"))
        assertThat(response.status, equalTo(HttpStatusCode.OK))
    }

    @Tag(TestTags.MANUAL_TEST)
    @Test
    fun `deserialiser wrapped`() = testBrevbakerApp { client ->
        val response = client.post("/letter/autobrev/pdf") {
            contentType(ContentType.Application.Json)
            setBody(reqWrapped)
        }
        assertThat(response.bodyAsText(), containsSubstring("file"))
        assertThat(response.status, equalTo(HttpStatusCode.OK))
    }

    @Test
    fun `response includes deserialization error`() = testBrevbakerApp { client ->
        val response = client.post("/letter/autobrev/pdf") {
            contentType(ContentType.Application.Json)
            setBody(
                """
                {
                    "kode": "${LetterExample.kode.kode()}"
                }
            """.trimIndent()
            )
        }
        assertThat(response.bodyAsText(), containsSubstring("value failed for JSON property "))
    }

    @Test
    fun `response includes letterData deserialization error`() = testBrevbakerApp { client ->
        val response = client.post("/letter/autobrev/pdf") {
            contentType(ContentType.Application.Json)
            setBody(
                BestillBrevRequest(
                    kode = LetterExample.kode,
                    letterData = EmptyBrevdata,
                    felles = Fixtures.fellesAuto,
                    language = LanguageCode.BOKMAL
                )
            )
        }
        assertThat(response.bodyAsText(), containsSubstring("Missing required creator property"))
    }
}

private val reqValue = """
    {
        "kode":"TESTBREV",
        "letterData":{
            "pensjonInnvilget":true,
            "datoInnvilget":"2025-08-04",
            "navneliste":[],
            "tilleggEksempel": [ 
                {
                  "navn" : "Test testerson 1",
                  "tillegg1" : 300,
                  "tillegg2" : null,
                  "tillegg3" : 500
                }, {
                  "navn" : "Test testerson 2",
                  "tillegg1" : 100,
                  "tillegg2" : 600,
                  "tillegg3" : null
                }, {
                  "navn" : "Test testerson 3",
                  "tillegg1" : null,
                  "tillegg2" : 300,
                  "tillegg3" : null
                } 
            ],
            "datoAvslaatt":"2025-08-04",
            "pensjonBeloep":100
            },
        "felles":{
            "dokumentDato":"2020-01-01",
            "saksnummer":"1337123",
            "avsenderEnhet":{
                "nettside":"nav.no",
                "navn":"Nav Familie- og pensjonsytelser Porsgrunn",
                "telefonnummer":"55553334"
            },
            "bruker":{
                "foedselsnummer":"01019878910",
                "fornavn":"Test",
                "mellomnavn":"\"bruker\"",
                "etternavn":"Testerson"
            },
            "vergeNavn":null,
            "signerendeSaksbehandlere":null
        },
        "language":"BOKMAL"
    }
""".trimIndent()

private val reqWrapped = """
    {
        "kode":"TESTBREV",
        "letterData":{
            "pensjonInnvilget":true,
            "datoInnvilget":"2025-08-04",
            "navneliste":[],
            "tilleggEksempel": [ 
                {
                  "navn" : "Test testerson 1",
                  "tillegg1" : {
                    "value": "300"
                  },
                  "tillegg2" : null,
                  "tillegg3" : {
                    "value":"500"
                  }                
                }, {
                  "navn" : "Test testerson 2",
                  "tillegg1" : 100,
                  "tillegg2" : 600,
                  "tillegg3" : null
                }, {
                  "navn" : "Test testerson 3",
                  "tillegg1" : null,
                  "tillegg2" : {
                    "value": "300"
                    },
                  "tillegg3" : null
                } 
            ],
            "datoAvslaatt":"2025-08-04",
            "pensjonBeloep":100
            },
        "felles":{
            "dokumentDato":"2020-01-01",
            "saksnummer":"1337123",
            "avsenderEnhet":{
                "nettside":"nav.no",
                "navn":"Nav Familie- og pensjonsytelser Porsgrunn",
                "telefonnummer": { 
                    "value":"55553334"
                }
            },
            "bruker":{
                "foedselsnummer":{
                    "value":"01019878910"
                },
                "fornavn":"Test",
                "mellomnavn":"\"bruker\"",
                "etternavn":"Testerson"
            },
            "vergeNavn":null,
            "signerendeSaksbehandlere":null
        },
        "language":"BOKMAL"
    }
""".trimIndent()