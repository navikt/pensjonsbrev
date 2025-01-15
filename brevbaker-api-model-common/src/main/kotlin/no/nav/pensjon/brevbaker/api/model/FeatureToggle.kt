package no.nav.pensjon.brevbaker.api.model

interface FeatureToggle {
    fun isEnabled(): Boolean
}
