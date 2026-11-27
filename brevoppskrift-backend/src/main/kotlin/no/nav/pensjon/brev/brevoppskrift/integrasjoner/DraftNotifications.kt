package no.nav.pensjon.brev.brevoppskrift.integrasjoner

import kotlinx.coroutines.*
import no.nav.pensjon.brev.brevoppskrift.lagring.DraftRecord
import org.slf4j.LoggerFactory
import java.io.Closeable

class DraftNotifications(
    private val service: NotificationService?,
    private val webUrl: WorkshopUrl,
) : Closeable {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val logger = LoggerFactory.getLogger("brevoppskrift.notifications")
    val enabled: Boolean get() = service != null
    internal val isActive: Boolean get() = scope.isActive

    fun notify(type: String, record: DraftRecord, count: Int = 0) {
        val service = service ?: return
        scope.launch {
            try {
                withTimeout(3000) { service.send(Notification(type, record.draft.metadata.name, count, if (record.archivedAt == null) "${webUrl.value}/arbeidsutkast/${record.id.value}" else null)) }
            } catch (cancelled: CancellationException) {
                if (cancelled !is TimeoutCancellationException) throw cancelled
                logger.warn("Slack notification timed out")
            } catch (_: Exception) {
                logger.warn("Slack notification failed")
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
