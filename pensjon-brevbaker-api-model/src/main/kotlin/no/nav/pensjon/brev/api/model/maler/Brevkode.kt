package no.nav.pensjon.brev.api.model.maler

object Brevkode {
    enum class Vedtak(vararg koder: String) {
        UNG_UFOER_AUTO("PE_BA_04_505", "UP_FULLTT_BELOPENDR"),
        OMSORG_EGEN_AUTO("OMSORG_EGEN_AUTO", "OMSORGP_EGENMLD"),
        UFOER_OMREGNING_ENSLIG("UT_DOD_ENSLIG_AUTO", "TILST_DOD_UT"),
        OMSORGP_GODSKRIVING("OMSORG_HJST_AUTO"),
        OPPHOER_BARNETILLEGG_AUTO("PE_UT_07_200")
        ;

        val brevkoder: Set<String> = koder.toSet()

        companion object {
            fun findByKode(kode: String): Vedtak? =
                Vedtak.values().find { it.brevkoder.contains(kode) }
        }
    }
}