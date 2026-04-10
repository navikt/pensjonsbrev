package no.nav.pensjon.brev

import no.nav.pensjon.brev.api.model.FeatureToggle

enum class BrevbakerFeatureToggles(key: String) {
    typstAutobrev("typst.autobrev"),
    typstRedigerbareBrev("typst.redigerbareBrev");

    val toggle = FeatureToggle(key)
}

