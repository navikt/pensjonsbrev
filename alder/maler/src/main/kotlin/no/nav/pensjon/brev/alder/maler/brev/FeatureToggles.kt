package no.nav.pensjon.brev.alder.maler.brev

import no.nav.pensjon.brev.api.model.FeatureToggle

enum class FeatureToggles(
    private val key: String,
) {
    afpPrivatUfore("afpPrivatUfore"),
    avslagAfpGammel("avslagAfpGammel"),
    avslagAfpPrivat("avslagAfpPrivat"),
    innvilgelseAvAfp("innvilgelseAvAfp"),
    innvilgelseAvAfpOffentligSektor("innvilgelseAvAfpOffentligSektor"),
    omregningAlderUfore2016("omregningAlderUfore2016"),
    varselAfpEtteroppgjoerForeloepig("varselAfpEtteroppgjoerForeloepig"),
    vedtakAfpEtteroppgjoerEtterbetaling("vedtakAfpEtteroppgjoerEtterbetaling"),
    vedtakAfpEtteroppgjoerEtterbetalingEtterSvar("vedtakAfpEtteroppgjoerEtterbetalingEtterSvar"),
    vedtakAfpEtteroppgjoerIngenEndring("vedtakAfpEtteroppgjoerIngenEndring"),
    vedtakAfpEtteroppgjoerIngenEndringEtterSvar("vedtakAfpEtteroppgjoerIngenEndringEtterSvar"),
    vedtakAfpEtteroppgjoerIngenEndringNyeOpplysninger("vedtakAfpEtteroppgjoerIngenEndringNyeOpplysninger"),
    vedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysninger("vedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysninger"),
    vedtakAfpEtteroppgjoerToleransebeloep("vedtakAfpEtteroppgjoerToleransebeloep"),
    vedtakAfpPrivatEndring("vedtakAfpPrivatEndring"),
    vedtakEndringAfpOffentligSektor("vedtakEndringAfpOffentligSektor");

    val toggle = FeatureToggle(key)
}
