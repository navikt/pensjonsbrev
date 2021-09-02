package no.nav.pensjon.brev.something

object Fagdelen {
    enum class SakTypeKode {
        GJENLEV, UFOREP, BARNEP, ALDER, AFP, AFP_PRIVAT
    }

    data class ReturAdresse(val navEnhetsNavn: String, val adresseLinje1: String, val postNr: String, val postSted: String)

    data class Mottaker(val fornavn: String, val etternavn: String, val gatenavn: String, val husnummer: String, val postnummer: String, val poststed: String)

    data class Felles(val dokumentDato: String, val saksId: String, val saksnummer: String, val navnAvsenderEnhet: String, val navTelefonnummer: String, val mottaker: Mottaker, val spraakKode: String)
}