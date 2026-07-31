package no.nav.pensjon.brev.pdfbygger

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MetricsRouteTest {

    // NB: må ankres på skilletegnet, ellers matcher regexet slutten av throwable="n/a".
    private fun grenseFra(bucket: String): String? =
        Regex("""[,{]le="([^"]+)"""").find(bucket)?.groupValues?.get(1)

    private fun withScrape(block: (List<String>) -> Unit) = testApplication {
        environment { config = ApplicationConfig(null) }

        client.get("/isAlive")
        val metrikker = client.get("/metrics").bodyAsText().lines()
            .filter { it.startsWith("ktor_http_server_requests_seconds") }
        // Uten denne ville assertFalse-testene under passert selv om scrapingen ikke ga noe.
        assertTrue(metrikker.isNotEmpty()) { "Fant ingen ktor_http_server_requests_seconds-metrikker" }
        block(metrikker)
    }

    @Test
    fun `latens rapporteres som histogram-buckets slik at de kan aggregeres paa tvers av poder`() =
        withScrape { metrikker ->
            val buckets = metrikker.filter { it.startsWith("ktor_http_server_requests_seconds_bucket") }
            assertTrue(buckets.isNotEmpty()) { "Forventet histogram-buckets, fikk:\n${metrikker.joinToString("\n")}" }

            // Grensene oppgis i nanosekunder til Micrometer, men skal rapporteres i sekunder.
            // Slår dette feil er sannsynligvis tidsenheten på serviceLevelObjectives feil.
            listOf("0.1", "0.25", "0.5", "1.0", "2.0", "5.0", "10.0", "20.0", "30.0", "60.0", "120.0", "300.0").forEach { grense ->
                assertTrue(buckets.any { it.contains("le=\"$grense\"") }) {
                    "Manglet bucket le=\"$grense\". Fant:\n${buckets.joinToString("\n")}"
                }
            }

            // percentilesHistogram gir flere buckets enn SLO-grensene alene, men uten min- og
            // maksgrense genererer micrometer 69 buckets per rute og statuskode.
            val grenser = buckets.mapNotNull { grenseFra(it) }.distinct()
            assertTrue(grenser.size in 20..60) {
                "Forventet et klamret sett med buckets, fant ${grenser.size} grenser: $grenser"
            }
        }

    @Test
    fun `scrapen inneholder ingen klientside-kvantiler som ikke kan aggregeres`() =
        withScrape { metrikker ->
            assertFalse(metrikker.any { it.contains("quantile=\"") }) {
                "Forventet ingen klientside-kvantiler, fant:\n${metrikker.filter { it.contains("quantile=") }.joinToString("\n")}"
            }
        }

    @Test
    fun `address-tagen er fjernet siden nais allerede gir oss pod som label`() =
        withScrape { metrikker ->
            assertFalse(metrikker.any { it.contains("address=\"") }) {
                "Forventet ingen address-tag, fant:\n${metrikker.filter { it.contains("address=") }.joinToString("\n")}"
            }
        }
}
