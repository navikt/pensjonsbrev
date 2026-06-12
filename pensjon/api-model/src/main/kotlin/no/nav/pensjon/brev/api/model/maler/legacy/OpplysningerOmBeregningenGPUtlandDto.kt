package no.nav.pensjon.brev.api.model.maler.legacy

import no.nav.pensjon.brev.api.model.maler.VedleggData
import no.nav.pensjon.brev.api.model.vedlegg.Trygdetid
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year
import java.time.LocalDate

/**
 * Vedlegg "Opplysninger om beregningen" for vedtak om gjenlevendepensjon når bruker er bosatt i utland.
 *
 * Konvertert fra Exstream-malen `PE_GP_Utland_opplysninger_om_beregningen_HORISONT`.
 *
 * Hver felt-kommentar peker tilbake til opphavet i PESYS XML
 * (`Name` / `rtv-brev brev …`-kolonnene i
 * `pensjonsbrev-utils/exstreamConverter/src/main/resources/pe_xml_mappinger(in).csv`).
 */
@Suppress("unused")
data class OpplysningerOmBeregningenGPUtlandDto(
    val pesysData: PesysData,
) : VedleggData {

    /** Kollapser Exstream-strengen `Beregningsmetode2` ("folketrygd"/"eos"/"nordisk" + alt annet = bilateral). */
    enum class Beregningsmetode {
        FOLKETRYGD,
        EOS,
        NORDISK,
        BILATERAL,
    }

    /** Per-år type-merknad i poengrekka. Exstream-strengene "j", "k", "l", "fpp" er tre omsorgspoeng-varianter, framtidig poeng eller "annet". */
    enum class Poengtallstype {
        OMSORGSPOENG_J,
        OMSORGSPOENG_K,
        OMSORGSPOENG_L,
        FRAMTIDIG_POENG,
        ANNET,
    }

    data class PesysData(
        // PE_Vedtaksdata_BeregningsData_Beregning_VirkDatoFom
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning VirkDatoFom)
        val virkDatoFom: LocalDate,
        val bruker: Bruker,
        val avdoed: Avdoed,
        val beregning: Beregning,
    )

    data class Bruker(
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningsSammendragBruker_BrukerFlyktning
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning BeregningsSammendragBruker BrukerFlyktning)
        val flyktning: Boolean,
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningsSammendragBruker_BrukerFpi
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning BeregningsSammendragBruker BrukerFpi)
        val forventetInntekt: Kroner,
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning BeregningUfore BeregningSivilstandAnvendt)
        // True når Exstream-strengen var "bormed 3-2" (samboer §3-2).
        val samboer3_2: Boolean,
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningEktefelleMottarPensjon
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning BeregningEktefelleMottarPensjon)
        val ektefelleMottarPensjon: Boolean,
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningEktefelleInntektOver2g
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning BeregningEktefelleInntektOver2g)
        val ektefelleInntektOver2g: Boolean,
    )

    data class Avdoed(
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_PenPersonFnr
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning BeregningNokkelinfo BeregningNokkelinfo2 PenPersonFnr)
        val fnr: Foedselsnummer,
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningsSammendragAvdod_AvdodFlyktning
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning BeregningsSammendragAvdod AvdodFlyktning)
        val flyktning: Boolean,
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningsSammendragAvdod_AvdodDodsdato
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning BeregningsSammendragAvdod AvdodDodsdato)
        val doedsdato: LocalDate,
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningsSammendragAvdod_AvdodDodsfallSkyldesYrkesskade
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning BeregningsSammendragAvdod AvdodDodsfallSkyldesYrkesskade)
        val doedsfallSkyldesYrkesskade: Boolean,
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningsSammendragAvdod_AvdodSkadetidspunktYrkesskade
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning BeregningsSammendragAvdod AvdodSkadetidspunktYrkesskade)
        // Nullable: Exstream sammenlignet `ykt <> ""`; tomt felt → null.
        val skadetidspunktYrkesskade: LocalDate?,
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningsSammendragAvdod_AvdodUngUforFodtEtter1940
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning BeregningsSammendragAvdod AvdodUngUforFodtEtter1940)
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningsSammendragAvdod_AvdodUngUforFodtFor1941
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning BeregningsSammendragAvdod AvdodUngUforFodtFor1941)
        val ungUfoer: Boolean,
        // PE_Grunnlag_PersongrunnlagslisteAvdod_TrygdeAvtaler_Avtaleland
        // (rtv-brev brev Grunnlag PersongrunnlagslisteAvdod TrygdeAvtaler Avtaleland)
        val avtaleland: String,
        // PE_Grunnlag_PersongrunnlagslisteAvdod_Trygdeavtaledetaljer_TTNordiskAr
        // (rtv-brev brev Grunnlag PersongrunnlagslisteAvdod Trygdeavtaledetaljer TTNordiskAr)
        val ttNordiskAar: Int,
        // PE_Grunnlag_PersongrunnlagslisteAvdod_Trygdeavtaledetaljer_TTNordiskMnd
        // (rtv-brev brev Grunnlag PersongrunnlagslisteAvdod Trygdeavtaledetaljer TTNordiskMnd)
        val ttNordiskMnd: Int,
        val trygdetidsgrunnlagNorge: List<Trygdetid>,
        val trygdetidsgrunnlagEos: List<Trygdetid>,
        val trygdetidsgrunnlagBilateral: List<Trygdetid>,
    )


    data class Beregning(
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Tilleggspensjon_TPinnvilget
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning BeregningYtelsesKomp Tilleggspensjon TPinnvilget)
        val tpInnvilget: Boolean,
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_Beregningsmetode2
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning BeregningNokkelinfo BeregningNokkelinfo2 Beregningsmetode2)
        val beregningsmetode: Beregningsmetode,
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_TTanvBest
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning BeregningNokkelinfo TTanvBest)
        val ttAnvBest: Int,
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_TTanv
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning BeregningNokkelinfo BeregningNokkelinfo2 TTanv)
        val ttAnv: Int,
        // PE_Vedtaksdata_BeregningsData_Beregning_Yug
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning Yug)
        val yug: Int,
        val sluttpoengtall: Sluttpoengtall,
        val poengrekke: Poengrekke,
        val yrke: YrkesskadeBeregning,
        val trygdetid: TrygdetidScalars,
    )

    data class Sluttpoengtall(
        val folketrygdMinusAvdoedFolketrygdMedOverkompensasjon: Double,
        // PE_Beregningsvedlegg_SluttpoengtallUtenOkEOSMinusSluttpoengtallMedOkEOS2
        val eosMinusEosMedOverkompensasjon: Kroner,
        // PE_Beregningsvedlegg_SluttpoengtallUtenOkNordiskMinusSluttpoengtallMedOkNordisk2
        val nordiskMinusNordiskMedOverkompensasjon: Kroner,

        val medOverkompensasjon: MedOverkompensasjon,
        val utenOverkompensasjon: UtenOverkompensasjon,
    ) {
        data class MedOverkompensasjon(
            // PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_OPT_SluttpoengtallMedok
            val folketrygd: Double,
            // PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_OPT_SluttpoengtallMedOkEOS
            val eos: Double,
            // PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_OPT_SluttpoengtallMedOkNordisk
            val nordisk: Double,
        )
        data class UtenOverkompensasjon(
            // PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_SPT_SluttpoengtallUtenOk
            val folketrygd: Double,
            // PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_SPT_SluttpoengtallUtenOkEOS
            val eos: Double,
            // PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_SPT_SluttpoengtallUtenOkNordisk
            val nordisk: Double,
            // PE_AP_SluttpoengtallUtenOkMinusSluttpoengtallMedOkAvdod
        )
    }

    data class YrkesskadeBeregning(
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_YPT_SluttpoengtallYrke
        val sluttpoengtallYrke: Double,
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_YPT_Poengrekke_PoengarYrke
        val poengaarYrke: Int,
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_YPT_Poengrekke_PoengarYrkef92
        val poengaarYrkeF92: Int,
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_YPT_Poengrekke_PoengarYrkee91
        val poengaarYrkeE91: Int,
    )

    data class Poengrekke(
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_SPT_PoengrekkePopulert
        val populert: Boolean,
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_SPT_Poengrekke_PoengarUtenOk
        val poengaarUtenOk: Int,
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_SPT_Poengrekke_PoengarUtenOkf92
        val poengaarUtenOkF92: Int,
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_SPT_Poengrekke_PoengarUtenOke91
        val poengaarUtenOkE91: Int,
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_SPT_Poengrekke_PoengarUtenOkFaktiskeNorge
        val poengaarUtenOkFaktiskeNorge: Int,
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_SPT_Poengrekke_PoengarUtenOkFaktiskeEOS
        val poengaarUtenOkFaktiskeEos: Int,
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_SPT_Poengrekke_PoengarUtenOkfaktiskeNorden
        val poengaarUtenOkFaktiskeNorden: Int,
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_SPT_Poengrekke_PoengarUtenOkTeoretiskEOS
        val poengaarUtenOkTeoretiskEos: Int,
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_SPT_Poengrekke_FramtidigPoengtall
        val framtidigPoengtall: Int,
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_SPT_Poengrekke_FramtidigPoengarNordenBrutto
        val framtidigPoengaarNordenBrutto: Int,
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_SPT_Poengrekke_FramtidigPoengarNordenNetto
        val framtidigPoengaarNordenNetto: Int,
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_SPT_Poengrekke_PoengarTellerEOS
        val poengaarTellerEos: Int,
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_SPT_Poengrekke_PoengarNevnerEOS
        val poengaarNevnerEos: Int,
        // PE_Beregningsvedlegg_PoengarUtenOkFaktiskNorgePlusPoengarUtenOkFaktiskeEOS2
        val poengaarUtenOkFaktiskNorgePlusEos2: Int,
        // PE_Beregningsvedlegg_PoengarUtenOkFaktiskNorgePlusFramtidigPoengarNordenNetto2
        val poengaarUtenOkFaktiskNorgePlusFramtidigPoengaarNordenNetto2: Int,
        // PE_Beregningsvedlegg_PoengarUtenOkFaktiskNorgePlusPoengarUtenOkFaktiskNorden2
        val poengaarUtenOkFaktiskNorgePlusFaktiskeNorden2: Int,
        val aar: List<PoengAar>,
    )

    data class PoengAar(
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_SPT_Poengrekke_Poengtall_Ar_Arstall
        val aarstall: Year,
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_SPT_Poengrekke_Poengtall_Ar_PensjonsgivendeInntekt
        val pensjonsgivendeInntekt: Kroner,
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_SPT_Poengrekke_Poengtall_Ar_GrunnbelopVeiet
        val grunnbeloepVeiet: Kroner,
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_SPT_Poengrekke_Poengtall_Ar_Pensjonspoeng
        val pensjonspoeng: Double,
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_SPT_Poengrekke_Poengtall_Ar_Poengtallstype
        // ("j" / "k" / "l" / "fpp" / annet) → enum.
        val poengtallstype: Poengtallstype,
    )

    data class TrygdetidScalars(
        // PE_Grunnlag_PersongrunnlagslisteAvdod_Trygdetid_FaTTNorge
        val faTTNorge: Int,
        // PE_Grunnlag_PersongrunnlagslisteAvdod_Trygdetid_FaTTEOS
        val faTTEos: Int,
        // PE_Grunnlag_PersongrunnlagslisteAvdod_Trygdetid_TTNordisk
        val ttNordisk: Int,
        // PE_Grunnlag_PersongrunnlagslisteAvdod_Trygdetid_FramtidigTTEOS
        val framtidigTTEos: Int,
        // PE_Grunnlag_PersongrunnlagslisteAvdod_Trygdetid_FramtidigTTNorsk
        val framtidigTTNorsk: Int,
        // PE_Grunnlag_PersongrunnlagslisteAvdod_Trygdetid_TTTellerEOS
        val ttTellerEos: Int,
        // PE_Grunnlag_PersongrunnlagslisteAvdod_Trygdetid_TTNevnerEOS
        val ttNevnerEos: Int,
        // PE_Grunnlag_PersongrunnlagslisteAvdod_Trygdetid_TTTellerNordisk
        val ttTellerNordisk: Int,
        // PE_Grunnlag_PersongrunnlagslisteAvdod_Trygdetid_TTNevnerNordisk
        val ttNevnerNordisk: Int,
        // PE_Grunnlag_PersongrunnlagslisteAvdod_TtUtlandTrygdeAvtale_FaTTBilateral
        val faTTBilateral: Int,
        // PE_Grunnlag_PersongrunnlagslisteAvdod_TtUtlandTrygdeAvtale_FramtidigTTAvtaleland
        val framtidigTTAvtaleland: Int,
        // PE_Grunnlag_PersongrunnlagslisteAvdod_TtUtlandTrygdeAvtale_TTTellerBilateral
        val ttTellerBilateral: Int,
        // PE_Grunnlag_PersongrunnlagslisteAvdod_TtUtlandTrygdeAvtale_TTNevnerBilateral
        val ttNevnerBilateral: Int,
        // PE_Beregningsvedlegg_FaTTNorgePlusFaTTEOSAvdodFyret
        val faTTNorgePlusFaTTEos: Int,
        // PE_Beregningsvedlegg_FaTTNorgePlusFaTTBilateralAvdodFyret
        val faTTNorgePlusFaTTBilateral: Int,
        // PE_Beregningsvedlegg_FaTTNorgePlusFaTTA10NettoAvdodFyret
        val faTTNorgePlusFaTTA10Netto: Int,
    )
}

