package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.FeatureToggle

enum class FeatureToggles(private val key: String) {
    // Sett inn featuretoggles her
    // val minFeature = UnleashToggle("minFeature")
    bekreftelsePaaPensjon("bekreftelsePaaPensjon"),
    bekreftelsePaaUfoeretrygd("bekreftelsePaaUfoeretrygd"),
    brevmalAvslagGjenlevendepensjon("brevmalAvslagGjenlevendepensjon"),
    brevmalAvslagGjenlevendepensjonUtland("brevmalAvslagGjenlevendepensjonUtland"),
    brevmalAvslagUfoerepensjon("brevmalAvslagUfoerepensjon"),
    brevmalInnvilgelseGjenlevendepensjonBosattNorgeEtterUtland("brevmalInnvilgelseGjenlevendepensjonBosattNorgeEtterUtland"),
    brevmalOpphoerGjenlevendepensjon("brevmalOpphoerGjenlevendepensjon"),
    brevmalUtAvslag("brevmalUtAvslag"),
    brevmalUtInnvilgelse("brevmalUtInnvilgelse"),
    brevmalUtOmregningUfoerepensjonTilUfoeretrygd("brevmalOmregningUfoerepensjonTilUfoeretrygd"),
    brukertestbrev2025("brukertestbrev2025"),
    orienteringOmForlengetSaksbehandlingstid("orienteringOmForlengetSaksbehandlingstid"),
    oversettelseAvDokumenter("oversettelseAvDokumenter"),
    pl7231ForventetSvartid("pl_7231.foreventet_svartid"),
    samletMeldingOmPensjonsvedtak("samletMeldingOmPensjonsvedtak"),
    samletMeldingOmPensjonsvedtakV2("samletMeldingOmPensjonsvedtakV2"),
    vedtakAvslagPaaOmsorgsopptjening("vedtakAvslagPaaOmsorgsopptjening"),
    vedtakOmFjerningAvOmsorgspoeng("vedtakOmFjerningAvOmsorgspoeng"),
    vedtakOmInnvilgelseAvOmsorgspoeng("vedtakOmInnvilgelseAvOmsorgspoeng"),
    vedtakOmLavereMinstesats("vedtakOmLavereMinstesats");

    val toggle = FeatureToggle(key)
}