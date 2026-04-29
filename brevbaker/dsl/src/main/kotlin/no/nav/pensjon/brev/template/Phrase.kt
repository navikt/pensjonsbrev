package no.nav.pensjon.brev.template

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

abstract class ParagraphPhrase<Lang : LanguageSupport> : AbstractParagraphPhrase<Lang>()

abstract class RedigerbarParagraphPhrase<Lang : LanguageSupport> : AbstractParagraphPhrase<Lang>(), RedigerbarFrase

sealed class AbstractParagraphPhrase<Lang : LanguageSupport> {
    abstract fun ParagraphOnlyScope<Lang, Unit>.template()
    fun apply(scope: ParagraphOnlyScope<in Lang, *>) {
        ParagraphOnlyScope<Lang, Unit>().apply { template() }.elements
            .forEach { scope.addParagraphContent(it) }
    }
}

abstract class OutlinePhrase<Lang : LanguageSupport> : AbstractOutlinePhrase<Lang>()

abstract class RedigerbarOutlinePhrase<Lang : LanguageSupport> : AbstractOutlinePhrase<Lang>(), RedigerbarFrase {
    fun ParagraphOnlyScope<Lang, *>.includePhrase(phrase: RedigerbarParagraphPhrase<Lang>) {
        phrase.apply(this)
    }
}

sealed class AbstractOutlinePhrase<Lang : LanguageSupport> {
    abstract fun OutlineOnlyScope<Lang, Unit>.template()
    fun apply(scope: OutlineOnlyScope<in Lang, *>) {
        OutlineOnlyScope<Lang, Unit>().apply { template() }.elements
            .forEach { scope.addOutlineContent(it) }
    }
}

sealed interface RedigerbarFrase {
    fun TemplateGlobalScope<*>.fritekst(beskrivelse: String): Fritekst = beskrivelse.takeIf { it.trim().isNotEmpty() }
        ?.let { Fritekst(it) }
        ?: throw IllegalArgumentException("Fritekstfelt må ha initiell tekst for at vi ikke skal lure bruker.")

    fun TemplateGlobalScope<*>.redigerbarData(variabel: StringExpression): RedigerbarData = RedigerbarData(variabel)
}