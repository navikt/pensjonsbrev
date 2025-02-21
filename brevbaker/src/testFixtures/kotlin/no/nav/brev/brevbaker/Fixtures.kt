package no.nav.brev.brevbaker

import no.nav.pensjon.brevbaker.api.model.Bruker
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.NAVEnhet
import no.nav.pensjon.brevbaker.api.model.SignerendeSaksbehandlere
import no.nav.pensjon.brevbaker.api.model.Telefonnummer
import java.time.LocalDate

object Fixtures {

    val felles = Felles(
        dokumentDato = LocalDate.of(2020, 1, 1),
        saksnummer = "1337123",
        avsenderEnhet = NavEnhetTestImpl(
            nettside = "nav.no",
            navn = "Nav Familie- og pensjonsytelser Porsgrunn",
            telefonnummer = TelefonnummerTestImpl("55553334"),
        ),
        bruker = Bruker(
            fornavn = "Test",
            mellomnavn = "\"bruker\"",
            etternavn = "Testerson",
            foedselsnummer = FoedselsnummerTestImpl("01019878910"),
        ),
        signerendeSaksbehandlere = SignerendeSaksbehandlere(
            saksbehandler = "Ole Saksbehandler",
            attesterendeSaksbehandler = "Per Attesterende",
        ),
        vergeNavn = null,
    )

    val fellesAuto = felles.copy(signerendeSaksbehandlere = null)
}