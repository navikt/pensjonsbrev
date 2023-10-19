package no.nav.pensjon.brev.api.model.maler

object Brevkode {
    enum class AutoBrev(vararg koder: String) {
        PE_ADHOC_FEILUTSENDING_GJENLEVENDERETT("ADHOC_FEILUTSENDING_GJR"),
        PE_ADHOC_2023_04_GJENLEVENDEINFOETTER1970("ADHOC_2023_04_GJP1"),
        PE_ADHOC_2023_04_GJENLEVENDEINFOFOER1971("ADHOC_2023_04_GJP2"),
        PE_OMSORG_HJELPESTOENAD_AUTO("OMSORG_HJST_AUTO"),
        PE_OMSORG_EGEN_AUTO("OMSORG_EGEN_AUTO", "OMSORGP_EGENMLD"),
        UT_OMREGNING_ENSLIG_AUTO("UT_DOD_ENSLIG_AUTO", "TILST_DOD_UT"),
        UT_OPPHOER_BT_AUTO("PE_UT_07_200", "OPPHOR_ENDRING_UT_BT"),
        UT_UNG_UFOER_20_AAR_AUTO("PE_BA_04_505", "UP_FULLTT_BELOPENDR"),
        UT_EO_FORHAANDSVARSEL_FEILUTBETALING_AUTO("PE_UT_23_001", "UT_EO_VARSEL_FU"),
        UT_2023_INFO_REGLERENDRET_GJT_12_18 ("ADHOC_2023_UT1"),
        UT_2023_INFO_REGLERENDRET_GJT_KONVERTERT2015 ("ADHOC_2023_UT2")
        ;

        val brevkoder: Set<String> = koder.toSet()

        companion object {
            @Suppress("unused")
            fun findByKode(kode: String): AutoBrev? =
                AutoBrev.values().find { it.brevkoder.contains(kode) || it.name == kode }
        }
    }
    enum class Redigerbar(val kode: String) {
        INFORMASJON_OM_SAKSBEHANDLINGSTID("AP_INFO_STID_MAN"),
        ;

        companion object {
            @Suppress("unused")
            fun findByKode(kode: String): Redigerbar? =
                Redigerbar.values().firstOrNull { it.kode == kode || it.name == kode}
        }
    }
}