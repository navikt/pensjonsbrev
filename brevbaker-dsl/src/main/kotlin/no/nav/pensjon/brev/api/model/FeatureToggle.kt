package no.nav.pensjon.brev.api.model

import no.nav.pensjon.brev.api.FeatureToggleService
import no.nav.pensjon.brev.template.StableHash
import java.util.Objects

object FeatureToggleSingleton {
    private lateinit var featureToggleService: FeatureToggleService
    val isInitialized get() = ::featureToggleService.isInitialized

    fun init(featureToggleService: FeatureToggleService) {
        this.featureToggleService = featureToggleService
    }

    fun isEnabled(toggle: FeatureToggle): Boolean {
        if (!isInitialized) {
            throw IllegalStateException("Du må sette opp en FeatureToggleService med FeatureToggleSingleton::init for å kunne bruke feature toggles.")
        }
        return featureToggleService.isEnabled(toggle)
    }

}

class FeatureToggle(val name: String) : StableHash by StableHash.of("Toggle: $name") {
    fun key(): String = name

    override fun equals(other: Any?): Boolean {
        if (other !is FeatureToggle) return false
        return name == other.name
    }
    override fun hashCode() = Objects.hash(name)
    override fun toString() = "FeatureToggle(name='$name')"
}