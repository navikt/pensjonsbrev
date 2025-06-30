package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.QuotationMarks.end
import no.nav.pensjon.brev.template.dsl.QuotationMarks.start
import no.nav.pensjon.brev.template.dsl.TextScope
import no.nav.pensjon.brev.template.dsl.text


fun TextScope<BaseLanguages, *>.namedReference(attachment: AttachmentTemplate<BaseLanguages, *>) {
    text(Bokmal to start(Bokmal), Nynorsk to start(Nynorsk), English to start(English))
    attachment.title.forEach { addTextContent(it) }
    text(Bokmal to end(Bokmal), Nynorsk to end(Nynorsk), English to end(English))
}

@JvmName("namedReferenceBokmalNynorsk")
fun TextScope<LangBokmalNynorsk, *>.namedReference(attachment: AttachmentTemplate<BaseLanguages, *>) {
    text(Bokmal to start(Bokmal), Nynorsk to start(Nynorsk))
    attachment.title.forEach { addTextContent(it) }
    text(Bokmal to end(Bokmal), Nynorsk to end(Nynorsk))
}

@JvmName("namedReferenceBokmalEnglish")
fun TextScope<LangBokmalEnglish, *>.namedReference(attachment: AttachmentTemplate<BaseLanguages, *>) {
    text(Bokmal to start(Bokmal), English to start(English))
    attachment.title.forEach { addTextContent(it as TextElement<LangBokmalEnglish>) }
    text(Bokmal to end(Bokmal), English to end(English))
}

@JvmName("namedReferenceBokmal")
fun TextScope<LangBokmal, *>.namedReference(attachment: AttachmentTemplate<BaseLanguages, *>) {
    text(Bokmal to start(Bokmal))
    attachment.title.forEach { addTextContent(it) }
    text(Bokmal to end(Bokmal))
}