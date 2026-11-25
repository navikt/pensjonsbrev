package no.nav.pensjon.brev.brevoppskrift.integrasjoner

import io.micrometer.core.instrument.MeterRegistry
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import java.io.Closeable
import java.util.concurrent.atomic.AtomicBoolean

class PrJobWorker(
    private val service: PrService?,
    private val repository: PrJobRepository,
    private val metrics: MeterRegistry,
) : Closeable {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val logger = LoggerFactory.getLogger("brevoppskrift.pr")
    private val started = AtomicBoolean(false)
    internal val isActive: Boolean get() = scope.isActive

    fun start() {
        if (!started.compareAndSet(false, true)) return
        val service = service ?: return
        scope.launch {
            while (isActive) {
                try {
                    val ticket = repository.claim()
                    if (ticket == null) { delay(1000); continue }
                    try {
                        val url = withTimeout(150_000) { service.create(ticket) }
                        repository.finish(ticket, url, null)
                        metrics.counter("brevoppskrift.pr.jobs", "status", "succeeded").increment()
                    } catch (failure: IntegrationFailure) {
                        repository.finish(ticket, null, failure.code)
                        metrics.counter("brevoppskrift.pr.jobs", "status", "failed").increment()
                    } catch (_: TimeoutCancellationException) {
                        repository.finish(ticket, null, "GITHUB_TIMEOUT")
                    } catch (cancelled: CancellationException) {
                        throw cancelled
                    } catch (_: Exception) {
                        repository.finish(ticket, null, "PR_FAILED")
                    }
                } catch (cancelled: CancellationException) {
                    throw cancelled
                } catch (_: Exception) {
                    logger.warn("PR worker unavailable")
                    delay(5000)
                }
            }
        }
    }

    override fun close() {
        scope.cancel()
        runBlocking {
            withTimeoutOrNull(5000) { scope.coroutineContext[Job]?.join() }
        }
    }
}
