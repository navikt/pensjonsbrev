package no.nav.pensjon.brev

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.EtteroppgjoerEtterbetalingAutoDto
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerUfoeretrygdDto
import no.nav.pensjon.brev.api.model.maler.OmsorgEgenAutoDto
import no.nav.pensjon.brev.api.model.maler.OpphoerBarnetilleggAutoDto
import no.nav.pensjon.brev.api.model.maler.OpptjeningVedForhoeyetHjelpesatsDto
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDto
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDto
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAP2016AutoDto
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAP2016Dto
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDto
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderDto
import no.nav.pensjon.brev.api.model.maler.alderApi.InfoAlderspensjonOvergang67AarAutoDto
import no.nav.pensjon.brev.api.model.maler.legacy.EndretBarnetilleggUfoeretrygdDto
import no.nav.pensjon.brev.api.model.maler.legacy.EndretUforetrygdPGAOpptjeningLegacyDto
import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.AvslagUfoeretrygdDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.*
import no.nav.pensjon.brev.api.model.maler.ufoerApi.VarselSaksbehandlingstidAutoDto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.api.model.vedlegg.EgenerklaeringOmsorgsarbeidDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brev.fixtures.alder.createAvslagUttakFoerNormertPensjonsalderAP2016AutoDto
import no.nav.pensjon.brev.fixtures.alder.createAvslagUttakFoerNormertPensjonsalderAP2016Dto
import no.nav.pensjon.brev.fixtures.alder.createAvslagUttakFoerNormertPensjonsalderDto
import no.nav.pensjon.brev.fixtures.alder.createAvslagUttakFoerNormertPensjonsalderAutoDto
import no.nav.pensjon.brev.fixtures.alder.createInfoAlderspensjonOvergang67AarAutoDto
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
import no.nav.pensjon.brev.fixtures.createUfoerOmregningEnsligDto
import no.nav.pensjon.brev.fixtures.createUngUfoerAutoDto
import no.nav.pensjon.brev.fixtures.redigerbar.createInnhentingInformasjonFraBrukerDto
import no.nav.pensjon.brev.fixtures.redigerbar.createOmsorgManuellDto
import no.nav.pensjon.brev.fixtures.redigerbar.createOrienteringOmSaksbehandlingstidDto
import no.nav.pensjon.brev.fixtures.redigerbar.createVarselRevurderingAvPensjonDto
import no.nav.pensjon.brev.fixtures.redigerbar.createVarselTilbakekrevingAvFeilutbetaltBeloep
import no.nav.pensjon.brev.fixtures.ufoere.createVarselSaksbehandlingstidAutoDto
import no.nav.pensjon.brev.maler.example.EksempelRedigerbartDto
import no.nav.pensjon.brev.maler.example.LetterExampleDto
import no.nav.pensjon.brev.maler.vedlegg.createDineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brevbaker.api.model.Year
import kotlin.reflect.KClass

object Fixtures {

    val felles = no.nav.brev.brevbaker.Fixtures.felles

    val fellesAuto = no.nav.brev.brevbaker.Fixtures.fellesAuto

    inline fun <reified T: Any> create(): T = create(T::class)

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> create(letterDataType: KClass<T>): T =
        when (letterDataType) {
            AvslagUfoeretrygdDto::class -> createAvslagUfoeretrygdDto() as T
            AvslagUttakFoerNormertPensjonsalderAutoDto::class -> createAvslagUttakFoerNormertPensjonsalderAutoDto() as T
            AvslagUttakFoerNormertPensjonsalderDto::class -> createAvslagUttakFoerNormertPensjonsalderDto() as T
            AvslagUttakFoerNormertPensjonsalderAP2016AutoDto::class -> createAvslagUttakFoerNormertPensjonsalderAP2016AutoDto() as T
            AvslagUttakFoerNormertPensjonsalderAP2016Dto::class -> createAvslagUttakFoerNormertPensjonsalderAP2016Dto() as T
            DineRettigheterOgMulighetTilAaKlageDto::class -> createDineRettigheterOgMulighetTilAaKlageDto() as T
            EgenerklaeringOmsorgsarbeidDto::class -> createEgenerklaeringOmsorgsarbeidDto() as T
            EksempelRedigerbartDto::class -> createEksempelbrevRedigerbartDto() as T
            EmptyBrevdata::class -> EmptyBrevdata as T
            EmptyRedigerbarBrevdata::class -> EmptyRedigerbarBrevdata as T
            EndretBarnetilleggUfoeretrygdDto::class -> createEndretBarnetilleggUfoeretrygdDto() as T
            EndretUfoeretrygdPGAInntektDto::class -> createEndretUfoeretrygdPGAInntektDto() as T
            EndretUTPgaInntektDtoV2::class -> createEndretUTPgaInntektDtoV2() as T
            EndretUforetrygdPGAOpptjeningLegacyDto::class -> createEndretUforetrygdPGAOpptjeningLegacyDto() as T
            EtteroppgjoerEtterbetalingAutoDto::class -> createEtteroppgjoerEtterbetalingAuto() as T
            ForespoerselOmDokumentasjonAvBotidINorgeDto::class -> createForespoerselOmDokumentasjonAvBotidINorgeDto() as T
            ForhaandsvarselEtteroppgjoerUfoeretrygdDto::class -> createForhaandsvarselEtteroppgjoerUfoeretrygdDto() as T
            InfoAlderspensjonOvergang67AarAutoDto::class -> createInfoAlderspensjonOvergang67AarAutoDto() as T
            InformasjonOmSaksbehandlingstidDto::class -> createInformasjonOmSaksbehandlingstidDto() as T
            InformasjonOmSaksbehandlingstidUtDto::class -> createInformasjonOmSaksbehandlingstidUtDto() as T
            InnhentingInformasjonFraBrukerDto::class -> createInnhentingInformasjonFraBrukerDto() as T
            LetterExampleDto::class -> createLetterExampleDto() as T
            MaanedligUfoeretrygdFoerSkattDto.UfoeretrygdPerMaaned::class -> createMaanedligUfoeretrygdFoerSkattDtoUfoeretrygdPerMaaned() as T
            MaanedligUfoeretrygdFoerSkattDto::class -> createMaanedligUfoeretrygdFoerSkattDto() as T
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
            OrienteringOmRettigheterUfoereDto::class -> createOrienteringOmRettigheterUfoereDto() as T
            OrienteringOmSaksbehandlingstidDto::class -> createOrienteringOmSaksbehandlingstidDto() as T
            VarselTilbakekrevingAvFeilutbetaltBeloepDto::class -> createVarselTilbakekrevingAvFeilutbetaltBeloep() as T
            MaanedligPensjonFoerSkattDto::class -> createMaanedligPensjonFoerSkatt() as T
            PE::class -> createPE() as T
            UfoerOmregningEnsligDto::class -> createUfoerOmregningEnsligDto() as T
            UngUfoerAutoDto::class -> createUngUfoerAutoDto() as T
            VarselRevurderingAvPensjonDto::class -> createVarselRevurderingAvPensjonDto() as T
            VarselSaksbehandlingstidAutoDto::class -> createVarselSaksbehandlingstidAutoDto() as T

            else -> throw IllegalArgumentException("Don't know how to construct: ${letterDataType.qualifiedName}")
        }
}