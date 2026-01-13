package no.nav.pensjon.brev.api

import no.nav.pensjon.brev.api.model.FeatureToggle

interface FeatureToggleService {
    fun isEnabled(toggle: FeatureToggle): Boolean
    fun verifiserAtAlleBrytereErDefinert(entries: List<FeatureToggle>)
}