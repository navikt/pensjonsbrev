package no.nav.pensjon.brev.api.model

interface FeatureToggle {
    fun isEnabled(): Boolean
}