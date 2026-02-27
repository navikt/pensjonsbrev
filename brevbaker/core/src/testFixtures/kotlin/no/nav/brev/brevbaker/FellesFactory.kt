package no.nav.brev.brevbaker

import no.nav.pensjon.brevbaker.api.model.BrevbakerFelles.Bruker
import no.nav.pensjon.brevbaker.api.model.BrevbakerFelles
import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.BrevbakerFelles.NavEnhet
import no.nav.pensjon.brevbaker.api.model.BrevbakerFelles.SignerendeSaksbehandlere
import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Telefonnummer
import java.time.LocalDate

object FellesFactory {
    fun lagFelles(
        dokumentDato: LocalDate = LocalDate.of(2020, 1, 1),
        saksnummer: String = "1337123",
        signerendeSaksbehandlere: SignerendeSaksbehandlere? = null
    ): BrevbakerFelles = BrevbakerFelles(
        dokumentDato = dokumentDato,
        saksnummer = saksnummer,
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
        signerendeSaksbehandlere = signerendeSaksbehandlere,
        annenMottakerNavn = null,
    )

    val felles = lagFelles(
        signerendeSaksbehandlere = SignerendeSaksbehandlere(
            saksbehandler = "Ole Saksbehandler",
            attesterendeSaksbehandler = "Per Attesterende"
        )
    )

    val fellesAuto = lagFelles(signerendeSaksbehandlere = null)
}

fun BrevbakerFelles.copy(
    dokumentDato: LocalDate = this.dokumentDato,
    saksnummer: String = this.saksnummer,
    avsenderEnhet: NavEnhet = this.avsenderEnhet,
    bruker: Bruker = this.bruker,
    annenMottaker: String? = this.annenMottakerNavn,
    signerendeSaksbehandlere: SignerendeSaksbehandlere? = this.signerendeSaksbehandlere,
): BrevbakerFelles = BrevbakerFelles(
    dokumentDato = dokumentDato,
    saksnummer = saksnummer,
    avsenderEnhet = avsenderEnhet,
    bruker = bruker,
    annenMottakerNavn = annenMottaker,
    signerendeSaksbehandlere = signerendeSaksbehandlere,
)