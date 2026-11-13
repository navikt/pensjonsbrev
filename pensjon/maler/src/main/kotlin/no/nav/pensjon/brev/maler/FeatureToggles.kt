package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.FeatureToggle

enum class FeatureToggles(private val key: String) {
    // Sett inn featuretoggles her
    // val minFeature = UnleashToggle("minFeature")
    bekreftelsePaaPensjon("bekreftelsePaaPensjon"),
    brevmalAnkeOrienteringOmSaksbehandling("brevmalAnkeOrienteringOmSaksbehandling"),
    brevmalAnkeTilsvarTilAnkendePart("brevmalAnkeTilsvarTilAnkendePart"),
    brevmalAvslagGjenlevendepensjon("brevmalAvslagGjenlevendepensjon"),
    brevmalAvslagGjenlevendepensjonUtland("brevmalAvslagGjenlevendepensjonUtland"),
    brevmalAvslagUfoerepensjon("brevmalAvslagUfoerepensjon"),
    brevmalInnvilgelseGjenlevendepensjonBosattNorgeEtterUtland("brevmalInnvilgelseGjenlevendepensjonBosattNorgeEtterUtland"),
    brevmalKlageBegrunnelseForVedtakUT("brevmalKlageBegrunnelseForVedtakUT"),
    brevmalKlageBeslutningOmAaIkkeGjoereOmVedtaketUT("brevmalKlageBeslutningOmAaIkkeGjoereOmVedtaketUT"),
    brevmalKlageBeslutningOmVedtaketSkalIverksettes("brevmalKlageBeslutningOmVedtaketSkalIverksettes"),
    brevmalKlageInnhentingAvOpplysningerUT("brevmalKlageInnhentingAvOpplysningerUT"),
    brevmalKlageOrienteringOmOversendelseTilKlageinstans("klageOrienteringOmOversendelseTilKlageinstans"),
    brevmalKlageOrienteringOmSaksbehandlingstid("brevmalKlageOrienteringOmSaksbehandlingstid"),
    brevmalKlageOversendelsesbrevTilKlageinstansPE("brevmalKlageOversendelsesbrevTilKlageinstansPE"),
    brevmalKlageOversendelsesbrevTilKlageinstansUT("brevmalKlageOversendelsesbrevTilKlageinstansUT"),
    brevmalKlageUtsattKlagefristUT("brevmalKlageUtsattKlagefristUT"),
    brevmalKlageVedtakAvvisningAvKlagePE("brevmalKlageVedtakAvvisningAvKlagePE"),
    brevmalKlageVedtakAvvisningAvKlageUT("brevmalKlageVedtakAvvisningAvKlageUT"),
    brevmalOpphoerGjenlevendepensjon("brevmalOpphoerGjenlevendepensjon"),
    brevmalOrienteringOmSvartidKlageUT("brevmalOrienteringOmSvartidKlageUT"),
    brevmalUtAvslag("brevmalUtAvslag"),
    brevmalUtBosattNorgeEtterUtland("brevmalUtBosattNorgeEtterUtland"),
    brevmalUtDelvisEksport("brevmalUtDelvisEksport"),
    brevmalUtEndretPgaInntekt("brevmalUtEndretPgaInntekt"),
    brevmalUtEndretPgaInntektNesteAr("brevmalUtEndretPgaInntektNesteAr"),
    brevmalUtInnvilgelseMedEndring("brevmalUtInnvilgelseMedEndring"),
    brevmalUtInnvilgelseTrygdeavtale("brevmalUtInnvilgelseTrygdeavtale"),
    brevmalUtOmregningUfoerepensjonTilUfoeretrygd("brevmalOmregningUfoerepensjonTilUfoeretrygd"),
    brevmalUtVedtakBlankMal("brevmalUtVedtakBlankMal"),
    brevmalUtsattKlagefristUT("brevmalUtsattKlagefristUT"),
    brukertestbrev2025("brukertestbrev2025"),
    okningUforegradV2("okninguforegrad.v2"),
    orienteringOmForlengetSaksbehandlingstid("orienteringOmForlengetSaksbehandlingstid"),
    oversettelseAvDokumenter("oversettelseAvDokumenter"),
    samletMeldingOmPensjonsvedtak("samletMeldingOmPensjonsvedtak"),
    vedtakAvslagPaaOmsorgsopptjening("vedtakAvslagPaaOmsorgsopptjening"),
    vedtakOmFjerningAvOmsorgspoeng("vedtakOmFjerningAvOmsorgspoeng"),
    vedtakOmInnvilgelseAvOmsorgspoeng("vedtakOmInnvilgelseAvOmsorgspoeng");


    val toggle = FeatureToggle(key)
}