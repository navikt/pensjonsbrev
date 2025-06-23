package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LocalizedFormatter
import no.nav.pensjon.brev.template.StableHash
import no.nav.pensjon.brev.template.StringExpression
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format

fun String.quoted(): StringExpression = expr().quoted()
fun StringExpression.quoted(): StringExpression = format(QuotedText)
fun quoted(str: String): StringExpression = str.quoted()
fun quoted(str: StringExpression): StringExpression = str.quoted()

object QuotedText : LocalizedFormatter<String>(), StableHash by StableHash.of("LocalizedFormatter.QuotedText") {
    override fun apply(first: String, second: Language): String =
        when (second) {
            Language.Bokmal, Language.Nynorsk -> "«$first»"
            Language.English -> "'$first'"
        }
}