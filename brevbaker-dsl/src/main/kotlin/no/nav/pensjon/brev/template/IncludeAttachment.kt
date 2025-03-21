package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.TextScope
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.PDFVedleggType

fun <Lang : LanguageSupport, LetterData : Any> createAttachment(
    title: TextElement<Lang>,
    includeSakspart: Boolean = false,
    outline: OutlineOnlyScope<Lang, LetterData>.() -> Unit
) = AttachmentTemplate<Lang, LetterData>(
    title,
    OutlineOnlyScope<Lang, LetterData>().apply(outline).elements,
    includeSakspart
)

fun TextScope<BaseLanguages, *>.namedReference(attachment: AttachmentTemplate<BaseLanguages, *>) {
    text(Language.Bokmal to "«", Language.Nynorsk to "«", Language.English to "“")
    addTextContent(attachment.title)
    text(Language.Bokmal to "»", Language.Nynorsk to "»", Language.English to "”")
}

data class IncludeAttachment<out Lang : LanguageSupport, AttachmentData : Any>(
    val data: Expression<AttachmentData>,
    val template: AttachmentTemplate<Lang, AttachmentData>,
    val predicate: Expression<Boolean> = Expression.Literal(true),
): StableHash by StableHash.of(data, template, predicate)

data class AttachmentTemplate<out Lang : LanguageSupport, AttachmentData : Any>(
    val title: TextElement<Lang>,
    val outline: List<OutlineElement<Lang>>,
    val includeSakspart: Boolean = false,
): HasModel<AttachmentData>, StableHash by StableHash.of(title, StableHash.of(outline), StableHash.of(includeSakspart))

data class PDFTemplate<AttachmentData : BrevbakerBrevdata>(
    val type: PDFVedleggType,
    val data: Expression<AttachmentData>
) : StableHash by StableHash.of(StableHash.of(type), data)