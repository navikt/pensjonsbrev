package no.nav.pensjon.brev.maler

import no.nav.brev.brevbaker.AllTemplates
import no.nav.brev.brevbaker.AutoMal
import no.nav.brev.brevbaker.RedigerbarMal
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.maler.adhoc.*
import no.nav.pensjon.brev.maler.adhoc.gjenlevenderett2027.*
import no.nav.pensjon.brev.maler.adhoc.skjermingstillegg.AdhocSkjermingstilleggFeilBeroertBruker
import no.nav.pensjon.brev.maler.adhoc.skjermingstillegg.AdhocSkjermingstilleggFeilMottaker
import no.nav.pensjon.brev.maler.alder.*
import no.nav.pensjon.brev.maler.alder.avslag.gradsendring.*
import no.nav.pensjon.brev.maler.alder.endring.sivilstand.EndringAvAlderspensjonPgaGarantitillegg
import no.nav.pensjon.brev.maler.alder.endring.sivilstand.EndringAvAlderspensjonSivilstand
import no.nav.pensjon.brev.maler.alder.endring.sivilstand.EndringAvAlderspensjonSivilstandAuto
import no.nav.pensjon.brev.maler.alder.endring.sivilstand.EndringAvAlderspensjonSivilstandSaerskiltSats
import no.nav.pensjon.brev.maler.alder.omregning.OmregningAlderUfore2016
import no.nav.pensjon.brev.maler.alder.omregning.OmregningAlderUfore2016Auto
import no.nav.pensjon.brev.maler.alder.omregning.opptjening.EndringPgaOpptjeningAuto
import no.nav.pensjon.brev.maler.legacy.EndretBarnetilleggUfoerertrygd
import no.nav.pensjon.brev.maler.legacy.EndretUfoeretrygdPGAInntektLegacy
import no.nav.pensjon.brev.maler.legacy.EndretUforetrygdPGAOpptjeningLegacy
import no.nav.pensjon.brev.maler.legacy.EtteroppgjoerEtterbetalingAutoLegacy
import no.nav.pensjon.brev.maler.legacy.redigerbar.AvslagUfoeretrygd
import no.nav.pensjon.brev.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheter
import no.nav.pensjon.brev.maler.legacy.redigerbar.VedtakEndringAvUttaksgrad
import no.nav.pensjon.brev.maler.redigerbar.*
import no.nav.pensjon.brev.maler.ufoereBrev.*
import no.nav.pensjon.brev.maler.ufoereBrev.adhoc.FeilBelopInntekstendringsbrev.*
import no.nav.pensjon.brev.maler.ufoereBrev.adhoc.FeilBelopInntekstendringsbrev_AvkortetTil0.*
import no.nav.pensjon.brev.maler.ufoereBrev.hvilenderett.HvilendeRettInfo4Aar
import no.nav.pensjon.brev.maler.ufoereBrev.hvilenderett.HvilendeRettMidlertidigOppHoer
import no.nav.pensjon.brev.maler.ufoereBrev.hvilenderett.HvilendeRettOppHoer
import no.nav.pensjon.brev.maler.ufoereBrev.hvilenderett.HvilendeRettVarselOpphoer

