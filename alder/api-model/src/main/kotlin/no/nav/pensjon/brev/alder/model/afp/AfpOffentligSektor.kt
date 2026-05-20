package no.nav.pensjon.brev.alder.model.afp

import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

/**
 * Felles modell-typer for AFP offentlig sektor-brevene (PE_AF_04_001 og PE_AF_04_020).
 *
 * Pakker enum-en og databeholderne inn i ett namespace slik at de ikke ligger
 * direkte på top-level i `model.afp`. Konsumenter aksesserer typene via det
 * fullt kvalifiserte navnet (f.eks. `AfpOffentligSektor.Sivilstand.ENSLIG`,
 * `AfpOffentligSektor.Beregning`).
 */
object AfpOffentligSektor {

    /**
     * Sivilstandsgrupperinger brukt i AFP offentlig sektor-brevene. Hver gruppe
     * samler de Pesys-sivilstand-kodene som gir samme «Du må også melde fra hvis»-tekst.
     */
    enum class Sivilstand {
        // PE_SivilstandAnvendt_enslig
        ENSLIG,

        // PE_SivilstandAnvendt_bormed_ektefelle
        // PE_SivilstandAnvendt_separert_bor_med_ektefelle
        BOR_MED_EKTEFELLE,

        // PE_SivilstandAnvendt_enslig_separert
        // PE_SivilstandAnvendt_gift_men_lever_adskilt
        SEPARERT_FRA_EKTEFELLE,

        // PE_SivilstandAnvendt_registrert_partner_men_lever_adskilt
        // PE_SivilstandAnvendt_enslig_separert_partner
        SEPARERT_FRA_PARTNER,

        // PE_SivilstandAnvendt_bormed_registrert_partner
        // PE_SivilstandAnvendt_separert_bormed_partner
        BOR_MED_PARTNER,

        // PE_SivilstandAnvendt_bormed_1-5
        // PE_SivilstandAnvendt_bormed_3_2
        // PE_SivilstandAnvendt_gift_ektefelle_bormed_3_2
        // PE_SivilstandAnvendt_registrert_partner_bormed_3_2
        // PE_SivilstandAnvendt_separert_partner_bormed_3_2
        // PE_SivilstandAnvendt_separert_bormed_3_2
        BOR_MED_SAMBOER,
    }
    data class Beregning(
        // PE_Vedtaksdata_BeregningsData_Beregning_Brutto
        val brutto: Kroner,

        // PE_Vedtaksdata_BeregningsData_Beregning_Netto
        val netto: Kroner,

        // PE_Vedtaksdata_BeregningsData_Beregning_Grunnpensjon
        val grunnpensjon: YtelsesKomponent,

        // PE_Vedtaksdata_BeregningsData_Beregning_Tilleggspensjon
        val tilleggspensjon: YtelsesKomponent?,

        // PE_Vedtaksdata_BeregningsData_Beregning_Saertillegg
        val saertillegg: YtelsesKomponent?,

        // PE_Vedtaksdata_BeregningsData_Beregning_MinstenivaatilleggIndividuelt
        val minstenivaatilleggIndividuelt: YtelsesKomponent?,

        // PE_Vedtaksdata_BeregningsData_Beregning_AfpTillegg
        val afpTillegg: YtelsesKomponent?,

        // PE_Vedtaksdata_BeregningsData_Beregning_Ektefelletillegg
        val ektefelletillegg: Ektefelletillegg?,

        // PE_Vedtaksdata_BeregningsData_Beregning_FasteUtgifterInstitusjon
        val fasteUtgifterInstitusjon: YtelsesKomponent?,

        // PE_Vedtaksdata_BeregningsData_Beregning_Familietillegg
        val familietillegg: YtelsesKomponent?,

        // PE_Vedtaksdata_BeregningsData_Beregning_BarnetilleggSerkull
        val barnetilleggSerkull: Boolean,

        // PE_Vedtaksdata_BeregningsData_Beregning_BarnetilleggFelles
        val barnetilleggFelles: Boolean,
    )

    data class YtelsesKomponent(
        val brutto: Kroner,
        val netto: Kroner,
    )

    data class Ektefelletillegg(
        val brutto: Kroner,
        val netto: Kroner,
        val inntektBruktIAvkortning: Kroner,
    )
}
