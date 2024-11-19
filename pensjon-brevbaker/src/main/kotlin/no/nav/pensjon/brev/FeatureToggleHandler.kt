package no.nav.pensjon.brev

import io.getunleash.DefaultUnleash
import io.getunleash.Unleash
import io.getunleash.UnleashContext

private const val unleashTogglePrefix = "pensjonsbrev.brevbaker."

object FeatureToggleHandler {

    private lateinit var unleashAction: () -> Unleash
    private val unleash: Unleash by lazy { unleashAction() }

    fun isEnabled(toggle: UnleashToggle): Boolean = unleash.isEnabled(unleashTogglePrefix + toggle.name, UnleashContext.builder().build())

    class Builder {
        private lateinit var config: FeatureToggleConfig
        private var state: InitState = InitState.NEW
        private lateinit var unleashBuilder: ((FeatureToggleConfig) -> Unleash)

        fun setConfig(config: FeatureToggleConfig) = apply {
            this.config = config
        }

        fun setUnleash(action: ((config: FeatureToggleConfig) -> Unleash)?) = apply {
            this.unleashBuilder = action
                ?: { DefaultUnleash(
                    io.getunleash.util.UnleashConfig.builder()
                        .appName(config.appName)
                        .environment(config.environment)
                        .unleashAPI(config.host + "/api")
                        .apiKey(config.apiToken).build()
                ) }
        }

        fun build() = FeatureToggleHandler.apply {
            if (state == InitState.DONE) {
                throw IllegalStateException("Kan ikke sette opp Unleash flere ganger")
            }
            if (!::config.isInitialized) {
                throw IllegalStateException("Må sette konfig")
            }
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

