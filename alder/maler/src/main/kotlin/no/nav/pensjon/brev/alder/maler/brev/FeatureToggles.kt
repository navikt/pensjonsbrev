package no.nav.pensjon.brev.alder.maler.brev

import no.nav.pensjon.brev.api.model.FeatureToggle

enum class FeatureToggles(
    private val key: String,
) {
    afpPrivatUfore("afpPrivatUfore"),
    avslagAfpGammel("avslagAfpGammel"),
    avslagAfpPrivat("avslagAfpPrivat"),
    feilutbetaltBelopTilbakekrevesIkke("feilutbetaltBelopTilbakekrevesIkke"),
    innvilgelseAvAfp("innvilgelseAvAfp"),
    innvilgelseAvAfpOffentligSektor("innvilgelseAvAfpOffentligSektor"),
    omregningAlderUfore2016("omregningAlderUfore2016"),
    vedtakAfpPrivatEndring("vedtakAfpPrivatEndring"),
    vedtakEndringAfpOffentligSektor("vedtakEndringAfpOffentligSektor"),
    vedtakOmOmgjoering("vedtakOmOmgjoering");

    val toggle = FeatureToggle(key)
}
