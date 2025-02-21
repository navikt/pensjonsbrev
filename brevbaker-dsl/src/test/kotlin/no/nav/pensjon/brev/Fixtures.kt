package no.nav.pensjon.brev

import no.nav.pensjon.brevbaker.api.model.Bruker
import no.nav.pensjon.brevbaker.api.model.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.NAVEnhet
import no.nav.pensjon.brevbaker.api.model.SignerendeSaksbehandlere
import no.nav.pensjon.brevbaker.api.model.Telefonnummer
import java.time.LocalDate

object Fixtures {

    class Felles(override val signerendeSaksbehandlere: SignerendeSaksbehandlere?) : no.nav.pensjon.brevbaker.api.model.Felles {
        override val dokumentDato = LocalDate.of(2020, 1, 1)
        override val saksnummer = "1337123"
        override val avsenderEnhet =
            object : NAVEnhet {
                override val nettside = "nav.no"
                override val navn = "Nav Familie- og pensjonsytelser Porsgrunn"
                override val telefonnummer = object : Telefonnummer {
                    override val value = "55553334"
                }
            }
        override val bruker =
            object : Bruker {
                override val fornavn = "Test"
                override val mellomnavn = "\"bruker\""
                override val etternavn = "Testerson"
                override val foedselsnummer =
                    object : Foedselsnummer {
                        override val value = "01019878910"
                    }
            }
        override val vergeNavn = null
    }

    val felles = Felles(
        object : SignerendeSaksbehandlere {
            override val saksbehandler = "Ole Saksbehandler"
            override val attesterendeSaksbehandler = "Per Attesterende"
        })

    val fellesAuto = Felles(null)

}