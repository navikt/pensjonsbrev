package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.template.dsl.TemplateContainerScope


fun <LetterData : Any> createAttachment(
    title: Element.Text.Literal<BaseLanguages>,
    includeSakspart: Boolean = false,
    outline: TemplateContainerScope<BaseLanguages, LetterData>.() -> Unit
) = AttachmentTemplate<LetterData>(
    title,
    TemplateContainerScope<BaseLanguages, LetterData>().apply(outline).children,
    includeSakspart
)


data class IncludeAttachment<AttachmentData : Any>(
    val data: Expression<AttachmentData>,
    val template: AttachmentTemplate<AttachmentData>
)

