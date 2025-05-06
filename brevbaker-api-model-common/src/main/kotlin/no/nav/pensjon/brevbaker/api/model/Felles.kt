package no.nav.pensjon.brevbaker.api.model

import java.time.LocalDate

class Felles(
    val dokumentDato: LocalDate,
    val saksnummer: String,
    val avsenderEnhet: NavEnhet,
    val bruker: Bruker,
    val vergeNavn: String?,
    val signerendeSaksbehandlere: SignerendeSaksbehandlere? = null
) {
    fun medSignerendeSaksbehandlere(signerendeSaksbehandlere: SignerendeSaksbehandlere?): Felles =
        Felles(
            dokumentDato = this.dokumentDato,
            saksnummer = this.saksnummer,
            avsenderEnhet = this.avsenderEnhet,
            bruker = this.bruker,
            vergeNavn = this.vergeNavn,
            signerendeSaksbehandlere = signerendeSaksbehandlere,
        )
}

class SignerendeSaksbehandlere(
    val saksbehandler: String,
    val attesterendeSaksbehandler: String? = null
)

class Bruker(
    val foedselsnummer: Foedselsnummer,
    val fornavn: String,
    val mellomnavn: String?,
    val etternavn: String
)

class NavEnhet(
    val nettside: String,
    val navn: String,
    val telefonnummer: Telefonnummer
)