object ProductionTemplates : AllTemplates {
    private val autobrev = setOf(
        AdhocAFPInformasjonOekningToleransebeloep,
        AdhocAlderspensjonFraFolketrygden,
        AdhocAlderspensjonFraFolketrygden2,
        AdhocAlderspensjonGjtOpprydding,
        AdhocAlderspensjonGjtVarselBrev,
        AdhocFeilEtteroppgjoer2023,
        AdhocGjenlevendEtter1970,
        AdhocInformasjonHvilendeRett4Aar,
        AdhocMidlertidigOpphoerHvilenderett10Aar,
        AdhocSkjermingstilleggFeilBeroertBruker,
        AdhocSkjermingstilleggFeilMottaker,
        AdhocUfoeretrygdEtterbetalingDagpenger,
        AdhocUfoeretrygdKombiDagpenger,
        AdhocUfoeretrygdKombiDagpengerInntektsavkorting,
        AdhocUfoeretrygdVarselOpphoerEktefelletillegg,
        AdhocVarselOpphoerMedHvilendeRett,
        AdhocVarselTilBrukerForsoergingstilleggIkkeTilUtbetaling,
        AdhocVarselTilBrukerMedForsoergingstilleggTilUtbetaling,
        OmregningAlderUfore2016Auto,
        AvslagGradsendringFoerNormertPensjonsalder2016Auto,
        AvslagGradsendringFoerNormertPensjonsalderAuto,
        AvslagGradsendringFoerNormertPensjonsalderFoerEttAarAuto,
        AvslagUttakFoerNormertPensjonsalderAP2016Auto,
        AvslagUttakFoerNormertPensjonsalderAuto,
        EndretBarnetilleggUfoerertrygd,
        EndretUfoeretrygdPGAInntektLegacy,
        EndretUfoeretrygdPGAInntektV2,
        EndretUforetrygdPGAInntektNesteAr,
        EndretUforetrygdPGAOpptjeningLegacy,
        EndringAvAlderspensjonSivilstandAuto,
        EndringPgaOpptjeningAuto,
        EtteroppgjoerEtterbetalingAutoLegacy,
        FeilUtsendingAvGjenlevenderett,
        ForhaandsvarselEtteroppgjoerUfoeretrygdAuto,
        InfoAldersovergang67AarAuto,
        OmsorgEgenAuto,
        OpphoerBarnetilleggAuto,
        OpptjeningVedForhoeyetHjelpesats,
        UfoerOmregningEnslig,
        UngUfoerAuto,
        VarselSaksbehandlingstidAuto,
        VarselGjpForlengetArskull6061,
        VarselGjpForlengetArskull6061Utland,
        VarselGjpForlengetArskull6270,
        VarselGjpForlengetArskull6270Utland,
        VarselGjpOpphorArskull6070,
        VarselGjpOpphorArskull6070Utland,
        VedtakGjpForlengetArskull6061,
        VedtakGjpForlengetArskull6061Utland,
        VedtakGjpForlengetArskull6270,
        VedtakGjpForlengetArskull6270Utland,
        VedtakGjpOpphorArskull6070,
        VedtakGjpOpphorArskull6070Utland,
        FeilBelopInntekstendringsbrev,
        FeilBelopInntekstendringsbrev_AvkortetTil0,
        HvilendeRettInfo4Aar,
        HvilendeRettMidlertidigOppHoer,
        HvilendeRettOppHoer,
        HvilendeRettVarselOpphoer
    ).map { AutoMal(it) }.toSet()

