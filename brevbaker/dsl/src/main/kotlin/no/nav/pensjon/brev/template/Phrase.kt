package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.template.dsl.*

abstract class TextOnlyPhrase<Lang : LanguageSupport> {
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

abstract class PlainTextOnlyPhrase<Lang : LanguageSupport> {

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

abstract class ParagraphPhrase<Lang : LanguageSupport> {
    abstract fun ParagraphOnlyScope<Lang, Unit>.template()
    fun apply(scope: ParagraphOnlyScope<in Lang, *>) {
        ParagraphOnlyScope<Lang, Unit>().apply { template() }.elements
            .forEach { scope.addParagraphContent(it) }
    }
}

abstract class OutlinePhrase<Lang : LanguageSupport> {
    abstract fun OutlineOnlyScope<Lang, Unit>.template()
    fun apply(scope: OutlineScope<in Lang, *>) {
        OutlineOnlyScope<Lang, Unit>().apply { template() }.elements
            .forEach { scope.addOutlineContent(it) }
    }
}

interface RedigerbarPhrase<T : RedigerbarTemplate<*>> {
    fun TemplateGlobalScope<*>.fritekst(beskrivelse: String): Fritekst = beskrivelse.takeIf { it.trim().isNotEmpty() }
        ?.let { Fritekst(it) }
        ?: throw IllegalArgumentException("Fritekstfelt må ha initiell tekst for at vi ikke skal lure bruker.")


    fun TemplateGlobalScope<*>.redigerbarData(variabel: StringExpression): RedigerbarData = RedigerbarData(variabel)
}