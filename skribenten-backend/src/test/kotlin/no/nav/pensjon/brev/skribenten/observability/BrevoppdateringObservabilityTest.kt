package no.nav.pensjon.brev.skribenten.observability

import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import io.micrometer.core.instrument.Meter
import io.micrometer.core.instrument.Timer
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig
import io.micrometer.core.instrument.distribution.pause.PauseDetector
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.api.common.AttributeKey
import io.opentelemetry.api.trace.Span
import io.opentelemetry.api.trace.SpanBuilder
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.api.trace.StatusCode
import io.opentelemetry.context.Context
import io.opentelemetry.extension.kotlin.asContextElement
import io.opentelemetry.sdk.testing.exporter.InMemorySpanExporter
import io.opentelemetry.sdk.trace.SdkTracerProvider
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor
import io.opentelemetry.sdk.trace.samplers.Sampler
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import no.nav.pensjon.brev.skribenten.Metrics
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.observability.BrevoppdateringObservability.Companion.fase
import no.nav.pensjon.brev.skribenten.observability.BrevoppdateringObservability.Companion.krypto
import no.nav.pensjon.brev.skribenten.observability.BrevoppdateringObservability.Fase
import no.nav.pensjon.brev.skribenten.observability.BrevoppdateringObservability.Kryptering
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit

class BrevoppdateringObservabilityTest {
    @Test
    suspend fun `parallelle kall beholder egne foreldre gjennom suspensjon og dispatcherbytte`() {
        BrevoppdateringTestTelemetry().use { telemetry ->
            val original = Context.current()
            val ready = List(2) { CompletableDeferred<Unit>() }
            coroutineScope {
                listOf(false, true).mapIndexed { index, release ->
                    async(Dispatchers.Default) {
                        val http = telemetry.tracer.spanBuilder("http").startSpan()
                        try {
                            withContext(Context.current().with(http).asContextElement()) {
                                telemetry.observer.observer(release) {
                                    fase(Fase.RENDER_BREV) {
                                        ready[index].complete(Unit)
                                        ready[1 - index].await()
                                        yield()
                                        withContext(Dispatchers.IO) {
                                            krypto(Kryptering.KRYPTER) { assertThat(Span.current().spanContext.isValid).isTrue() }
                                        }
                                    }
                                    success(Unit)
                                }
                                assertThat(Span.current().spanContext).isEqualTo(http.spanContext)
                            }
                        } finally {
                            http.end()
                        }
                    }
                }.awaitAll()
            }

            val spans = telemetry.exporter.finishedSpanItems
            assertThat(spans).hasSize(8)
            val roots = spans.filter { it.name == "skribenten.brev.update" }
            assertThat(roots.map { it.traceId }).doesNotHaveDuplicates()
            roots.forEach { root ->
                val http = spans.single { it.spanId == root.parentSpanId }
                assertThat(http.name).isEqualTo("http")
                val phase = spans.single { it.parentSpanId == root.spanId }
                assertThat(phase.name).isEqualTo("skribenten.brev.update.render_letter")
                val crypto = spans.single { it.parentSpanId == phase.spanId }
                assertThat(crypto.name).isEqualTo("skribenten.brev.update.crypto.encrypt")
                assertThat(crypto.attributes.get(AttributeKey.stringKey("skribenten.submission")))
                    .isEqualTo(root.attributes.get(AttributeKey.stringKey("skribenten.submission")))
            }
            assertThat(Context.current()).isSameAs(original)
            fase(Fase.REQUEST) { Unit }
            krypto(Kryptering.KRYPTER) { Unit }
            assertThat(telemetry.exporter.finishedSpanItems).hasSize(8)
            assertThat(telemetry.registry.find(Metrics.brevoppdateringMetricName).tag("phase", "request_decode").timer()).isNull()
        }
    }

    @Test
    suspend fun `skiller suksess domeneavvisning og ikke funnet uten aa endre resultatet`() {
        BrevoppdateringTestTelemetry().use { telemetry ->
            val results: List<Pair<Outcome<*, *>?, String>> = listOf(
                success(Unit) to "success",
                failure("not exported") to "domain_failure",
                null to "not_found",
            )
            results.forEach { (result, outcome) ->
                assertThat(telemetry.observer.observer(false) { result }).isSameAs(result)
                val span = telemetry.exporter.finishedSpanItems.last()
                assertThat(span.attributes.get(AttributeKey.stringKey("skribenten.outcome"))).isEqualTo(outcome)
                assertThat(telemetry.registry.get(Metrics.brevoppdateringMetricName).tag("outcome", outcome).timer().count()).isEqualTo(1)
            }
        }
    }

