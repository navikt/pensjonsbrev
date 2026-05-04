package no.nav.pensjon.brev.ufore.maler

import no.nav.pensjon.brev.api.model.FeatureToggle

enum class FeatureToggles(private val key: String) {
    feilutbetaling("ut.tilbakekreving"),
    avslagMedlemskap("ut.avslagmedlemskap"),
    avslagMedlemskapUtland("ut.avslagmedlemskaputland"),
    feilutbetalingNy("ut.feilutbetaling.ny"),
    innhentingOpplysninger("ut.innhentingopplysninger"),
    varsellavereminstesats("ut.varsellavereminstesats"),
    varselhoyereminstesatsifuoglaverereduksjonsprosent("ut.varselhoyereminstesatsifuoglaverereduksjonsprosent"),
    ;

    val toggle = FeatureToggle(key)
}