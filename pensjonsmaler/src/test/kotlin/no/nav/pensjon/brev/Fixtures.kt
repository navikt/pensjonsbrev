package no.nav.pensjon.brev

import no.nav.pensjon.brev.api.model.maler.*
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027Dto
import no.nav.pensjon.brev.api.model.maler.alderApi.*
import no.nav.pensjon.brev.api.model.maler.legacy.EndretBarnetilleggUfoeretrygdDto
import no.nav.pensjon.brev.api.model.maler.legacy.EndretUforetrygdPGAOpptjeningLegacyDto
import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.AvslagUfoeretrygdDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvUttaksgradDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.*
import no.nav.pensjon.brev.api.model.maler.ufoerApi.VarselSaksbehandlingstidAutoDto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2
import no.nav.pensjon.brev.api.model.vedlegg.*
import no.nav.pensjon.brev.fixtures.*
import no.nav.pensjon.brev.fixtures.adhoc.gjenlevenderett2027.createGjenlevenderett2027Dto
import no.nav.pensjon.brev.fixtures.alder.*
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
import no.nav.pensjon.brev.maler.vedlegg.createDineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.maler.vedlegg.createMaanedligPensjonFoerSkattAlderspensjonDto
import no.nav.pensjon.brev.maler.vedlegg.createOrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brevbaker.api.model.Year
import kotlin.reflect.KClass

object Fixtures {

    val felles = no.nav.brev.brevbaker.Fixtures.felles

    val fellesAuto = no.nav.brev.brevbaker.Fixtures.fellesAuto

