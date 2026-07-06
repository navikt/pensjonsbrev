package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.FeatureToggle

enum class FeatureToggles(private val key: String) {
    // Sett inn featuretoggles her
    // val minFeature = UnleashToggle("minFeature")
    bekreftelsePaaPensjon("bekreftelsePaaPensjon"),
    bekreftelsePaaUfoeretrygd("bekreftelsePaaUfoeretrygd"),
    brevmalAnkeTilsvarTilAnkendePart("brevmalAnkeTilsvarTilAnkendePart"),
    brevmalAvslagGjenlevendepensjon("brevmalAvslagGjenlevendepensjon"),
    brevmalAvslagGjenlevendepensjonUtland("brevmalAvslagGjenlevendepensjonUtland"),
    brevmalAvslagUfoerepensjon("brevmalAvslagUfoerepensjon"),
    brevmalInnvilgelseGjenlevendepensjonBosattNorgeEtterUtland("brevmalInnvilgelseGjenlevendepensjonBosattNorgeEtterUtland"),
    brevmalKlageOrienteringOmOversendelseTilKlageinstans("klageOrienteringOmOversendelseTilKlageinstans"),
    brevmalKlageOrienteringOmSaksbehandlingstid("brevmalKlageOrienteringOmSaksbehandlingstid"),
    brevmalOpphoerGjenlevendepensjon("brevmalOpphoerGjenlevendepensjon"),
    brevmalUtAvslag("brevmalUtAvslag"),
    brevmalUtBosattNorgeEtterUtland("brevmalUtBosattNorgeEtterUtland"),
    brevmalUtDelvisEksport("brevmalUtDelvisEksport"),
    brevmalUtEndring("brevmalUtEndring"),
    brevmalUtInnvilgelse("brevmalUtInnvilgelse"),
    brevmalUtInnvilgelseMedEndring("brevmalUtInnvilgelseMedEndring"),
    brevmalUtOkningUforegrad("brevmalUtOkningUforegrad"),
    brevmalUtOmregningUfoerepensjonTilUfoeretrygd("brevmalOmregningUfoerepensjonTilUfoeretrygd"),
    brukertestbrev2025("brukertestbrev2025"),
    orienteringOmForlengetSaksbehandlingstid("orienteringOmForlengetSaksbehandlingstid"),
    oversettelseAvDokumenter("oversettelseAvDokumenter"),
    pl7231ForventetSvartid("pl_7231.foreventet_svartid"),
    samletMeldingOmPensjonsvedtak("samletMeldingOmPensjonsvedtak"),
    vedtakAvslagPaaOmsorgsopptjening("vedtakAvslagPaaOmsorgsopptjening"),
    vedtakOmFjerningAvOmsorgspoeng("vedtakOmFjerningAvOmsorgspoeng"),
    vedtakOmInnvilgelseAvOmsorgspoeng("vedtakOmInnvilgelseAvOmsorgspoeng"),
    vedtakOmLavereMinstesats("vedtakOmLavereMinstesats"),
    vedtakOmOktMinsteIFUOgReduksjonsprosent("vedtakOmOktMinsteIFUOgReduksjonsprosent");

    val toggle = FeatureToggle(key)
}