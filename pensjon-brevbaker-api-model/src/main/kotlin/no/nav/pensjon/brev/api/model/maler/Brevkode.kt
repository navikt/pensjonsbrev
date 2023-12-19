package no.nav.pensjon.brev.api.model.maler

object Brevkode {
    enum class AutoBrev {
        PE_OMSORG_HJELPESTOENAD_AUTO,
        PE_OMSORG_EGEN_AUTO,
        UT_OMREGNING_ENSLIG_AUTO,
        UT_OPPHOER_BT_AUTO,
        UT_UNG_UFOER_20_AAR_AUTO,
        UT_EO_FORHAANDSVARSEL_FEILUTBETALING_AUTO,
        UT_ADHOC_2023_INFORMASJON_OM_FEIL,
        ;
    }
    enum class Redigerbar {
        INFORMASJON_OM_SAKSBEHANDLINGSTID,
        ;
    }
}