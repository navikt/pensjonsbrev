package no.nav.pensjon.brev.skribenten

import io.ktor.server.application.*
import io.ktor.server.metrics.micrometer.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.micrometer.core.instrument.config.MeterFilter
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

object Metrics {
    private val prometheusRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)

    // Skribenten er en interaktiv saksbehandlerflate, så vi er interessert i lavere latens enn i
    // brevbaker. Ytterpunktene styrer hvor micrometer genererer automatiske buckets.
    // Den øverste matcher requestTimeout mot brevbaker (BrevbakerService), slik at et kall som
    // går til timeout havner innenfor histogrammet og ikke i +Inf.
    // Nedre grense er beholdt selv om metadatarutene (/land, /me/userinfo) ligger på 2-8ms: de
    // alarmerer vi ikke på, og et gulv på 10ms ville kostet 18 ekstra buckets per rute.
    private val forventetLavest = 50.milliseconds
    private val forventetHoeyest = 60.seconds

    // SLO-grenser vi vil kunne alarmere eksakt på. Må være sortert.
    // Den øverste ligger bevisst over forventetHoeyest: i prod er høyest observerte svartid 91s
    // (/external/api/v1/brev), altså over timeouten mot brevbaker, så uten denne ville halen
    // havnet i +Inf. Grenser over ytterpunktet tas med av micrometer uten at det genereres tett
    // bucket-oppløsning i et område vi sjelden er i.
    private val latencyBuckets = listOf(
        50.milliseconds,
        100.milliseconds,
        250.milliseconds,
        500.milliseconds,
        1.seconds,
        2.seconds,
        5.seconds,
        10.seconds,
        30.seconds,
        60.seconds,
        120.seconds,
    ).map { it.inWholeNanoseconds.toDouble() }

    fun Application.configureMetrics() {
        // Ktor tagger hver request med address=<podnavn>:<port>. Det er redundant med labelene
        // nais legger på ved scraping, og gir nye tidsserier for hver deploy.
        prometheusRegistry.config().meterFilter(MeterFilter.ignoreTags("address"))

        install(MicrometerMetrics) {
            registry = prometheusRegistry
            // Ktor eksporterer som standard latens som en summary med klientside-kvantiler, og de
            // kan ikke aggregeres på tvers av poder - en p99 fra én pod sier ingenting om p99 for
            // tjenesten. Når vi setter bucket-grenser eksporteres metrikken i stedet som et ekte
            // histogram, slik at histogram_quantile() kan brukes i alarmer og dashboards.
            //
            // percentilesHistogram gir et tett sett med buckets innenfor min/max, slik at
            // histogram_quantile() blir presis uansett hvor latensen faktisk legger seg.
            // serviceLevelObjectives kommer i tillegg til disse, og gir oss eksakte grenser å
            // alarmere mot ("andel raskere enn 2s"). Uten min/max ville micrometer generert 69
            // buckets per rute og statuskode, som blir unødvendig mange tidsserier.
            distributionStatisticConfig = DistributionStatisticConfig.Builder()
                .percentilesHistogram(true)
                .minimumExpectedValue(forventetLavest.inWholeNanoseconds.toDouble())
                .maximumExpectedValue(forventetHoeyest.inWholeNanoseconds.toDouble())
                .serviceLevelObjectives(*latencyBuckets.toDoubleArray())
                .build()
        }
        routing {
            get("/metrics") {
                call.respond(prometheusRegistry.scrape())
            }
        }
    }
}