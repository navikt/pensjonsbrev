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
        RedigerbarMal(AvslagForLiteTrygdetidAP),
        RedigerbarMal(AvslagGradsendringFoerNormertPensjonsalder),
        RedigerbarMal(AvslagGradsendringFoerNormertPensjonsalderAP2016),
        RedigerbarMal(AvslagGradsendringFoerNormertPensjonsalderFoerEttAar),
        RedigerbarMal(AvslagPaaGjenlevenderettIAlderspensjon),
        RedigerbarMal(AvslagUfoeretrygd),
        RedigerbarMal(AvslagUttakFoerNormertPensjonsalder),
        RedigerbarMal(AvslagUttakFoerNormertPensjonsalderAP2016),
        RedigerbarMal(BekreftelsePaaFlyktningstatus),
        RedigerbarMal(BrukerTestBrev),
        RedigerbarMal(EndringAvAlderspensjonPgaGarantitillegg),
        RedigerbarMal(EndringAvAlderspensjonSivilstand),
        RedigerbarMal(EndringAvAlderspensjonSivilstandSaerskiltSats),
        RedigerbarMal(ForespoerselOmDokumentasjonAvBotidINorgeAlder),
        RedigerbarMal(ForespoerselOmDokumentasjonAvBotidINorgeEtterlatte),
        RedigerbarMal(ForhaandsvarselVedTilbakekreving),
        RedigerbarMal(InformasjonOmGjenlevenderettigheter),
        RedigerbarMal(InformasjonOmSaksbehandlingstid),
        RedigerbarMal(InformasjonOmSaksbehandlingstidUT),
        RedigerbarMal(InnhentingDokumentasjonFraBruker),
        RedigerbarMal(InnhentingInformasjonFraBruker),
        RedigerbarMal(InnhentingOpplysningerFraBruker),
        RedigerbarMal(InnvilgelseAvAlderspensjon),
        RedigerbarMal(InnvilgelseAvAlderspensjonTrygdeavtale),
        RedigerbarMal(OmregningAlderUfore2016),
        RedigerbarMal(OmsorgEgenManuell),
        RedigerbarMal(OrienteringOmForlengetSaksbehandlingstid),
        RedigerbarMal(OrienteringOmSaksbehandlingstid),
        RedigerbarMal(OversettelseAvDokumenter),
        RedigerbarMal(SamletMeldingOmPensjonsvedtak),
        RedigerbarMal(TilbakekrevingAvFeilutbetaltBeloep),
        RedigerbarMal(VarselOmMuligAvslag),
        RedigerbarMal(VarselRevurderingAvPensjon),
        RedigerbarMal(VarselTilbakekrevingAvFeilutbetaltBeloep),
        RedigerbarMal(VedtakEndringAvAlderspensjonFordiOpptjeningErEndret),
        RedigerbarMal(VedtakEndringAvAlderspensjonGjenlevenderettigheter),
        RedigerbarMal(VedtakEndringAvAlderspensjonInstitusjonsopphold),
        RedigerbarMal(VedtakEndringAvUttaksgrad),
        RedigerbarMal(VedtakEndringAvUttaksgradStansIkkeInitiertAvBrukerEllerVerge),
        RedigerbarMal(VedtakEndringAvUttaksgradStansInitiertAvBrukerEllerVerge),
        RedigerbarMal(VedtakEndringVedFlyttingMellomLand),
        RedigerbarMal(VedtakOmFjerningAvOmsorgsopptjening),
        RedigerbarMal(VedtakStansAlderspensjonFlyttingMellomLand),
        RedigerbarMal(AvslagUforetrygdDemo),
    )

    override fun hentAutobrevmaler() = autobrev

    override fun hentRedigerbareMaler() = redigerbare
}