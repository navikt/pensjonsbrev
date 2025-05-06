package no.nav.brev.brevbaker

import no.nav.brev.InterneDataklasser
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

@OptIn(InterneDataklasser::class)
object FellesFactory {
    fun lagFelles(
        dokumentDato: LocalDate = LocalDate.of(2020, 1, 1),
        saksnummer: String = "1337123",
        signerendeSaksbehandlere: SignerendeSaksbehandlere? = null
    ): Felles = FellesImpl(
        dokumentDato = dokumentDato,
        saksnummer = saksnummer,
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
        signerendeSaksbehandlere = signerendeSaksbehandlere,
        vergeNavn = null,
    )

    val felles = lagFelles(
        signerendeSaksbehandlere = SignerendeSaksbehandlereImpl(
            saksbehandler = "Ole Saksbehandler",
            attesterendeSaksbehandler = "Per Attesterende"
        )
    )

    val fellesAuto = lagFelles(signerendeSaksbehandlere = null)


    fun copy(
        dokumentDato: LocalDate,
        saksnummer: String,
        avsenderEnhet: NAVEnhet,
        bruker: Bruker,
        vergeNavn: String?,
        signerendeSaksbehandlere: SignerendeSaksbehandlere?,
    ): Felles = FellesImpl(
        dokumentDato = dokumentDato,
        saksnummer = saksnummer,
        avsenderEnhet = avsenderEnhet,
        bruker = bruker,
        vergeNavn = vergeNavn,
        signerendeSaksbehandlere = signerendeSaksbehandlere,
    )
}