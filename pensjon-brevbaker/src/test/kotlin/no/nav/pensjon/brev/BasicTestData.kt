package no.nav.pensjon.brev

import no.nav.pensjon.brev.api.dto.*
import java.time.LocalDate

var felles =
    Felles(
        dokumentDato = LocalDate.of(2020, 1, 1),
        saksnummer = "1337123",
        avsenderEnhet = NAVEnhet(
            returAdresse = ReturAdresse(
                adresseLinje1 = "Postboks 6600 Etterstad",
                postNr = "0607",
                postSted = "Oslo",
            ),
            navenhetnettside = "nav.no",
            navn = "NAV Familie- og pensjonsytelser Porsgrunn",
            navTelefonnummer = "55553334",
        ),
        mottaker = Mottaker(
            fornavn = "PERLAKI",
            etternavn = "YASIR-MASK",
            foedselsnummer = "12345678910",
            kortnavn = "YASIR-MASK PERLAKI",
            adresse = Adresse(
                adresselinje1 = "JERNBANETORGET 4 F",
                postnummer = "1344",
                poststed = "HASLUM",
                land = "Norge",
            ),
        ),
        spraakKode = "TEST",
        signerendeSaksbehandlere = SignerendeSaksbehandlere(
            saksbehandler = "saksbehandler saksbehandlerson",
            attesterendeSaksbehandler = "saksbehandler attesterendeSaksbehandlerson",
        ),
    )