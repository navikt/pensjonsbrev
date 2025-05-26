package no.nav.brev.brevbaker

import no.nav.brev.InternKonstruktoer
import no.nav.pensjon.brev.api.model.maler.VedleggBrevdata
import no.nav.pensjon.brev.template.AttachmentTemplate
import no.nav.pensjon.brev.template.ContentOrControlStructure
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.IncludeAttachment
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.ParagraphContentElement
import no.nav.pensjon.brev.template.TextElement
import no.nav.pensjon.brev.template.dsl.TextOnlyScope
import no.nav.pensjon.brevbaker.api.model.ElementTags

fun <Lang : LanguageSupport> createParagraph(paragraph: List<ParagraphContentElement<Lang>>) =
    Element.OutlineContent.Paragraph(paragraph)

fun <Lang : LanguageSupport> createTitle1(text: List<TextElement<Lang>>) = Element.OutlineContent.Title1(text)

fun <Lang : LanguageSupport, C : Element<Lang>> createContent(content: C) = ContentOrControlStructure.Content(content)

fun <Lang : LanguageSupport, AttachmentData : VedleggBrevdata> createIncludeAttachment(
    data: Expression<AttachmentData>,
    template: AttachmentTemplate<Lang, AttachmentData>,
    predicate: Expression<Boolean> = createExpressionLiteral(true),
) = IncludeAttachment(data = data, template = template, predicate = predicate)

@OptIn(InternKonstruktoer::class)
fun <Out> createExpressionLiteral(value: Out, tags: Set<ElementTags> = emptySet()) = Expression.Literal(value, tags)

fun <Lang : LanguageSupport, LetterData : Any> createTextOnlyScope() = TextOnlyScope<Lang, LetterData>()