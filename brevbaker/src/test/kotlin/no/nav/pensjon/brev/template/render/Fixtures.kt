package no.nav.pensjon.brev.template.render

import no.nav.pensjon.brev.template.HasModel
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

object Fixtures {
    val felles = no.nav.brev.brevbaker.Fixtures.felles
}

internal val testLetterMetadata =
    LetterMetadata(
        displayTitle = "En fin display tittel",
        isSensitiv = false,
        distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
        brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
    )

@TemplateModelHelpers
object SomeDtoHelperGen : HasModel<SomeDto>

data class SomeDto(val name: String, val pensjonInnvilget: Boolean, val kortNavn: String? = null)
