package no.nav.pensjon.brev.template.render.dsl

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

val bokmalTittel = newText(Language.Bokmal to "test brev")
val nynorskTittel = newText(Language.Nynorsk to "test brev")
val testLetterMetadata = LetterMetadata(
    displayTitle = "En fin display tittel",
    isSensitiv = false,
    distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
    brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
)

data class SomeDto(val name: String, val pensjonInnvilget: Boolean, val kortNavn: String? = null)
