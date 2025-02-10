package no.nav.pensjon.brev.pdfbygger

import io.micrometer.core.instrument.Tag
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import java.util.concurrent.atomic.AtomicInteger

internal class ActiveCounter {
    private val activeJobs = AtomicInteger(0)

    private fun register(registry: PrometheusMeterRegistry, name: String, tags: List<Tag> = emptyList()) {
        registry.gauge(name, tags , activeJobs)
    }

    internal suspend fun <T : Any> count(block: suspend () -> T): T {
        activeJobs.getAndUpdate { maxOf(it + 1, 1) }

        return try {
            block()
        } finally {
            activeJobs.getAndUpdate { maxOf(it - 1, 0) }
        }
    }

    internal fun currentCount(): Int = activeJobs.get()

    companion object {
        operator fun invoke(registry: PrometheusMeterRegistry, name: String, tags: List<Tag> = emptyList()): ActiveCounter =
            ActiveCounter().apply {
                register(registry, "$name-active", tags)
            }
    }
}