package no.nav.pensjon.brev.api.model.maler

object Brevkode {
    enum class AutoBrev {
        PE_AP_ADHOC_2023_REGLERENDRET_INNVGJT_20_19A,
        PE_OMSORG_HJELPESTOENAD_AUTO,
        PE_OMSORG_EGEN_AUTO,
        UT_OMREGNING_ENSLIG_AUTO,
        UT_OPPHOER_BT_AUTO,
        UT_UNG_UFOER_20_AAR_AUTO,
        UT_EO_FORHAANDSVARSEL_FEILUTBETALING_AUTO,
        ;
    }
    enum class Redigerbar() {
        INFORMASJON_OM_SAKSBEHANDLINGSTID,
        ;
    }
}