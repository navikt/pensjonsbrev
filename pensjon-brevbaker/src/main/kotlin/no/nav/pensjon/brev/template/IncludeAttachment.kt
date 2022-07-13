package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.template.dsl.OutlineScope
import no.nav.pensjon.brev.template.dsl.expression.expr


fun <Lang : LanguageSupport, LetterData : Any> createAttachment(
    title: Element.Text.Literal<Lang>,
    includeSakspart: Boolean = false,
    outline: OutlineScope<Lang, LetterData>.() -> Unit
) = AttachmentTemplate<Lang, LetterData>(
    title,
    OutlineScope<Lang, LetterData>().apply(outline).children,
    includeSakspart
)


data class IncludeAttachment<out Lang : LanguageSupport, AttachmentData : Any>(
    val data: Expression<AttachmentData>,
    val template: AttachmentTemplate<Lang, AttachmentData>,
    val predicate: Expression<Boolean> = true.expr(),
)

data class AttachmentTemplate<out Lang : LanguageSupport, AttachmentData : Any>(
    val title: Element.Text.Literal<Lang>,
    val outline: List<Element<Lang>>,
    val includeSakspart: Boolean = false,
)