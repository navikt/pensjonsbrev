package no.nav.pensjon.brev.skribenten.services

import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brevbaker.api.model.Bruker
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.FellesImpl
import no.nav.pensjon.brevbaker.api.model.NAVEnhet
import no.nav.pensjon.brevbaker.api.model.SignerendeSaksbehandlere
import java.time.LocalDate

@OptIn(InterneDataklasser::class)
fun Felles.kopier(
    dokumentDato: LocalDate = this.dokumentDato,
    saksnummer: String = this.saksnummer,
    avsenderEnhet: NAVEnhet = this.avsenderEnhet,
    bruker: Bruker = this.bruker,
    vergeNavn: String? = this.vergeNavn,
    signerendeSaksbehandlere: SignerendeSaksbehandlere? = this.signerendeSaksbehandlere,
) = FellesImpl(
    dokumentDato = dokumentDato,
    saksnummer = saksnummer,
    avsenderEnhet = avsenderEnhet,
    bruker = bruker,
    vergeNavn = vergeNavn,
    signerendeSaksbehandlere = signerendeSaksbehandlere
)