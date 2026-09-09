package no.nav.pensjon.brev.maler

import no.nav.brev.brevbaker.AllTemplates
import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.maler.adhoc.*
import no.nav.pensjon.brev.maler.adhoc.fullmakterbprof.AdHocVarselUgyldiggjoringFullmaktsgiver
import no.nav.pensjon.brev.maler.adhoc.fullmakterbprof.AdHocVarselUgyldiggjoringFullmektig
import no.nav.pensjon.brev.maler.klageOgAnke.AnkeOrienteringOmSaksbehandling
import no.nav.pensjon.brev.maler.klageOgAnke.AnkeTilsvarTilAnkendePart
import no.nav.pensjon.brev.maler.klageOgAnke.KlageOrienteringOmOversendelseTilKlageinstans
import no.nav.pensjon.brev.maler.klageOgAnke.KlageOrienteringOmSaksbehandlingstid
import no.nav.pensjon.brev.maler.legacy.redigerbar.*
import no.nav.pensjon.brev.maler.redigerbar.*
import no.nav.pensjon.brev.maler.ufore.endring.EndretUfoeretrygdPGAInntektRedigerbar
import no.nav.pensjon.brev.maler.ufore.endring.EndretUfoeretrygdPGAInntektV2
import no.nav.pensjon.brev.maler.ufore.endring.EndretUforetrygdPGAInntektNesteAr
import no.nav.pensjon.brev.maler.ufore.VarselSaksbehandlingstidAuto
import no.nav.pensjon.brev.maler.ufore.adhoc.FeilBelopInntekstendringsbrev.FeilBelopInntekstendringsbrev
import no.nav.pensjon.brev.maler.ufore.adhoc.FeilBelopInntekstendringsbrev_AvkortetTil0.FeilBelopInntekstendringsbrev_AvkortetTil0
import no.nav.pensjon.brev.maler.ufore.hvilenderett.HvilendeRettInfo4Aar
import no.nav.pensjon.brev.maler.ufore.hvilenderett.HvilendeRettMidlertidigOppHoer
import no.nav.pensjon.brev.maler.ufore.hvilenderett.HvilendeRettOppHoer
import no.nav.pensjon.brev.maler.ufore.hvilenderett.HvilendeRettVarselOpphoer
import no.nav.pensjon.brev.maler.ufore.lovendringer2026.minstesats.ReverseringLavereMinstesatsAuto
import no.nav.pensjon.brev.maler.ufore.lovendringer2026.oktbunnfradrag.VedtakOmEndringBTEPSOktoberAuto
import no.nav.pensjon.brev.maler.ufore.lovendringer2026.ifureduksjonsprosent.VedtakOmEndringBarnetilleggEPSAuto
import no.nav.pensjon.brev.maler.ufore.lovendringer2026.minstesats.VedtakOmEndringBarnetilleggEPSRevAuto
import no.nav.pensjon.brev.maler.ufore.lovendringer2026.minstesats.VedtakOmLavereMinstesatsAuto
import no.nav.pensjon.brev.maler.ufore.lovendringer2026.ifureduksjonsprosent.VedtakOmLavereReduksjonsprosentAuto
import no.nav.pensjon.brev.maler.ufore.lovendringer2026.oktbunnfradrag.VedtakOmOktBunnfradragAuto
import no.nav.pensjon.brev.maler.ufore.lovendringer2026.oktbunnfradrag.VedtakOmOktFribelopAuto
import no.nav.pensjon.brev.maler.ufore.lovendringer2026.ifureduksjonsprosent.VedtakOmOktMinsteIFUAuto
import no.nav.pensjon.brev.maler.ufore.lovendringer2026.ifureduksjonsprosent.VedtakOmOktMinsteIFULavereReduksjonsprosentAuto
import no.nav.pensjon.brev.maler.ufore.lovendringer2026.minstesats.ReverseringLavereMinstesatsRedigerbar
import no.nav.pensjon.brev.maler.ufore.lovendringer2026.oktbunnfradrag.VedtakOmEndringBTEPSRedigerbar
import no.nav.pensjon.brev.maler.ufore.lovendringer2026.minstesats.VedtakOmLavereMinstesatsRedigerbar
import no.nav.pensjon.brev.maler.ufore.lovendringer2026.ifureduksjonsprosent.VedtakOmLavereReduksjonsprosentRedigerbar
import no.nav.pensjon.brev.maler.ufore.lovendringer2026.oktbunnfradrag.VedtakOmOktBunnfradragRedigerbar
import no.nav.pensjon.brev.maler.ufore.lovendringer2026.oktbunnfradrag.VedtakOmOktFribelopRedigerbar
import no.nav.pensjon.brev.maler.ufore.lovendringer2026.ifureduksjonsprosent.VedtakOmOktMinsteIFULavereReduksjonsprosentRedigerbar
import no.nav.pensjon.brev.maler.ufore.lovendringer2026.ifureduksjonsprosent.VedtakOmOktMinsteIFURedigerbar
import no.nav.pensjon.brev.maler.ufore.etteroppgjor.ForhaandsvarselEtteroppgjoerUfoeretrygdAuto
import no.nav.pensjon.brev.maler.ufore.barnetillegg.OpphoerBarnetilleggAuto
import no.nav.pensjon.brev.maler.ufore.UfoerOmregningEnslig
import no.nav.pensjon.brev.maler.ufore.UngUfoerAuto
import no.nav.pensjon.brev.maler.ufore.avslag.AvslagUfoerepensjonRedigerbar
import no.nav.pensjon.brev.maler.ufore.avslag.AvslagUfoeretrygdRedigerbar
import no.nav.pensjon.brev.maler.ufore.barnetillegg.EndretBarnetilleggUfoerertrygdAuto
import no.nav.pensjon.brev.maler.ufore.diverse.BekreftelsePaaUfoeretrygdRedigerbar
import no.nav.pensjon.brev.maler.ufore.diverse.EndretUforetrygdPGAOpptjeningLegacy
import no.nav.pensjon.brev.maler.ufore.diverse.InformasjonOmSaksbehandlingstidUTRedigerbar
import no.nav.pensjon.brev.maler.ufore.diverse.OmregningUfoerepensjonTilUfoeretrygdRedigerbar
import no.nav.pensjon.brev.maler.ufore.diverse.OrienteringOmSaksbehandlingstidRedigerbar
import no.nav.pensjon.brev.maler.ufore.diverse.VedtakOmEtterbetalingOpphor2026Redigerbar
import no.nav.pensjon.brev.maler.ufore.endring.EndringUforetrygdRedigerbar
import no.nav.pensjon.brev.maler.ufore.etteroppgjor.EtteroppgjoerEtterbetalingAutoLegacy
import no.nav.pensjon.brev.maler.ufore.innvilgelse.InnvilgelseUforetrygdBosattNorgeEtterUtlandRedigerbar
import no.nav.pensjon.brev.maler.ufore.innvilgelse.InnvilgelseUforetrygdMedEndringRedigerbar
import no.nav.pensjon.brev.maler.ufore.innvilgelse.InnvilgelseUforetrygdMellombehandlingRedigerbar
import no.nav.pensjon.brev.maler.ufore.innvilgelse.InnvilgelseUforetrygdRedigerbar
import no.nav.pensjon.brev.maler.ufore.innvilgelse.InnvilgelseUforetrygdUtlandRedigerbar
import no.nav.pensjon.brev.maler.ufore.lovendringer2026.ifureduksjonsprosent.VedtakOmEtterbetalingOpphor2026Auto
import no.nav.pensjon.brev.maler.ufore.lovendringer2026.ifureduksjonsprosent.VedtakOmEtterbetalingOpphor2026LavereReduksjonsprosentAuto
import no.nav.pensjon.brev.maler.ufore.lovendringer2026.ifureduksjonsprosent.VedtakOmEtterbetalingOpphor2026OktIfuAuto
import no.nav.pensjon.brev.maler.ufore.uforegrad.OkningUforegradRedigerbar
import no.nav.pensjon.brev.maler.ufore.utland.DelvisEksportAvUforetrygdRedigerbar
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
        EndretBarnetilleggUfoerertrygdAuto,
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

    private val redigerbare: Set<RedigerbarTemplate<out RedigerbarBrevdata<*>>> = setOf(
        AnkeOrienteringOmSaksbehandling,
        AnkeTilsvarTilAnkendePart,
        AvslagGjenlevendepensjon,
        AvslagGjenlevendepensjonUtland,
        AvslagUfoerepensjonRedigerbar,
        AvslagUfoeretrygdRedigerbar,
        BekreftelsePaaFlyktningstatus,
        BekreftelsePaaPensjon,
        BekreftelsePaaUfoeretrygdRedigerbar,
        BrukerTestBrev,
        BrukerTestVedtaksbrev,
        DelvisEksportAvUforetrygdRedigerbar,
        EndringUforetrygdRedigerbar,
        EndretUfoeretrygdPGAInntektRedigerbar,
        VedtakOmEtterbetalingOpphor2026Redigerbar,
        ForespoerselOmDokumentasjonAvBotidINorgeEtterlatte,
        InformasjonOmGjenlevenderettigheter,
        InformasjonOmSaksbehandlingstid,
        InformasjonOmSaksbehandlingstidUTRedigerbar,
        InnhentingDokumentasjonFraBruker,
        InnhentingOpplysningerFraBruker,
        InnvilgelseGjenlevendepensjonBosattNorgeEtterUtland,
        InnvilgelseUforetrygdRedigerbar,
        InnvilgelseUforetrygdUtlandRedigerbar,
        InnvilgelseUforetrygdMellombehandlingRedigerbar,
        InnvilgelseUforetrygdBosattNorgeEtterUtlandRedigerbar,
        InnvilgelseUforetrygdMedEndringRedigerbar,
        KlageOrienteringOmOversendelseTilKlageinstans,
        KlageOrienteringOmSaksbehandlingstid,
        OkningUforegradRedigerbar,
        OmregningUfoerepensjonTilUfoeretrygdRedigerbar,
        OmsorgEgenManuell,
        OpphoerGjenlevendepensjon,
        OrienteringOmForlengetSaksbehandlingstid,
        OrienteringOmSaksbehandlingstidRedigerbar,
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