package no.nav.brev.brevbaker

import no.nav.pensjon.brevbaker.api.model.Bruker
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.NAVEnhet
import no.nav.pensjon.brevbaker.api.model.SignerendeSaksbehandlere
import no.nav.pensjon.brevbaker.api.model.Telefonnummer
import java.time.LocalDate

object FellesFactory {
    fun lagFelles(
        dokumentDato: LocalDate = LocalDate.of(2020, 1, 1),
        saksnummer: String = "1337123",
        signerendeSaksbehandlere: SignerendeSaksbehandlere? = null
    ): Felles = Felles(
        dokumentDato = dokumentDato,
        saksnummer = saksnummer,
        avsenderEnhet = NAVEnhet(
            nettside = "nav.no",
            navn = "Nav Familie- og pensjonsytelser Porsgrunn",
            telefonnummer = Telefonnummer("55553334"),
        ),
        bruker = Bruker(
            fornavn = "Test",
            mellomnavn = "\"bruker\"",
            etternavn = "Testerson",
            foedselsnummer = Foedselsnummer("01019878910"),
        ),
        signerendeSaksbehandlere = signerendeSaksbehandlere,
        vergeNavn = null,
    )

    val felles = lagFelles(
        signerendeSaksbehandlere = SignerendeSaksbehandlere(
            saksbehandler = "Ole Saksbehandler",
            attesterendeSaksbehandler = "Per Attesterende"
        )
    )

    val fellesAuto = lagFelles(signerendeSaksbehandlere = null)


    // TODO: Mistenkjer at denne berre blir brukt til signerendeSaksbehandlere, kan i så fall fjerne
    fun copy(
        dokumentDato: LocalDate,
        saksnummer: String,
        avsenderEnhet: NAVEnhet,
        bruker: Bruker,
        vergeNavn: String?,
        signerendeSaksbehandlere: SignerendeSaksbehandlere?,
    ): Felles = Felles(
        dokumentDato = dokumentDato,
        saksnummer = saksnummer,
        avsenderEnhet = avsenderEnhet,
        bruker = bruker,
        vergeNavn = vergeNavn,
        signerendeSaksbehandlere = signerendeSaksbehandlere,
    )
}