package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.StringExpression
import no.nav.pensjon.brev.template.dsl.LiteralOrExpressionBuilder.LiteralOrExpression
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus

fun bokmal(block: LiteralOrExpressionBuilder.() -> LiteralOrExpression): Pair<Language.Bokmal, LiteralOrExpression> =
    Language.Bokmal to LiteralOrExpressionBuilder().block()

fun nynorsk(block: LiteralOrExpressionBuilder.() -> LiteralOrExpression): Pair<Language.Nynorsk, LiteralOrExpression> =
    Language.Nynorsk to LiteralOrExpressionBuilder().block()

fun english(block: LiteralOrExpressionBuilder.() -> LiteralOrExpression): Pair<Language.English, LiteralOrExpression> =
    Language.English to LiteralOrExpressionBuilder().block()

class LiteralOrExpressionBuilder {
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
}