package no.nav.pensjon.brev.template

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