    inline fun <reified T : Any> create(): T = create(T::class)

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> create(letterDataType: KClass<T>): T =
        when (letterDataType) {
            AvslagForLiteTrygdetidAPDto::class -> createAvslagForLiteTrygdetidAPDto() as T
            AvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto::class -> createAvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto() as T
            AvslagGradsendringFoerNormertPensjonsalderFoerEttAarDto::class -> createAvslagGradsendringFoerNormertPensjonsalderFoerEttAarDto() as T
            AvslagPaaGjenlevenderettIAlderspensjonDto::class -> createAvslagPaaGjenlevenderettIAlderspensjon() as T
            AvslagUfoeretrygdDto::class -> createAvslagUfoeretrygdDto() as T
            AvslagUttakFoerNormertPensjonsalderAutoDto::class -> createAvslagUttakFoerNormertPensjonsalderAutoDto() as T
            AvslagUttakFoerNormertPensjonsalderDto::class -> createAvslagUttakFoerNormertPensjonsalderDto() as T
            AvslagUttakFoerNormertPensjonsalderAP2016AutoDto::class -> createAvslagUttakFoerNormertPensjonsalderAP2016AutoDto() as T
            AvslagUttakFoerNormertPensjonsalderAP2016Dto::class -> createAvslagUttakFoerNormertPensjonsalderAP2016Dto() as T
            BrukerTestBrevDto::class -> createBrukerTestBrevDto() as T
            DineRettigheterOgMulighetTilAaKlageDto::class -> createDineRettigheterOgMulighetTilAaKlageDto() as T
            EgenerklaeringOmsorgsarbeidDto::class -> createEgenerklaeringOmsorgsarbeidDto() as T
            EksempelRedigerbartDto::class -> createEksempelbrevRedigerbartDto() as T
            EmptyBrevdata::class -> EmptyBrevdata as T
            EmptyRedigerbarBrevdata::class -> EmptyRedigerbarBrevdata as T
            EndretBarnetilleggUfoeretrygdDto::class -> createEndretBarnetilleggUfoeretrygdDto() as T
            EndretUTPgaInntektDtoV2::class -> createEndretUTPgaInntektDtoV2() as T
            EndretUfoeretrygdPGAInntektDto::class -> createEndretUfoeretrygdPGAInntektDto() as T
            EndretUforetrygdPGAOpptjeningLegacyDto::class -> createEndretUforetrygdPGAOpptjeningLegacyDto() as T
            EndringPgaOpptjeningAutoDto::class -> createEndringPgaOpptjeningAutoDto() as T
            EndringAvAlderspensjonSivilstandDto::class -> createEndringAvAlderspensjonSivilstandDto() as T
            EndringAvAlderspensjonSivilstandSaerskiltSatsDto::class -> createEndringAvAlderspensjonSivilstandSaerskiltSatsDto() as T
            EndringAvAlderspensjonSivilstandAutoDto::class -> createEndringAvAlderspensjonSivilstandAutoDto() as T
            EndringAvAlderspensjonGarantitilleggDto::class -> createEndringAvAlderspensjonGarantitilleggDto() as T
            EndringAvUttaksgradAutoDto::class -> createEndringAvUttaksgradAutoDto() as T
            EtteroppgjoerEtterbetalingAutoDto::class -> createEtteroppgjoerEtterbetalingAuto() as T
            FeilBelopInntekstendringsbrev::class -> EmptyBrevdata as T
            ForespoerselOmDokumentasjonAvBotidINorgeDto::class -> createForespoerselOmDokumentasjonAvBotidINorgeDto() as T
            ForhaandsvarselEtteroppgjoerUfoeretrygdDto::class -> createForhaandsvarselEtteroppgjoerUfoeretrygdDto() as T
            Gjenlevenderett2027Dto::class -> createGjenlevenderett2027Dto() as T
            InfoAlderspensjonOvergang67AarAutoDto::class -> createInfoAlderspensjonOvergang67AarAutoDto() as T
            InformasjonOmGjenlevenderettigheterDto::class -> createInformasjonOmGjenlevenderettigheterDto() as T
            InformasjonOmSaksbehandlingstidDto::class -> createInformasjonOmSaksbehandlingstidDto() as T
            InformasjonOmSaksbehandlingstidUtDto::class -> createInformasjonOmSaksbehandlingstidUtDto() as T
            InnhentingInformasjonFraBrukerDto::class -> createInnhentingInformasjonFraBrukerDto() as T
            InnvilgelseAvAlderspensjonDto::class -> createInnvilgelseAvAlderspensjonDto() as T
            InnvilgelseAvAlderspensjonAutoDto::class -> createInnvilgelseAvAlderspensjonAutoDto() as T
            InnvilgelseAvAlderspensjonTrygdeavtaleDto::class -> createInnvilgelseAvAlderspensjonTrygdeavtaleDto() as T
            LetterExampleDto::class -> createLetterExampleDto() as T
            MaanedligPensjonFoerSkattAP2025Dto::class -> createMaanedligPensjonFoerSkattAP2025() as T
            MaanedligPensjonFoerSkattAlderspensjonDto::class -> createMaanedligPensjonFoerSkattAlderspensjonDto() as T
            MaanedligPensjonFoerSkattDto::class -> createMaanedligPensjonFoerSkatt() as T
            MaanedligUfoeretrygdFoerSkattDto.UfoeretrygdPerMaaned::class -> createMaanedligUfoeretrygdFoerSkattDtoUfoeretrygdPerMaaned() as T
            MaanedligUfoeretrygdFoerSkattDto::class -> createMaanedligUfoeretrygdFoerSkattDto() as T
            OmregningAlderUfore2016Dto::class -> createOmregningAlderUfore2016Dto() as T
            OmregningAlderUfore2016RedigerbarDto::class -> createOmregningAlderUfore2016RedigerbarDto() as T
            OmsorgEgenAutoDto::class -> createOmsorgEgenAutoDto() as T
            OmsorgEgenManuellDto::class -> createOmsorgManuellDto() as T
            OpphoerBarnetilleggAutoDto::class -> createOpphoerBarnetilleggAutoDto() as T
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
            OpptjeningVedForhoeyetHjelpesatsDto::class -> OpptjeningVedForhoeyetHjelpesatsDto(Year(2021), false) as T
            OrienteringOmRettigheterOgPlikterDto::class -> createOrienteringOmRettigheterOgPlikterDto() as T
            OrienteringOmRettigheterUfoereDto::class -> createOrienteringOmRettigheterUfoereDto() as T
            OrienteringOmSaksbehandlingstidDto::class -> createOrienteringOmSaksbehandlingstidDto() as T
            PE::class -> createPE() as T
            SamletMeldingOmPensjonsvedtakDto::class -> createSamletMeldingOmPensjonsvedtakDto() as T
            TilbakekrevingAvFeilutbetaltBeloepDto::class -> createTilbakekrevingAvFeilutbetaltBeloepDto() as T
            UfoerOmregningEnsligDto::class -> createUfoerOmregningEnsligDto() as T
            UngUfoerAutoDto::class -> createUngUfoerAutoDto() as T
            VarselRevurderingAvPensjonDto::class -> createVarselRevurderingAvPensjonDto() as T
            VarselSaksbehandlingstidAutoDto::class -> createVarselSaksbehandlingstidAutoDto() as T
            VarselTilbakekrevingAvFeilutbetaltBeloepDto::class -> createVarselTilbakekrevingAvFeilutbetaltBeloep() as T
            VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto::class -> createVedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto() as T
            VedtakEndringAvAlderspensjonGjenlevenderettigheterDto::class -> createVedtakEndringAvAlderspensjonGjenlevenderettigheterDto() as T
            VedtakEndringAvAlderspensjonInstitusjonsoppholdDto::class -> createVedtakEndringAvAlderspensjonInstitusjonsoppholdDto() as T
            VedtakEndringAvUttaksgradDto::class -> createVedtakEndringAvUttaksgradDto() as T
            VedtakEndringAvUttaksgradStansBrukerEllerVergeDto::class -> createVedtakEndringAvUttaksgradStansBrukerEllerVergeDto() as T
            VedtakEndringAvUttaksgradStansIkkeBrukerEllerVergeDto::class -> createVedtakEndringAvUttaksgradStansIkkeBrukerEllerVergeDto() as T
            VedtakEndringVedFlyttingMellomLandDto::class -> createVedtakEndringVedFlyttingMellomLandDto() as T
            VedtakOmFjerningAvOmsorgsopptjeningDto::class -> createVedtakOmFjerningAvOmsorgsopptjeningDto() as T
            VedtakStansAlderspensjonFlyttingMellomLandDto::class -> createVedtakStansAlderspensjonFlyttingMellomLandDto() as T


            else -> throw IllegalArgumentException("Don't know how to construct: ${letterDataType.qualifiedName}")
        }
}