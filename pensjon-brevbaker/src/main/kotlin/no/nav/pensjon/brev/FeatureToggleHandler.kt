package no.nav.pensjon.brev

import io.getunleash.DefaultUnleash
import io.getunleash.FakeUnleash
import io.getunleash.Unleash
import io.getunleash.UnleashContext
import io.getunleash.util.UnleashConfig
import no.nav.pensjon.brev.api.FeatureToggleService
import no.nav.pensjon.brev.api.model.FeatureToggle
import no.nav.pensjon.brev.api.model.FeatureToggleSingleton

const val unleashTogglePrefix = "pensjonsbrev.brevbaker."

object FeatureToggleHandler : FeatureToggleService {
    private lateinit var unleash: Unleash

    override fun isEnabled(toggle: FeatureToggle): Boolean =
        unleash.isEnabled(unleashTogglePrefix + toggle.key(), UnleashContext.builder().build())

    fun configure(block: FeatureToggleConfig.() -> Unit) {
        unleash = createUnleash(FeatureToggleConfig().apply(block))
        FeatureToggleSingleton.init(this)
    }

    private fun createUnleash(config: FeatureToggleConfig): Unleash = with(config) {
        if (useFakeUnleash) {
            FakeUnleash().apply {
                if (fakeUnleashEnableAll) enableAll() else disableAll()
            }
        } else {
            DefaultUnleash(
                UnleashConfig.builder()
                    .appName(appName!!)
                    .environment(environment!!)
                    .unleashAPI("${host!!}/api")
                    .apiKey(apiToken!!).build()
            )
        }
    }

    fun shutdown() = unleash.shutdown()
}

class FeatureToggleConfig {
    var useFakeUnleash: Boolean = false
    var fakeUnleashEnableAll: Boolean = false

    var appName: String? = null
    var environment: String? = null
    var host: String? = null
    var apiToken: String? = null
}

