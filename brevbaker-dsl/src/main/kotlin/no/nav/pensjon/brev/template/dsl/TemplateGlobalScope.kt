package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.LiteralOrExpressionBuilder.LiteralOrExpression
import no.nav.pensjon.brevbaker.api.model.Felles

interface TemplateGlobalScope<LetterData : Any> {
    val argument: Expression<LetterData>
        get() = Expression.FromScope.Argument()

    val felles: Expression<Felles>
        get() = Expression.FromScope.Felles

    fun bokmal(block: LiteralOrExpressionBuilder.() -> LiteralOrExpression): Pair<Language.Bokmal, LiteralOrExpression> =
        Language.Bokmal to LiteralOrExpressionBuilder().block()

    fun nynorsk(block: LiteralOrExpressionBuilder.() -> LiteralOrExpression): Pair<Language.Nynorsk, LiteralOrExpression> =
        Language.Nynorsk to LiteralOrExpressionBuilder().block()

    fun english(block: LiteralOrExpressionBuilder.() -> LiteralOrExpression): Pair<Language.English, LiteralOrExpression> =
        Language.English to LiteralOrExpressionBuilder().block()
}