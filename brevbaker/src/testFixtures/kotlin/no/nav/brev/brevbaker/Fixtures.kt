package no.nav.brev.brevbaker

import no.nav.pensjon.brevbaker.api.model.Bruker
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.NavEnhet
import no.nav.pensjon.brevbaker.api.model.SignerendeSaksbehandlere
import java.time.LocalDate
import kotlin.reflect.KClass

interface LetterDataFactory {
    fun <T : Any> create(letterDataType: KClass<T>): T
}

object Fixtures {

    val felles: Felles = FellesFactory.felles

    val fellesAuto: Felles = FellesFactory.fellesAuto
}

fun Felles.copy(
    dokumentDato: LocalDate = this.dokumentDato,
    saksnummer: String = this.saksnummer,
    avsenderEnhet: NavEnhet = this.avsenderEnhet,
    bruker: Bruker = this.bruker,
    annenMottaker: String? = this.annenMottakerNavn,
    signerendeSaksbehandlere: SignerendeSaksbehandlere? = this.signerendeSaksbehandlere,
): Felles = FellesFactory.copy(
    dokumentDato = dokumentDato,
    saksnummer = saksnummer,
    avsenderEnhet = avsenderEnhet,
    bruker = bruker,
    annenMottaker = annenMottaker,
    signerendeSaksbehandlere = signerendeSaksbehandlere
)