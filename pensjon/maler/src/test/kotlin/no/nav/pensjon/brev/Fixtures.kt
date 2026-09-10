package no.nav.pensjon.brev

import no.nav.brev.brevbaker.LetterDataFactory
import no.nav.brev.brevbaker.SaksbehandlervalgIDSLTestImpl
import no.nav.pensjon.brev.api.model.maler.*
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.*
import no.nav.pensjon.brev.api.model.maler.redigerbar.*
import no.nav.pensjon.brev.api.model.vedlegg.*
import no.nav.pensjon.brev.fixtures.*
import no.nav.pensjon.brev.fixtures.adhoc.fullmakterbprof.createFullmaktsgiverBprofAutoDto
import no.nav.pensjon.brev.fixtures.adhoc.fullmakterbprof.createFullmektigBprofAutoDto
import no.nav.pensjon.brev.fixtures.redigerbar.createVedtakOmEtterbetalingOpphor2026AutoDto
import no.nav.pensjon.brev.fixtures.redigerbar.*
import no.nav.pensjon.brev.fixtures.ufoere.createVarselSaksbehandlingstidAutoDto
import no.nav.pensjon.brev.maler.redigerbar.createVedtakOmFjerningAvOmsorgsopptjeningDto
import no.nav.pensjon.brev.maler.ufore.adhoc.FeilBelopInntekstendringsbrev
import no.nav.pensjon.brev.maler.vedlegg.*
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year
import kotlin.reflect.KClass
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.maler.*
import no.nav.pensjon.brev.maler.adhoc.*
import no.nav.pensjon.brev.maler.adhoc.fullmakterbprof.*
import no.nav.pensjon.brev.maler.example.*
import no.nav.pensjon.brev.maler.klageOgAnke.*
import no.nav.pensjon.brev.maler.legacy.redigerbar.*
import no.nav.pensjon.brev.maler.redigerbar.*
import no.nav.pensjon.brev.maler.ufore.*
import no.nav.pensjon.brev.maler.ufore.adhoc.FeilBelopInntekstendringsbrev_AvkortetTil0
import no.nav.pensjon.brev.maler.ufore.avslag.*
import no.nav.pensjon.brev.maler.ufore.barnetillegg.*
import no.nav.pensjon.brev.maler.ufore.diverse.*
import no.nav.pensjon.brev.maler.ufore.endring.*
import no.nav.pensjon.brev.maler.ufore.etteroppgjor.*
import no.nav.pensjon.brev.maler.ufore.hvilenderett.*
import no.nav.pensjon.brev.maler.ufore.innvilgelse.*
import no.nav.pensjon.brev.maler.ufore.lovendringer2026.ifureduksjonsprosent.*
import no.nav.pensjon.brev.maler.ufore.lovendringer2026.minstesats.*
import no.nav.pensjon.brev.maler.ufore.lovendringer2026.oktbunnfradrag.*
import no.nav.pensjon.brev.maler.ufore.uforegrad.OkningUforegradRedigerbar
import no.nav.pensjon.brev.maler.ufore.utland.DelvisEksportAvUforetrygdRedigerbar
import no.nav.pensjon.brev.template.BrevTemplate

object Fixtures : LetterDataFactory {

    val felles = no.nav.brev.brevbaker.FellesFactory.felles

    val fellesAuto = no.nav.brev.brevbaker.FellesFactory.fellesAuto

    inline fun <reified T : Any> createVedlegg(): T = createVedlegg(T::class)


