package no.nav.pensjon.brev.api.model

import java.time.LocalDate

data class Felles(
    val dokumentDato: LocalDate,
    val saksnummer: String,
    val avsenderEnhet: NAVEnhet,
    val bruker: Bruker,
    val mottaker: Adresse,
    val signerendeSaksbehandlere: SignerendeSaksbehandlere? = null,
)

data class ReturAdresse(val adresseLinje1: String, val postNr: String, val postSted: String)

data class SignerendeSaksbehandlere(val saksbehandler: String, val attesterendeSaksbehandler: String)

data class Bruker(
    val foedselsnummer: Foedselsnummer,
    val fornavn: String,
    val mellomnavn: String?,
    val etternavn: String,
)

data class Adresse(
    val linje1: String,
    val linje2: String,
    val linje3: String? = null,
    val linje4: String? = null,
    val linje5: String? = null,
)

data class NAVEnhet(
    val returAdresse: ReturAdresse,
    val nettside: String,
    val navn: String,
    val telefonnummer: Telefonnummer,
)