    @Test
    suspend fun `unntak og kansellering lukkes og propageres uten sensitive feildetaljer`() {
        BrevoppdateringTestTelemetry().use { telemetry ->
            listOf(IllegalStateException("not exported"), CancellationException("not exported")).forEach { error ->
                val previous = Context.current()
                val caught = runCatching {
                    telemetry.observer.observer(false) {
                        fase(Fase.API) {
                            withContext(Dispatchers.IO) {
                                yield()
                                throw error
                            }
                        }
                    }
                }.exceptionOrNull()
                assertThat(generateSequence(caught) { it.cause }.last()).isSameAs(error)
                assertThat(Context.current()).isSameAs(previous)
                val outcome = if (error is CancellationException) "cancelled" else "exception"
                val spans = telemetry.exporter.finishedSpanItems.takeLast(2)
                assertThat(spans).hasSize(2)
                spans.forEach { span ->
                    assertThat(span.attributes.get(AttributeKey.stringKey("skribenten.outcome"))).isEqualTo(outcome)
                    assertThat(span.attributes.get(AttributeKey.stringKey("error.type"))).isEqualTo(error.javaClass.name)
                    assertThat(span.attributes.asMap().keys.map { it.key })
                        .containsExactlyInAnyOrder("skribenten.submission", "skribenten.outcome", "error.type")
                    assertThat(span.events).isEmpty()
                    assertThat(span.status.description).isEmpty()
                    assertThat(span.status.statusCode).isEqualTo(if (error is CancellationException) StatusCode.UNSET else StatusCode.ERROR)
                }
            }
        }
    }

    @Test
    suspend fun `aktiv kansellering avslutter maalingene og lekker ikke scope`() {
        BrevoppdateringTestTelemetry().use { telemetry ->
            val ready = CompletableDeferred<Unit>()
            coroutineScope {
                val job = launch {
                    telemetry.observer.observer(false) {
                        fase(Fase.BREVDATA) {
                            ready.complete(Unit)
                            awaitCancellation()
                        }
                    }
                }
                ready.await()
                job.cancel()
                job.join()
            }
            assertThat(telemetry.exporter.finishedSpanItems).hasSize(2).allSatisfy {
                assertThat(it.attributes.get(AttributeKey.stringKey("skribenten.outcome"))).isEqualTo("cancelled")
            }
            fase(Fase.REQUEST) { Unit }
            assertThat(telemetry.registry.meters).hasSize(2)
        }
    }

    @Test
    suspend fun `maalingsfeil bevarer returverdier unntak og kansellering og avslutter spans`() {
        val registry = FailingRecordingMeterRegistry()
        try {
            BrevoppdateringTestTelemetry().use { telemetry ->
                val observer = BrevoppdateringObservability(registry, telemetry.tracer)
                val previous = Context.current()
                val results: List<Outcome<*, *>?> = listOf(success(Unit), failure(Unit), null)
                results.forEach { result ->
                    assertThat(observer.observer(false) {
                        fase(Fase.DTO) { krypto(Kryptering.KRYPTER) { result } }
                    }).isSameAs(result)
                }
                listOf(IllegalArgumentException("not exported"), CancellationException("not exported")).forEach { error ->
                    val caught = runCatching {
                        observer.observer(false) {
                            fase(Fase.DTO) { krypto(Kryptering.KRYPTER) { throw error } }
                        }
                    }.exceptionOrNull()
                    assertThat(generateSequence(caught) { it.cause }.last()).isSameAs(error)
                    assertThat(error.suppressed).isEmpty()
                }
                assertThat(registry.recordAttempts).isEqualTo(15)
                assertThat(telemetry.exporter.finishedSpanItems).hasSize(15)
                assertThat(Context.current()).isSameAs(previous)
            }
        } finally {
            registry.close()
        }
    }

