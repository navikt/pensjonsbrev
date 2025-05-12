package no.nav.brev.brevbaker

import no.nav.pensjon.brevbaker.api.model.Bruker
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.NavEnhet
import no.nav.pensjon.brevbaker.api.model.SignerendeSaksbehandlere
import java.time.LocalDate

object Fixtures {

    val felles: Felles = FellesFactory.felles

    val fellesAuto: Felles = FellesFactory.fellesAuto
}

fun Felles.copy(
    dokumentDato: LocalDate = this.dokumentDato,
    saksnummer: String = this.saksnummer,
    avsenderEnhet: NavEnhet = this.avsenderEnhet,
    bruker: Bruker = this.bruker,
    vergeNavn: String? = this.vergeNavn,
    signerendeSaksbehandlere: SignerendeSaksbehandlere? = this.signerendeSaksbehandlere,
): Felles = FellesFactory.copy(
    dokumentDato,
    saksnummer,
    avsenderEnhet,
    bruker,
    vergeNavn,
    signerendeSaksbehandlere
)