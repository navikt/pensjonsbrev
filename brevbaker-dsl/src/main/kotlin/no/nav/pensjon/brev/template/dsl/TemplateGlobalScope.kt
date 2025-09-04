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
        no.nav.pensjon.brev.template.dsl.bokmal(block)

    fun nynorsk(block: LiteralOrExpressionBuilder.() -> LiteralOrExpression): Pair<Language.Nynorsk, LiteralOrExpression> =
        no.nav.pensjon.brev.template.dsl.nynorsk(block)

    fun english(block: LiteralOrExpressionBuilder.() -> LiteralOrExpression): Pair<Language.English, LiteralOrExpression> =
        no.nav.pensjon.brev.template.dsl.english(block)
}