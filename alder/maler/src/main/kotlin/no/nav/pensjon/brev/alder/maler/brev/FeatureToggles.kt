package no.nav.pensjon.brev.alder.maler.brev

import no.nav.pensjon.brev.api.model.FeatureToggle

enum class FeatureToggles(
    private val key: String,
) {
    omregningAlderUfore2016("omregningAlderUfore2016"),
    afpPrivatUfore("afpPrivatUfore"),
    innvilgelseAvAfpOffentligSektor("innvilgelseAvAfpOffentligSektor"),
    innvilgelseAvAfp("innvilgelseAvAfp"),
    vedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysninger("vedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysninger"),
    avslagAfpPrivat("avslagAfpPrivat"),
    avslagAfpGammel("avslagAfpGammel"),
    vedtakAfpPrivatEndring("vedtakAfpPrivatEndring"),
    vedtakAfpEtteroppgjoerToleransebeloep("vedtakAfpEtteroppgjoerToleransebeloep"),
    vedtakAfpEtteroppgjoerEtterbetaling("vedtakAfpEtteroppgjoerEtterbetaling"),
    vedtakAfpEtteroppgjoerIngenEndring("vedtakAfpEtteroppgjoerIngenEndring"),
    vedtakEndringAfpOffentligSektor("vedtakEndringAfpOffentligSektor");

    val toggle = FeatureToggle(key)
}
