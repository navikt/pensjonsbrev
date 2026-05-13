package no.nav.pensjon.brev.alder.model.vedlegg

import no.nav.pensjon.brev.api.model.maler.VedleggData
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

/**
 * Vedlegg «Hvordan pensjonen er beregnet» for AFP i offentlig sektor (gammel AFP).
 *
 * Konvertert fra Exstream-malen `PE_AF_hvordan_pensjonen_beregnes`. Vedlegget
 * forklarer beregningen av grunnpensjon, tilleggspensjon, særtillegg og
 * ektefelletillegg, og brukes i flere AFP-brev (PE_AF_04_001 m.fl.).
 *
 * Felt som peker tilbake til Pesys-modellen er kommentert med original
 * XML-node-sti for sporing til `pe_xml_mappinger(in).csv`.
 */
data class HvordanPensjonenErBeregnetAfpOffentligDto(
    // PE_Vedtaksdata_BeregningsData_Beregning_Grunnbelop
    val grunnbeloep: Kroner,

    // PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_TTanvBest
    val trygdetid: Int,

    // PE_Vedtaksdata_BeregningsData_Beregning_BeregningsSammendragBruker_BrukerFlyktning
    val brukerErFlyktning: Boolean,

    // PE_Vedtaksdata_BeregningsData_Beregning_BeregningEktefelleInntektOver2g
    val ektefelleInntektOver2G: Boolean,

    // PE_Vedtaksdata_BeregningsData_Beregning_BeregningEktefelleMottarPensjon
    val ektefelleMottarPensjon: Boolean,

    // PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Tilleggspensjon_*
    // null ⇔ Tilleggspensjon ikke innvilget.
    val tilleggspensjon: Tilleggspensjon?,

    // PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Sertillegg_STinnvilget
    val saertilleggInnvilget: Boolean,

    // PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_*
    // null ⇔ Ektefelletillegg ikke innvilget.
    val ektefelletillegg: Ektefelletillegg?,
) : VedleggData {

    data class Tilleggspensjon(
        // PE_…_BeregningNokkelinfo_BeregningNokkelinfo1_SPT_sluttpoengtallutenok
        val sluttpoengtallUtenOk: Double,
        // PE_…_SPT_Poengrekke_poengarutenok
        val poengaarUtenOk: Int,
        // PE_…_SPT_Poengrekke_poengarutenoke91 (poengår etter 1991)
        val poengaarUtenOke91: Int,
        // PE_…_SPT_Poengrekke_poengarutenokf92 (poengår før 1992)
        val poengaarUtenOkf92: Int,
    )

    data class Ektefelletillegg(
        // PE_…_Ektefelletillegg_ETfribelop
        val fribeloep: Kroner,
    )
}
