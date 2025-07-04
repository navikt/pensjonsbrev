package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.maler.Brevkode.Automatisk
import no.nav.pensjon.brev.api.model.maler.Brevkode.Redigerbart

object Pesysbrevkoder {
    enum class AutoBrev : Automatisk {
        PE_ADHOC_2024_FEIL_INFOBREV_AP_SENDT_BRUKER,
        PE_ADHOC_2024_FEIL_ETTEROPPGJOER_2023,
        PE_ADHOC_2024_VEDTAK_GJENLEVENDETTER1970,
        PE_AFP_2024_INFO_TOLERANSEBELOP,
        PE_AP_2024_IKKEUTBET_FT_VARSEL_OPPH,
        PE_AP_2024_UTBET_FT_VARSEL_OPPH,
        PE_AP_ADHOC_2024_REGLERENDRET_GJR_AP_MNTINDV,
        PE_AP_ADHOC_2024_GJR_AP_MNTINDV_2,
        PE_UT_ADHOC_2024_INFO_HVILENDE_RETT_4_AAR,
        PE_UT_ADHOC_2024_MIDL_OPPHOER_HVILENDE_RETT_10_AAR,
        PE_AP_ADHOC_2025_VARSELBREV_GJT_KAP_20,
        PE_AP_ADHOC_2025_OPPRYDDING_GJT_KAP_20,
        PE_AP_INFO_ALDERSOVERGANG_67_AAR_AUTO,
        PE_AP_AVSLAG_GRAD_FOER_NORM_PEN_ALDER_AP2016_AUTO,
        PE_AP_AVSLAG_GRAD_FOER_NORM_PEN_ALDER_AUTO,
        PE_AP_AVSLAG_GRAD_FOER_NORM_PEN_ALDER_ETT_AAR_AUTO,
        PE_AP_AVSLAG_UTTAK_FOER_NORM_PEN_ALDER_AP2016_AUTO,
        PE_AP_AVSLAG_UTTAK_FOER_NORM_PEN_ALDER_AUTO,
        PE_OMSORG_EGEN_AUTO,
        PE_OMSORG_HJELPESTOENAD_AUTO,
        UT_ADHOC_UFOERETRYGD_ETTERBETALING_DAGPENGER,
        UT_ADHOC_UFOERETRYGD_KOMBI_DAGPENGER,
        UT_ADHOC_UFOERETRYGD_KOMBI_DAGPENGER_AVKORTNING,
        UT_ADHOC_VARSEL_OPPHOER_EKTEFELLETILLEGG,
        UT_ADHOC_VARSEL_OPPHOER_MED_HVILENDE_RETT,
        UT_ENDRET_PGA_INNTEKT,
        UT_ENDRET_PGA_INNTEKT_V2,
        UT_ENDRET_PGA_INNTEKT_NESTE_AR,
        UT_ENDRET_PGA_OPPTJENING,
        UT_EO_FORHAANDSVARSEL_FEILUTBETALING_AUTO,
        UT_ETTEROPPGJOER_ETTERBETALING_AUTO,
        UT_OMREGNING_ENSLIG_AUTO,
        UT_OPPHOER_BT_AUTO,
        UT_UNG_UFOER_20_AAR_AUTO,
        UT_VARSEL_SAKSBEHANDLINGSTID_AUTO,
        UT_BARNETILLEGG_ENDRET_AUTO,
        GJP_VARSEL_FORLENGELSE_60_61,
        GJP_VARSEL_FORLENGELSE_62_70,
        GJP_VARSEL_OPPHOR_60_70,
        GJP_VEDTAK_FORLENGELSE_60_61,
        GJP_VEDTAK_FORLENGELSE_62_70,
        GJP_VEDTAK_OPPHOR_60_70,
        GJP_VARSEL_FORLENGELSE_60_61_UTLAND,
        GJP_VARSEL_FORLENGELSE_62_70_UTLAND,
        GJP_VARSEL_OPPHOR_60_70_UTLAND,
        GJP_VEDTAK_FORLENGELSE_60_61_UTLAND,
        GJP_VEDTAK_FORLENGELSE_62_70_UTLAND,
        GJP_VEDTAK_OPPHOR_60_70_UTLAND;


        override fun kode(): String = this.name
    }

    enum class Redigerbar : Redigerbart {
        INFORMASJON_OM_SAKSBEHANDLINGSTID,
        PE_AP_AVSLAG_GRAD_FOER_NORM_PEN_ALDER_AP2016,
        PE_AP_AVSLAG_GRAD_FOER_NORM_PEN_ALDER,
        PE_AP_AVSLAG_GRAD_FOER_NORM_PEN_ALDER_ETT_AAR,
        PE_AP_AVSLAG_UTTAK_FOER_NORM_PEN_ALDER_AP2016,
        PE_AP_AVSLAG_UTTAK_FOER_NORM_PEN_ALDER,
        PE_AP_ENDRET_UTTAKSGRAD,
        PE_AP_ENDRET_UTTAKSGRAD_STANS_IKKE_BRUKER_VERGE,
        PE_AP_ENDRET_UTTAKSGRAD_STANS_BRUKER_ELLER_VERGE,
        PE_AP_ENDRING_GJENLEVENDERETT,
        PE_AP_INNHENTING_DOKUMENTASJON_FRA_BRUKER,
        PE_AP_INNHENTING_INFORMASJON_FRA_BRUKER,
        PE_AP_INNHENTING_OPPLYSNINGER_FRA_BRUKER,
        PE_AP_ENDRING_AV_ALDERSPENSJON_SIVILSTAND,
        PE_BEKREFTELSE_PAA_FLYKTNINGSTATUS,
        PE_FORESPOERSELOMDOKUMENTASJONAVBOTIDINORGE_ALDER,
        PE_FORESPOERSEL_DOKUM_BOTIDINORGE_ETTERLATTE,
        PE_FORHAANDSVARSEL_VED_TILBAKEKREVING,
        PE_INFORMASJON_OM_GJENLEVENDERETTIGHETER,
        PE_OMSORG_EGEN_MANUELL,
        PE_ORIENTERING_OM_FORLENGET_SAKSBEHANDLINGSTID,
        PE_OVERSETTELSE_AV_DOKUMENTER,
        PE_TILBAKEKREVING_AV_FEILUTBETALT_BELOEP,
        PE_VARSEL_OM_MULIG_AVSLAG,
        PE_VARSEL_OM_TILBAKEKREVING_FEILUTBETALT_BELOEP,
        PE_VARSEL_REVURDERING_AV_PENSJON,
        PE_VEDTAK_OM_FJERNING_AV_OMSORGSPOENG,
        UT_AVSLAG_UFOERETRYGD,
        UT_INFORMASJON_OM_SAKSBEHANDLINGSTID,
        UT_ORIENTERING_OM_SAKSBEHANDLINGSTID;

        override fun kode(): String = this.name
    }
}

