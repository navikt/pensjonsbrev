package no.nav.pensjon.brev.api.dto

import java.time.LocalDate

data class Felles(
    val dokumentDato: LocalDate,
    val saksnummer: String,
    val avsenderEnhet: NAVEnhet,
    val mottaker: Mottaker,
    val signerendeSaksbehandlere: SignerendeSaksbehandlere? = null
)

data class ReturAdresse(val adresseLinje1: String, val postNr: String, val postSted: String)

data class SignerendeSaksbehandlere(val saksbehandler: String, val attesterendeSaksbehandler: String)

data class Mottaker(
    val fornavn: String,
    val mellomnavn: String? = null,
    val etternavn: String,
    val foedselsnummer: String,
    val kortnavn: String,
    val adresse: Adresse
)

data class Adresse(
    val adresselinje1: String,
    val adresselinje2: String? = null,
    val adresselinje3: String? = null,
    val postnummer: String?,
    val poststed: String?,
    val land: String
)

data class NAVEnhet(
    val returAdresse: ReturAdresse,
    val nettside: String,
    val navn: String,
    val telefonnummer: String,
)