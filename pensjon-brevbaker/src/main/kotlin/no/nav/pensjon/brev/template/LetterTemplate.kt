package no.nav.pensjon.brev.template

import no.nav.pensjon.brevbaker.api.model.*
import kotlin.reflect.KClass

data class LetterTemplate<Lang : LanguageSupport, out LetterData : Any>(
    val name: String,
    val title: List<TextElement<Lang>>,
    val letterDataType: KClass<out LetterData>,
    val language: Lang,
    val outline: List<OutlineElement<Lang>>,
    val attachments: List<IncludeAttachment<Lang, *>> = emptyList(),
    val letterMetadata: LetterMetadata,
) {
    init {
        if (title.isEmpty()) {
            throw MissingTitleInTemplateException("Missing title in template: $name")
        }
    }
}