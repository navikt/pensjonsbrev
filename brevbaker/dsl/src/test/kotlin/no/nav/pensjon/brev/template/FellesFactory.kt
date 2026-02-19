package no.nav.pensjon.brev.template

import no.nav.pensjon.brevbaker.api.model.BrevFelles.Bruker
import no.nav.pensjon.brevbaker.api.model.BrevFelles.Felles
import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.BrevFelles.NavEnhet
import no.nav.pensjon.brevbaker.api.model.BrevFelles.SignerendeSaksbehandlere
import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Telefonnummer
import java.time.LocalDate

object FellesFactory {
    val felles: Felles = Felles(
        dokumentDato = LocalDate.of(2020, 1, 1),
        saksnummer = "1337123",
        avsenderEnhet =
            NavEnhet(
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
        signerendeSaksbehandlere = SignerendeSaksbehandlere(
            saksbehandler = "Ole Saksbehandler",
            attesterendeSaksbehandler = "Per Attesterende"
        ),
        annenMottakerNavn = null,
    )
}