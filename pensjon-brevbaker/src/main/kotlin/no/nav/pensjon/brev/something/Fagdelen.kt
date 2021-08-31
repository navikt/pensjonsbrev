package no.nav.pensjon.brev.something

object Fagdelen {
    enum class SakTypeKode {
        GJENLEV, UFOREP, BARNEP, ALDER, AFP, AFP_PRIVAT
    }

    data class ReturAdresse(val navEnhetsNavn: String, val adresseLinje1: String, val postNr: String, val postSted: String, val jadda: Int)

    data class Mottaker(val fornavn: String, val etternavn: String, val gatenavn: String, val husnummer: String, val postnummer: String, val poststed: String)
}