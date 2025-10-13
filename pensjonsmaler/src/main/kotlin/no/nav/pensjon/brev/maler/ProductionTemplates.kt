package no.nav.pensjon.brev.maler

import no.nav.brev.brevbaker.AllTemplates
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.maler.adhoc.*
import no.nav.pensjon.brev.maler.adhoc.gjenlevenderett2027.*
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
import no.nav.pensjon.brev.maler.ufoereBrev.EndretUfoeretrygdPGAInntektV2
import no.nav.pensjon.brev.maler.ufoereBrev.EndretUforetrygdPGAInntektNesteAr
import no.nav.pensjon.brev.maler.ufoereBrev.VarselSaksbehandlingstidAuto
import no.nav.pensjon.brev.maler.ufoereBrev.adhoc.FeilBelopInntekstendringsbrev.FeilBelopInntekstendringsbrev
import no.nav.pensjon.brev.maler.ufoereBrev.adhoc.FeilBelopInntekstendringsbrev_AvkortetTil0.FeilBelopInntekstendringsbrev_AvkortetTil0
import no.nav.pensjon.brev.maler.ufoereBrev.hvilenderett.HvilendeRettInfo4Aar
import no.nav.pensjon.brev.maler.ufoereBrev.hvilenderett.HvilendeRettMidlertidigOppHoer
import no.nav.pensjon.brev.maler.ufoereBrev.hvilenderett.HvilendeRettOppHoer
import no.nav.pensjon.brev.maler.ufoereBrev.hvilenderett.HvilendeRettVarselOpphoer
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.RedigerbarTemplate

object ProductionTemplates : AllTemplates {
    private val autobrev: Set<AutobrevTemplate<BrevbakerBrevdata>> = setOf(
        AdhocFeilEtteroppgjoer2023,
        AdhocGjenlevendEtter1970,
        AdhocInformasjonHvilendeRett4Aar,
        AdhocMidlertidigOpphoerHvilenderett10Aar,
        AdhocUfoeretrygdEtterbetalingDagpenger,
        AdhocUfoeretrygdKombiDagpenger,
        AdhocUfoeretrygdKombiDagpengerInntektsavkorting,
        AdhocUfoeretrygdVarselOpphoerEktefelletillegg,
        AdhocVarselOpphoerMedHvilendeRett,
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
        EndringAvUttaksgradAuto,
        EtteroppgjoerEtterbetalingAutoLegacy,
        ForhaandsvarselEtteroppgjoerUfoeretrygdAuto,
        InfoAldersovergang67AarAuto,
        InnvilgelseAvAlderspensjonAuto,
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
    )

    private val redigerbare: Set<RedigerbarTemplate<out RedigerbarBrevdata<*, *>>> = setOf(
        AvslagForLiteTrygdetidAP,
        AvslagGradsendringFoerNormertPensjonsalder,
        AvslagGradsendringFoerNormertPensjonsalderAP2016,
        AvslagGradsendringFoerNormertPensjonsalderFoerEttAar,
        AvslagPaaGjenlevenderettIAlderspensjon,
        AvslagUfoeretrygd,
        AvslagUttakFoerNormertPensjonsalder,
        AvslagUttakFoerNormertPensjonsalderAP2016,
        BekreftelsePaaFlyktningstatus,
        BrukerTestBrev,
        EndringAvAlderspensjonPgaGarantitillegg,
        EndringAvAlderspensjonSivilstand,
        EndringAvAlderspensjonSivilstandSaerskiltSats,
        ForespoerselOmDokumentasjonAvBotidINorgeAlder,
        ForespoerselOmDokumentasjonAvBotidINorgeEtterlatte,
        InformasjonOmGjenlevenderettigheter,
        InformasjonOmSaksbehandlingstid,
        InformasjonOmSaksbehandlingstidUT,
        InnhentingDokumentasjonFraBruker,
        InnhentingInformasjonFraBruker,
        InnhentingOpplysningerFraBruker,
        InnvilgelseAvAlderspensjon,
        InnvilgelseAvAlderspensjonTrygdeavtale,
        OmregningAlderUfore2016,
        OmsorgEgenManuell,
        OrienteringOmForlengetSaksbehandlingstid,
        OrienteringOmSaksbehandlingstid,
        OversettelseAvDokumenter,
        SamletMeldingOmPensjonsvedtak,
        TilbakekrevingAvFeilutbetaltBeloep,
        VarselOmMuligAvslag,
        VarselRevurderingAvPensjon,
        VarselTilbakekrevingAvFeilutbetaltBeloep,
        VedtakEndringAvAlderspensjonFordiOpptjeningErEndret,
        VedtakEndringAvAlderspensjonGjenlevenderettigheter,
        VedtakEndringAvAlderspensjonInstitusjonsopphold,
        VedtakEndringAvUttaksgrad,
        VedtakEndringAvUttaksgradStansIkkeInitiertAvBrukerEllerVerge,
        VedtakEndringAvUttaksgradStansInitiertAvBrukerEllerVerge,
        VedtakEndringVedFlyttingMellomLand,
        VedtakOmFjerningAvOmsorgsopptjening,
        VedtakStansAlderspensjonFlyttingMellomLand,
    )

    override fun hentAutobrevmaler() = autobrev

    override fun hentRedigerbareMaler() = redigerbare
}