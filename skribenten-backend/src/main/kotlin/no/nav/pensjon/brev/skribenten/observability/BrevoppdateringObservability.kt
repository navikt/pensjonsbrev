package no.nav.pensjon.brev.skribenten.observability

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import io.opentelemetry.api.GlobalOpenTelemetry
import io.opentelemetry.api.trace.StatusCode
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.context.Context
import io.opentelemetry.context.ContextKey
import io.opentelemetry.extension.kotlin.asContextElement
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withContext
import no.nav.pensjon.brev.skribenten.Metrics
import no.nav.pensjon.brev.skribenten.common.Outcome
import org.slf4j.LoggerFactory

class BrevoppdateringObservability(
    private val registry: MeterRegistry = Metrics.registry,
    private val tracer: Tracer = GlobalOpenTelemetry.getTracer("no.nav.pensjon.brev.skribenten.brevoppdatering"),
) {
    enum class Fase(val tag: String) {
        TOTAL("total"),
        REQUEST("request_decode"),
        VERDIER("apply_values"),
        BREVDATA("fetch_letter_data"),
        RENDER_BREV("render_letter"),
        FLETT_BREV("merge_letter"),
        RENDER_VEDLEGG("render_attachments"),
        FLETT_VEDLEGG("merge_attachments"),
        DTO("build_dto"),
        API("map_api"),
        RESPONS("respond"),
        RESERVASJON_TRANSAKSJON("reservation_transaction"),
        RESERVASJON_BLOKK("reservation_block"),
        REDIGERING_TRANSAKSJON("editing_transaction"),
        REDIGERING_BLOKK("editing_block"),
    }

    enum class Transaksjon(val total: Fase, val blokk: Fase) {
        RESERVASJON(Fase.RESERVASJON_TRANSAKSJON, Fase.RESERVASJON_BLOKK),
        REDIGERING(Fase.REDIGERING_TRANSAKSJON, Fase.REDIGERING_BLOKK),
    }

    enum class Krypto(val tag: String) {
        KRYPTER("encrypt"),
        DEKRYPTER("decrypt"),
        AVLED_NOEKKEL("derive_key"),
    }

    private enum class Utfall(val tag: String) {
        SUCCESS("success"),
        DOMAIN_FAILURE("domain_failure"),
        NOT_FOUND("not_found"),
        EXCEPTION("exception"),
        CANCELLED("cancelled"),
    }

    private data class Scope(val observer: BrevoppdateringObservability, val submission: String)

    suspend fun <T : Outcome<*, *>?> observer(frigiReservasjon: Boolean, block: suspend () -> T): T {
        val scope = Scope(this, if (frigiReservasjon) "release_reservation" else "keep_reservation")
        return withContext(Context.current().with(scopeKey, scope).asContextElement()) {
            fase(Fase.TOTAL, block)
        }
    }

    private inner class Maaling(scope: Scope, fase: Fase? = null, krypto: Krypto? = null) {
        private val name = when {
            fase == Fase.TOTAL -> "skribenten.brev.update"
            fase?.tag != null -> "skribenten.brev.update.${fase.tag}"
            krypto != null -> "skribenten.brev.update.crypto.${krypto.tag}"
            else -> throw IllegalArgumentException("Required value was null: either `fase` or `krypto` must be non-null")
        }

        val span = tracer.spanBuilder(name).startSpan()!!
        private val sample = Timer.start(registry)
        private val tags = if (fase != null) {
            arrayOf("phase", fase.tag, "submission", scope.submission)
        } else {
            arrayOf("operation", requireNotNull(krypto).tag)
        }
        private val metricName = if (fase != null) Metrics.brevoppdateringMetricName else Metrics.brevoppdateringCryptoMetricName
        private var utfall = Utfall.SUCCESS

        init {
            span.setAttribute("skribenten.submission", scope.submission)
        }

        fun resultat(value: Any?) {
            utfall = when (value) {
                null -> Utfall.NOT_FOUND
                is Outcome.Failure<*> -> Utfall.DOMAIN_FAILURE
                else -> Utfall.SUCCESS
            }
        }

        fun feil(error: Throwable) {
            utfall = if (error is CancellationException) Utfall.CANCELLED else Utfall.EXCEPTION
            if (error !is CancellationException) span.setStatus(StatusCode.ERROR)
            // Exception messages can contain request data; retain only the technical type.
            span.setAttribute("error.type", error.javaClass.name)
        }

        fun avslutt() {
            try {
                span.setAttribute("skribenten.outcome", utfall.tag)
                sample.stop(registry.timer(metricName, *tags, "outcome", utfall.tag))
            } catch (measurementError: Exception) {
                logger.warn("Kunne ikke avslutte måling {}: {}", name, measurementError.javaClass.name)
            } finally {
                try {
                    span.end()
                } catch (measurementError: Exception) {
                    logger.warn("Kunne ikke avslutte span {}: {}", name, measurementError.javaClass.name)
                }
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(BrevoppdateringObservability::class.java)
        private val scopeKey = ContextKey.named<Scope>("skribenten-brevoppdatering")

        suspend fun <T> fase(fase: Fase?, block: suspend () -> T): T {
            val scope = Context.current().get(scopeKey) ?: return block()
            if (fase == null) return block()
            val maaling = scope.observer.Maaling(scope, fase = fase)
            return try {
                withContext(Context.current().with(maaling.span).asContextElement()) {
                    block().also(maaling::resultat)
                }
            } catch (error: Throwable) {
                maaling.feil(error)
                throw error
            } finally {
                maaling.avslutt()
            }
        }

        fun <T> krypto(operasjon: Krypto, block: () -> T): T {
            val scope = Context.current().get(scopeKey) ?: return block()
            val maaling = scope.observer.Maaling(scope, krypto = operasjon)
            return try {
                Context.current().with(maaling.span).makeCurrent().use {
                    block().also(maaling::resultat)
                }
            } catch (error: Throwable) {
                maaling.feil(error)
                throw error
            } finally {
                maaling.avslutt()
            }
        }
    }
}