    @Suppress("UNCHECKED_CAST")
    override fun <T : BrevbakerBrevdata> create(templateType: KClass<out BrevTemplate<T, *>>): T =
        when (templateType) {
            AdHocVarselUgyldiggjoringFullmektig::class -> createFullmektigBprofAutoDto() as T
            AdHocVarselUgyldiggjoringFullmaktsgiver::class -> createFullmaktsgiverBprofAutoDto() as T
            AvslagUfoeretrygdRedigerbar::class -> createAvslagUfoeretrygdDto() as T
            BekreftelsePaaPensjon::class -> createBekreftelsePaaPensjonDto() as T
            BekreftelsePaaUfoeretrygdRedigerbar::class -> createBekreftelsePaaUfoeretrygdDto() as T
            BrukerTestBrev::class -> createBrukerTestBrevDto() as T
            BrukerTestVedtaksbrev::class -> createBrukerTestVedtaksbrevDto() as T
            EksempelbrevRedigerbart::class -> createEksempelbrevRedigerbartDto() as T
            FeilBelopInntekstendringsbrev.FeilBelopInntekstendringsbrev::class -> EmptyAutobrevdata as T
            FeilBelopInntekstendringsbrev_AvkortetTil0.FeilBelopInntekstendringsbrev_AvkortetTil0::class -> EmptyAutobrevdata as T
            AdhocMidlertidigOpphoerHvilenderett10Aar::class -> EmptyAutobrevdata as T
            AdhocUfoeretrygdKombiDagpengerInntektsavkorting::class -> EmptyAutobrevdata as T
            AdhocUfoeretrygdEtterbetalingDagpenger::class -> EmptyAutobrevdata as T
            AdhocUfoeretrygdKombiDagpenger::class -> EmptyAutobrevdata as T
            AdhocVarselOpphoerMedHvilendeRett::class -> EmptyAutobrevdata as T
            AdhocUfoeretrygdVarselOpphoerEktefelletillegg::class -> EmptyAutobrevdata as T
            AdhocFeilEtteroppgjoer2023::class -> EmptyAutobrevdata as T
            AdhocInformasjonHvilendeRett4Aar::class -> EmptyAutobrevdata as T
            InnhentingDokumentasjonFraBruker::class -> EmptyRedigerbarBrevdata(saksbehandlerValg = SaksbehandlervalgIDSLTestImpl()) as T
            InnhentingOpplysningerFraBruker::class -> EmptyRedigerbarBrevdata(saksbehandlerValg = SaksbehandlervalgIDSLTestImpl()) as T
            VarselOmMuligAvslag::class -> EmptyRedigerbarBrevdata(saksbehandlerValg = SaksbehandlervalgIDSLTestImpl()) as T
            BekreftelsePaaFlyktningstatus::class -> EmptyRedigerbarBrevdata(saksbehandlerValg = SaksbehandlervalgIDSLTestImpl()) as T
            OrienteringOmForlengetSaksbehandlingstid::class -> EmptyRedigerbarBrevdata(saksbehandlerValg = SaksbehandlervalgIDSLTestImpl()) as T
            OversettelseAvDokumenter::class -> EmptyRedigerbarBrevdata(saksbehandlerValg = SaksbehandlervalgIDSLTestImpl()) as T
            KlageOrienteringOmSaksbehandlingstid::class -> EmptyRedigerbarBrevdata(saksbehandlerValg = SaksbehandlervalgIDSLTestImpl()) as T
            AnkeOrienteringOmSaksbehandling::class -> EmptyRedigerbarBrevdata(saksbehandlerValg = SaksbehandlervalgIDSLTestImpl()) as T
            AnkeTilsvarTilAnkendePart::class -> EmptyRedigerbarBrevdata(saksbehandlerValg = SaksbehandlervalgIDSLTestImpl()) as T
            KlageOrienteringOmOversendelseTilKlageinstans::class -> EmptyRedigerbarBrevdata(saksbehandlerValg = SaksbehandlervalgIDSLTestImpl()) as T
            EndretBarnetilleggUfoerertrygdAuto::class -> createEndretBarnetilleggUfoeretrygdDto() as T
            EndretUfoeretrygdPGAInntektV2::class -> createEndretUTPgaInntektDtoV2() as T
            EndretUforetrygdPGAInntektNesteAr::class -> createEndretUTPgaInntektDtoV2() as T
            EndretUfoeretrygdPGAInntektRedigerbar::class -> createEndretUfoeretrygdPGAInntektRedigerbarDto() as T
            EndretUforetrygdPGAOpptjeningLegacy::class -> createEndretUforetrygdPGAOpptjeningLegacyDto() as T
            EtteroppgjoerEtterbetalingAutoLegacy::class -> createEtteroppgjoerEtterbetalingAuto() as T
            ForespoerselOmDokumentasjonAvBotidINorgeEtterlatte::class -> createForespoerselOmDokumentasjonAvBotidINorgeDto() as T
            ForhaandsvarselEtteroppgjoerUfoeretrygdAuto::class -> createForhaandsvarselEtteroppgjoerUfoeretrygdDto() as T
            InformasjonOmGjenlevenderettigheter::class -> createInformasjonOmGjenlevenderettigheterDto() as T
            InformasjonOmSaksbehandlingstid::class -> createInformasjonOmSaksbehandlingstidDto() as T
            InformasjonOmSaksbehandlingstidUTRedigerbar::class -> createInformasjonOmSaksbehandlingstidUtDto() as T
            LetterExample::class -> createLetterExampleDto() as T
            OmsorgEgenAuto::class -> createOmsorgEgenAutoDto() as T
            OmsorgEgenManuell::class -> createOmsorgManuellDto() as T
            OpphoerBarnetilleggAuto::class -> createOpphoerBarnetilleggAutoDto() as T
            OpptjeningVedForhoeyetHjelpesats::class -> OpptjeningVedForhoeyetHjelpesatsDto(Year(2021), false) as T
            OrienteringOmSaksbehandlingstidRedigerbarV2::class -> createOrienteringOmSaksbehandlingstidDto() as T
            OrienteringOmSaksbehandlingstidRedigerbar::class -> createOrienteringOmSaksbehandlingstidDto() as T
            SamletMeldingOmPensjonsvedtakV2::class -> createSamletMeldingOmPensjonsvedtakV2Dto() as T
            TilbakekrevingAvFeilutbetaltBeloep::class -> createTilbakekrevingAvFeilutbetaltBeloepDto() as T
            UfoerOmregningEnslig::class -> createUfoerOmregningEnsligDto() as T
            UngUfoerAuto::class -> createUngUfoerAutoDto() as T
            VarselRevurderingAvPensjon::class -> createVarselRevurderingAvPensjonDto() as T
            VarselSaksbehandlingstidAutoV2::class -> createVarselSaksbehandlingstidAutoDto() as T
            VarselSaksbehandlingstidAuto::class -> createVarselSaksbehandlingstidAutoDto() as T
            VarselTilbakekrevingAvFeilutbetaltBeloep::class -> createVarselTilbakekrevingAvFeilutbetaltBeloep() as T
            VedtakAvslagPaaOmsorgsopptjening::class -> createVedtakAvslagPaaOmsorgsopptjeningDto() as T
            VedtakOmFjerningAvOmsorgsopptjening::class -> createVedtakOmFjerningAvOmsorgsopptjeningDto() as T
            VedtakOmInnvilgelseAvOmsorgspoeng::class -> createVedtakOmInnvilgelseAvOmsorgspoengDto() as T
            InnvilgelseUforetrygdRedigerbar::class -> createInnvilgelseUfoeretrygdDto() as T
            InnvilgelseUforetrygdBosattNorgeEtterUtlandRedigerbar::class -> createInnvilgelseUforetrygdBosattNorgeEtterUtlandDto() as T
            InnvilgelseUforetrygdMedEndringRedigerbar::class -> createInnvilgelseUforetrygdMedEndringDto() as T
            OkningUforegradRedigerbar::class -> createOkningUforegradDto() as T
            InnvilgelseUforetrygdUtlandRedigerbar::class -> createInnvilgelseUfoeretrygdUtlandDto() as T
            InnvilgelseUforetrygdMellombehandlingRedigerbar::class -> createInnvilgelseUforetrygdMellombehandlingDto() as T
            EndringUforetrygdRedigerbar::class -> createEndringUfoeretrygdDto() as T
            DelvisEksportAvUforetrygdRedigerbar::class -> createEndringUfoeretrygdFlyttingUtlandDto() as T
            OmregningUfoerepensjonTilUfoeretrygdRedigerbar::class -> createOmregningUfoerepensjonTilUfoeretrygdDto() as T
            OpphoerGjenlevendepensjon::class -> createOpphoerGjenlevendepensjonDto() as T
            AvslagGjenlevendepensjon::class -> createAvslagGjenlevendepensjonDto() as T
            AvslagGjenlevendepensjonUtland::class -> createAvslagGjenlevendepensjonUtlandDto() as T
            VedtakOmLavereMinstesatsAuto::class -> createVedtakOmLavereMinstesatsAutoDto() as T
            VedtakOmOktBunnfradragAuto::class -> createVedtakOmOktBunnfradragAutoDto() as T
            VedtakOmOktFribelopAuto::class -> createVedtakOmOktFribelopAutoDto() as T
            VedtakOmLavereReduksjonsprosentRedigerbar::class -> createVedtakOmIFUReduksjonsprosentRedigerbarDto() as T
            VedtakOmOktMinsteIFURedigerbar::class -> createVedtakOmIFUReduksjonsprosentRedigerbarDto() as T
            VedtakOmOktMinsteIFULavereReduksjonsprosentRedigerbar::class -> createVedtakOmIFUReduksjonsprosentRedigerbarDto() as T
            VedtakOmLavereReduksjonsprosentAuto::class -> createVedtakOmIFUReduksjonsprosentAutoDto() as T
            VedtakOmOktMinsteIFULavereReduksjonsprosentAuto::class -> createVedtakOmIFUReduksjonsprosentAutoDto() as T
            VedtakOmOktMinsteIFUAuto::class -> createVedtakOmIFUReduksjonsprosentAutoDto() as T
            VedtakOmLavereMinstesatsRedigerbar::class -> createVedtakOmLavereMinstesatsRedigerbarDto() as T
            VedtakOmOktBunnfradragRedigerbar::class -> createVedtakOmOktBunnfradragRedigerbarDto() as T
            VedtakOmOktFribelopRedigerbar::class -> createVedtakOmOktFribelopRedigerbarDto() as T
            InnvilgelseGjenlevendepensjonBosattNorgeEtterUtland::class -> createInnvilgelseGjenlevendepensjonBosattNorgeEtterUtlandDto() as T
            HvilendeRettMidlertidigOppHoer::class -> createHvilendeRettUforetrygdDto() as T
            HvilendeRettOppHoer::class -> createHvilendeRettUforetrygdDto() as T
            HvilendeRettInfo4Aar::class -> createHvilendeRettUforetrygdDto() as T
            HvilendeRettVarselOpphoer::class -> createHvilendeRettUforetrygdDto() as T
            AvslagUfoerepensjonRedigerbar::class -> createAvslagUfoerepensjonDto() as T
            VedtakOmEtterbetalingOpphor2026LavereReduksjonsprosentAuto::class -> createVedtakOmEtterbetalingOpphor2026AutoDto() as T
            VedtakOmEtterbetalingOpphor2026OktIfuAuto::class -> createVedtakOmEtterbetalingOpphor2026AutoDto() as T
            VedtakOmEtterbetalingOpphor2026Auto::class -> createVedtakOmEtterbetalingOpphor2026AutoDto() as T
            VedtakOmEtterbetalingOpphor2026Redigerbar::class -> createVedtakOmEtterbetalingOpphor2026RedigerbarDto() as T
            VedtakOmEndringBarnetilleggEPSAuto::class -> createVedtakOmEndringBarnetilleggEPSAutoDto() as T
            VedtakOmEndringBarnetilleggEPSRevAuto::class -> createVedtakOmEndringBarnetilleggEPSAutoDto() as T
            VedtakOmEndringBTEPSOktoberAuto::class -> createVedtakOmEndringBarnetilleggEPSAutoDto() as T
            ReverseringLavereMinstesatsRedigerbar::class -> createReverseringLavereMinstesatsRedigerbarDto() as T
            ReverseringLavereMinstesatsAuto::class -> createReverseringLavereMinstesatsAutoDto() as T
            VedtakOmEndringBTEPSRedigerbar::class -> createVedtakOmEndringBarnetilleggEPSRedigerbarDto() as T
            else -> throw IllegalArgumentException("Don't know how to construct: ${templateType.qualifiedName}")
        }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> createVedlegg(letterDataType: KClass<T>): T = when(letterDataType) {
        DineRettigheterOgMulighetTilAaKlageDto::class -> createDineRettigheterOgMulighetTilAaKlageDto() as T
        DineRettigheterOgPlikterUforeDto::class -> createDineRettigheterOgPlikterUforeDto() as T
        EgenerklaeringOmsorgsarbeidDto::class -> createEgenerklaeringOmsorgsarbeidDto() as T
        MaanedligUfoeretrygdFoerSkattDto.UfoeretrygdPerMaaned::class -> createMaanedligUfoeretrygdFoerSkattDtoUfoeretrygdPerMaaned() as T
        MaanedligUfoeretrygdFoerSkattDto::class -> createMaanedligUfoeretrygdFoerSkattDto() as T
        OpplysningerBruktIBeregningUTDto.BarnetilleggGjeldende.Fellesbarn::class -> createOpplysningerBruktIBeregningUTDtoBarnetilleggGjeldendeFellesbarn() as T
        OpplysningerBruktIBeregningUTDto.BarnetilleggGjeldende.Saerkullsbarn::class -> createOpplysningerBruktIBeregningUTDtoBarnetilleggGjeldendeSaerkullsbarn() as T
        OpplysningerBruktIBeregningUTDto.BarnetilleggGjeldende::class -> createOpplysningerBruktIBeregningUTDtoBarnetilleggGjeldende() as T
        OpplysningerBruktIBeregningUTDto.BeregnetUTPerManedGjeldende::class -> createOpplysningerBruktIBeregningUTDtoBeregnetUTPerManedGjeldende() as T
        OpplysningerBruktIBeregningUTDto.InntektFoerUfoereGjeldende::class -> createOpplysningerBruktIBeregningUTDtoInntektFoerUfoereGjeldende() as T
        OpplysningerBruktIBeregningUTDto.InntektsAvkortingGjeldende::class -> createOpplysningerBruktIBeregningUTDtoInntektsAvkortingGjeldende() as T
        OpplysningerBruktIBeregningUTDto.TrygdetidsdetaljerGjeldende.UtenforEOSogNorden::class -> createOpplysningerBruktIBeregningUTDtoTrygdetidsdetaljerGjeldendeUtenforEOSogNorden() as T
        OpplysningerBruktIBeregningUTDto.TrygdetidsdetaljerGjeldende::class -> createOpplysningerBruktIBeregningUTDtoTrygdetidsdetaljerGjeldende() as T
        OpplysningerBruktIBeregningUTDto.UfoeretrygdGjeldende::class -> createOpplysningerBruktIBeregningUTDtoUfoeretrygdGjeldende() as T
        OpplysningerBruktIBeregningUTDto.YrkesskadeGjeldende::class -> createOpplysningerBruktIBeregningUTDtoYrkesskadeGjeldende() as T
        OpplysningerBruktIBeregningUTDto::class -> createOpplysningerBruktIBeregningUTDto() as T
        OpplysningerOmEtteroppgjoeretDto::class -> createForhaandsvarselEtteroppgjoerUfoeretrygdDtoOpplysningerOmEtteroppgjoret() as T
        OrienteringOmRettigheterOgPlikterDto::class -> createOrienteringOmRettigheterOgPlikterDto() as T
        OrienteringOmRettigheterUfoereDto::class -> createOrienteringOmRettigheterUfoereDto() as T
        OversiktOverFeilutbetalingPEDto::class -> createOversiktOverFeilutbetalingPEDto() as T
        PEgruppe10::class -> createPEgruppe10() as T
        else -> throw IllegalArgumentException("Don't know how to construct: ${letterDataType.qualifiedName}")
    }
}