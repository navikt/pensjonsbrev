package no.nav.pensjon.brev.alder.model.afp

import no.nav.pensjon.brev.alder.model.vedlegg.HvordanPensjonenErBeregnetAfpOffentligDto
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

/**
 * Vedtak — innvilgelse av avtalefestet pensjon (AFP) i offentlig sektor (gammel AFP).
 *
 * Ported from the Exstream brevkode `PE_AF_04_001`. Den nye, privat-sektor varianten
 * (PE_AF_04_111 / 04_115) finnes i [InnvilgelseAvAfpDto] / [InnvilgelseAvAfpAutoDto].
 *
 * Feltkommentarene refererer til den originale Pesys-modellen (PE_…) slik at
 * mapping-teamet kan slå opp riktig XML-node i `pe_xml_mappinger(in).csv`.
 */
data class InnvilgelseAvAfpOffentligSektorDto(
    override val saksbehandlerValg: EmptySaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EmptySaksbehandlerValg, InnvilgelseAvAfpOffentligSektorDto.PesysData> {

    data class PesysData(
        // PE_Vedtaksdata_Kravhode_KravMottatdato
        // (rtv-brev brev Vedtaksdata Kravhode KravMottatdato)
        val kravMottattDato: LocalDate,

        // PE_Vedtaksdata_VirkningFOM
        // (rtv-brev brev Vedtaksdata VirkningFOM)
        val virkningFom: LocalDate,

        // PE_Vedtaksdata_BeregningsData_Beregning_VirkDatoFOM
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning VirkDatoFOM)
        val beregningVirkDatoFom: LocalDate,

        // PE_Vedtaksdata_BeregningsData_Beregning_AFPpensjonsgrad
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning AFPpensjonsgrad)
        val afpPensjonsgrad: Int,

        // PE_Vedtaksdata_BeregningsData_Beregning_Grunnbelop
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning Grunnbelop)
        val grunnbeloep: Kroner,

        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningsSammendragBruker_BrukerFPI
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning BeregningsSammendragBruker BrukerFPI)
        // Framtidig pensjonsgivende inntekt benyttet i beregningen (per år).
        val framtidigArligInntekt: Kroner,

        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo1_SPT_Poengrekke_PoengarUtenOKTPI
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning BeregningNokkelinfo BeregningNokkelinfo1 SPT Poengrekke PoengarUtenOKTPI)
        // Tidligere arbeidsinntekt, beregnet snitt — vises som beløp i brevet.
        val tidligereArbeidsinntekt: Kroner,

        // PE_Vedtaksdata_BeregningsData_BeregningAntallPerioder
        // (rtv-brev brev Vedtaksdata BeregningsData BeregningAntallPerioder)
        // Brevet henviser bare til vedlegg når antall perioder > 1; modellert som
        // scalar Boolean per convert-exstream-letter skill Step 3.
        val flerePerioder: Boolean,

        // PE_VedtaksData_Etterbetaling
        // (rtv-brev brev VedtaksData Etterbetaling)
        val etterbetaling: Boolean,

        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning BeregningSivilstandAnvendt)
        // Pesys-rådatabasen leverer en av ~15 sivilstand-strenger; brevet bruker
        // dem kun som 6 grupperinger for "Du må også melde fra hvis"-listen.
        val sivilstand: SivilstandGruppe,

        // PE_Kontaktinformasjon_NavNAvsenderEnhet
        // (rtv-brev brev Kontaktinformasjon NavNAvsenderEnhet)
        // Avsendernavn brukt i signatur ("Med vennlig hilsen …").
        val avsenderEnhet: String,

        val beregning: Beregning,

        // Data til vedlegget «Hvordan pensjonen er beregnet» (PE_AF_hvordan_pensjonen_beregnes).
        val hvordanPensjonenErBeregnet: HvordanPensjonenErBeregnetAfpOffentligDto,
    ) : FagsystemBrevdata

    /**
     * Sivilstandsgrupperinger brukt i Exstream-brevet. Hver gruppe samler de
     * Pesys-sivilstand-kodene som gir samme «Du må også melde fra hvis»-tekst.
     */
    enum class SivilstandGruppe {
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

    /**
     * Beregningsdetaljer. `null` for bruttobeløpet betyr at komponenten ikke er
     * innvilget — erstatter Exstreams parallelle `…innvilget: Boolean`-flagg
     * (per convert-exstream-letter skill, Step 1: gjør umulige tilstander
     * urepresenterbare).
     */
    data class Beregning(
        // PE_Vedtaksdata_BeregningsData_Beregning_Brutto
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning Brutto)
        val brutto: Kroner,

        // PE_Vedtaksdata_BeregningsData_Beregning_Netto
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning Netto)
        val netto: Kroner,

        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Grunnpensjon_GPbrutto/GPnetto
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning BeregningYtelsesKomp Grunnpensjon GPbrutto/GPnetto)
        val grunnpensjon: YtelsesKomponent,

        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Tilleggspensjon_TPbrutto/TPnetto
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning BeregningYtelsesKomp Tilleggspensjon TPbrutto/TPnetto)
        // Tilstede ⇔ PE_…_Tilleggspensjon_TPinnvilget = true.
        val tilleggspensjon: YtelsesKomponent?,

        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseomp_Sertillegg_STbrutto
        //   (Exstream-typo bevart: «BeregningYtelseomp»)
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Sertillegg_STnetto
        // Tilstede ⇔ PE_…_Sertillegg_STinnvilget = true.
        val saertillegg: YtelsesKomponent?,

        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_MinstenivatilleggIndividuelt_MNTIbrutto/MNTInetto
        // Tilstede ⇔ PE_…_MNTIinnvilget = true.
        val minstenivaatilleggIndividuelt: YtelsesKomponent?,

        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_AFPtillegg_AFPbrutto
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_AFPnetto
        // Tilstede ⇔ PE_…_AFPtillegg_AFPinnvilget = true.
        val afpTillegg: YtelsesKomponent?,

        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETbrutto/ETnetto
        // Tilstede ⇔ PE_…_Ektefelletillegg_ETinnvilget = true.
        val ektefelletillegg: Ektefelletillegg?,

        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_FasteUtgifter_FasteUtgifterBrutto/FasteUtgifterNetto
        // Tilstede ⇔ PE_…_FasteUtgifterInnvilget = true.
        val fasteUtgifterInstitusjon: YtelsesKomponent?,

        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_FamileTillegg_FamilieTilleggBrutto/FamilieTilleggNetto
        //   (Exstream-typo «FamileTillegg» bevart i kilden.)
        // Tilstede ⇔ PE_…_FamilieTilleggInnvilget = true.
        val familietillegg: YtelsesKomponent?,

        // Barnetillegg-flagg påvirker visning av «Ektefelletillegg og inntekt»-
        // setningen i kort-listen midt i brevet.
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget
        val barnetilleggSerkull: Boolean,

        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget
        val barnetilleggFelles: Boolean,
    )

    data class YtelsesKomponent(
        val brutto: Kroner,
        val netto: Kroner,
    )

    data class Ektefelletillegg(
        val brutto: Kroner,
        val netto: Kroner,
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinntektBruktIAvkortning
        val inntektBruktIAvkortning: Kroner,
    )
}
