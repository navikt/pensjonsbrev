package no.nav.pensjon.brev.maler

import no.nav.brev.brevbaker.AllTemplates
import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.maler.adhoc.*
import no.nav.pensjon.brev.maler.adhoc.fullmakterbprof.AdHocVarselUgyldiggjoringFullmaktsgiver
import no.nav.pensjon.brev.maler.adhoc.fullmakterbprof.AdHocVarselUgyldiggjoringFullmektig
import no.nav.pensjon.brev.maler.klageOgAnke.AnkeTilsvarTilAnkendePart
import no.nav.pensjon.brev.maler.klageOgAnke.KlageOrienteringOmOversendelseTilKlageinstans
import no.nav.pensjon.brev.maler.klageOgAnke.KlageOrienteringOmSaksbehandlingstid
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.maler.legacy.redigerbar.*
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
import no.nav.pensjon.brev.template.AlltidValgbartVedlegg
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.RedigerbarTemplate

object ProductionTemplates : AllTemplates {
    private val autobrev: Set<AutobrevTemplate<AutobrevData>> = setOf(
        AdHocVarselUgyldiggjoringFullmaktsgiver,
        AdHocVarselUgyldiggjoringFullmektig,
        AdhocFeilEtteroppgjoer2023,
        AdhocInformasjonHvilendeRett4Aar,
        AdhocMidlertidigOpphoerHvilenderett10Aar,
        AdhocUfoeretrygdEtterbetalingDagpenger,
        AdhocUfoeretrygdKombiDagpenger,
        AdhocUfoeretrygdKombiDagpengerInntektsavkorting,
        AdhocUfoeretrygdVarselOpphoerEktefelletillegg,
        AdhocVarselOpphoerMedHvilendeRett,
        EndretBarnetilleggUfoerertrygd,
        EndretUfoeretrygdPGAInntektV2,
        EndretUforetrygdPGAInntektNesteAr,
        EndretUforetrygdPGAOpptjeningLegacy,
        VedtakOmEtterbetalingOpphor2026Auto,
        VedtakOmEtterbetalingOpphor2026OktIfuAuto,
        VedtakOmEtterbetalingOpphor2026LavereReduksjonsprosentAuto,
        EtteroppgjoerEtterbetalingAutoLegacy,
        ForhaandsvarselEtteroppgjoerUfoeretrygdAuto,
        OmsorgEgenAuto,
        OpphoerBarnetilleggAuto,
        OpptjeningVedForhoeyetHjelpesats,
        UfoerOmregningEnslig,
        UngUfoerAuto,
        VarselSaksbehandlingstidAuto,
        FeilBelopInntekstendringsbrev,
        FeilBelopInntekstendringsbrev_AvkortetTil0,
        HvilendeRettInfo4Aar,
        HvilendeRettMidlertidigOppHoer,
        HvilendeRettOppHoer,
        HvilendeRettVarselOpphoer,
        VedtakOmLavereMinstesatsAuto,
        VedtakOmOktBunnfradragAuto,
        VedtakOmOktFribelopAuto,
        VedtakOmOktMinsteIFUAuto,
        VedtakOmLavereReduksjonsprosentAuto,
        VedtakOmOktMinsteIFULavereReduksjonsprosentAuto,
        VedtakOmEndringBarnetilleggEPSAuto,
        ReverseringLavereMinstesatsAuto,
        VedtakOmEndringBarnetilleggEPSRevAuto,
        VedtakOmEndringBTEPSOktoberAuto,
    )

    private val redigerbare: Set<RedigerbarTemplate<out RedigerbarBrevdata<*, *>>> = setOf(
        AnkeTilsvarTilAnkendePart,
        AvslagGjenlevendepensjon,
        AvslagGjenlevendepensjonUtland,
        AvslagPaaGjenlevenderettIAlderspensjon,
        AvslagUfoerepensjon,
        AvslagUfoeretrygd,
        BekreftelsePaaFlyktningstatus,
        BekreftelsePaaPensjon,
        BekreftelsePaaUfoeretrygd,
        BrukerTestBrev,
        BrukerTestVedtaksbrev,
        DelvisEksportAvUforetrygd,
        EndringUforetrygd,
        VedtakOmEtterbetalingOpphor2026Redigerbar,
        ForespoerselOmDokumentasjonAvBotidINorgeAlder,
        ForespoerselOmDokumentasjonAvBotidINorgeEtterlatte,
        InformasjonOmGjenlevenderettigheter,
        InformasjonOmSaksbehandlingstid,
        InformasjonOmSaksbehandlingstidUT,
        InnhentingDokumentasjonFraBruker,
        InnhentingInformasjonFraBruker,
        InnhentingOpplysningerFraBruker,
        InnvilgelseGjenlevendepensjonBosattNorgeEtterUtland,
        InnvilgelseUforetrygd,
        InnvilgelseUforetrygdUtland,
        InnvilgelseUforetrygdMellombehandling,
        InnvilgelseUforetrygdBosattNorgeEtterUtland,
        InnvilgelseUforetrygdMedEndring,
        KlageOrienteringOmOversendelseTilKlageinstans,
        KlageOrienteringOmSaksbehandlingstid,
        OkningUforegrad,
        OmregningUfoerepensjonTilUfoeretrygd,
        OmsorgEgenManuell,
        OpphoerGjenlevendepensjon,
        OrienteringOmForlengetSaksbehandlingstid,
        OrienteringOmSaksbehandlingstid,
        OversettelseAvDokumenter,
        SamletMeldingOmPensjonsvedtakV2,
        TilbakekrevingAvFeilutbetaltBeloep,
        VarselOmMuligAvslag,
        VarselRevurderingAvPensjon,
        VarselTilbakekrevingAvFeilutbetaltBeloep,
        VedtakAvslagPaaOmsorgsopptjening,
        VedtakOmFjerningAvOmsorgsopptjening,
        VedtakOmInnvilgelseAvOmsorgspoeng,
        VedtakOmLavereMinstesatsRedigerbar,
        VedtakOmOktBunnfradragRedigerbar,
        VedtakOmOktFribelopRedigerbar,
        VedtakOmOktMinsteIFURedigerbar,
        VedtakOmLavereReduksjonsprosentRedigerbar,
        VedtakOmOktMinsteIFULavereReduksjonsprosentRedigerbar,
        ReverseringLavereMinstesatsRedigerbar,
        VedtakOmEndringBTEPSRedigerbar,
    )

    override fun hentAutobrevmaler() = autobrev

    override fun hentRedigerbareMaler() = redigerbare

    override fun hentAlltidValgbareVedlegg(): Set<AlltidValgbartVedlegg<*>> = setOf()
}