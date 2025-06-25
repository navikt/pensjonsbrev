package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.template.dsl.QuotationMarks
import no.nav.pensjon.brev.template.dsl.TextScope

fun <Lang : BaseLanguages> TextScope<in Lang, *>.namedReference(attachment: AttachmentTemplate<Lang, *>) {
    eval(QuotationMarks.start)
    attachment.title.forEach { addTextContent(it) }
    eval(QuotationMarks.start)
}

@JvmName("namedReferenceBokmalEnglish")
fun TextScope<LangBokmalEnglish, *>.namedReference(attachment: AttachmentTemplate<BaseLanguages, *>) {
    attachment.title.forEach {
        // Safe because we know that a template that support BaseLanguages will support Bokmal and English
        @Suppress("UNCHECKED_CAST")
        addTextContent(it as TextElement<LangBokmalEnglish>)
    }
}
