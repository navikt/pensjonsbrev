package no.nav.pensjon.brev.skribenten

import kotlin.reflect.KProperty

object Features {
    val brevbakerbrev: Boolean by EnvironmentToggle()

    private val _overrides = mutableMapOf<String, Boolean>()
    val overrides: Map<String, Boolean> = _overrides
    fun override(key: String, value: Boolean) {
        _overrides[key] = value
    }
}

private class EnvironmentToggle {
    fun isEnabled(property: KProperty<*>): Boolean =
        System.getenv().getOrDefault("FEATURE_${property.name}", "false").toBoolean()

    operator fun getValue(thisRef: Features, property: KProperty<*>): Boolean =
        thisRef.overrides[property.name] ?: isEnabled(property)
}