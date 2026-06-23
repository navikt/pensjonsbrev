package no.nav.pensjon.brev

import no.nav.brev.brevbaker.LetterDataFactory
import no.nav.pensjon.brev.api.model.maler.*
import no.nav.pensjon.brev.api.model.maler.adhoc.fullmakterbprof.FullmaktsgiverBprofAutoDto
import no.nav.pensjon.brev.api.model.maler.adhoc.fullmakterbprof.FullmektigBprofAutoDto
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvUttaksgradAutoDto
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringPgaOpptjeningAutoDto
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDto
import no.nav.pensjon.brev.api.model.maler.legacy.EndretBarnetilleggUfoeretrygdDto
import no.nav.pensjon.brev.api.model.maler.legacy.EndretUforetrygdPGAOpptjeningLegacyDto
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmEndringBarnetilleggEPSAutoDto
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmEtterbetalingOpphor2026AutoDto
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentAutoDto
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsAutoDto
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.*
import no.nav.pensjon.brev.api.model.maler.redigerbar.*
import no.nav.pensjon.brev.api.model.maler.ufoerApi.HvilendeRettUforetrygdDto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.VarselSaksbehandlingstidAutoDto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2
import no.nav.pensjon.brev.api.model.vedlegg.*
import no.nav.pensjon.brev.fixtures.*
import no.nav.pensjon.brev.fixtures.adhoc.fullmakterbprof.createFullmaktsgiverBprofAutoDto
import no.nav.pensjon.brev.fixtures.adhoc.fullmakterbprof.createFullmektigBprofAutoDto
import no.nav.pensjon.brev.fixtures.alder.createEndringAvUttaksgradAutoDto
import no.nav.pensjon.brev.fixtures.alder.createEndringPgaOpptjeningAutoDto
import no.nav.pensjon.brev.fixtures.alder.createInnvilgelseAvAlderspensjonAutoDto
import no.nav.pensjon.brev.fixtures.alder.createOpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDto
import no.nav.pensjon.brev.fixtures.redigerbar.createVedtakOmEtterbetalingOpphor2026AutoDto
import no.nav.pensjon.brev.fixtures.redigerbar.*
import no.nav.pensjon.brev.fixtures.ufoere.createVarselSaksbehandlingstidAutoDto
import no.nav.pensjon.brev.maler.example.EksempelRedigerbartDto
import no.nav.pensjon.brev.maler.example.LetterExampleDto
import no.nav.pensjon.brev.maler.legacy.redigerbar.createVedtakEndringAvUttaksgradDto
import no.nav.pensjon.brev.maler.redigerbar.createVedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto
import no.nav.pensjon.brev.maler.redigerbar.createVedtakEndringAvUttaksgradStansBrukerEllerVergeDto
import no.nav.pensjon.brev.maler.redigerbar.createVedtakEndringAvUttaksgradStansIkkeBrukerEllerVergeDto
import no.nav.pensjon.brev.maler.redigerbar.createVedtakOmFjerningAvOmsorgsopptjeningDto
import no.nav.pensjon.brev.maler.ufoereBrev.adhoc.FeilBelopInntekstendringsbrev
import no.nav.pensjon.brev.maler.vedlegg.*
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year
import kotlin.reflect.KClass

object Fixtures : LetterDataFactory {

    val felles = no.nav.brev.brevbaker.FellesFactory.felles

    val fellesAuto = no.nav.brev.brevbaker.FellesFactory.fellesAuto

