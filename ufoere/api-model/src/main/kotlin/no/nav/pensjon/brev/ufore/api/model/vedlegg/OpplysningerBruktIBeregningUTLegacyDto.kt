package no.nav.pensjon.brev.ufore.api.model.vedlegg

import no.nav.pensjon.brev.api.model.maler.VedleggData
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

/**
 * DTO for vedlegget "Opplysninger brukt i beregningen" (uføretrygd), portert fra det legacy
 * PE-baserte vedlegget `vedleggOpplysningerBruktIBeregningUTLegacy` i pensjon/maler.
 *
 * I motsetning til legacy-varianten brukes ikke PE-objektet (PEgruppe10). All brevkode- og
 * kravårsak-avhengig logikk er trukket ut til eksplisitte, ferdig beregnede felter — primært
 * de boolske flaggene i [Visningsflagg]. Disse skal beregnes i databyggeren i pen (del 2),
 * slik at malen/frasene holdes rene.
 *
 * Språk-/tekstvarianter (bokmål/nynorsk/engelsk) løses i malen, ikke her.
 */
data class OpplysningerBruktIBeregningUTLegacyDto(
    val beregning: Beregning,
    val avkortning: Avkortning,
    val ytelsesgrunnlag: Ytelsesgrunnlag,
    val minsteytelse: Minsteytelse?,
    val inntektFoerUfoere: InntektFoerUfoere,
    val trygdetid: Trygdetid,
    val trygdetidAvdoed: TrygdetidAvdoed?,
    val gjenlevendetillegg: Gjenlevendetillegg?,
    val barnetilleggFelles: Barnetillegg?,
    val barnetilleggSaerkull: Barnetillegg?,
    val harEktefelletillegg: Boolean,
    val belopsendring: Belopsendring?,
    val yrkesskade: Yrkesskade?,
    val krav: Krav,
    val person: Person,
    val visningsflagg: Visningsflagg,
) : VedleggData {

    data class Beregning(
        val grunnbeloep: Kroner,
        val beregningVirkningDatoFom: LocalDate,
        val ufoeregrad: Int,
        val ufoeretidspunkt: LocalDate,
        val beregningsmetode: Beregningsmetode,
        val anvendtTrygdetid: Int,
        val mottarMinsteytelse: Boolean,
        val proRataBroekTeller: Int?,
        val proRataBroekNevner: Int?,
        val yrkesskadegrad: Int?,
        val totalNetto: Kroner?,
        val beloepRedusert: Boolean,
        val brukerKonvertertUP: Boolean,
    )

    data class Avkortning(
        val forventetInntektAar: Kroner,
        val inntektsgrenseAar: Kroner,
        val inntektsgrenseFaktisk: Kroner,
        val inntektstak: Kroner,
        val beloepsgrense: Kroner,
        val kompensasjonsgrad: Double,
        val utbetalingsgrad: Int,
        val oifu: Kroner?,
        val andelYtelseAvOifu: Double?,
        val ugradertBruttoPerAar: Kroner?,
        val fradrag: Kroner?,
        val nettoAkk: Kroner?,
        val nettoRestAar: Kroner?,
    )

    data class Ytelsesgrunnlag(
        val beregningsgrunnlagOrdinaerAarsbeloep: Kroner,
        val yrkesskadeAarsbeloep: Kroner?,
        val inntektVedSkadetidspunkt: Kroner?,
        val antallAarOver1G: Int?,
        val antallAarInntektIAvtaleland: Int?,
        val opptjeningUTListe: List<OpptjeningUT>,
    ) {
        data class OpptjeningUT(
            val aar: Int,
            val omsorgsaar: Boolean,
            val foerstegangstjeneste: Boolean,
        )
    }

    data class Minsteytelse(
        val sats: Double,
        val oppfyltUngUfoer: Boolean,
    )

    data class InntektFoerUfoere(
        val ifuInntekt: Kroner,
        val ifuBegrunnelse: String?,
        val ieuInntekt: Kroner?,
        val ieuBegrunnelse: String?,
    )

    data class Trygdetid(
        val faktiskTTNorge: Int?,
        val framtidigTTNorsk: Int?,
        val faktiskTTEOS: Int?,
        val framtidigTTEOS: Int?,
        val tellerTTEOS: Int?,
        val nevnerTTEOS: Int?,
        val faktiskTTNordisk: Int?,
        val tellerTTNordisk: Int?,
        val nevnerTTNordisk: Int?,
        val faktiskTTBilateral: Int?,
        val framtidigTTAvtaleland: Int?,
        val tellerTTBilateral: Int?,
        val nevnerTTBilateral: Int?,
        val sumFattNorgeFattEOS: Int?,
        val sumFattNorgeFattA10Netto: Int?,
        val sumFattNorgeFattBilateral: Int?,
        val sumFattNorgeFramtidigTTNorgeDiv12: Int?,
        val redusertFramtidigTrygdetid: Boolean,
        val trygdetidFomNorge: LocalDate?,
        val trygdetidFomBilateral: LocalDate?,
    )

    data class TrygdetidAvdoed(
        val faktiskTTNorge: Int?,
        val framtidigTTNorsk: Int?,
        val faktiskTTEOS: Int?,
        val framtidigTTEOS: Int?,
        val tellerTTEOS: Int?,
        val nevnerTTEOS: Int?,
        val faktiskTTNordisk: Int?,
        val tellerTTNordisk: Int?,
        val nevnerTTNordisk: Int?,
        val faktiskTTBilateral: Int?,
        val framtidigTTAvtaleland: Int?,
        val tellerTTBilateral: Int?,
        val nevnerTTBilateral: Int?,
        val fattNorgePlussFattEOSAvdoed: Int?,
        val fattNorgePlussFattA10NettoAvdoed: Int?,
        val fattNorgePlussFattBilateralAvdoed: Int?,
        val sumFattNorgeFramtidigTTNorgeDiv12Avdoed: Int?,
        val framtidigTrygdetidUnder40Aar: Boolean,
        val trygdetidFomNorge: LocalDate?,
        val trygdetidFomBilateral: LocalDate?,
    )

    data class Gjenlevendetillegg(
        val nyttGjenlevendetillegg: Boolean,
        val anvendtTrygdetid: Int?,
        val ufoeretidspunkt: LocalDate?,
        val yrkesskadegrad: Int?,
        val inntektVedSkadetidspunkt: Kroner?,
        val beregningsgrunnlagAvdoedAarsbeloep: Kroner?,
        val yrkesskadeAarsbeloep: Kroner?,
        val minsteytelseBenyttetUngUfoer: Boolean,
    )

    data class Barnetillegg(
        val innvilget: Boolean,
        val antallBarn: Int,
        val netto: Kroner,
        val brutto: Kroner?,
        val bruttoPerAar: Kroner?,
        val nettoPerAar: Kroner?,
        val fribeloep: Kroner?,
        val inntektBruktIAvkortning: Kroner?,
        val inntektBruktIAvkortningMinusFribeloep: Kroner?,
        val avkortingsbeloepPerAar: Kroner?,
        val justeringsbeloepPerAar: Kroner?,
        val justeringsbeloepPerAarUtenMinus: Kroner?,
        val inntektstak: Kroner?,
        val fribeloepErPeriodisert: Boolean,
        val inntektErPeriodisert: Boolean,
        // Kun for fellesbarn:
        val brukersInntektTilAvkortning: Kroner?,
        val beloepFratrukketAnnenForeldersInntekt: Kroner?,
        val inntektAnnenForelder: Kroner?,
    )

    data class Belopsendring(
        val beloepGammelUT: Kroner,
        val beloepNyUT: Kroner,
    )

    data class Yrkesskade(
        val yrkesskadegrad: Int,
        val skadetidspunkt: LocalDate,
        val inntektVedSkadetidspunkt: Kroner,
        val beregningsgrunnlagBeloepAar: Kroner,
    )

    data class Krav(
        val kravaarsaktype: Kravaarsaktype,
        val onsketVirkningsdato: LocalDate?,
        val boddArbeidetUtland: Boolean,
        val boddArbeidetUtlandAvdoed: Boolean,
        val vurderTrygdeavtale: Boolean,
    )

    data class Person(
        val foedselsdato: LocalDate,
        val sivilstand: Sivilstand,
        val borMedSivilstand: BorMedSivilstand?,
        val erFlyktning: Boolean,
        val bostedsland: String?,
        val foedselsdatoTilBarnTilleggErInnvilgetFor: List<LocalDate>,
    )

    /**
     * Ferdig beregnede visningsbetingelser som i legacy lå i LegacyFunksjoner.kt (ut_tbu*-funksjonene)
     * og i brevkode-/kravårsak-sjekker i selve vedlegget/frasene. Beregnes i pen-databyggeren (del 2).
     */
    data class Visningsflagg(
        val visTrygdetid: Boolean,
        val visTrygdetidAvdoed: Boolean,
        val visAvdoed: Boolean,
        val uforetidspunktFoer17: Boolean,
        val virkningstidspunktStoerreEnn01012016: Boolean,
        val sisteOpptjeningsaarLikUfoeretidspunkt: Boolean,
        val foerstegangstjenesteIkkeNull: Boolean,
        val harOpptjeningUTMedOmsorg: Boolean,
        val harOpptjeningUTMedFoerstegangstjenesteOgOmsorg: Boolean,
        val harOpptjeningUTMedFoerstegangstjenesteOgIkkeOmsorg: Boolean,
        val harOpptjeningUTMedOmsorgOgIkkeFoerstegangstjeneste: Boolean,
        val harOpptjeningUTMedOpptjeningBruktAaretFoerOgFoerstegangstjeneste: Boolean,
        val avdoedHarOpptjeningUTMedFoerstegangstjenesteOgOmsorg: Boolean,
        val avdoedHarOpptjeningUTMedFoerstegangstjenesteOgIkkeOmsorg: Boolean,
        val avdoedHarOpptjeningUTMedOmsorgOgIkkeFoerstegangstjeneste: Boolean,
        // Barnetillegg/etteroppgjør-visning (tbu601-613, 069, 605, 056):
        val tbu056v: Boolean,
        val tbu069v: Boolean,
        val tbu501v: Boolean,
        val tbu601v604v: Boolean,
        val tbu605: Boolean,
        val tbu605vEllerTilDin: Boolean,
        val tbu606v608v: Boolean,
        val tbu606v611v: Boolean,
        val tbu608FaarIkke: Boolean,
        val tbu609v611v: Boolean,
        val tbu611FaarIkke: Boolean,
        val tbu613v: Boolean,
        val tbu613v1_3: Boolean,
        val tbu613v4_5: Boolean,
        val etteroppgjoerBtUtbetalt: Boolean,
    )
}