    private val redigerbare: Set<RedigerbarMal<RedigerbarBrevdata<*, *>>> = setOf(
        RedigerbarMal(AvslagForLiteTrygdetidAP, FeatureToggles.avslagForLiteTrygdetidAP.toggle),
        RedigerbarMal(AvslagGradsendringFoerNormertPensjonsalder, FeatureToggles.apAvslagGradsendringNormertPensjonsalder.toggle),
        RedigerbarMal(AvslagGradsendringFoerNormertPensjonsalderAP2016, FeatureToggles.apAvslagGradsendringNormertPensjonsalderAP2016.toggle),
        RedigerbarMal(AvslagGradsendringFoerNormertPensjonsalderFoerEttAar, FeatureToggles.apAvslagGradsendringNormertPensjonsalderFoerEttAar.toggle),
        RedigerbarMal(AvslagPaaGjenlevenderettIAlderspensjon, FeatureToggles.apAvslagGjenlevenderett.toggle),
        RedigerbarMal(AvslagUfoeretrygd, FeatureToggles.brevmalUtAvslag.toggle),
        RedigerbarMal(AvslagUttakFoerNormertPensjonsalder, FeatureToggles.apAvslagNormertPensjonsalder.toggle),
        RedigerbarMal(AvslagUttakFoerNormertPensjonsalderAP2016, FeatureToggles.apAvslagNormertPensjonsalderAP2016.toggle),
        RedigerbarMal(BekreftelsePaaFlyktningstatus),
        RedigerbarMal(BrukerTestBrev, FeatureToggles.brukertestbrev2025.toggle),
        RedigerbarMal(EndringAvAlderspensjonPgaGarantitillegg, FeatureToggles.endringAvAlderspensjonSivilstandGarantitillegg.toggle),
        RedigerbarMal(EndringAvAlderspensjonSivilstand, FeatureToggles.endringAvAlderspensjonSivilstand.toggle),
        RedigerbarMal(EndringAvAlderspensjonSivilstandSaerskiltSats, FeatureToggles.endringAvAlderspensjonSivilstandVurderSaerskiltSats.toggle),
        RedigerbarMal(ForespoerselOmDokumentasjonAvBotidINorgeAlder),
        RedigerbarMal(ForespoerselOmDokumentasjonAvBotidINorgeEtterlatte),
        RedigerbarMal(ForhaandsvarselVedTilbakekreving),
        RedigerbarMal(InformasjonOmGjenlevenderettigheter, FeatureToggles.informasjonOmGjenlevenderettigheter.toggle),
        RedigerbarMal(InformasjonOmSaksbehandlingstid),
        RedigerbarMal(InformasjonOmSaksbehandlingstidUT),
        RedigerbarMal(InnhentingDokumentasjonFraBruker),
        RedigerbarMal(InnhentingInformasjonFraBruker),
        RedigerbarMal(InnhentingOpplysningerFraBruker),
        RedigerbarMal(InnvilgelseAvAlderspensjon, FeatureToggles.innvilgelseAvAlderspensjon.toggle),
        RedigerbarMal(InnvilgelseAvAlderspensjonTrygdeavtale, FeatureToggles.innvilgelseAvAlderspensjonTrygdeavtale.toggle),
        RedigerbarMal(OmregningAlderUfore2016, FeatureToggles.omregningAlderUfore2016.toggle),
        RedigerbarMal(OmsorgEgenManuell, FeatureToggles.omsorgEgenManuell.toggle),
        RedigerbarMal(OrienteringOmForlengetSaksbehandlingstid, FeatureToggles.orienteringOmForlengetSaksbehandlingstid.toggle),
        RedigerbarMal(OrienteringOmSaksbehandlingstid),
        RedigerbarMal(OversettelseAvDokumenter, FeatureToggles.oversettelseAvDokumenter.toggle),
        RedigerbarMal(SamletMeldingOmPensjonsvedtak, FeatureToggles.samletMeldingOmPensjonsvedtak.toggle),
        RedigerbarMal(TilbakekrevingAvFeilutbetaltBeloep, FeatureToggles.vedtakTilbakekrevingAvFeilutbetaltBeloep.toggle),
        RedigerbarMal(VarselOmMuligAvslag),
        RedigerbarMal(VarselRevurderingAvPensjon),
        RedigerbarMal(VarselTilbakekrevingAvFeilutbetaltBeloep),
        RedigerbarMal(VedtakEndringAvAlderspensjonFordiOpptjeningErEndret, FeatureToggles.vedtakEndringOpptjeningEndret.toggle),
        RedigerbarMal(VedtakEndringAvAlderspensjonGjenlevenderettigheter, FeatureToggles.vedtakEndringAvAlderspensjonGjenlevenderettigheter.toggle),
        RedigerbarMal(VedtakEndringAvAlderspensjonInstitusjonsopphold, FeatureToggles.vedtakEndringAvAlderspensjonInstitusjonsopphold.toggle),
        RedigerbarMal(VedtakEndringAvUttaksgrad, FeatureToggles.vedtakEndringAvUttaksgrad.toggle),
        RedigerbarMal(VedtakEndringAvUttaksgradStansIkkeInitiertAvBrukerEllerVerge, FeatureToggles.vedtakEndringAvUttaksgradStans.toggle),
        RedigerbarMal(VedtakEndringAvUttaksgradStansInitiertAvBrukerEllerVerge, FeatureToggles.vedtakEndringAvUttaksgradStans.toggle),
        RedigerbarMal(VedtakEndringVedFlyttingMellomLand, FeatureToggles.vedtakEndringVedFlyttingMellomLand.toggle),
        RedigerbarMal(VedtakOmFjerningAvOmsorgsopptjening, FeatureToggles.vedtakOmFjerningAvOmsorgspoeng.toggle),
        RedigerbarMal(VedtakStansAlderspensjonFlyttingMellomLand, FeatureToggles.vedtakStansFlyttingMellomLand.toggle),
        RedigerbarMal(AvslagUforetrygdDemo, FeatureToggles.utAvslagUforetrygdDemo.toggle),
    )

    override fun hentAutobrevmaler() = autobrev

    override fun hentRedigerbareMaler() = redigerbare
}