package no.nav.pensjon.brev

import io.getunleash.DefaultUnleash
import io.getunleash.Unleash
import io.getunleash.UnleashContext

private const val unleashTogglePrefix = "pensjonsbrev.brevbaker."

object FeatureToggleInitializer {

    private var unleash: Unleash? = null
    private val overrides: Overrides = Overrides()

    fun initUnleash(config: UnleashConfig) {
        unleash = DefaultUnleash(
            io.getunleash.util.UnleashConfig.builder()
                .appName(config.appName)
                .environment(config.environment)
                .unleashAPI(config.host + "/api")
                .apiKey(config.apiToken).build()
        )
        overrides.init(config.overrides)
    }

    fun isEnabled(toggle: UnleashToggle): Boolean =
        overrides[toggle]
            ?: unleash?.isEnabled(unleashTogglePrefix + toggle.name, context())
            ?: false

    private fun context(): UnleashContext = UnleashContext.builder().build()
}

data class UnleashConfig(
    val appName: String,
    val environment: String,
    val host: String,
    val apiToken: String,
    val overrides: Map<UnleashToggle, Boolean>
)

class Overrides {
    private lateinit var overrides: Map<UnleashToggle, Boolean>
    val values by lazy {
        overrides
    }

    fun init(overrides: Map<UnleashToggle, Boolean>) {
        if (!::overrides.isInitialized) {
            this.overrides = overrides
        } else {
            throw IllegalStateException("Overrides already initialized")
        }
    }

    internal operator fun get(toggle: UnleashToggle) = overrides[toggle]
}