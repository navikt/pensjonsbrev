package no.nav.brev.brevbaker

import no.nav.pensjon.brevbaker.api.model.Bruker
import no.nav.pensjon.brevbaker.api.model.BrukerImpl
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.FellesImpl
import no.nav.pensjon.brevbaker.api.model.FoedselsnummerImpl
import no.nav.pensjon.brevbaker.api.model.NAVEnhet
import no.nav.pensjon.brevbaker.api.model.NavEnhetImpl
import no.nav.pensjon.brevbaker.api.model.SignerendeSaksbehandlere
import no.nav.pensjon.brevbaker.api.model.SignerendeSaksbehandlereImpl
import no.nav.pensjon.brevbaker.api.model.TelefonnummerImpl
import java.time.LocalDate

object Fixtures {

    val felles: Felles = FellesImpl(
        dokumentDato = LocalDate.of(2020, 1, 1),
        saksnummer = "1337123",
        avsenderEnhet = NavEnhetImpl(
            nettside = "nav.no",
            navn = "Nav Familie- og pensjonsytelser Porsgrunn",
            telefonnummer = TelefonnummerImpl("55553334"),
        ),
        bruker = BrukerImpl(
            fornavn = "Test",
            mellomnavn = "\"bruker\"",
            etternavn = "Testerson",
            foedselsnummer = FoedselsnummerImpl("01019878910"),
        ),
        signerendeSaksbehandlere = SignerendeSaksbehandlereImpl(
            saksbehandler = "Ole Saksbehandler",
            attesterendeSaksbehandler = "Per Attesterende",
        ),
        vergeNavn = null,
    )

    val fellesAuto: Felles = (felles as FellesImpl).copy(signerendeSaksbehandlere = null)
}

fun Felles.copy(
    dokumentDato: LocalDate = this.dokumentDato,
    saksnummer: String = this.saksnummer,
    avsenderEnhet: NAVEnhet = this.avsenderEnhet,
    bruker: Bruker = this.bruker,
    vergeNavn: String? = this.vergeNavn,
    signerendeSaksbehandlere: SignerendeSaksbehandlere? = this.signerendeSaksbehandlere,
): Felles = FellesImpl(
    dokumentDato = dokumentDato,
    saksnummer = saksnummer,
    avsenderEnhet = avsenderEnhet,
    bruker = bruker,
    vergeNavn = vergeNavn,
    signerendeSaksbehandlere = signerendeSaksbehandlere,
)