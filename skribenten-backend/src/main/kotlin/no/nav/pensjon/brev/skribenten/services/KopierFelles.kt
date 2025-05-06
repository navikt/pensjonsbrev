package no.nav.pensjon.brev.skribenten.services

import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.FellesImpl
import no.nav.pensjon.brevbaker.api.model.SignerendeSaksbehandlere

@OptIn(InterneDataklasser::class)
fun Felles.medSignerendeSaksbehandlere(signerendeSaksbehandlere: SignerendeSaksbehandlere? = this.signerendeSaksbehandlere) =
    FellesImpl(
        dokumentDato = this.dokumentDato,
        saksnummer = this.saksnummer,
        avsenderEnhet = this.avsenderEnhet,
        bruker = this.bruker,
        vergeNavn = this.vergeNavn,
        signerendeSaksbehandlere = signerendeSaksbehandlere
    )