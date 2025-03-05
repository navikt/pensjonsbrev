@file:OptIn(InterneDataklasser::class)

package no.nav.pensjon.brev.template.dsl

import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import kotlin.reflect.KClass

@OptIn(InterneDataklasser::class)
fun <Lang : LanguageSupport, LetterData : Any> createTemplate(
    name: String,
    letterDataType: KClass<LetterData>,
    languages: Lang,
    letterMetadata: LetterMetadata,
    init: TemplateRootScope<Lang, LetterData>.() -> Unit
): LetterTemplate<Lang, LetterData> =
    with(TemplateRootScope<Lang, LetterData>().apply(init)) {
        return LetterTemplateImpl(name, title, letterDataType, languages, outline, attachments, letterMetadata)
    }

@LetterTemplateMarker
class TemplateRootScope<Lang : LanguageSupport, LetterData : Any>(
    val title: MutableList<TextElement<Lang>> = mutableListOf(),
    val outline: MutableList<OutlineElement<Lang>> = mutableListOf(),
    val attachments: MutableList<IncludeAttachment<Lang, *>> = mutableListOf(),
) : TemplateGlobalScope<LetterData> {

    fun title(init: PlainTextOnlyScope<Lang, LetterData>.() -> Unit) {
        title.addAll(PlainTextOnlyScope<Lang, LetterData>().apply(init).elements)
    }

    fun outline(init: OutlineOnlyScope<Lang, LetterData>.() -> Unit) {
        outline.addAll(OutlineOnlyScope<Lang, LetterData>().apply(init).elements)
    }

    fun includeAttachment(template: AttachmentTemplate<Lang, EmptyBrevdata>) {
        attachments.add(IncludeAttachment(EmptyBrevdata.expr(), template, true.expr()))
    }

    fun <AttachmentData : Any> includeAttachment(
        template: AttachmentTemplate<Lang, AttachmentData>,
        attachmentData: Expression<AttachmentData>,
        predicate: Expression<Boolean> = true.expr(),
    ) {
        attachments.add(IncludeAttachment(attachmentData, template, predicate))
    }

    fun includeAttachment(
        template: AttachmentTemplate<Lang, Unit>,
        predicate: Expression<Boolean> = true.expr(),
    ) {
        attachments.add(IncludeAttachment(Unit.expr(), template, predicate))
    }

    fun <AttachmentData : Any> includeAttachmentIfNotNull(
        template: AttachmentTemplate<Lang, AttachmentData>,
        attachmentData: Expression<AttachmentData?>,
    ) {
        @Suppress("UNCHECKED_CAST")
        attachments.add(IncludeAttachment(attachmentData as Expression<AttachmentData>, template, attachmentData.notNull()))
    }
}


@LetterTemplateMarker
class TemplateFormChoiceScope<Lang : LanguageSupport, LetterData : Any>(
    val choices: MutableList<Element.OutlineContent.ParagraphContent.Text<Lang>> = mutableListOf()
) : TemplateGlobalScope<LetterData>

@OptIn(InterneDataklasser::class)
fun <Lang1 : Language, LetterData : Any> TemplateFormChoiceScope<LanguageSupport.Single<Lang1>, LetterData>.choice(
    lang1: Pair<Lang1, String>,
    fontType: FontType = FontType.PLAIN
) {
    ElementImpl.OutlineContentImpl.ParagraphContentImpl.TextImpl.LiteralImpl.create(lang1, fontType).also { choices.add(it) }
}

fun <Lang1 : Language, Lang2 : Language, LetterData : Any> TemplateFormChoiceScope<LanguageSupport.Double<Lang1, Lang2>, LetterData>.choice(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
    fontType: FontType = FontType.PLAIN,
) {
    ElementImpl.OutlineContentImpl.ParagraphContentImpl.TextImpl.LiteralImpl.create(lang1, lang2, fontType).also { choices.add(it) }
}

fun <Lang1 : Language, Lang2 : Language, Lang3 : Language, LetterData : Any> TemplateFormChoiceScope<LanguageSupport.Triple<Lang1, Lang2, Lang3>, LetterData>.choice(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
    lang3: Pair<Lang3, String>,
    fontType: FontType = FontType.PLAIN,
) {
    ElementImpl.OutlineContentImpl.ParagraphContentImpl.TextImpl.LiteralImpl.create(lang1, lang2, lang3, fontType).also { choices.add(it) }
}

@DslMarker
internal annotation class LetterTemplateMarker
