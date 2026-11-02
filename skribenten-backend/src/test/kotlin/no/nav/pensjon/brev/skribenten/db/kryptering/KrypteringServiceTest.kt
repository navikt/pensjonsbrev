package no.nav.pensjon.brev.skribenten.db.kryptering

import no.nav.pensjon.brev.skribenten.Metrics
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.observability.BrevoppdateringTestTelemetry
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class KrypteringServiceTest {

    private val service = KrypteringService.also { it.init("PZUgqsFjAc3GViO47HHMVQ==") }

    @Test
    fun `krypter data og les tilbake`() {
        val bytes = "Test string".encodeToByteArray()

        val kryptertBytes = service.krypter(bytes)
        assertThat(kryptertBytes).isNotEqualTo(bytes)

        val dekryptertBytes = service.dekrypter(kryptertBytes)
        assertThat(dekryptertBytes).isEqualTo(bytes)
    }

    @Test
    suspend fun `maaler krypto og noekkelavledning kun innenfor oppdateringsscope`() {
        BrevoppdateringTestTelemetry().use { telemetry ->
            val bytes = "test".encodeToByteArray()
            assertThat(service.dekrypter(service.krypter(bytes))).isEqualTo(bytes)
            assertThat(telemetry.registry.meters).isEmpty()

            telemetry.observer.observer(false) {
                assertThat(service.dekrypter(service.krypter(bytes))).isEqualTo(bytes)
                success(Unit)
            }
            assertThat(telemetry.registry.get(Metrics.brevoppdateringCryptoMetricName).tag("operation", "encrypt").timer().count()).isEqualTo(1)
            assertThat(telemetry.registry.get(Metrics.brevoppdateringCryptoMetricName).tag("operation", "decrypt").timer().count()).isEqualTo(1)
            assertThat(telemetry.registry.get(Metrics.brevoppdateringCryptoMetricName).tag("operation", "derive_key").timer().count()).isEqualTo(2)
            val spans = telemetry.exporter.finishedSpanItems
            assertThat(spans.filter { it.name.endsWith("derive_key") }).allSatisfy { derivation ->
                assertThat(spans.single { it.spanId == derivation.parentSpanId }.name)
                    .isIn("skribenten.brev.update.crypto.encrypt", "skribenten.brev.update.crypto.decrypt")
            }
        }
    }

    @Test
    suspend fun `ugyldig kryptert input beholder unntaket og telles som feil`() {
        BrevoppdateringTestTelemetry().use { telemetry ->
            val encrypted = service.krypter("test".encodeToByteArray()).bytes.copyOf()
            encrypted[encrypted.lastIndex] = (encrypted.last().toInt() xor 1).toByte()
            val caught = runCatching {
                telemetry.observer.observer(false) {
                    service.dekrypter(EncryptedByteArray(encrypted))
                    success(Unit)
                }
            }.exceptionOrNull()
            assertThat(caught).isInstanceOf(javax.crypto.AEADBadTagException::class.java)
            assertThat(telemetry.registry.get(Metrics.brevoppdateringCryptoMetricName)
                .tags("operation", "decrypt", "outcome", "exception").timer().count()).isEqualTo(1)
            assertThat(telemetry.exporter.finishedSpanItems).hasSize(3)
        }
    }
}