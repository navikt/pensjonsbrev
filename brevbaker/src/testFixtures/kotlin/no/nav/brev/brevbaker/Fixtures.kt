package no.nav.brev.brevbaker

import java.time.LocalDate

object Fixtures {

    val felles = FellesTestImpl(
        dokumentDato = LocalDate.of(2020, 1, 1),
        saksnummer = "1337123",
        avsenderEnhet = NavEnhetTestImpl(
            nettside = "nav.no",
            navn = "Nav Familie- og pensjonsytelser Porsgrunn",
            telefonnummer = TelefonnummerTestImpl("55553334"),
        ),
        bruker = BrukerTestImpl(
            fornavn = "Test",
            mellomnavn = "\"bruker\"",
            etternavn = "Testerson",
            foedselsnummer = FoedselsnummerTestImpl("01019878910"),
        ),
        signerendeSaksbehandlere = SignerendeSaksbehandlereTestImpl(
            saksbehandler = "Ole Saksbehandler",
            attesterendeSaksbehandler = "Per Attesterende",
        ),
        vergeNavn = null,
    )

    val fellesAuto = felles.copy(signerendeSaksbehandlere = null)
}