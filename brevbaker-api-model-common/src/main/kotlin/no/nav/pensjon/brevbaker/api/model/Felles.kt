package no.nav.pensjon.brevbaker.api.model

import no.nav.brev.InterneDataklasser
import java.time.LocalDate

class Felles internal constructor(
    val dokumentDato: LocalDate,
    val saksnummer: String,
    val avsenderEnhet: NAVEnhet,
    val bruker: Bruker,
    val vergeNavn: String?,
    val signerendeSaksbehandlere: SignerendeSaksbehandlere? = null
) {

    fun medSignerendeSaksbehandlere(signerendeSaksbehandlere: SignerendeSaksbehandlere): Felles =
        Felles(
            dokumentDato = this.dokumentDato,
            saksnummer = this.saksnummer,
            avsenderEnhet = this.avsenderEnhet,
            bruker = this.bruker,
            vergeNavn = this.vergeNavn,
            signerendeSaksbehandlere = signerendeSaksbehandlere,
        )
}

@InterneDataklasser
data class SignerendeSaksbehandlereImpl(
    override val saksbehandler: String,
    override val attesterendeSaksbehandler: String? = null
) : SignerendeSaksbehandlere

interface SignerendeSaksbehandlere {
    val saksbehandler: String
    val attesterendeSaksbehandler: String?
}

@InterneDataklasser
data class BrukerImpl(
    override val foedselsnummer: Foedselsnummer,
    override val fornavn: String,
    override val mellomnavn: String?,
    override val etternavn: String,
) : Bruker

interface Bruker {
    val foedselsnummer: Foedselsnummer
    val fornavn: String
    val mellomnavn: String?
    val etternavn: String
}

class NAVEnhet internal constructor(
    val nettside: String,
    val navn: String,
    val telefonnummer: Telefonnummer
)