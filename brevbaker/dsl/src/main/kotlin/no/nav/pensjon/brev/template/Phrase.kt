package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.template.dsl.*


abstract class TextOnlyPhrase<Lang : LanguageSupport> : AbstractTextOnlyPhrase<Lang>()

sealed class AbstractTextOnlyPhrase<Lang : LanguageSupport> {
    abstract fun TextOnlyScope<Lang, Unit>.template()
    fun apply(scope: TextOnlyScope<in Lang, *>) {
        TextOnlyScope<Lang, Unit>().apply { template() }.elements
            .forEach { scope.addTextContent(it) }
    }

    fun apply(scope: ParagraphScope<in Lang, *>) {
        TextOnlyScope<Lang, Unit>().apply { template() }.elements
            .forEach { scope.addTextContent(it) }
    }
}

abstract class PlainTextOnlyPhrase<Lang : LanguageSupport> : AbstractPlainTextOnlyPhrase<Lang>()

sealed class AbstractPlainTextOnlyPhrase<Lang : LanguageSupport> {

    abstract fun PlainTextOnlyScope<Lang, Unit>.template()

    fun apply(scope: PlainTextOnlyScope<in Lang, *>) {
        applyPlainTextScope().forEach { scope.addTextContent(it) }
    }

    fun apply(scope: TextOnlyScope<in Lang, *>) {
        applyPlainTextScope().forEach { scope.addTextContent(it) }
    }

    fun apply(scope: ParagraphOnlyScope<in Lang, *>) {
        applyPlainTextScope().forEach { scope.addTextContent(it) }
    }

    private fun applyPlainTextScope() = PlainTextOnlyScope<Lang, Unit>().apply { template() }.elements
}

abstract class ParagraphPhrase<Lang : LanguageSupport> : AbstractParagraphPhrase<Lang, Unit>()

abstract class RedigerbarParagraphPhrase<Lang : LanguageSupport> :
    AbstractParagraphPhrase<Lang, RedigerbarPhraseBrevdata>(), DslExtensionForRedigerbareBrev

sealed class AbstractParagraphPhrase<Lang : LanguageSupport, LetterData : Any> {
    abstract fun ParagraphOnlyScope<Lang, LetterData>.template()
    fun apply(scope: ParagraphOnlyScope<in Lang, *>) {
        ParagraphOnlyScope<Lang, LetterData>().apply { template() }.elements
            .forEach { scope.addParagraphContent(it) }
    }
}

abstract class OutlinePhrase<Lang : LanguageSupport> : AbstractOutlinePhrase<Lang, Unit>()

abstract class RedigerbarOutlinePhrase<Lang : LanguageSupport> :
    AbstractOutlinePhrase<Lang, RedigerbarPhraseBrevdata>(), DslExtensionForRedigerbareBrev

sealed class AbstractOutlinePhrase<Lang : LanguageSupport, LetterData : Any> {
    abstract fun OutlineOnlyScope<Lang, LetterData>.template()
    fun apply(scope: OutlineOnlyScope<in Lang, *>) {
        OutlineOnlyScope<Lang, LetterData>(scope.validator.subScope()).apply { template() }.elements
            .forEach { scope.addOutlineContent(it) }
    }
}

interface RedigerbarPhraseBrevdata : RedigerbarBrevdata<SaksbehandlervalgIDSL, EmptyFagsystemdata>