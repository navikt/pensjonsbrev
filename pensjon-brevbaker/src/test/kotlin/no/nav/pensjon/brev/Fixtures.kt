package no.nav.pensjon.brev

import no.nav.pensjon.brev.api.model.*
import java.time.LocalDate

object Fixtures {

    val felles = Felles(
        dokumentDato = LocalDate.of(2020, 1, 1),
        saksnummer = "1337123",
        avsenderEnhet = NAVEnhet(
            returAdresse = ReturAdresse(
                adresseLinje1 = "Postboks 6600 Etterstad",
                postNr = "0607",
                postSted = "Oslo",
            ),
            nettside = "nav.no",
            navn = "NAV Familie- og pensjonsytelser Porsgrunn",
            telefonnummer = Telefonnummer("55553334"),
        ),
        mottaker = Mottaker(
            fornavn = "TEST",
            etternavn = "TESTERSON",
            foedselsnummer = Foedselsnummer("01019878910"),
            kortnavn = "TESTERSON TEST",
            adresse = Adresse(
                linje1 = "TEST TESTERSON",
                linje2 = "JERNBANETORGET 4 F",
                linje3 = "1344 HASLUM",
            ),
        ),
        signerendeSaksbehandlere = SignerendeSaksbehandlere(
            saksbehandler = "Ole Saksbehandler",
            attesterendeSaksbehandler = "Per Attesterende",
        ),
    )

    val fellesAuto = felles.copy(signerendeSaksbehandlere = null)
}