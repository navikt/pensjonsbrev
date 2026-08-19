package no.nav.pensjon.brev.skribenten

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.server.config.MapApplicationConfig
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import no.nav.pensjon.brev.skribenten.Metrics.configureMetrics
import no.nav.pensjon.brev.skribenten.routes.healthRoute
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MetricsRouteTest {

    // NB: må ankres på skilletegnet, ellers matcher regexet slutten av throwable="n/a".
    private fun grenseFra(bucket: String): String? =
        Regex("""[,{]le="([^"]+)"""").find(bucket)?.groupValues?.get(1)

    // Vi installerer kun metrikkene, ikke hele appen, slik at testen slipper database og
    // innlogging. Tom konfigurasjon er nødvendig fordi application.conf ellers lastes og krever
    // miljøvariabler som DB_PASSWORD.
    private fun withScrape(block: (List<String>) -> Unit) = testApplication {
        environment { config = MapApplicationConfig() }
        application { configureMetrics() }

        // Må være en rute som faktisk instrumenteres. /metrics filtreres nå bort, mens en ukjent
        // sti registreres som route="n/a".
        client.get("/finnes-ikke")
        val metrikker = client.get("/metrics").bodyAsText().lines()
            .filter { it.startsWith("ktor_http_server_requests_seconds") }
        // Uten denne ville assertFalse-testene under passert selv om scrapingen ikke ga noe.
        assertTrue(metrikker.isNotEmpty()) { "Fant ingen ktor_http_server_requests_seconds-metrikker" }
        block(metrikker)
    }

    @Test
    fun `helsesjekker og metrikkendepunktet instrumenteres ikke`() = testApplication {
        environment { config = MapApplicationConfig() }
        // healthRoute() leser bare et AtomicBoolean-flagg, så den kan installeres uten database.
        // Vi bruker de ekte rutene her med vilje: hadde vi latt /isAlive og /isReady være
        // uregistrerte, ville de blitt 404 og fått route="n/a", og assert-ene under ville passert
        // uten å bevise noe som helst.
        application {
            configureMetrics()
            routing { healthRoute() }
        }

        client.get("/isAlive")
        client.get("/isReady")
        client.get("/finnes-ikke")

        val metrikker = client.get("/metrics").bodyAsText().lines()
            .filter { it.startsWith("ktor_http_server_requests_seconds") }
        assertTrue(metrikker.isNotEmpty()) { "Fant ingen ktor_http_server_requests_seconds-metrikker" }

        // Ved normal last i prod utgjør disse ~42 % av trafikken, og andelen stiger mot 100 %
        // utenfor kontortid. Blir de tatt med, fortynner de nevneren i feilrate-alarmene.
        listOf("/isAlive", "/isReady", "/metrics").forEach { sti ->
            assertFalse(metrikker.any { it.contains("route=\"$sti\"") }) {
                "Forventet ingen metrikker for $sti, fant:\n${metrikker.filter { it.contains("route=\"$sti\"") }.joinToString("\n")}"
            }
        }
    }

    @Test
    fun `latens rapporteres som histogram-buckets slik at de kan aggregeres paa tvers av poder`() =
        withScrape { metrikker ->
            val buckets = metrikker.filter { it.startsWith("ktor_http_server_requests_seconds_bucket") }
            assertTrue(buckets.isNotEmpty()) { "Forventet histogram-buckets, fikk:\n${metrikker.joinToString("\n")}" }

            // Grensene oppgis i nanosekunder til Micrometer, men skal rapporteres i sekunder.
            // Slår dette feil er sannsynligvis tidsenheten på serviceLevelObjectives feil.
            listOf("0.05", "0.1", "0.25", "0.5", "1.0", "2.0", "5.0", "10.0", "30.0", "60.0", "120.0").forEach { grense ->
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
