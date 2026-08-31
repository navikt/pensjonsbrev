package no.nav.pensjon.brev

import io.ktor.server.application.*
import io.ktor.server.metrics.micrometer.*
import io.ktor.server.request.path
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.micrometer.core.instrument.config.MeterFilter
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

object Metrics {
    val prometheusRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)

    // Helsesjekker og metrikkendepunktet kalles av kubernetes og prometheus uavhengig av last
    // (~0,25 req/s per pod). De holdes derfor utenfor både access-loggen og metrikkene:
    //  - De fortynner nevneren i feilrate-alarmer. I prod utgjør de ~79 % av trafikken ved
    //    normal last, slik at en alarm på 5 % feilrate først ville utløst når ~25 % av de ekte
    //    kallene feilet. Fortynningen er verst når trafikken er lav - altså nettopp når en
    //    stanset batch gir få ekte kall å måle på.
    //  - Hver rute koster ~58 tidsserier fordi den får hele bucket-settet, for en latens som er
    //    konstant og under millisekundet.
    // /ping_authorized er også en helsesjekk, bare bak autentisering.
    private val ikkeObserverteStier = setOf("/isAlive", "/isReady", "/metrics", "/ping_authorized")

    // Både CallLogging og MicrometerMetrics tar med kallet når predikatet er true.
    fun skalObserveres(call: ApplicationCall): Boolean = call.request.path() !in ikkeObserverteStier

    // Brevbaker kaller pdf-bygger og bruker derfor sekunder, ikke millisekunder, på /letter-rutene.
    // Ytterpunktene styrer hvor micrometer genererer automatiske buckets.
    // Høyest observerte svartid i prod er 8,5s (/letter/redigerbar/pdf), så taket har god margin.
    // Nedre grense er beholdt selv om /templates-rutene ligger på 2-7ms: de alarmerer vi ikke på,
    // og et gulv på 10ms ville kostet 18 ekstra buckets per rute.
    private val forventetLavest = 100.milliseconds
    private val forventetHoeyest = 60.seconds

    // SLO-grenser vi vil kunne alarmere eksakt på. Må være sortert.
    // De to øverste ligger bevisst over forventetHoeyest: kall mot pdf-bygger avbrytes først av
    // withTimeoutOrNull(300s) i PensjonPdfByggerService, og uten disse ville alt mellom 60s og
    // 300s havnet i +Inf. Grenser over ytterpunktet tas med av micrometer uten at det genereres
    // tett bucket-oppløsning i et område vi sjelden er i.
    private val latencyBuckets = listOf(
        100.milliseconds,
        250.milliseconds,
        500.milliseconds,
        1.seconds,
        2.seconds,
        5.seconds,
        10.seconds,
        20.seconds,
        30.seconds,
        60.seconds,
        120.seconds,
        300.seconds,
    ).map { it.inWholeNanoseconds.toDouble() }

    fun Application.configureMetrics() {
        // Ktor tagger hver request med address=<podnavn>:<port>. Det er redundant med labelene
        // nais legger på ved scraping, og gir nye tidsserier for hver deploy.
        prometheusRegistry.config().meterFilter(MeterFilter.ignoreTags("address"))

        install(MicrometerMetrics) {
            registry = prometheusRegistry
            filter(::skalObserveres)
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