    inline fun <reified T : Any> create(): T = create(T::class)
    inline fun <reified T : Any> createVedlegg(): T = createVedlegg(T::class)


    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> create(letterDataType: KClass<T>): T =
        when (letterDataType) {
            FullmektigBprofAutoDto::class -> createFullmektigBprofAutoDto() as T
            FullmaktsgiverBprofAutoDto::class -> createFullmaktsgiverBprofAutoDto() as T
            AvslagForLiteTrygdetidAPDto::class -> createAvslagForLiteTrygdetidAPDto() as T
            AvslagPaaGjenlevenderettIAlderspensjonDto::class -> createAvslagPaaGjenlevenderettIAlderspensjon() as T
            AvslagUfoeretrygdDto::class -> createAvslagUfoeretrygdDto() as T
            BekreftelsePaaPensjonDto::class -> createBekreftelsePaaPensjonDto() as T
            BekreftelsePaaUfoeretrygdDto::class -> createBekreftelsePaaUfoeretrygdDto() as T
            BrukerTestBrevDto::class -> createBrukerTestBrevDto() as T
            EksempelRedigerbartDto::class -> createEksempelbrevRedigerbartDto() as T
            EmptyAutobrevdata::class -> EmptyAutobrevdata as T
            EmptyRedigerbarBrevdata::class -> EmptyRedigerbarBrevdata as T
            EmptySaksbehandlerValg::class -> EmptySaksbehandlerValg as T
            EndretBarnetilleggUfoeretrygdDto::class -> createEndretBarnetilleggUfoeretrygdDto() as T
            EndretUTPgaInntektDtoV2::class -> createEndretUTPgaInntektDtoV2() as T
            EndretUfoeretrygdPGAInntektDto::class -> createEndretUfoeretrygdPGAInntektDto() as T
            EndretUforetrygdPGAOpptjeningLegacyDto::class -> createEndretUforetrygdPGAOpptjeningLegacyDto() as T
            EndringPgaOpptjeningAutoDto::class -> createEndringPgaOpptjeningAutoDto() as T
            EndringAvUttaksgradAutoDto::class -> createEndringAvUttaksgradAutoDto() as T
            EtteroppgjoerEtterbetalingAutoDto::class -> createEtteroppgjoerEtterbetalingAuto() as T
            FeilBelopInntekstendringsbrev::class -> EmptyAutobrevdata as T
            ForespoerselOmDokumentasjonAvBotidINorgeDto::class -> createForespoerselOmDokumentasjonAvBotidINorgeDto() as T
            ForhaandsvarselEtteroppgjoerUfoeretrygdDto::class -> createForhaandsvarselEtteroppgjoerUfoeretrygdDto() as T
            InformasjonOmGjenlevenderettigheterDto::class -> createInformasjonOmGjenlevenderettigheterDto() as T
            InformasjonOmSaksbehandlingstidDto::class -> createInformasjonOmSaksbehandlingstidDto() as T
            InformasjonOmSaksbehandlingstidUtDto::class -> createInformasjonOmSaksbehandlingstidUtDto() as T
            InnhentingInformasjonFraBrukerDto::class -> createInnhentingInformasjonFraBrukerDto() as T
            InnvilgelseAvAlderspensjonDto::class -> createInnvilgelseAvAlderspensjonDto() as T
            InnvilgelseAvAlderspensjonAutoDto::class -> createInnvilgelseAvAlderspensjonAutoDto() as T
            InnvilgelseAvAlderspensjonTrygdeavtaleDto::class -> createInnvilgelseAvAlderspensjonTrygdeavtaleDto() as T
            KlageOrienteringOmOversendelseTilKlageinstansDto::class -> createKlageOrienteringOmOversendelseTilKlageinstansDto() as T
            KlageOrienteringOmSaksbehandlingstidDto::class -> createKlageOrienteringOmSaksbehandlingstidDto() as T
            LetterExampleDto::class -> createLetterExampleDto() as T
            OmsorgEgenAutoDto::class -> createOmsorgEgenAutoDto() as T
            OmsorgEgenManuellDto::class -> createOmsorgManuellDto() as T
            OpphoerBarnetilleggAutoDto::class -> createOpphoerBarnetilleggAutoDto() as T
            OpptjeningVedForhoeyetHjelpesatsDto::class -> OpptjeningVedForhoeyetHjelpesatsDto(Year(2021), false) as T
            OrienteringOmSaksbehandlingstidDto::class -> createOrienteringOmSaksbehandlingstidDto() as T
            PEgruppe10::class -> createPEgruppe10() as T
            SamletMeldingOmPensjonsvedtakDto::class -> createSamletMeldingOmPensjonsvedtakDto() as T
            SamletMeldingOmPensjonsvedtakV2Dto::class -> createSamletMeldingOmPensjonsvedtakV2Dto() as T
            TilbakekrevingAvFeilutbetaltBeloepDto::class -> createTilbakekrevingAvFeilutbetaltBeloepDto() as T
            UfoerOmregningEnsligDto::class -> createUfoerOmregningEnsligDto() as T
            UngUfoerAutoDto::class -> createUngUfoerAutoDto() as T
            VarselRevurderingAvPensjonDto::class -> createVarselRevurderingAvPensjonDto() as T
            VarselSaksbehandlingstidAutoDto::class -> createVarselSaksbehandlingstidAutoDto() as T
            VarselTilbakekrevingAvFeilutbetaltBeloepDto::class -> createVarselTilbakekrevingAvFeilutbetaltBeloep() as T
            VedtakAvslagPaaOmsorgsopptjeningDto::class -> createVedtakAvslagPaaOmsorgsopptjeningDto() as T
            VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto::class -> createVedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto() as T
            VedtakEndringAvAlderspensjonGjenlevenderettigheterDto::class -> createVedtakEndringAvAlderspensjonGjenlevenderettigheterDto() as T
            VedtakEndringAvAlderspensjonInstitusjonsoppholdDto::class -> createVedtakEndringAvAlderspensjonInstitusjonsoppholdDto() as T
            VedtakEndringAvUttaksgradDto::class -> createVedtakEndringAvUttaksgradDto() as T
            VedtakEndringAvUttaksgradStansBrukerEllerVergeDto::class -> createVedtakEndringAvUttaksgradStansBrukerEllerVergeDto() as T
            VedtakEndringAvUttaksgradStansIkkeBrukerEllerVergeDto::class -> createVedtakEndringAvUttaksgradStansIkkeBrukerEllerVergeDto() as T
            VedtakEndringVedFlyttingMellomLandDto::class -> createVedtakEndringVedFlyttingMellomLandDto() as T
            VedtakOmFjerningAvOmsorgsopptjeningDto::class -> createVedtakOmFjerningAvOmsorgsopptjeningDto() as T
            VedtakOmInnvilgelseAvOmsorgspoengDto::class -> createVedtakOmInnvilgelseAvOmsorgspoengDto() as T
            InnvilgelseUfoeretrygdDto::class -> createInnvilgelseUfoeretrygdDto() as T
            InnvilgelseUforetrygdBosattNorgeEtterUtlandDto::class -> createInnvilgelseUforetrygdBosattNorgeEtterUtlandDto() as T
            InnvilgelseUforetrygdMedEndringDto::class -> createInnvilgelseUforetrygdMedEndringDto() as T
            OkningUforegradDto::class -> createOkningUforegradDto() as T
            InnvilgelseUfoeretrygdUtlandDto::class -> createInnvilgelseUfoeretrygdUtlandDto() as T
            InnvilgelseUfoeretrygdMellombehandlingDto::class -> createInnvilgelseUforetrygdMellombehandlingDto() as T
            EndringUfoeretrygdDto::class -> createEndringUfoeretrygdDto() as T
            EndringUfoeretrygdFlyttingUtlandDto::class -> createEndringUfoeretrygdFlyttingUtlandDto() as T
            OmregningUfoerepensjonTilUfoeretrygdDto::class -> createOmregningUfoerepensjonTilUfoeretrygdDto() as T
            OpphoerGjenlevendepensjonDto::class -> createOpphoerGjenlevendepensjonDto() as T
            AvslagGjenlevendepensjonDto::class -> createAvslagGjenlevendepensjonDto() as T
            AvslagGjenlevendepensjonUtlandDto::class -> createAvslagGjenlevendepensjonUtlandDto() as T
            VedtakOmLavereMinstesatsAutoDto::class -> createVedtakOmLavereMinstesatsAutoDto() as T
            VedtakOmIFUReduksjonsprosentRedigerbarDto::class -> createVedtakOmIFUReduksjonsprosentRedigerbarDto() as T
            VedtakOmIFUReduksjonsprosentAutoDto::class -> createVedtakOmIFUReduksjonsprosentAutoDto() as T
            VedtakOmLavereMinstesatsRedigerbarDto::class -> createVedtakOmLavereMinstesatsRedigerbarDto() as T
            InnvilgelseGjenlevendepensjonBosattNorgeEtterUtlandDto::class -> createInnvilgelseGjenlevendepensjonBosattNorgeEtterUtlandDto() as T
            HvilendeRettUforetrygdDto::class -> createHvilendeRettUforetrygdDto() as T
            AvslagUfoerepensjonDto::class -> createAvslagUfoerepensjonDto() as T
            VedtakOmEtterbetalingOpphor2026AutoDto::class -> createVedtakOmEtterbetalingOpphor2026AutoDto() as T
            VedtakOmEtterbetalingOpphor2026RedigerbarDto::class -> createVedtakOmEtterbetalingOpphor2026RedigerbarDto() as T
            VedtakOmEndringBarnetilleggEPSAutoDto::class -> createVedtakOmEndringBarnetilleggEPSAutoDto() as T

            else -> throw IllegalArgumentException("Don't know how to construct: ${letterDataType.qualifiedName}")
        }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> createVedlegg(letterDataType: KClass<T>): T = when(letterDataType) {
        DineRettigheterOgMulighetTilAaKlageDto::class -> createDineRettigheterOgMulighetTilAaKlageDto() as T
        DineRettigheterOgPlikterUforeDto::class -> createDineRettigheterOgPlikterUforeDto() as T
        EgenerklaeringOmsorgsarbeidDto::class -> createEgenerklaeringOmsorgsarbeidDto() as T
        MaanedligPensjonFoerSkattAP2025Dto::class -> createMaanedligPensjonFoerSkattAP2025() as T
        MaanedligPensjonFoerSkattAlderspensjonDto::class -> createMaanedligPensjonFoerSkattAlderspensjonDto() as T
        MaanedligPensjonFoerSkattDto::class -> createMaanedligPensjonFoerSkatt() as T
        MaanedligUfoeretrygdFoerSkattDto.UfoeretrygdPerMaaned::class -> createMaanedligUfoeretrygdFoerSkattDtoUfoeretrygdPerMaaned() as T
        MaanedligUfoeretrygdFoerSkattDto::class -> createMaanedligUfoeretrygdFoerSkattDto() as T
        OpplysningerBruktIBeregningenAlderAP2025Dto::class -> createOpplysningerBruktIBeregningAlderAP2025Dto() as T
        OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDto::class -> createOpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDto() as T
        OpplysningerOmAvdoedBruktIBeregningDto::class -> createOpplysningerOmAvdoedBruktIBeregningDto() as T
        OpplysningerBruktIBeregningenAlderDto::class -> createOpplysningerBruktIBeregningAlderDto() as T
        OpplysningerBruktIBeregningenEndretUttaksgradDto::class -> createOpplysningerBruktIBeregningenEndretUttaksgradDto() as T
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