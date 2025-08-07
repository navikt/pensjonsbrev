package no.nav.pensjon.brev

import no.nav.brev.brevbaker.TestTags
import no.nav.pensjon.brev.maler.FeatureToggles
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.nio.file.Paths
import kotlin.io.path.notExists
import kotlin.io.path.readLines
import kotlin.test.assertEquals

class FeatureToggleHandlerTest {

    @Test
    @Tag(TestTags.INTEGRATION_TEST)
    fun `alle definerte brytere fins`() {
        val unleashFile = Paths.get("secrets").resolve("unleash.env")
        if (unleashFile.notExists()) {
            println("Unleash-secrets-fil fins ikke, avslutter")
            return
        }

        unleashFile.readLines().map { it.split("=") }.associate { it[0] to it[1] }.let {
            FeatureToggleHandler.configure {
                useFakeUnleash = false
                fakeUnleashEnableAll = false
                appName = "pensjon-brevbaker"
                environment = it["UNLEASH_SERVER_API_ENV"]
                host = it["UNLEASH_SERVER_API_URL"]
                apiToken = it["UNLEASH_SERVER_API_TOKEN"]
            }
        }

        val alleBrevbakerbrytereIUnleash = FeatureToggleHandler.alleDefinerteBrytere()

        val ikkeDefinerte = FeatureToggles.entries.filterNot {
            alleBrevbakerbrytereIUnleash.contains("pensjonsbrev.brevbaker.${it.toggle().name}")
        }

        assertEquals(expected = emptyList(), actual = ikkeDefinerte)
    }

}