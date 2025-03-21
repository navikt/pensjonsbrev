package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.template.dsl.*

abstract class TextOnlyPhrase<Lang : LanguageSupport> {
    abstract fun TextOnlyScope<Lang, Unit>.template()
    fun apply(scope: TextOnlyScope<in Lang, *>) {
        TextOnlyScope<Lang, Unit>().apply { template() }.elements
            .forEach { scope.addTextContent(it) }
    }

    fun applyToParagraphScope(scope: ParagraphScope<in Lang, *>) {
        TextOnlyScope<Lang, Unit>().apply { template() }.elements
            .forEach { scope.addTextContent(it) }
    }
}

abstract class PlainTextOnlyPhrase<Lang : LanguageSupport> {

    abstract fun PlainTextOnlyScope<Lang, Unit>.template()

    fun apply(scope: PlainTextOnlyScope<in Lang, *>) {
        applyPlainTextScope().forEach { scope.addTextContent(it) }
    }

    fun applyToTextScope(scope: TextOnlyScope<in Lang, *>) {
        applyPlainTextScope().forEach { scope.addTextContent(it) }
    }

    fun applyToParagraphScope(scope: ParagraphOnlyScope<in Lang, *>) {
        applyPlainTextScope().forEach { scope.addTextContent(it) }
    }

    private fun applyPlainTextScope() = PlainTextOnlyScope<Lang, Unit>().apply { template() }.elements
}

abstract class ParagraphPhrase<Lang : LanguageSupport> {
    abstract fun ParagraphOnlyScope<Lang, Unit>.template()
    fun apply(scope: ParagraphOnlyScope<in Lang, *>) {
        ParagraphOnlyScope<Lang, Unit>().apply { template() }.elements
            .forEach { scope.addParagraphContent(it) }
    }
}

abstract class OutlinePhrase<Lang : LanguageSupport> {
    abstract fun OutlineOnlyScope<Lang, Unit>.template()
    fun apply(scope: OutlineOnlyScope<in Lang, *>) {
        OutlineOnlyScope<Lang, Unit>().apply { template() }.elements
            .forEach { scope.addOutlineContent(it) }
    }
}
