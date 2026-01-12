package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.FeatureToggle

enum class FeatureToggles(private val key: String) {
    // Sett inn featuretoggles her
    // val minFeature = UnleashToggle("minFeature")
    bekreftelsePaaPensjon("bekreftelsePaaPensjon"),
    bekreftelsePaaUfoeretrygd("bekreftelsePaaUfoeretrygd"),
    pl7231ForventetSvartid("pl_7231.foreventet_svartid"),
    oversettelseAvDokumenter("oversettelseAvDokumenter"),
    brevmalUtAvslag("brevmalUtAvslag"),
    brevmalUtInnvilgelse("brevmalUtInnvilgelse"),
    informasjonOmGjenlevenderettigheter("informasjonOmGjenlevenderettigheter"),
    vedtakEndringAvUttaksgradStans("vedtakEndringAvUttaksgradStans"),
    orienteringOmForlengetSaksbehandlingstid("orienteringOmForlengetSaksbehandlingstid"),
    samletMeldingOmPensjonsvedtak("samletMeldingOmPensjonsvedtak"),
    samletMeldingOmPensjonsvedtakV2("samletMeldingOmPensjonsvedtakV2"),
    vedtakOmFjerningAvOmsorgspoeng("vedtakOmFjerningAvOmsorgspoeng"),
    brukertestbrev2025("brukertestbrev2025"),
    vedtakOmInnvilgelseAvOmsorgspoeng("vedtakOmInnvilgelseAvOmsorgspoeng");

    val toggle = FeatureToggle(key)
}