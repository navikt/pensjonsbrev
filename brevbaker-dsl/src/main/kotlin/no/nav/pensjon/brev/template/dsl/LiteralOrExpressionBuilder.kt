package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.StringExpression
import no.nav.pensjon.brev.template.dsl.LiteralOrExpressionBuilder.LiteralOrExpression
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus

fun bokmal(block: LiteralOrExpressionBuilder.() -> LiteralOrExpression): Pair<Language.Bokmal, LiteralOrExpression> =
    Language.Bokmal to LiteralOrExpressionBuilder(Quotation.BokmalNynorsk).block()

fun nynorsk(block: LiteralOrExpressionBuilder.() -> LiteralOrExpression): Pair<Language.Nynorsk, LiteralOrExpression> =
    Language.Nynorsk to LiteralOrExpressionBuilder(Quotation.BokmalNynorsk).block()

fun english(block: LiteralOrExpressionBuilder.() -> LiteralOrExpression): Pair<Language.English, LiteralOrExpression> =
    Language.English to LiteralOrExpressionBuilder(Quotation.English).block()

interface Quotation {
    val start: String
    val end: String

    object BokmalNynorsk : Quotation {
        override val start = "«"
        override val end = "»"
    }
    object English : Quotation {
        override val start = "'"
        override val end = "'"
    }
}

class LiteralOrExpressionBuilder(private val quotation: Quotation) {
    // brukes for å bruke unary plus som plus. Kan skje om plus er på ny linje.
    private var previous: LiteralOrExpression? = null
    sealed class LiteralOrExpression() {
        abstract val expr: StringExpression
    }
    class LiteralWrapper(val str: String) : LiteralOrExpression() {
        override val expr: StringExpression
            get() = str.expr()
    }

    class ExpressionWrapper(override val expr: StringExpression) : LiteralOrExpression()

    operator fun StringExpression.unaryPlus() = previous?.let { it + this } ?: ExpressionWrapper(this).also { previous = it }

    operator fun String.unaryPlus() = previous?.let { it + this } ?: LiteralWrapper(this).also { previous = it }

    operator fun LiteralOrExpression.plus(other: StringExpression) = when(this) {
        is ExpressionWrapper -> ExpressionWrapper(expr + other)
        is LiteralWrapper -> ExpressionWrapper(str.expr() + other)
    }.also { previous = it }

    operator fun LiteralOrExpression.plus(other: String) = when(this) {
        is ExpressionWrapper -> ExpressionWrapper(expr + other)
        is LiteralWrapper -> LiteralWrapper(str + other)
    }.also { previous = it }

    @JvmName("quotedStr")
    fun String.quoted(): String = quotation.start + this + quotation.end

    @JvmName("quotedExpr")
    fun StringExpression.quoted(): StringExpression = quotation.start.expr() + this + quotation.end.expr()
    fun quoted(str: String): String = str.quoted()
    fun quoted(str: StringExpression): StringExpression = str.quoted()
}