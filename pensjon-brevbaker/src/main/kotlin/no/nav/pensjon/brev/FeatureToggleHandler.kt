package no.nav.pensjon.brev

import io.getunleash.DefaultUnleash
import io.getunleash.Unleash
import io.getunleash.UnleashContext
import io.getunleash.util.UnleashConfig

const val unleashTogglePrefix = "pensjonsbrev.brevbaker."

object FeatureToggleHandler {

    private lateinit var unleashAction: () -> Unleash
    private val unleash: Unleash by lazy { unleashAction() }

    fun isEnabled(toggle: UnleashToggle): Boolean =
        unleash.isEnabled(unleashTogglePrefix + toggle.name, UnleashContext.builder().build())

    fun configure(block: FeatureToggleConfig.() -> Unit) {
        Builder().setConfig(FeatureToggleConfig().apply(block)).build()
    }

    private class Builder {
        private lateinit var config: FeatureToggleConfig
        private var state: InitState = InitState.NEW

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
            unleashAction = { config.unleash(config) }
            state = InitState.DONE
        }
    }
}

enum class InitState { NEW, DONE }

class FeatureToggleConfig {
    internal lateinit var appName: String
    internal lateinit var environment: String
    internal lateinit var host: String
    internal lateinit var apiToken: String
    internal var unleash: ((FeatureToggleConfig) -> Unleash) = {
        DefaultUnleash(
            UnleashConfig.builder()
                .appName(appName)
                .environment(environment)
                .unleashAPI("$host/api")
                .apiKey(apiToken).build()
        )
    }
}

