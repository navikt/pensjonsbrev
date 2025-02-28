package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.ContentOrControlStructureImpl.ContentImpl
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.ElementImpl
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.StringExpression
import no.nav.pensjon.brev.template.TextElement

interface TextScope<Lang : LanguageSupport, LetterData : Any> : TemplateGlobalScope<LetterData> {

    fun addTextContent(e: TextElement<Lang>)

    fun eval(expression: StringExpression, fontType: FontType = FontType.PLAIN) {
        addTextContent(ContentImpl(ElementImpl.OutlineContentImpl.ParagraphContentImpl.TextImpl.ExpressionImpl(expression, fontType)))
    }

    fun newline()
}