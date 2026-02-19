package no.nav.pensjon.brev

import no.nav.brev.brevbaker.LetterDataFactory
import no.nav.pensjon.brev.api.model.maler.*
import no.nav.pensjon.brev.api.model.maler.adhoc.fullmakterbprof.FullmaktsgiverBprofAutoDto
import no.nav.pensjon.brev.api.model.maler.adhoc.fullmakterbprof.FullmektigBprofAutoDto
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027Dto
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvUttaksgradAutoDto
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringPgaOpptjeningAutoDto
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDto
import no.nav.pensjon.brev.api.model.maler.legacy.EndretBarnetilleggUfoeretrygdDto
import no.nav.pensjon.brev.api.model.maler.legacy.EndretUforetrygdPGAOpptjeningLegacyDto
import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.AvslagUfoeretrygdDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdFlyttingUtlandDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvUttaksgradDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.BekreftelsePaaPensjonDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.BekreftelsePaaUfoeretrygdDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.BrukerTestBrevDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.ForespoerselOmDokumentasjonAvBotidINorgeDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmGjenlevenderettigheterDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidUtDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnhentingInformasjonFraBrukerDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.OmsorgEgenManuellDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.OrienteringOmSaksbehandlingstidDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.TilbakekrevingAvFeilutbetaltBeloepDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VarselRevurderingAvPensjonDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VarselTilbakekrevingAvFeilutbetaltBeloepDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakAvslagPaaOmsorgsopptjeningDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonInstitusjonsoppholdDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvUttaksgradStansBrukerEllerVergeDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvUttaksgradStansIkkeBrukerEllerVergeDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakOmFjerningAvOmsorgsopptjeningDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakOmInnvilgelseAvOmsorgspoengDto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.VarselSaksbehandlingstidAutoDto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.api.model.vedlegg.EgenerklaeringOmsorgsarbeidDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattAlderspensjonDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025Dto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenEndretUttaksgradDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brev.api.model.vedlegg.OversiktOverFeilutbetalingPEDto
import no.nav.pensjon.brev.fixtures.adhoc.fullmakterbprof.createFullmaktsgiverBprofAutoDto
import no.nav.pensjon.brev.fixtures.adhoc.fullmakterbprof.createFullmektigBprofAutoDto
import no.nav.pensjon.brev.fixtures.adhoc.gjenlevenderett2027.createGjenlevenderett2027Dto
import no.nav.pensjon.brev.fixtures.alder.createEndringAvUttaksgradAutoDto
import no.nav.pensjon.brev.fixtures.alder.createEndringPgaOpptjeningAutoDto
import no.nav.pensjon.brev.fixtures.alder.createInnvilgelseAvAlderspensjonAutoDto
import no.nav.pensjon.brev.fixtures.alder.createOpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDto
import no.nav.pensjon.brev.fixtures.createAvslagUfoeretrygdDto
import no.nav.pensjon.brev.fixtures.createEgenerklaeringOmsorgsarbeidDto
import no.nav.pensjon.brev.fixtures.createEksempelbrevRedigerbartDto
import no.nav.pensjon.brev.fixtures.createEndretBarnetilleggUfoeretrygdDto
import no.nav.pensjon.brev.fixtures.createEndretUTPgaInntektDtoV2
import no.nav.pensjon.brev.fixtures.createEndretUfoeretrygdPGAInntektDto
import no.nav.pensjon.brev.fixtures.createEndretUforetrygdPGAOpptjeningLegacyDto
import no.nav.pensjon.brev.fixtures.createEtteroppgjoerEtterbetalingAuto
import no.nav.pensjon.brev.fixtures.createForespoerselOmDokumentasjonAvBotidINorgeDto
import no.nav.pensjon.brev.fixtures.createForhaandsvarselEtteroppgjoerUfoeretrygdDto
import no.nav.pensjon.brev.fixtures.createForhaandsvarselEtteroppgjoerUfoeretrygdDtoOpplysningerOmEtteroppgjoret
import no.nav.pensjon.brev.fixtures.createInformasjonOmSaksbehandlingstidDto
import no.nav.pensjon.brev.fixtures.createInformasjonOmSaksbehandlingstidUtDto
import no.nav.pensjon.brev.fixtures.createLetterExampleDto
import no.nav.pensjon.brev.fixtures.createMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.fixtures.createMaanedligPensjonFoerSkattAP2025
import no.nav.pensjon.brev.fixtures.createMaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.fixtures.createMaanedligUfoeretrygdFoerSkattDtoUfoeretrygdPerMaaned
import no.nav.pensjon.brev.fixtures.createOmsorgEgenAutoDto
import no.nav.pensjon.brev.fixtures.createOpphoerBarnetilleggAutoDto
import no.nav.pensjon.brev.fixtures.createOpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.fixtures.createOpplysningerBruktIBeregningUTDtoBarnetilleggGjeldende
import no.nav.pensjon.brev.fixtures.createOpplysningerBruktIBeregningUTDtoBarnetilleggGjeldendeFellesbarn
import no.nav.pensjon.brev.fixtures.createOpplysningerBruktIBeregningUTDtoBarnetilleggGjeldendeSaerkullsbarn
import no.nav.pensjon.brev.fixtures.createOpplysningerBruktIBeregningUTDtoBeregnetUTPerManedGjeldende
import no.nav.pensjon.brev.fixtures.createOpplysningerBruktIBeregningUTDtoInntektFoerUfoereGjeldende
import no.nav.pensjon.brev.fixtures.createOpplysningerBruktIBeregningUTDtoInntektsAvkortingGjeldende
import no.nav.pensjon.brev.fixtures.createOpplysningerBruktIBeregningUTDtoTrygdetidsdetaljerGjeldende
import no.nav.pensjon.brev.fixtures.createOpplysningerBruktIBeregningUTDtoTrygdetidsdetaljerGjeldendeUtenforEOSogNorden
import no.nav.pensjon.brev.fixtures.createOpplysningerBruktIBeregningUTDtoUfoeretrygdGjeldende
import no.nav.pensjon.brev.fixtures.createOpplysningerBruktIBeregningUTDtoYrkesskadeGjeldende
import no.nav.pensjon.brev.fixtures.createOrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brev.fixtures.createPE
import no.nav.pensjon.brev.fixtures.createSamletMeldingOmPensjonsvedtakDto
import no.nav.pensjon.brev.fixtures.createSamletMeldingOmPensjonsvedtakV2Dto
import no.nav.pensjon.brev.fixtures.createUfoerOmregningEnsligDto
import no.nav.pensjon.brev.fixtures.createUngUfoerAutoDto
import no.nav.pensjon.brev.fixtures.redigerbar.createVedtakAvslagPaaOmsorgsopptjeningDto
import no.nav.pensjon.brev.fixtures.redigerbar.createVedtakOmInnvilgelseAvOmsorgspoengDto
import no.nav.pensjon.brev.fixtures.redigerbar.createAvslagForLiteTrygdetidAPDto
import no.nav.pensjon.brev.fixtures.redigerbar.createAvslagPaaGjenlevenderettIAlderspensjon
import no.nav.pensjon.brev.fixtures.redigerbar.createBekreftelsePaaPensjonDto
import no.nav.pensjon.brev.fixtures.redigerbar.createBekreftelsePaaUfoeretrygdDto
import no.nav.pensjon.brev.fixtures.redigerbar.createBrukerTestBrevDto
import no.nav.pensjon.brev.fixtures.redigerbar.createEndringUfoeretrygdDto
import no.nav.pensjon.brev.fixtures.redigerbar.createEndringUfoeretrygdFlyttingUtlandDto
import no.nav.pensjon.brev.fixtures.redigerbar.createInformasjonOmGjenlevenderettigheterDto
import no.nav.pensjon.brev.fixtures.redigerbar.createInnhentingInformasjonFraBrukerDto
import no.nav.pensjon.brev.fixtures.redigerbar.createInnvilgelseAvAlderspensjonDto
import no.nav.pensjon.brev.fixtures.redigerbar.createInnvilgelseAvAlderspensjonTrygdeavtaleDto
import no.nav.pensjon.brev.fixtures.redigerbar.createInnvilgelseUfoeretrygdDto
import no.nav.pensjon.brev.fixtures.redigerbar.createOmsorgManuellDto
import no.nav.pensjon.brev.fixtures.redigerbar.createOpplysningerOmAvdoedBruktIBeregningDto
import no.nav.pensjon.brev.fixtures.redigerbar.createOrienteringOmSaksbehandlingstidDto
import no.nav.pensjon.brev.fixtures.redigerbar.createOversiktOverFeilutbetalingPEDto
import no.nav.pensjon.brev.fixtures.redigerbar.createTilbakekrevingAvFeilutbetaltBeloepDto
import no.nav.pensjon.brev.fixtures.redigerbar.createVarselRevurderingAvPensjonDto
import no.nav.pensjon.brev.fixtures.redigerbar.createVarselTilbakekrevingAvFeilutbetaltBeloep
import no.nav.pensjon.brev.fixtures.redigerbar.createVedtakEndringAvAlderspensjonGjenlevenderettigheterDto
import no.nav.pensjon.brev.fixtures.redigerbar.createVedtakEndringAvAlderspensjonInstitusjonsoppholdDto
import no.nav.pensjon.brev.fixtures.redigerbar.createVedtakEndringVedFlyttingMellomLandDto
import no.nav.pensjon.brev.fixtures.ufoere.createVarselSaksbehandlingstidAutoDto
import no.nav.pensjon.brev.maler.example.EksempelRedigerbartDto
import no.nav.pensjon.brev.maler.example.LetterExampleDto
import no.nav.pensjon.brev.maler.legacy.redigerbar.createVedtakEndringAvUttaksgradDto
import no.nav.pensjon.brev.maler.redigerbar.createVedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto
import no.nav.pensjon.brev.maler.redigerbar.createVedtakEndringAvUttaksgradStansBrukerEllerVergeDto
import no.nav.pensjon.brev.maler.redigerbar.createVedtakEndringAvUttaksgradStansIkkeBrukerEllerVergeDto
import no.nav.pensjon.brev.maler.redigerbar.createVedtakOmFjerningAvOmsorgsopptjeningDto
import no.nav.pensjon.brev.maler.ufoereBrev.adhoc.FeilBelopInntekstendringsbrev
import no.nav.pensjon.brev.maler.vedlegg.createDineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.maler.vedlegg.createMaanedligPensjonFoerSkattAlderspensjonDto
import no.nav.pensjon.brev.maler.vedlegg.createOpplysningerBruktIBeregningAlderAP2025Dto
import no.nav.pensjon.brev.maler.vedlegg.createOpplysningerBruktIBeregningAlderDto
import no.nav.pensjon.brev.maler.vedlegg.createOpplysningerBruktIBeregningenEndretUttaksgradDto
import no.nav.pensjon.brev.maler.vedlegg.createOrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brevbaker.api.model.Year
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
            Gjenlevenderett2027Dto::class -> createGjenlevenderett2027Dto() as T
            InformasjonOmGjenlevenderettigheterDto::class -> createInformasjonOmGjenlevenderettigheterDto() as T
            InformasjonOmSaksbehandlingstidDto::class -> createInformasjonOmSaksbehandlingstidDto() as T
            InformasjonOmSaksbehandlingstidUtDto::class -> createInformasjonOmSaksbehandlingstidUtDto() as T
            InnhentingInformasjonFraBrukerDto::class -> createInnhentingInformasjonFraBrukerDto() as T
            InnvilgelseAvAlderspensjonDto::class -> createInnvilgelseAvAlderspensjonDto() as T
            InnvilgelseAvAlderspensjonAutoDto::class -> createInnvilgelseAvAlderspensjonAutoDto() as T
            InnvilgelseAvAlderspensjonTrygdeavtaleDto::class -> createInnvilgelseAvAlderspensjonTrygdeavtaleDto() as T
            LetterExampleDto::class -> createLetterExampleDto() as T
            OmsorgEgenAutoDto::class -> createOmsorgEgenAutoDto() as T
            OmsorgEgenManuellDto::class -> createOmsorgManuellDto() as T
            OpphoerBarnetilleggAutoDto::class -> createOpphoerBarnetilleggAutoDto() as T
            OpptjeningVedForhoeyetHjelpesatsDto::class -> OpptjeningVedForhoeyetHjelpesatsDto(Year(2021), false) as T
            OrienteringOmSaksbehandlingstidDto::class -> createOrienteringOmSaksbehandlingstidDto() as T
            PE::class -> createPE() as T
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
            EndringUfoeretrygdDto::class -> createEndringUfoeretrygdDto() as T
            EndringUfoeretrygdFlyttingUtlandDto::class -> createEndringUfoeretrygdFlyttingUtlandDto() as T

            else -> throw IllegalArgumentException("Don't know how to construct: ${letterDataType.qualifiedName}")
        }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> createVedlegg(letterDataType: KClass<T>): T = when(letterDataType) {
        DineRettigheterOgMulighetTilAaKlageDto::class -> createDineRettigheterOgMulighetTilAaKlageDto() as T
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
        PE::class -> createPE() as T
        else -> throw IllegalArgumentException("Don't know how to construct: ${letterDataType.qualifiedName}")
    }
}