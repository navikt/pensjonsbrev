package no.nav.pensjon.brev.maler

import no.nav.brev.brevbaker.AllTemplates
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.maler.adhoc.AdhocFeilEtteroppgjoer2023
import no.nav.pensjon.brev.maler.adhoc.AdhocGjenlevendEtter1970
import no.nav.pensjon.brev.maler.adhoc.AdhocInformasjonHvilendeRett4Aar
import no.nav.pensjon.brev.maler.adhoc.AdhocMidlertidigOpphoerHvilenderett10Aar
import no.nav.pensjon.brev.maler.adhoc.AdhocUfoeretrygdEtterbetalingDagpenger
import no.nav.pensjon.brev.maler.adhoc.AdhocUfoeretrygdKombiDagpenger
import no.nav.pensjon.brev.maler.adhoc.AdhocUfoeretrygdKombiDagpengerInntektsavkorting
import no.nav.pensjon.brev.maler.adhoc.AdhocUfoeretrygdVarselOpphoerEktefelletillegg
import no.nav.pensjon.brev.maler.adhoc.AdhocVarselOpphoerMedHvilendeRett
import no.nav.pensjon.brev.maler.adhoc.fullmakterbprof.AdHocVarselUgyldiggjoringFullmaktsgiver
import no.nav.pensjon.brev.maler.adhoc.fullmakterbprof.AdHocVarselUgyldiggjoringFullmektig
import no.nav.pensjon.brev.maler.adhoc.gjenlevenderett2027.VarselGjpForlengetArskull6061
import no.nav.pensjon.brev.maler.adhoc.gjenlevenderett2027.VarselGjpForlengetArskull6061Utland
import no.nav.pensjon.brev.maler.adhoc.gjenlevenderett2027.VarselGjpForlengetArskull6270
import no.nav.pensjon.brev.maler.adhoc.gjenlevenderett2027.VarselGjpForlengetArskull6270Utland
import no.nav.pensjon.brev.maler.adhoc.gjenlevenderett2027.VarselGjpOpphorArskull6070
import no.nav.pensjon.brev.maler.adhoc.gjenlevenderett2027.VarselGjpOpphorArskull6070Utland
import no.nav.pensjon.brev.maler.adhoc.gjenlevenderett2027.VedtakGjpForlengetArskull6061
import no.nav.pensjon.brev.maler.adhoc.gjenlevenderett2027.VedtakGjpForlengetArskull6061Utland
import no.nav.pensjon.brev.maler.adhoc.gjenlevenderett2027.VedtakGjpForlengetArskull6270
import no.nav.pensjon.brev.maler.adhoc.gjenlevenderett2027.VedtakGjpForlengetArskull6270Utland
import no.nav.pensjon.brev.maler.adhoc.gjenlevenderett2027.VedtakGjpOpphorArskull6070
import no.nav.pensjon.brev.maler.adhoc.gjenlevenderett2027.VedtakGjpOpphorArskull6070Utland
import no.nav.pensjon.brev.maler.alder.EndringAvUttaksgradAuto
import no.nav.pensjon.brev.maler.alder.InnvilgelseAvAlderspensjonAuto
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
import no.nav.pensjon.brev.maler.redigerbar.AvslagForLiteTrygdetidAP
import no.nav.pensjon.brev.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjon
import no.nav.pensjon.brev.maler.redigerbar.BekreftelsePaaFlyktningstatus
import no.nav.pensjon.brev.maler.redigerbar.BrukerTestBrev
import no.nav.pensjon.brev.maler.redigerbar.ForespoerselOmDokumentasjonAvBotidINorgeAlder
import no.nav.pensjon.brev.maler.redigerbar.ForespoerselOmDokumentasjonAvBotidINorgeEtterlatte
import no.nav.pensjon.brev.maler.redigerbar.InformasjonOmGjenlevenderettigheter
import no.nav.pensjon.brev.maler.redigerbar.InformasjonOmSaksbehandlingstid
import no.nav.pensjon.brev.maler.redigerbar.InformasjonOmSaksbehandlingstidUT
import no.nav.pensjon.brev.maler.redigerbar.InnhentingDokumentasjonFraBruker
import no.nav.pensjon.brev.maler.redigerbar.InnhentingInformasjonFraBruker
import no.nav.pensjon.brev.maler.redigerbar.InnhentingOpplysningerFraBruker
import no.nav.pensjon.brev.maler.redigerbar.InnvilgelseAvAlderspensjon
import no.nav.pensjon.brev.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtale
import no.nav.pensjon.brev.maler.redigerbar.OmsorgEgenManuell
import no.nav.pensjon.brev.maler.redigerbar.OrienteringOmForlengetSaksbehandlingstid
import no.nav.pensjon.brev.maler.redigerbar.OrienteringOmSaksbehandlingstid
import no.nav.pensjon.brev.maler.redigerbar.OversettelseAvDokumenter
import no.nav.pensjon.brev.maler.redigerbar.TilbakekrevingAvFeilutbetaltBeloep
import no.nav.pensjon.brev.maler.redigerbar.VarselOmMuligAvslag
import no.nav.pensjon.brev.maler.redigerbar.VarselRevurderingAvPensjon
import no.nav.pensjon.brev.maler.redigerbar.VarselTilbakekrevingAvFeilutbetaltBeloep
import no.nav.pensjon.brev.maler.redigerbar.VedtakEndringAvAlderspensjonFordiOpptjeningErEndret
import no.nav.pensjon.brev.maler.redigerbar.VedtakEndringAvAlderspensjonInstitusjonsopphold
import no.nav.pensjon.brev.maler.redigerbar.VedtakEndringAvUttaksgradStansIkkeInitiertAvBrukerEllerVerge
import no.nav.pensjon.brev.maler.redigerbar.VedtakEndringAvUttaksgradStansInitiertAvBrukerEllerVerge
import no.nav.pensjon.brev.maler.redigerbar.VedtakEndringVedFlyttingMellomLand
import no.nav.pensjon.brev.maler.redigerbar.VedtakOmFjerningAvOmsorgsopptjening
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
        AdHocVarselUgyldiggjoringFullmaktsgiver,
        AdHocVarselUgyldiggjoringFullmektig,
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
        EndretBarnetilleggUfoerertrygd,
        EndretUfoeretrygdPGAInntektLegacy,
        EndretUfoeretrygdPGAInntektV2,
        EndretUforetrygdPGAInntektNesteAr,
        EndretUforetrygdPGAOpptjeningLegacy,
        EndringPgaOpptjeningAuto,
        EndringAvUttaksgradAuto,
        EtteroppgjoerEtterbetalingAutoLegacy,
        ForhaandsvarselEtteroppgjoerUfoeretrygdAuto,
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
        AvslagPaaGjenlevenderettIAlderspensjon,
        AvslagUfoeretrygd,
        BekreftelsePaaFlyktningstatus,
        BrukerTestBrev,
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
    )

    override fun hentAutobrevmaler() = autobrev

    override fun hentRedigerbareMaler() = redigerbare
}