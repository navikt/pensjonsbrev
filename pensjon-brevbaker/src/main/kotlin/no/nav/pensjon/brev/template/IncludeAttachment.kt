package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.template.dsl.TemplateContainerScope
import kotlin.reflect.KClass


fun <Lang : LanguageSupport, LetterData : Any> createAttachment(
        title: Element.Text.Literal<Lang>,
        includeSakspart: Boolean = false,
        outline: TemplateContainerScope<Lang, LetterData>.() -> Unit
) = AttachmentTemplate<Lang, LetterData>(
    title,
    TemplateContainerScope<Lang, LetterData>().apply(outline).children,
    includeSakspart
)


data class IncludeAttachment<out Lang : LanguageSupport, AttachmentData : Any>(
    val data: Expression<AttachmentData>,
    val template: AttachmentTemplate<Lang, AttachmentData>
)

data class AttachmentTemplate<out Lang : LanguageSupport, AttachmentData : Any>(
    val title: Element.Text.Literal<Lang>,
    val outline: List<Element<Lang>>,
    val includeSakspart: Boolean = false,
)