package no.nav.pensjon.brevbaker.api.model

import no.nav.brev.InterneDataklasser
import java.time.LocalDate

@InterneDataklasser
data class FellesImpl(
    override val dokumentDato: LocalDate,
    override val saksnummer: String,
    override val avsenderEnhet: NAVEnhet,
    override val bruker: Bruker,
    override val vergeNavn: String?,
    override val signerendeSaksbehandlere: SignerendeSaksbehandlere? = null,
) : Felles {
    override fun medSignerendeSaksbehandlere(signerendeSaksbehandlere: SignerendeSaksbehandlere) = this.copy(signerendeSaksbehandlere = signerendeSaksbehandlere)
}

interface Felles {
    val dokumentDato: LocalDate
    val saksnummer: String
    val avsenderEnhet: NAVEnhet
    val bruker: Bruker
    val vergeNavn: String?
    val signerendeSaksbehandlere: SignerendeSaksbehandlere?

    fun medSignerendeSaksbehandlere(signerendeSaksbehandlere: SignerendeSaksbehandlere): Felles
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

@InterneDataklasser
data class NavEnhetImpl(
    override val nettside: String,
    override val navn: String,
    override val telefonnummer: Telefonnummer,
) : NAVEnhet

interface NAVEnhet {
    val nettside: String
    val navn: String
    val telefonnummer: Telefonnummer
}