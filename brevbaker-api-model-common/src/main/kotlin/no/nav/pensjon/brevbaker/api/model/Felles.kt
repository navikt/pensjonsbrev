package no.nav.pensjon.brevbaker.api.model

import java.time.LocalDate

class Felles internal constructor(
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

class SignerendeSaksbehandlere internal constructor(
    val saksbehandler: String,
    val attesterendeSaksbehandler: String?
) {
    companion object {
        fun from(saksbehandler: String, attesterendeSaksbehandler: String? = null): SignerendeSaksbehandlere =
            SignerendeSaksbehandlere(
                saksbehandler = saksbehandler,
                attesterendeSaksbehandler = attesterendeSaksbehandler
            )
    }
}

class Bruker internal constructor(
    val foedselsnummer: Foedselsnummer,
    val fornavn: String,
    val mellomnavn: String?,
    val etternavn: String
)

class NavEnhet internal constructor(
    val nettside: String,
    val navn: String,
    val telefonnummer: Telefonnummer
)