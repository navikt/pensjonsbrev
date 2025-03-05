package no.nav.pensjon.brev.template.render

import no.nav.pensjon.brev.template.HasModel
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brevbaker.api.model.BrukerImpl
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.FellesImpl
import no.nav.pensjon.brevbaker.api.model.FoedselsnummerImpl
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadataImpl
import no.nav.pensjon.brevbaker.api.model.NavEnhetImpl
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

internal val bokmalTittel = newText(Language.Bokmal to "test brev")
internal val nynorskTittel = newText(Language.Nynorsk to "test brev")
internal val testLetterMetadata = LetterMetadata(
    displayTitle = "En fin display tittel",
    isSensitiv = false,
    distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
    brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
)

@TemplateModelHelpers
object SomeDtoHelperGen : HasModel<SomeDto>
data class SomeDto(val name: String, val pensjonInnvilget: Boolean, val kortNavn: String? = null)