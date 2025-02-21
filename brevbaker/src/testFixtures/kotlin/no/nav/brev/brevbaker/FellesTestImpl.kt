package no.nav.brev.brevbaker

import no.nav.pensjon.brevbaker.api.model.Bruker
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.NAVEnhet
import no.nav.pensjon.brevbaker.api.model.SignerendeSaksbehandlere
import no.nav.pensjon.brevbaker.api.model.Telefonnummer
import java.time.LocalDate

data class NavEnhetTestImpl(
    override val nettside: String,
    override val navn: String,
    override val telefonnummer: Telefonnummer,
) : NAVEnhet

data class TelefonnummerTestImpl(override val value: String) : Telefonnummer

data class FoedselsnummerTestImpl(override val value: String) : Foedselsnummer

data class BrukerTestImpl(
    override val foedselsnummer: Foedselsnummer,
    override val fornavn: String,
    override val mellomnavn: String?,
    override val etternavn: String,
) : Bruker

data class SignerendeSaksbehandlereTestImpl(
    override val saksbehandler: String,
    override val attesterendeSaksbehandler: String? = null
) : SignerendeSaksbehandlere

data class FellesTestImpl(
    override val dokumentDato: LocalDate,
    override val saksnummer: String,
    override val avsenderEnhet: NAVEnhet,
    override val bruker: Bruker,
    override val vergeNavn: String?,
    override val signerendeSaksbehandlere: SignerendeSaksbehandlere? = null,
) : Felles