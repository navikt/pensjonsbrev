package no.nav.pensjon.brev

import io.getunleash.DefaultUnleash
import io.getunleash.Unleash
import io.getunleash.UnleashContext

private const val unleashTogglePrefix = "pensjonsbrev.brevbaker."

object FeatureToggleHandler {

    private lateinit var unleash: Unleash
    private lateinit var overrides: Map<UnleashToggle, Boolean>

    fun isEnabled(toggle: UnleashToggle): Boolean =
        overrides[toggle]
            ?: unleash.isEnabled(unleashTogglePrefix + toggle.name, context())

    private fun context(): UnleashContext = UnleashContext.builder().build()

    class Builder {
        private val builderOverrides: MutableMap<UnleashToggle, Boolean> = mutableMapOf()
        private lateinit var config: FeatureToggleConfig
        private var state: InitState = InitState.NEW

        fun override(key: UnleashToggle, value: Boolean) = apply {
            builderOverrides[key] = value
        }

        fun setConfig(config: FeatureToggleConfig) = apply {
            this.config = config
        }

        fun build() = FeatureToggleHandler.apply {
            if (state == InitState.DONE) {
                throw IllegalStateException("Kan ikke sette opp Unleash flere ganger")
            }
            if (!::config.isInitialized) {
                throw IllegalStateException("Må sette konfig")
            }
            overrides = builderOverrides.toMap()
            unleash = DefaultUnleash(
                io.getunleash.util.UnleashConfig.builder()
                    .appName(config.appName)
                    .environment(config.environment)
                    .unleashAPI(config.host + "/api")
                    .apiKey(config.apiToken).build()
            )
            state = InitState.DONE
        }
    }
}

enum class InitState { NEW, DONE }

data class FeatureToggleConfig(
    val appName: String,
    val environment: String,
    val host: String,
    val apiToken: String,
)

