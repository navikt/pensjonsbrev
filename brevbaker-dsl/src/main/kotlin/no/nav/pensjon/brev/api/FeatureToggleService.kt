package no.nav.pensjon.brev.api

import no.nav.pensjon.brev.api.model.ToggleName

interface FeatureToggleService {
    fun isEnabled(toggle: ToggleName): Boolean
}