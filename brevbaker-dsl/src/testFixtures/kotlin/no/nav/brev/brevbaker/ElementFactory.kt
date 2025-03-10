package no.nav.brev.brevbaker

import no.nav.pensjon.brev.template.AttachmentTemplate
import no.nav.pensjon.brev.template.ContentOrControlStructure
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.IncludeAttachment
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.ParagraphContentElement
import no.nav.pensjon.brev.template.TextElement

fun <Lang : LanguageSupport> createParagraph(paragraph: List<ParagraphContentElement<Lang>>) =
    Element.OutlineContent.Paragraph(paragraph)

fun <Lang : LanguageSupport> createTitle1(text: List<TextElement<Lang>>) = Element.OutlineContent.Title1(text)

fun <Lang : LanguageSupport, C : Element<Lang>> createContent(content: C) = ContentOrControlStructure.Content(content)

fun <Lang : LanguageSupport, AttachmentData : Any> createIncludeAttachment(
    data: Expression<AttachmentData>,
    template: AttachmentTemplate<Lang, AttachmentData>,
    predicate: Expression<Boolean> = Expression.Literal(true),
) = IncludeAttachment(data = data, template = template, predicate = predicate)