package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.template.dsl.TemplateContainerScope
import kotlin.reflect.KClass

data class Phrase<out Lang : LanguageSupport, PhraseData: Any>(
    val elements: List<Element<Lang>>,
    val phraseDataType: KClass<PhraseData>,
)

inline fun <Lang : LanguageSupport, reified PhraseData : Any> createPhrase(
    phraseDataType: KClass<PhraseData> = PhraseData::class,
    init: TemplateContainerScope<Lang, PhraseData>.() -> Unit
): Phrase<Lang, PhraseData> =
    with(TemplateContainerScope<Lang, PhraseData>().apply(init)) {
        Phrase(children, phraseDataType)
    }
