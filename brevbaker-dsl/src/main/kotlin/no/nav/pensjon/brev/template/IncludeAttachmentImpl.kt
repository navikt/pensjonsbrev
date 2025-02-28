package no.nav.pensjon.brev.template

import no.nav.brev.InterneDataklasser

@InterneDataklasser
data class IncludeAttachmentImpl<out Lang : LanguageSupport, AttachmentData : Any>(
    override val data: Expression<AttachmentData>,
    override val template: AttachmentTemplate<Lang, AttachmentData>,
    override val predicate: Expression<Boolean> = ExpressionImpl.LiteralImpl(true),
): IncludeAttachment<Lang, AttachmentData>, StableHash by StableHash.of(data, template, predicate)

@InterneDataklasser
data class AttachmentTemplateImpl<out Lang : LanguageSupport, AttachmentData : Any>(
    override val title: TextElement<Lang>,
    override val outline: List<OutlineElement<Lang>>,
    override val includeSakspart: Boolean = false,
): AttachmentTemplate<Lang, AttachmentData>, StableHash by StableHash.of(title, StableHash.of(outline), StableHash.of(includeSakspart))