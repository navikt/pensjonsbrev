package no.nav.pensjon.brev.alder.model.vedlegg

import no.nav.pensjon.brev.api.model.maler.VedleggData
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Percent
import java.time.LocalDate

/**
 * Vedlegg «Opplysninger om beregningen» for AFP-brev.
 *
 * Konvertert fra Exstream-malen `PE_AF_opplysninger_om_beregningen_MR71`. Brukes
 * av PE_AF_04_001 (innvilgelse AFP offentlig sektor) og er ment gjenbrukt av
 * andre AFP-brev. Inneholder en oppsummering av opplysningene som er lagt til
 * grunn ved beregningen, samt poengrekken.
 *
 * Felt-kommentarer refererer til original Pesys-modell (PE_…) slik at
 * mapping-teamet kan slå opp riktig XML-node.
 */
data class OpplysningerOmBeregningenAfpDto(
    // PE_Vedtaksdata_BeregningsData_Beregning_VirkDatoFOM
    val beregningVirkDatoFom: LocalDate,

    // PE_Vedtaksdata_BeregningsData_Beregning_AFPpensjonsgrad
    val afpPensjonsgrad: Percent,

    // PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo1_SPT_Poengrekke_PoengarUtenOKTPI
    val tidligereArbeidsinntekt: Kroner,

    // PE_Vedtaksdata_BeregningsData_Beregning_BeregningsSammendragBruker_BrukerFPI
    val framtidigArligInntekt: Kroner,

    // PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinntektBruktiAvkortning
    // null ⇔ Ektefelletillegg ikke innvilget.
    val ektefelletilleggInntektBruktIAvkortning: Kroner?,

    // PE_Vedtaksdata_Kravhode_KravAFPordning ∈ {lonho, navo, finans, afpstat}
    val afpOrdning: AfpOrdning,

    // PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt — kollapset til
    // de visningskategoriene som faktisk brukes i vedlegget.
    val sivilstand: SivilstandKategori,

    // PE_Vedtaksdata_BeregningsData_Beregning_BeregningEktefelleMottarPensjon
    // Vises bare når sivilstand impliserer at det finnes en ektefelle/partner/samboer.
    val ektefelleEllerPartnerMottarPensjon: Boolean,

    // PE_Vedtaksdata_BeregningsData_Beregning_BeregningEktefelleInntektOver2g
    val ektefelleEllerPartnerInntektOver2G: Boolean,

    // PE_Vedtaksdata_BeregningsData_Beregning_BeregningsSammendragBruker_BrukerFlyktning
    val brukerErFlyktning: Boolean,

    // PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_TTanvBest
    val trygdetid: Int,

    // PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Tilleggspensjon_*
    // null ⇔ Tilleggspensjon ikke innvilget (PE_…_TPinnvilget = false).
    val tilleggspensjon: Tilleggspensjon?,

    // PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo1_SPT_Poengrekke_Poengtall_Ar
    val poengrekke: List<Pensjonspoeng>,

    // True hvis minst én poengrad har omsorgspoeng (PoengTallsType ∈ {G, H, J, K, L}).
    // Styrer den ekstra forklarings­setningen «For år der egen opptjening er høyere…».
    val harOmsorgspoeng: Boolean,
) : VedleggData {

    enum class AfpOrdning { LO_NHO, SPEKTER, FINANS, STATLIG }

    /**
     * Forenkler Pesys' 15+ sivilstand-koder til de visningskategoriene som
     * faktisk brukes i vedlegget.
     *
     * - GIFT_SEPARERT viser «Du eller ektefellen er registrert med annet bosted...».
     * - PARTNER_SEPARERT viser «Du eller partneren er registrert med annet bosted...».
     */
    enum class SivilstandKategori {
        ENSLIG,
        GIFT,
        GIFT_SEPARERT,
        PARTNER,
        PARTNER_SEPARERT,
        SAMBOER_FOLKETRYGD_3_2_5LEDD,
        SAMBOER_FOLKETRYGD_1_5,
    }

    data class Tilleggspensjon(
        // PE_…_BeregningNokkelinfo_BeregningNokkelinfo1_SPT_sluttpoengtallutenok
        val sluttpoengtallUtenOk: Double,
        // PE_…_SPT_Poengrekke_poengarutenok
        val poengaarUtenOk: Int,
        // PE_…_SPT_Poengrekke_poengarutenoke91 — år med pensjonsprosent 42
        val poengaarUtenOke91: Int,
        // PE_…_SPT_Poengrekke_poengarutenokf92 — år med pensjonsprosent 45
        val poengaarUtenOkf92: Int,
    )
}
