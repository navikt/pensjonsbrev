package no.nav.pensjon.brev.routing

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import no.nav.pensjon.brev.testBrevbakerApp
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MetricsRouteTest {

    // NB: må ankres på skilletegnet, ellers matcher regexet slutten av throwable="n/a".
    private fun grenseFra(bucket: String): String? =
        Regex("""[,{]le="([^"]+)"""").find(bucket)?.groupValues?.get(1)

    private suspend fun io.ktor.client.HttpClient.scrapeAfterRequest(): List<String> {
        // Må være en rute som faktisk instrumenteres. Helsesjekkene filtreres bort, mens en
        // ukjent sti registreres som route="n/a" og krever ingen innlogging.
        get("/finnes-ikke")
        val metrikker = get("/metrics").bodyAsText().lines()
            .filter { it.startsWith("ktor_http_server_requests_seconds") }
        // Uten denne ville assertFalse-testene under passert selv om scrapingen ikke ga noe.
        assertTrue(metrikker.isNotEmpty()) { "Fant ingen ktor_http_server_requests_seconds-metrikker" }
        return metrikker
    }

    @Test
    fun `helsesjekker og metrikkendepunktet instrumenteres ikke`() =
        testBrevbakerApp(isIntegrationTest = false) { client ->
            client.get("/isAlive")
            client.get("/isReady")
            client.get("/ping_authorized")

            val metrikker = client.scrapeAfterRequest()

            // Ved normal last i prod utgjør disse ~79 % av trafikken. Blir de tatt med, fortynner
            // de nevneren i feilrate-alarmene så kraftig at ~25 % av de ekte kallene må feile før
            // en 5 %-alarm utløser.
            listOf("/isAlive", "/isReady", "/metrics", "/ping_authorized").forEach { sti ->
                assertFalse(metrikker.any { it.contains("route=\"$sti\"") }) {
                    "Forventet ingen metrikker for $sti, fant:\n${metrikker.filter { it.contains("route=\"$sti\"") }.joinToString("\n")}"
                }
            }
        }

    @Test
    fun `latens rapporteres som histogram-buckets slik at de kan aggregeres paa tvers av poder`() =
        testBrevbakerApp(isIntegrationTest = false) { client ->
            val metrikker = client.scrapeAfterRequest()

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
        testBrevbakerApp(isIntegrationTest = false) { client ->
            val metrikker = client.scrapeAfterRequest()

            // Kvantilene forsvinner fordi metrikken eksporteres som histogram når
            // serviceLevelObjectives er satt, ikke fordi vi lar være å sette percentiles.
            // Testen sikrer at scrapen faktisk er fri for dem, uansett årsak.
            assertFalse(metrikker.any { it.contains("quantile=\"") }) {
                "Forventet ingen klientside-kvantiler, fant:\n${metrikker.filter { it.contains("quantile=") }.joinToString("\n")}"
            }
        }

    @Test
    fun `address-tagen er fjernet siden nais allerede gir oss pod som label`() =
        testBrevbakerApp(isIntegrationTest = false) { client ->
            val metrikker = client.scrapeAfterRequest()

            assertFalse(metrikker.any { it.contains("address=\"") }) {
                "Forventet ingen address-tag, fant:\n${metrikker.filter { it.contains("address=") }.joinToString("\n")}"
            }
        }
}
