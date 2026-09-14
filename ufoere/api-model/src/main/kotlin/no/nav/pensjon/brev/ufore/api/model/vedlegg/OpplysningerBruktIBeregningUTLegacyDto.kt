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
    val antallBarnTotalt: Int?,
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
        val prosentsatsOifuForTak: Double?,
        val gradertOppjustertIFU: Kroner?,
        val ugradertBruttoPerAar: Kroner?,
        val fradrag: Kroner?,
        val nettoAkk: Kroner?,
        val nettoRestAar: Kroner?,
        val overskytendeInntekt: Kroner?,
        val reduksjonPerAar: Kroner?,
        val nettoAkkPlussNettoRestAar: Kroner?,
        val nettoAkkPlussNettoRestAarPlussForventetInntekt: Kroner?,
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
            val brukt: Boolean,
            val pensjonsgivendeInntekt: Kroner,
            val justertBeloep: Kroner,
            val avkortetBeloep: Kroner,
            val omsorgsaar: Boolean,
            val foerstegangstjeneste: Boolean,
            val inntektIAvtaleland: Boolean,
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
        val avtaleland: String?,
        val perioderNorge: List<Periode>,
        val perioderEOS: List<PeriodeMedLand>,
        val perioderBilateral: List<PeriodeMedLand>,
    ) {
        data class Periode(
            val fom: LocalDate?,
            val tom: LocalDate?,
        )

        data class PeriodeMedLand(
            val land: String?,
            val fom: LocalDate?,
            val tom: LocalDate?,
        )
    }

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
        val perioderNorge: List<Trygdetid.Periode>,
        val perioderEOS: List<Trygdetid.PeriodeMedLand>,
        val perioderBilateral: List<Trygdetid.PeriodeMedLand>,
    )

    data class Gjenlevendetillegg(
        val nyttGjenlevendetillegg: Boolean,
        val gtInnvilget: Boolean,
        val avdoedFoedselsnummer: String,
        val avdoedErFlyktning: Boolean,
        val anvendtTrygdetid: Int?,
        val ufoeretidspunkt: LocalDate?,
        val yrkesskadegrad: Int?,
        val inntektVedSkadetidspunkt: Kroner?,
        val beregningsgrunnlagAvdoedAarsbeloep: Kroner?,
        val yrkesskadeAarsbeloep: Kroner?,
        val minsteytelseBenyttetUngUfoer: Boolean,
        val opptjeningUTListeAvdoed: List<Ytelsesgrunnlag.OpptjeningUT>,
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
        val sivilstatusVisning: SivilstatusVisning?,
        val foedselsdatoTilBarnTilleggErInnvilgetFor: List<LocalDate>,
    )

    /**
     * Ferdig beregnede visningsbetingelser som i legacy lå i LegacyFunksjoner.kt (ut_tbu*-funksjonene)
     * og i brevkode-/kravårsak-sjekker i selve vedlegget/frasene. Beregnes i pen-databyggeren (del 2).
     */
    data class Visningsflagg(
        val visTrygdetid: Boolean,
        // Trygdetid-seksjon (TBU039V_TBU044V_1 m.fl.):
        val visStandardFastsettelseTrygdetid: Boolean,
        val harFramtidigTrygdetid: Boolean,
        val yrkesskadeVilkaarOppfylt: Boolean,
        val ufoeregradLikYrkesskadegrad: Boolean,
        val erMaanedEtterFoedsel: Boolean,
        val visTrygdetidNorgeTabell: Boolean,
        val visTrygdetidEOS: Boolean,
        val visTrygdetidBilateral: Boolean,
        val visRedusertFramtidigTrygdetidTekst: Boolean,
        // Inntekt-seksjon (TBU037V_*/TBU038V_*):
        val visInntektsseksjon: Boolean,
        val visInntektsgrunnlagAvdoed: Boolean,
        val visInntektsgrunnlagAvdoedUtland: Boolean,
        // Avdoed trygdetid-tabeller (TBU1187/TBU1382/TBU1384):
        val visTrygdetidAvdoedNorgeTabell: Boolean,
        val visTrygdetidAvdoedEOS: Boolean,
        val visTrygdetidAvdoedBilateral: Boolean,
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
        // Slik beregner vi-seksjon (TBUxx2V / TBU011V_TBU016V / TBU034V_036V):
        val visBrukerKonvertertUP: Boolean,
        val visEndringBeregningsgrunnlag: Boolean,
        val visEndringGjelderGjenlevende: Boolean,
        val visOektUfoeregrad: Boolean,
        val visGjenlevendetilleggOekerMedGrad: Boolean,
        val visBeregningStandardFemAar: Boolean,
        val visBeregningLigningIkkeFerdig: Boolean,
        val visBeregningFoerstegangstjeneste: Boolean,
        val visBeregningOmsorgsaar: Boolean,
        val visBeregningEOS: Boolean,
        val visGrunnbeloepJustering: Boolean,
        val visYrkesskadeBeregning: Boolean,
        // Minsteytelse-seksjon (TBU080V_TBU027V):
        val visMinsteytelse: Boolean,
        val visMinsteytelseSatsBlokk1: Boolean,
        val visMinsteytelseSatsBlokk2: Boolean,
        val visMinsteytelseSatsBlokk3: Boolean,
        val visMinsteytelseSatsBlokk4: Boolean,
        val ungUfoerUnder20: Boolean,
        val visUngUfoerOver20EtterJuli2024: Boolean,
        val visUngUfoerOver20FoerJuli2024: Boolean,
        val visMinsteytelseEosSats: Boolean,
        val visUngUfoerUnder20Fremtid: Boolean,
        val visUngUfoerUnder20FremtidEtterJuli2024: Boolean,
        val borMedEktefelleEllerSamboer: Boolean,
        val visHalvMinsteytelse: Boolean,
        val visEosBeregningIkkeSammenlegging: Boolean,
        val visEosBeregningSammenlegging: Boolean,
        val visBilateralBeregningSammenlegging: Boolean,
        val visFolketrygdVurdertTrygdeavtale: Boolean,
        // Avdoed opplysninger-tabell (TBUxx1V):
        val visAvdoedOpplysningerTabell: Boolean,
        // Brukerens opplysningstabell (TBU010V):
        val visBeregningsgrunnlag: Boolean,
        val visInntektFoerUfoere: Boolean,
        val visInntektsgrense: Boolean,
        val visForventetInntekt: Boolean,
        val visReduksjonsprosent: Boolean,
        val inntektsgrenseLavereEnnInntektstak: Boolean,
        val visSivilstatusRad: Boolean,
        val visPartnerAnnetBosted: Boolean,
        val visEktefelleAnnetBosted: Boolean,
        val visUngUfoerRad: Boolean,
        val beregningsmetodeErBilateral: Boolean,
        val visNorskFramtidigTrygdetid: Boolean,
        val visAarOver1G: Boolean,
        val visOifuFortak: Boolean,
        val visBarnetilleggSaerkullIkkeUtbetalt: Boolean,
        val visBarnetilleggFellesIkkeUtbetalt: Boolean,
        // Slik reduserer vi barnetillegget ut fra inntekt (TBU052V_TBU073V):
        val visBarnetilleggRedusertKortTrygdetid: Boolean,
        val visBarnetilleggIkkeUtbetaltPgaInntekt: Boolean,
        val visBarnetilleggRedusertTilDegOgPartner: Boolean,
        val visBarnetilleggRedusertDin: Boolean,
        // Legacy: anvendt trygdetid < 40 og yrkesskaderesultat <> "oppfylt", når både felles- og særkullsbarnetillegg er innvilget og virkningstidspunkt > 01.01.2016.
        val visBarnetilleggBeggeTyperKortTrygdetidEtter2016: Boolean,
        // Legacy: kravårsakstype = "endret_inntekt".
        val visBarnetilleggEndretInntekt: Boolean,
        // Legacy: kravårsakstype <> "endret_inntekt".
        val visBarnetilleggIkkeEndretInntekt: Boolean,
        // Legacy: barnetillegg felles innvilget og beregningssivilstand anvendt er "bormed ektefelle"/"bormed registrert partner"/"bormed 1-5"/"bormed 1_5"/"bormed 3-2".
        // Legacy: PE_UT_TBU605V_eller_til_din og barnetillegg felles innvilget og partner-sivilstand anvendt.
        val visBarnetilleggTbu605EllerTilDinFellesMedPartner: Boolean,
        // Legacy: kravårsakstype = "endret_inntekt" og barnetillegg felles innvilget og partner-sivilstand anvendt.
        val visBarnetilleggEndretInntektFellesMedPartner: Boolean,
        // Legacy: kravårsakstype <> "endret_inntekt" og partner-sivilstand anvendt.
        val visBarnetilleggIkkeEndretInntektMedPartner: Boolean,
        // Legacy: kravårsakstype = "endret_inntekt" og partner-sivilstand anvendt.
        val visBarnetilleggEndretInntektMedPartner: Boolean,
        // Legacy: fribeløp/inntekt er periodisert for felles- eller særkullsbarnetillegg.
        val visBarnetilleggFribeloepEllerInntektPeriodisert: Boolean,
        // Legacy: fribeløp/inntekt er ikke periodisert for verken felles- eller særkullsbarnetillegg.
        val visBarnetilleggIngenPeriodisering: Boolean,
        // Legacy: justeringsbeløp per år = 0 for både felles- og særkullsbarnetillegg.
        val visBarnetilleggIngenJustering: Boolean,
        // Legacy: justeringsbeløp per år <> 0 for felles- eller særkullsbarnetillegg.
        val visBarnetilleggHarJustering: Boolean,
        // Legacy: justeringsbeløp per år > 0 for felles- eller særkullsbarnetillegg.
        val visBarnetilleggJusteringPositiv: Boolean,
        // Legacy: justeringsbeløp per år < 0 for felles- eller særkullsbarnetillegg.
        val visBarnetilleggJusteringNegativ: Boolean,
        // Legacy: FF_CheckIfFirstDayAndMonthOfYear(vilkår virkning fom) = true.
        val visBarnetilleggVirkningFomFoersteJanuar: Boolean,
        // Legacy: FF_CheckIfFirstDayAndMonthOfYear(vilkår virkning fom) = false.
        // Legacy: fellesbarnetillegg netto > 0 og TBU606/TBU608/TBU611-flagg.
        val visBarnetilleggFellesNettoPositiv: Boolean,
        // Legacy: fellesbarnetillegg netto = 0 og TBU606/TBU608/TBU611-flagg.
        val visBarnetilleggFellesNettoNull: Boolean,
        // Legacy: fellesbarnetillegg netto = 0 og justeringsbeløp per år <> 0.
        val visBarnetilleggFellesNettoNullMedJustering: Boolean,
        // Legacy: fellesbarnetillegg netto/justering tilsier at fribeløp og reduksjonsrader skal vises.
        val visBarnetilleggFellesVisReduksjonsrader: Boolean,
        // Legacy: fellesbarnetillegg avkortingsbeløp per år > 0 kombinert med visning av reduksjonsrader.
        val visBarnetilleggFellesVisAvkortingsbeloep: Boolean,
        // Legacy: fellesbarnetillegg justeringsbeløp per år <> 0.
        val visBarnetilleggFellesHarJustering: Boolean,
        // Legacy: fellesbarnetillegg justeringsbeløp per år > 0.
        val visBarnetilleggFellesJusteringPositiv: Boolean,
        // Legacy: fellesbarnetillegg justeringsbeløp per år < 0.
        // Legacy: fellesbarnetillegg netto = 0 og justeringsbeløp per år = 0 og TBU606/TBU608/TBU611-flagg.
        val visBarnetilleggFellesVisInntektstak: Boolean,
        // Legacy: særkullsbarnetillegg netto > 0 og TBU606/TBU609/TBU611-flagg.
        val visBarnetilleggSaerkullNettoPositiv: Boolean,
        // Legacy: særkullsbarnetillegg netto = 0 og TBU606/TBU609/TBU611-flagg.
        val visBarnetilleggSaerkullNettoNull: Boolean,
        // Legacy: særkullsbarnetillegg netto = 0 og justeringsbeløp per år <> 0.
        val visBarnetilleggSaerkullNettoNullMedJustering: Boolean,
        // Legacy: særkullsbarnetillegg netto/justering tilsier at fribeløp og reduksjonsrader skal vises.
        val visBarnetilleggSaerkullVisReduksjonsrader: Boolean,
        // Legacy: særkullsbarnetillegg avkortingsbeløp per år > 0 kombinert med visning av reduksjonsrader.
        val visBarnetilleggSaerkullVisAvkortingsbeloep: Boolean,
        // Legacy: særkullsbarnetillegg justeringsbeløp per år <> 0.
        val visBarnetilleggSaerkullHarJustering: Boolean,
        // Legacy: særkullsbarnetillegg justeringsbeløp per år > 0.
        val visBarnetilleggSaerkullJusteringPositiv: Boolean,
        // Legacy: særkullsbarnetillegg justeringsbeløp per år < 0.
        // Legacy: særkullsbarnetillegg netto = 0 og justeringsbeløp per år = 0 og TBU606/TBU609/TBU611-flagg.
        val visBarnetilleggSaerkullVisInntektstak: Boolean,
        // Slik fastsetter vi inntekten din (TBUxx4v_og_TBU048V_TBU055V):
        val visFastsetterInntektFoerUfoer: Boolean,
        val visMinstenivaaInntektFoerUfoer: Boolean,
        val visSlikFastsetterViInntektEtterUfoer: Boolean,
        // Kompensasjonsgrad/utbetaling (TBU052V_TBU073V_*):
        val visKompensasjonOppjustertInntekt: Boolean,
        val visReduksjonAvUforetrygd: Boolean,
        val visUtbetalingNaarInntektEndres: Boolean,
        val visUtbetalingNaarInntektEndresDetaljer: Boolean,
        val visUtbetalingFoerSkattTabell: Boolean,
        val visUtbetalingFoerSkattTittelKun: Boolean,
        val visMaanedligReduksjon: Boolean,
        val visUforetrygdInntektSum: Boolean,
        val visUtbetalingRedusertIkkeUtbetalt: Boolean,
        val visIkkeUtbetaltOver80Prosent: Boolean,
        val visFaarTilbakeUtenSoknad: Boolean,
        // Gjenlevendetillegg (TBU052V_TBU073V_SlikBeregnerViGjenlevendetilleggetDitt):
        val visGjenlevendetilleggBeregning: Boolean,
        val visGjenlevendetilleggBeregningUP: Boolean,
        // Ektefelletillegg (TBU052V_TBU073V_ForDegSomMottarEktefelletillegg):
        val visEktefelletilleggInnvilgetIkkeUnntak: Boolean,
        val visEktefelletilleggBeregningSeksjon: Boolean,
        val visEktefelletilleggBeregningUP: Boolean,
        val visEktefelletilleggMinstepensjon60: Boolean,
        // Etteroppgjør (TBU052V_TBU073V_EtteroppgjoerAvUforetrygdOgBarnetillegg) – gating i vedlegget:
        val visEtteroppgjoer: Boolean,
    )
}
