package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.template.dsl.*
import kotlin.reflect.KClass

data class Phrase<out Lang : LanguageSupport, PhraseData: Any>(
    val elements: List<Element<Lang>>,
    val phraseDataType: KClass<PhraseData>,
)

@Deprecated(
    "Erstattet av TextOnlyPhrase, ParagraphPhrase og OutlinePhrase",
)
inline fun <Lang : LanguageSupport, reified PhraseData : Any> createPhrase(
    phraseDataType: KClass<PhraseData> = PhraseData::class,
    init: OutlineScope<Lang, PhraseData>.() -> Unit
): Phrase<Lang, PhraseData> =
    with(OutlineScope<Lang, PhraseData>().apply(init)) {
        Phrase(children, phraseDataType)
    }

class TextOnlyPhrase<Lang : LanguageSupport, PhraseData>(private val phraseBody: TextOnlyScope<Lang, Unit>.(arg: Expression<PhraseData>) -> Unit) {
    fun apply(scope: TextOnlyScope<in Lang, *>, data: Expression<PhraseData>) {
        TextOnlyScope<Lang, Unit>().apply {
            phraseBody(data)
        }.also { scope.addAll(it.children) }
    }
}

class ParagraphPhrase<Lang : LanguageSupport, PhraseData>(private val phraseBody: ParagraphScope<Lang, Unit>.(arg: Expression<PhraseData>) -> Unit) {
    fun apply(scope: ParagraphScope<in Lang, *>, data: Expression<PhraseData>) {
        ParagraphScope<Lang, Unit>().apply {
            phraseBody(data)
        }.also { scope.addAll(it.children) }
    }
}

class OutlinePhrase<Lang : LanguageSupport, PhraseData>(private val phraseBody: OutlineScope<Lang, Unit>.(arg: Expression<PhraseData>) -> Unit) {
    fun apply(scope: OutlineScope<in Lang, *>, data: Expression<PhraseData>) {
        OutlineScope<Lang, Unit>().apply {
            phraseBody(data)
        }.also { scope.addAll(it.children) }
    }
}
