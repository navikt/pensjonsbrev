package no.nav.pensjon.brev.pdfbygger

import com.natpryce.hamkrest.assertion.assertThat
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class ActiveCounterTest {

    @Test
    fun `currentCount is as expected while jobs are running`() {
        val counter = ActiveCounter()

        assertEquals(
            2,
            counter.runAfterJobsStarted(2) {
                it.currentCount()
            }
        )
    }

    @Test
    fun `currentCount is 0 before and after jobs`() {
        val counter = ActiveCounter()

        assertEquals(0, runBlocking { counter.currentCount() })
        counter.runAfterJobsStarted(2) { }
        assertEquals(0, runBlocking { counter.currentCount() })
    }

    @Test
    fun `currentCount is at least awaitJobsCount`() {
        val counter = ActiveCounter()

        assertTrue(counter.runAfterJobsStarted(100, awaitJobsCount = 20) { it.currentCount() } >= 20)
    }

    private fun <T> ActiveCounter.runAfterJobsStarted(jobCount: Int, awaitJobsCount: Int = jobCount, jobDelay: Duration = 10.seconds, queueBody: suspend (ActiveCounter) -> T) = runBlocking {
        val scope = CoroutineScope(Dispatchers.Default)

        val hasStarted = Channel<Int>()

        // fill counter with slow jobs
        val jobs = (0 ..< jobCount).map {
            scope.launch {
                this@runAfterJobsStarted.count {
                    hasStarted.send(it)
                    delay(jobDelay)
                }
            }
        }

        // wait until jobs have started
        repeat(awaitJobsCount.coerceAtMost(jobCount)) { hasStarted.receive() }

        return@runBlocking try {
            queueBody(this@runAfterJobsStarted)
        } finally {
            jobs.forEach { it.cancelAndJoin() }
        }
    }

}