package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LocalizedFormatter
import no.nav.pensjon.brev.template.StableHash
import no.nav.pensjon.brev.template.StringExpression
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format

@JvmName("quotedStr")
fun String.quoted(): StringExpression = expr().quoted()

@JvmName("quotedExpr")
fun StringExpression.quoted(): StringExpression = format(QuotedText)
fun quoted(str: String): StringExpression = str.quoted()
fun quoted(str: StringExpression): StringExpression = str.quoted()

object QuotedText : LocalizedFormatter<String>(), StableHash by StableHash.of("LocalizedFormatter.QuotedText") {
    override fun apply(first: String, second: Language): String =
        QuotationMarks.start(second) + first + QuotationMarks.end(second)
}

object QuotationMarks {
    fun start(language: Language) = when (language) {
        Language.Bokmal -> "«"
        Language.Nynorsk -> "«"
        Language.English -> "'"
    }

    fun end(language: Language) = when (language) {
        Language.Bokmal -> "»"
        Language.Nynorsk -> "»"
        Language.English -> "'"
    }
}