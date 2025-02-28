package no.nav.pensjon.brev.template.render

import no.nav.brev.brevbaker.BrukerTestImpl
import no.nav.brev.brevbaker.FellesTestImpl
import no.nav.brev.brevbaker.FoedselsnummerTestImpl
import no.nav.brev.brevbaker.NavEnhetTestImpl
import no.nav.brev.brevbaker.SignerendeSaksbehandlereTestImpl
import no.nav.brev.brevbaker.TelefonnummerTestImpl
import no.nav.pensjon.brev.template.HasModel
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadataImpl
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

internal val bokmalTittel = newText(Language.Bokmal to "test brev")
internal val nynorskTittel = newText(Language.Nynorsk to "test brev")
internal val testLetterMetadata = LetterMetadataImpl(
    displayTitle = "En fin display tittel",
    isSensitiv = false,
    distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
    brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
)

@TemplateModelHelpers
object SomeDtoHelperGen : HasModel<SomeDto>
data class SomeDto(val name: String, val pensjonInnvilget: Boolean, val kortNavn: String? = null)