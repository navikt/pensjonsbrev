package no.nav.pensjon.brev.brevoppskrift.integrasjoner

import io.micrometer.core.instrument.MeterRegistry
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import java.io.Closeable
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.time.Duration.Companion.seconds

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
                    if (ticket == null) { delay(1.seconds); continue }
                    val url = try {
                        withTimeout(150.seconds) { service.create(ticket) }
                    } catch (failure: IntegrationFailure) {
                        repository.finish(ticket, null, failure.code)
                        metrics.counter("brevoppskrift.pr.jobs", "status", "failed").increment()
                        continue
                    } catch (_: TimeoutCancellationException) {
                        repository.finish(ticket, null, "GITHUB_TIMEOUT")
                        continue
                    } catch (cancelled: CancellationException) {
                        throw cancelled
                    } catch (_: Exception) {
                        repository.finish(ticket, null, "PR_FAILED")
                        continue
                    }
                    repository.finish(ticket, url, null)
                    metrics.counter("brevoppskrift.pr.jobs", "status", "succeeded").increment()
                } catch (cancelled: CancellationException) {
                    throw cancelled
                } catch (_: Exception) {
                    logger.warn("PR worker unavailable")
                    delay(5.seconds)
                }
            }
        }
    }

    override fun close() {
        scope.cancel()
        runBlocking {
            withTimeoutOrNull(5.seconds) { scope.coroutineContext[Job]?.join() }
        }
    }
}
