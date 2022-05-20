package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.api.model.Felles
import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Element.Text.FontType
import no.nav.pensjon.brev.template.base.BaseTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import kotlin.reflect.KClass

fun <Lang : LanguageSupport, LetterData : Any> createTemplate(
    name: String,
    base: BaseTemplate,
    letterDataType: KClass<LetterData>,
    title: Element.Text.Literal<Lang>,
    letterMetadata: LetterMetadata,
    init: TemplateRootScope<Lang, LetterData>.() -> Unit
): LetterTemplate<Lang, LetterData> =
    with(TemplateRootScope<Lang, LetterData>().apply(init)) {
        return LetterTemplate(name, title, base, letterDataType, title.languages, outline, attachments, letterMetadata)
    }

open class TemplateGlobalScope<LetterData : Any> {
    fun argument(): Expression<LetterData> =
        Expression.FromScope(ExpressionScope<LetterData, *>::argument)

    fun felles(): Expression<Felles> =
        Expression.FromScope(ExpressionScope<LetterData, *>::felles)

}

@LetterTemplateMarker
open class TemplateRootScope<Lang : LanguageSupport, LetterData : Any>(
    val outline: MutableList<Element<Lang>> = mutableListOf(),
    val attachments: MutableList<IncludeAttachment<Lang, *>> = mutableListOf(),
) : TemplateGlobalScope<LetterData>() {

    fun outline(init: OutlineScope<Lang, LetterData>.() -> Unit) {
        outline.addAll(OutlineScope<Lang, LetterData>().apply(init).children)
    }

    fun <AttachmentData : Any> includeAttachment(
        template: AttachmentTemplate<Lang, AttachmentData>,
        attachmentData: Expression<AttachmentData>,
        predicate: Expression<Boolean> = true.expr(),
    ) {
        attachments.add(IncludeAttachment(attachmentData, template, predicate))
    }

}


@LetterTemplateMarker
class TemplateFormChoiceScope<Lang : LanguageSupport, LetterData : Any>(
    val choices: MutableList<Element.Text<Lang>> = mutableListOf()
) : TemplateGlobalScope<LetterData>()

fun <Lang1 : Language, LetterData : Any> TemplateFormChoiceScope<LanguageSupport.Single<Lang1>, LetterData>.choice(
    lang1: Pair<Lang1, String>,
    fontType: FontType = FontType.PLAIN
) {
    Element.Text.Literal.create(lang1, fontType).also { choices.add(it) }
}

fun <Lang1 : Language, Lang2 : Language, LetterData : Any> TemplateFormChoiceScope<LanguageSupport.Double<Lang1, Lang2>, LetterData>.choice(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
    fontType: FontType = FontType.PLAIN,
) {
    Element.Text.Literal.create(lang1, lang2, fontType).also { choices.add(it) }
}

fun <Lang1 : Language, Lang2 : Language, Lang3 : Language, LetterData : Any> TemplateFormChoiceScope<LanguageSupport.Triple<Lang1, Lang2, Lang3>, LetterData>.choice(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
    lang3: Pair<Lang3, String>,
    fontType: FontType = FontType.PLAIN,
) {
    Element.Text.Literal.create(lang1, lang2, lang3, fontType).also { choices.add(it) }
}

@DslMarker
annotation class LetterTemplateMarker