    @Test
    suspend fun `feil fra span end bevarer resultat og opprinnelig applikasjonsfeil`() {
        BrevoppdateringTestTelemetry().use { telemetry ->
            var endings = 0
            val failingTracer = object : Tracer by telemetry.tracer {
                override fun spanBuilder(spanName: String): SpanBuilder {
                    val builder = telemetry.tracer.spanBuilder(spanName)
                    return object : SpanBuilder by builder {
                        override fun startSpan(): Span {
                            val span = builder.startSpan()
                            return object : Span by span {
                                override fun end() {
                                    endings++
                                    span.end()
                                    throw IllegalStateException("not exported")
                                }
                            }
                        }
                    }
                }
            }
            val observer = BrevoppdateringObservability(telemetry.registry, failingTracer)
            val result = success(Unit)
            assertThat(observer.observer(false) { fase(Fase.DTO) { result } }).isSameAs(result)
            val error = IllegalArgumentException("application")
            val caught = runCatching {
                observer.observer(false) { fase(Fase.DTO) { throw error } }
            }.exceptionOrNull()
            assertThat(generateSequence(caught) { it.cause }.last()).isSameAs(error)
            assertThat(endings).isEqualTo(4)
            assertThat(telemetry.exporter.finishedSpanItems).hasSize(4)
        }
    }

    @Test
    suspend fun `metrikker registreres uten sampling og uten agent`() {
        BrevoppdateringTestTelemetry(Sampler.alwaysOff()).use { telemetry ->
            telemetry.observer.observer(false) {
                fase(Fase.RENDER_BREV) { krypto(Kryptering.KRYPTER) { Unit } }
                success(Unit)
            }
            assertThat(telemetry.exporter.finishedSpanItems).isEmpty()
            assertThat(telemetry.registry.meters).hasSize(3)
            assertThat(telemetry.registry.get(Metrics.brevoppdateringCryptoMetricName).timer().count()).isEqualTo(1)
        }
        val registry = SimpleMeterRegistry()
        try {
            BrevoppdateringObservability(registry, OpenTelemetry.noop().getTracer("test")).observer(true) { success(Unit) }
            assertThat(registry.get(Metrics.brevoppdateringMetricName).tag("submission", "release_reservation").timer().count()).isEqualTo(1)
        } finally {
            registry.close()
        }
    }

    @Test
    suspend fun `histogrammet har kun faste labels og avgrensede buckets`() {
        val registry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
        try {
            registry.config().meterFilter(Metrics.brevoppdateringFilter())
            val observer = BrevoppdateringObservability(registry, OpenTelemetry.noop().getTracer("test"))
            observer.observer(false) {
                fase(Fase.RENDER_BREV) { krypto(Kryptering.KRYPTER) { Unit } }
                success(Unit)
            }
            val lines = registry.scrape().lines().filter { it.startsWith("skribenten_brevoppdatering") }
            assertThat(lines).hasSize(39)
            val buckets = lines.filter { it.startsWith("${Metrics.brevoppdateringMetricName}_bucket") && it.contains("phase=\"total\"") }
            val bounds = buckets.map { Regex("""[,{]le="([^"]+)"""").find(it)!!.groupValues[1] }
            assertThat(bounds).containsExactly("0.001", "0.01", "0.1", "0.5", "1.0", "2.0", "5.0", "10.0", "60.0", "+Inf")
            assertThat(lines).noneMatch { it.contains("quantile=") }
            registry.meters.forEach { meter ->
                val expected = if (meter.id.name == Metrics.brevoppdateringMetricName) {
                    listOf("phase", "submission", "outcome")
                } else {
                    listOf("operation", "outcome")
                }
                assertThat(meter.id.tags.map { it.key }).containsExactlyInAnyOrderElementsOf(expected)
            }
        } finally {
            registry.close()
        }
    }
}

internal class BrevoppdateringTestTelemetry(sampler: Sampler = Sampler.alwaysOn()) : AutoCloseable {
    val registry = SimpleMeterRegistry()
    val exporter = InMemorySpanExporter.create()
    private val provider = SdkTracerProvider.builder()
        .setSampler(sampler)
        .addSpanProcessor(SimpleSpanProcessor.create(exporter))
        .build()
    val tracer = provider.get("test")
    val observer = BrevoppdateringObservability(registry, tracer)

    override fun close() {
        provider.close()
        registry.close()
    }
}

internal class FailingRecordingMeterRegistry : SimpleMeterRegistry() {
    var recordAttempts = 0
        private set

    override fun newTimer(id: Meter.Id, distributionStatisticConfig: DistributionStatisticConfig, pauseDetector: PauseDetector): Timer {
        val timer = super.newTimer(id, distributionStatisticConfig, pauseDetector)
        return object : Timer by timer {
            override fun record(amount: Long, unit: TimeUnit) {
                recordAttempts++
                throw IllegalStateException("not exported")
            }
        }
    }
}
