package no.nav.pensjon.brev.skribenten.db

import io.opentelemetry.api.trace.Span
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.yield
import no.nav.pensjon.brev.skribenten.Metrics
import no.nav.pensjon.brev.skribenten.brevredigering.application.BrevredigeringHandlerTestBase
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.model.SaksbehandlervalgMap
import no.nav.pensjon.brev.skribenten.model.SaksbehandlervalgVerdi
import no.nav.pensjon.brev.skribenten.observability.BrevoppdateringObservability.Transaksjon
import no.nav.pensjon.brev.skribenten.observability.BrevoppdateringTestTelemetry
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.v1.core.Transaction
import org.jetbrains.exposed.v1.core.statements.StatementContext
import org.jetbrains.exposed.v1.core.statements.StatementInterceptor
import org.junit.jupiter.api.Test
import java.sql.SQLException

class TransactionalObservabilityTest : BrevredigeringHandlerTestBase() {
    @Test
    suspend fun `automatisk flush og commit har aktivt ytre span etter at blokken er avsluttet`() {
        BrevoppdateringTestTelemetry().use { telemetry ->
            val original = opprettBrev().resultOrFail()
            val choices = SaksbehandlervalgMap().apply { put("valg", SaksbehandlervalgVerdi.Boolean(false)) }
            var bodyFinished = false
            var committed = false
            telemetry.observer.observer(false) {
                transactional.rollbackOnFailure(observasjon = Transaksjon.REDIGERING) {
                    registerInterceptor(object : StatementInterceptor {
                        override fun beforeExecution(transaction: Transaction, context: StatementContext) {
                            if (bodyFinished) telemetry.tracer.spanBuilder("test.completion_sql").startSpan().end()
                        }

                        override fun afterCommit(transaction: Transaction) {
                            committed = true
                            assertThat(bodyFinished).isTrue()
                            val finished = telemetry.exporter.finishedSpanItems
                            assertThat(finished.map { it.name }).contains("skribenten.brev.update.editing_block")
                            assertThat(finished.map { it.name }).doesNotContain("skribenten.brev.update.editing_transaction")
                            telemetry.tracer.spanBuilder("test.after_commit").startSpan().end()
                        }
                    })
                    BrevredigeringEntity.findByIdAndSaksId(original.info.id, sak1.saksId)!!.saksbehandlerValg = choices
                    yield()
                    bodyFinished = true
                    success(Unit)
                }
            }
            assertThat(committed).isTrue()
            assertThat(hentBrev(original.info.id).resultOrFail().saksbehandlerValg).isEqualTo(choices)
            val spans = telemetry.exporter.finishedSpanItems
            val outer = spans.single { it.name == "skribenten.brev.update.editing_transaction" }
            val body = spans.single { it.name == "skribenten.brev.update.editing_block" }
            val completion = spans.filter { it.name.startsWith("test.") }
            assertThat(completion.map { it.name }).contains("test.completion_sql", "test.after_commit")
            completion.forEach { span ->
                assertThat(span.parentSpanId).isEqualTo(outer.spanId)
                assertThat(span.startEpochNanos).isGreaterThanOrEqualTo(body.endEpochNanos)
                assertThat(span.endEpochNanos).isLessThanOrEqualTo(outer.endEpochNanos)
            }
            assertThat(body.parentSpanId).isEqualTo(outer.spanId)
            assertThat(Span.current().spanContext.isValid).isFalse()
        }
    }

    @Test
    suspend fun `domeneavvisning unntak og kansellering beholder rollback og utfall`() {
        BrevoppdateringTestTelemetry().use { telemetry ->
            val original = opprettBrev().resultOrFail()
            val error = IllegalStateException("not exported")
            val cancellation = CancellationException("not exported")
            listOf(null to "domain_failure", error to "exception", cancellation to "cancelled").forEach { (thrown, outcome) ->
                var rolledBack = false
                val result = runCatching {
                    telemetry.observer.observer(false) {
                        transactional.rollbackOnFailure(observasjon = Transaksjon.REDIGERING) {
                            registerInterceptor(object : StatementInterceptor {
                                override fun afterRollback(transaction: Transaction) {
                                    rolledBack = true
                                }
                            })
                            BrevredigeringEntity.findByIdAndSaksId(original.info.id, sak1.saksId)!!.saksbehandlerValg = SaksbehandlervalgMap()
                            if (thrown != null) throw thrown
                            failure(Unit)
                        }
                    }
                }
                if (thrown == null) {
                    assertThat(result.getOrThrow()).isInstanceOf(Outcome.Failure::class.java)
                } else {
                    assertThat(generateSequence(result.exceptionOrNull()) { it.cause }.last()).isSameAs(thrown)
                }
                assertThat(rolledBack).isTrue()
                assertThat(hentBrev(original.info.id).resultOrFail().saksbehandlerValg).isEqualTo(original.saksbehandlerValg)
                assertThat(telemetry.registry.get(Metrics.brevoppdateringMetricName)
                    .tags("phase", "editing_transaction", "outcome", outcome).timer().count()).isEqualTo(1)
                assertThat(telemetry.registry.get(Metrics.brevoppdateringMetricName)
                    .tags("phase", "editing_block", "outcome", outcome).timer().count()).isEqualTo(1)
            }
        }

    }

    @Test
    suspend fun `retry maaler hvert blokkforsoek under en transaksjon`() {
        BrevoppdateringTestTelemetry().use { telemetry ->
            var attempts = 0
            telemetry.observer.observer(false) {
                transactional.rollbackOnFailure(observasjon = Transaksjon.REDIGERING) {
                    maxAttempts = 2
                    minRetryDelay = 0
                    maxRetryDelay = 0
                    attempts++
                    yield()
                    if (attempts == 1) throw SQLException("retry", "40001")
                    success(Unit)
                }
            }
            assertThat(attempts).isEqualTo(2)
            val spans = telemetry.exporter.finishedSpanItems
            val outer = spans.single { it.name == "skribenten.brev.update.editing_transaction" }
            assertThat(spans.filter { it.name == "skribenten.brev.update.editing_block" }).hasSize(2).allSatisfy {
                assertThat(it.parentSpanId).isEqualTo(outer.spanId)
            }
            assertThat(telemetry.registry.get(Metrics.brevoppdateringMetricName).tag("phase", "total").timer().count()).isEqualTo(1)
            assertThat(telemetry.registry.get(Metrics.brevoppdateringMetricName).tag("phase", "editing_transaction").timer().count()).isEqualTo(1)
            assertThat(telemetry.registry.get(Metrics.brevoppdateringMetricName)
                .tags("phase", "editing_block", "outcome", "exception").timer().count()).isEqualTo(1)
            assertThat(telemetry.registry.get(Metrics.brevoppdateringMetricName)
                .tags("phase", "editing_block", "outcome", "success").timer().count()).isEqualTo(1)
        }
    }
}
