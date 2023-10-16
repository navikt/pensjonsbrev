package no.nav.pensjon.brev.pdfbygger

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ActiveCounter {
    private var activeJobs: Int = 0
    private val activeJobsMutex = Mutex()
    suspend fun <T : Any> count(block: suspend () -> T): T {
        activeJobsMutex.withLock { activeJobs++ }

        return try {
            block()
        } finally {
            activeJobsMutex.withLock {
                if (--activeJobs < 0) {
                    activeJobs = 0
                }
            }
        }
    }
    suspend fun currentCount(): Int = activeJobsMutex.withLock { activeJobs }

}