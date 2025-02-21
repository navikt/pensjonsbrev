package no.nav.pensjon.brevbaker.api.model

import no.nav.brev.InterneDataklasser
import java.time.LocalDate

data class Felles(
    val dokumentDato: LocalDate,
    val saksnummer: String,
    val avsenderEnhet: NAVEnhet,
    val bruker: Bruker,
    val vergeNavn: String?,
    val signerendeSaksbehandlere: SignerendeSaksbehandlere? = null,
)

data class SignerendeSaksbehandlere(val saksbehandler: String, val attesterendeSaksbehandler: String? = null)

data class Bruker(
    val foedselsnummer: Foedselsnummer,
    val fornavn: String,
    val mellomnavn: String?,
    val etternavn: String,
)

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