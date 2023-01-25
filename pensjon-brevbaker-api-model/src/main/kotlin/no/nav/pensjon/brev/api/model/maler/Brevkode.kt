package no.nav.pensjon.brev.api.model.maler

object Brevkode {
    enum class Vedtak(vararg koder: String) {
        UNG_UFOER_AUTO("PE_BA_04_505", "UP_FULLTT_BELOPENDR"),
        OMSORG_EGEN_AUTO("OMSORG_EGEN_AUTO", "OMSORGP_EGENMLD"),
        UFOER_OMREGNING_ENSLIG("UT_DOD_ENSLIG_AUTO", "TILST_DOD_UT"),
        OMSORGP_GODSKRIVING("OMSORG_HJST_AUTO"),
        UT_OPPHOER_BT_AUTO("PE_UT_07_200", "OPPHOR_ENDRING_UT_BT"),
        ;

        val brevkoder: Set<String> = koder.toSet()

        companion object {
            @Suppress("unused")
            fun findByKode(kode: String): Vedtak? =
                Vedtak.values().find { it.brevkoder.contains(kode) }
        }
    }
    enum class Redigerbar(val kode: String) {
        INFORMASJON_OM_SAKSBEHANDLINGSTID("AP_INFO_STID_MAN"),
        ;

        companion object {
            @Suppress("unused")
            fun findByKode(kode: String): Redigerbar? =
                Redigerbar.values().firstOrNull { it.kode == kode }
        }
    }
}