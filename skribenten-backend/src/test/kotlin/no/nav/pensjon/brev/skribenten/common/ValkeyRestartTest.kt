package no.nav.pensjon.brev.skribenten.common

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.skribenten.ValkeyConfig
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.parallel.Isolated
import org.testcontainers.containers.wait.strategy.Wait
import kotlin.time.Duration.Companion.minutes

@Isolated
class ValkeyRestartTest {

    val valkeyConfig: ValkeyConfig

    init {
        valkeyContainer.withExposedPorts(6379)
        valkeyContainer.waitingFor(Wait.forListeningPort())
        valkeyContainer.start()
        valkeyConfig = ValkeyConfig(
            host = valkeyContainer.host,
            port = valkeyContainer.getMappedPort(6379),
            username = "default",
            password = "",
            ssl = false,
            enabled = true,
        )
    }

    @Test
    fun `henter fortsatt verdi etter at tilkoblinger droppes server-side`() {
        val cache = Valkey(valkeyConfig)
        val key = "gjenoppretting"
        runBlocking {
            cache.update(key, "verdi", 10.minutes)
            assertEquals("verdi", cache.read(key))

            // Simulerer en Aiven-failover/nodebytte
            valkeyContainer.execInContainer("valkey-cli", "CLIENT", "KILL", "TYPE", "normal")

            assertEquals("verdi", cache.read(key))
        }
    }
}