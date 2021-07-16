package no.nav.pensjon.brev.something

object Fagdelen {
    enum class SakTypeKode {
        GJENLEV, UFOREP, BARNEP, ALDER, AFP, AFP_PRIVAT
    }

    data class ReturAdresse(val navEnhetsNavn: String, val adresseLinje1: String, val postNr: String, val postSted: String, val jadda: Int)

}


