package no.nav.pensjon.brev.template.dsl.expression

import no.nav.pensjon.brev.template.*

fun <T> Expression<T>.format(formatter: LocalizedFormatter<T>): StringExpression =
    Expression.BinaryInvoke(
        first = this,
        second = Expression.FromScope.Language,
        operation = formatter,
    )

