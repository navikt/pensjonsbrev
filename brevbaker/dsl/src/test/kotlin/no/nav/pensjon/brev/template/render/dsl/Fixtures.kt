package no.nav.pensjon.brev.template.render.dsl

import no.nav.brev.brevbaker.newText
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.LangNynorsk
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.TemplateRootScope
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

val bokmalTittel = newText(Language.Bokmal to "test brev")
fun TemplateRootScope<LangBokmal, *>.bokmalTittel() = title { text(bokmal { +"test brev" }) }
val nynorskTittel = newText(Language.Nynorsk to "test brev")
fun TemplateRootScope<LangNynorsk, *>.nynorskTittel() = title { text(nynorsk { +"test brev" }) }
val testLetterMetadata = LetterMetadata(
    displayTitle = "En fin display tittel",
    distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
    brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
)

data class SomeDto(val name: String, val pensjonInnvilget: Boolean, val kortNavn: String? = null)
