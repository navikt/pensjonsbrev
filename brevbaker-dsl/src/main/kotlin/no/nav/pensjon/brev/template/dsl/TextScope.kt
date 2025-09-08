package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.BaseLanguages
import no.nav.pensjon.brev.template.ContentOrControlStructure.Content
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.StringExpression
import no.nav.pensjon.brev.template.TextElement

interface TextScope<Lang : LanguageSupport, LetterData : Any> : TemplateGlobalScope<LetterData> {

    fun addTextContent(e: TextElement<BaseLanguages>)
    fun addTextContent(e: TextElement<Lang>)

    fun eval(expression: StringExpression, fontType: FontType = FontType.PLAIN) {
        addTextContent(Content(Element.OutlineContent.ParagraphContent.Text.Expression<Lang>(expression, fontType)))
    }

    fun newline()
}