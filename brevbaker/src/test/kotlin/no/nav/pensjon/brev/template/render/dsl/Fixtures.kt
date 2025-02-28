package no.nav.pensjon.brev.template.render.dsl

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadataImpl

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