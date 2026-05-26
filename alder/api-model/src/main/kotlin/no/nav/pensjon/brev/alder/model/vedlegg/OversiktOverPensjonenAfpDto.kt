package no.nav.pensjon.brev.alder.model.vedlegg

import no.nav.pensjon.brev.api.model.maler.VedleggData
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

/**
 * Vedlegg «Oversikt over pensjonens størrelse» (PE_AF_oversikt_over_pensjonen_MR71).
 *
 * Inkluderes kun når vedtaket har flere beregningsperioder (PE_Vedtaksdata_BeregningsData_BeregningAntallPerioder > 1).
 * Hver [Periode] viser den månedlige pensjonen i en avgrenset periode (FOM-TOM), og
 * [sistePeriode] viser den endelige/siste beregningen (kun FOM).
 *
 * Endringsårsakene kommer fra PE_Vedtaksdata_BeregningsData_…_PeriodeArsakListe_PeriodeArsakKode-listen.
 * Mapping-teamet aggregerer kodene til de [Endringsaarsaker]-flaggene som brevet faktisk leser.
 */
data class OversiktOverPensjonenAfpDto(
    val perioder: List<Periode>,
    val sistePeriode: SistePeriode,
) : VedleggData {
    data class Periode(
        // PE_Vedtaksdata_BeregningsData_BeregningPeriode_VirkDatoFOM
        val virkDatoFom: LocalDate,
        // PE_Vedtaksdata_BeregningsData_BeregningPeriode_VirkDatoTOM
        val virkDatoTom: LocalDate,
        // PE_Vedtaksdata_BeregningsData_BeregningPeriode_Grunnbelop
        val grunnbeloep: Kroner,
        // PE_Vedtaksdata_BeregningsData_BeregningPeriode_BeregningsSammendragBruker_BrukerFPI
        val framtidigArligInntekt: Kroner,
        val endringsaarsaker: Endringsaarsaker,
        val beregning: PeriodeBeregning,
    )

    data class SistePeriode(
        // PE_Vedtaksdata_BeregningsData_Beregning_VirkDatoFOM
        val virkDatoFom: LocalDate,
        // PE_Vedtaksdata_BeregningsData_Beregning_Grunnbelop
        val grunnbeloep: Kroner,
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningsSammendragBruker_BrukerFPI
        val framtidigArligInntekt: Kroner,
        val endringsaarsaker: Endringsaarsaker,
        val beregning: PeriodeBeregning,
    )

    /**
     * Flagg utledet fra PE_…_PeriodeArsakListe_PeriodeArsakKode. Eksstream evaluerte
     * SYS_TableRow-likhet mot hver kode; vi flater til Boolean-felter.
     */
    data class Endringsaarsaker(
        // endr_vedtakhistorikk
        val endringIVedtak: Boolean,
        // endr_regel
        val endringIRegelEllerSats: Boolean,
        // endr_personinfo
        val endringIFamilieforhold: Boolean,
        // endr_inntekt
        val endringIInntekt: Boolean,
        // endr_inst_justering
        val endringIInstitusjonsopphold: Boolean,
        // endr_inst_fasteutgif
        val endringIFasteUtgifterInstitusjon: Boolean,
        // endr_aldersovergang
        val aldersovergang: Boolean,
        // endr_uttaksgrad
        val endringIUttaksgrad: Boolean,
        // endr_opptjening
        val endringIOpptjeningsgrunnlag: Boolean,
    )

    data class PeriodeBeregning(
        // PE_…_Brutto
        val brutto: Kroner,
        // PE_…_Netto
        val netto: Kroner,
        // PE_…_BeregningYtelseKomp_Grunnpensjon (alltid innvilget)
        val grunnpensjon: YtelsesKomponent,
        // PE_…_BeregningYtelseKomp_Tilleggspensjon (TPinnvilget)
        val tilleggspensjon: YtelsesKomponent?,
        // PE_…_BeregningYtelseKomp_Sertillegg (STinnvilget)
        val saertillegg: YtelsesKomponent?,
        // PE_…_BeregningYtelseKomp_MinstenivatilleggIndividuelt (MNTIinnvilget)
        val minstenivaatilleggIndividuelt: YtelsesKomponent?,
        // PE_…_BeregningYtelseKomp_AFPtillegg (AFPinnvilget)
        val afpTillegg: YtelsesKomponent?,
        // PE_…_BeregningYtelseKomp_Ektefelletillegg (ETinnvilget)
        val ektefelletillegg: YtelsesKomponent?,
        // PE_…_BeregningYtelseKomp_FasteUtgifter (FasteUtgifterInnvilget)
        val fasteUtgifterInstitusjon: YtelsesKomponent?,
        // PE_…_BeregningYtelseKomp_FamilieTillegg (FamilieTilleggInnvilget)
        val familietillegg: YtelsesKomponent?,
    )

    data class YtelsesKomponent(
        val brutto: Kroner,
        val netto: Kroner,
    )
}
