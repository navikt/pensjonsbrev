package no.nav.pensjon.brev.skribenten

import com.typesafe.config.Config
import io.getunleash.DefaultUnleash
import io.getunleash.Unleash
import io.getunleash.util.UnleashConfig
import kotlin.reflect.KProperty

object Features {
    val brevbakerbrev: Boolean by UnleashToggle()

    var unleash: Unleash? = null
        private set

    private val _overrides = mutableMapOf<String, Boolean>()
    val overrides: Map<String, Boolean> = _overrides
    fun override(key: String, value: Boolean) {
        _overrides[key] = value
    }

    fun initUnleash(config: Config) {
        unleash = DefaultUnleash(
            UnleashConfig.builder()
                .appName(config.getString("appName"))
                .environment(config.getString("environment"))
                .unleashAPI(config.getString("host") + "/api")
                .apiKey(config.getString("apiToken")).build()
        )
    }
}

private class EnvironmentToggle {
    fun isEnabled(property: KProperty<*>): Boolean =
        System.getenv().getOrDefault("FEATURE_${property.name}", "false").toBoolean()

    operator fun getValue(thisRef: Features, property: KProperty<*>): Boolean =
        thisRef.overrides[property.name] ?: isEnabled(property)
}

private const val unleashTogglePrefix = "pensjonsbrev.skribenten."

private class UnleashToggle {
    fun isEnabled(thisRef: Features, property: KProperty<*>): Boolean? =
        thisRef.unleash?.isEnabled(unleashTogglePrefix + property.name)

    operator fun getValue(thisRef: Features, property: KProperty<*>): Boolean =
        thisRef.overrides[property.name] ?: isEnabled(thisRef, property) ?: false
}