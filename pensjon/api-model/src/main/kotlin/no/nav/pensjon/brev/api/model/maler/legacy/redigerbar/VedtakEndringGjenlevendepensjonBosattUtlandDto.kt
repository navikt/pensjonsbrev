package no.nav.pensjon.brev.api.model.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.DisplayText
import java.time.LocalDate

/**
 * Vedtak om endring av gjenlevendepensjon for bruker bosatt i utlandet.
 *
 * Konvertert fra Exstream-malen `PE_GP_04_029_vedtak_endring_gp_utland_bosatt_utland`.
 *
 * Hver felt-kommentar peker tilbake til opphavet i PESYS XML
 * (`Name` / `rtv-brev brev …`-kolonnene i
 * `pensjonsbrev-utils/exstreamConverter/src/main/resources/pe_xml_mappinger(in).csv`).
 */
@Suppress("unused")
data class VedtakEndringGjenlevendepensjonBosattUtlandDto(
    override val saksbehandlerValg: Saksbehandlervalg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<
    VedtakEndringGjenlevendepensjonBosattUtlandDto.Saksbehandlervalg,
    VedtakEndringGjenlevendepensjonBosattUtlandDto.PesysData,
> {

    /**
     * Saksbehandler-styrte valg. Hver av `<FRITEKST: VELG ETT AV ALTERNATIVENE …>`-blokkene
     * i Exstream-kilden er konvertert til en typed enum som driver `showIf` i malen, slik at
     * saksbehandler velger ett alternativ i Skribenten i stedet for å slette tekst manuelt.
     */
    data class Saksbehandlervalg(
        // Kilde: <FRITEKST: VELG ETT AV ALTERNATIVENE UNDER …> (Alt 1/2/3)
        @DisplayText("Årsak til endring")
        val aarsakEndring: AarsakEndring,
        // Kilde: <FRITEKST: VELG ET ALTERNATIV …> for forventet inntekt (Alt 1/2/3)
        @DisplayText("Forventet inntekt")
        val forventetInntektNivaa: ForventetInntektNivaa,
        // Kilde: <FRITEKST: FORSLAG TEKST - BEHOV FOR OPPFØLGING – inkluder oppfølgingsavsnittet?
        @DisplayText("Inkluder avsnitt om behov for oppfølging")
        val harBehovForOppfoelging: Boolean,
        // Kilde: <FRITEKST: fjern overskrifter og de alternativer som ikke passer> for skatt (Alt 1/2)
        @DisplayText("Skatt")
        val skattAlternativ: SkattAlternativ,
        // Kilde: <FRITEKST: Etterbetaling/Feilutbetaling - stryk om det ikke passer> (Alt 1/2)
        @DisplayText("Etterbetaling eller feilutbetaling")
        val utbetalingAlternativ: UtbetalingAlternativ,
    ) : SaksbehandlerValgBrevdata

    enum class AarsakEndring {
        OEKNING_AV_INNTEKT,
        REDUKSJON_AV_INNTEKT,
        SAMBOER_12_AV_18_MAANEDER,
    }

    enum class ForventetInntektNivaa {
        UNDER_HALV_G,
        OVER_HALV_G,
        REDUSERT_TIL_NULL,
    }

    enum class SkattAlternativ {
        INFORMASJON_OM_SKATT,
        KILDESKATT,
    }

    enum class UtbetalingAlternativ {
        ETTERBETALING,
        FEILUTBETALING,
    }

    data class PesysData(
        // PE_Vedtaksdata_VirkningFom
        // (rtv-brev brev Vedtaksdata VirkningFom)
        val virkningFom: LocalDate,
        val beregning: Beregning,
        val avdoed: Avdoed,
        val ektefelle: EktefelleData,
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning BeregningSivilstandAnvendt)
        // Kollapset til enum: Exstream sammenlignet med strengene "enslig" og "bormed 3-2".
        val sivilstand: Sivilstand,
        // Vedlegg "Opplysninger om beregningen" (PE_GP_Utland_opplysninger_om_beregningen_HORISONT).
        // Nullable så bestiller kan utelate vedlegget når det ikke er relevant.
        val opplysningerOmBeregningen: OpplysningerOmBeregningenGPUtlandDto? = null,
    ) : FagsystemBrevdata

    data class Beregning(
        // PE_Vedtaksdata_BeregningsData_Beregning_VirkDatoFom
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning VirkDatoFom)
        val virkDatoFom: LocalDate,
        // PE_Vedtaksdata_BeregningsData_Beregning_Grunnbelop
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning Grunnbelop)
        val grunnbeloep: Kroner,
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningsSammendragBruker_BrukerFpi
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning BeregningsSammendragBruker BrukerFpi)
        // Modellert som Kroner (originalt Int) for å få .format() = "X kroner".
        val framtidigAarligInntekt: Kroner,
        // PE_Vedtaksdata_BeregningsData_Beregning_Brutto
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning Brutto)
        val brutto: Kroner,
        // PE_Vedtaksdata_BeregningsData_Beregning_Netto
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning Netto)
        val netto: Kroner,
        val grunnpensjon: Komponent,
        // null når TPinnvilget = false. Når innvilget, har felt brutto/netto.
        val tilleggspensjon: Komponent?,
        // null når STinnvilget = false.
        val saertillegg: Komponent?,
        // null når FasteUtgifterInnvilget = false.
        val fasteUtgifter: Komponent?,
        // null når FamilieTilleggInnvilget = false.
        val familietillegg: Komponent?,
        // PE_Vedtaksdata_BeregningsData_Beregning_Yug != ""
        // Kollapset til Boolean: Exstream brukte kun `Yug <> ""` som flagg
        // for "avdøde mottok pensjon beregnet etter yrkesskade-regler".
        val harYrkesskadegradFraAvdoed: Boolean,
    )

    data class Komponent(
        // *_brutto (gpbrutto / tpbrutto / stbrutto / fasteutgifterbrutto / familietilleggbrutto)
        val brutto: Kroner,
        // *_netto (gpnetto / tpnetto / stnetto / fasteutgifternetto / familietilleggnetto)
        val netto: Kroner,
    )

    data class Avdoed(
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningsSammendragAvdod_AvdodDodsfallSkyldesYrkesskade
        val doedsfallSkyldesYrkesskade: Boolean,
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningsSammendragAvdod_AvdodFlyktning
        val flyktning: Boolean,
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningsSammendragAvdod_AvdodUngUforFodtEtter1940
        val ungUfoerFodtEtter1940: Boolean,
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningsSammendragAvdod_AvdodUngUforFodtFor1941
        val ungUfoerFodtFor1941: Boolean,
    )

    data class EktefelleData(
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningEktefelleMottarPensjon
        val mottarPensjon: Boolean,
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningEktefelleInntektOver2g
        val inntektOver2g: Boolean,
    )

    enum class Sivilstand {
        ENSLIG,
        SAMBOER_3_2,
        ANNET,
    }
}

