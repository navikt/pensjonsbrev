package no.nav.pensjon.brev.template.dsl.expression

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.api.model.FeatureToggle
import no.nav.pensjon.brevbaker.api.model.Kroner

fun <T> Expression<Collection<T>>.isEmpty(): Expression<Boolean> =
    ExpressionImpl.UnaryInvokeImpl(
        value = this,
        operation = UnaryOperation.IsEmpty,
    )

fun <T> Expression<Collection<T>>.size(): Expression<Int> =
    ExpressionImpl.UnaryInvokeImpl(value = this, operation = UnaryOperation.SizeOf)

fun Expression<Int>.absoluteValue(): Expression<Int> =
    ExpressionImpl.UnaryInvokeImpl(value = this, operation = UnaryOperation.AbsoluteValue)

fun Expression<FeatureToggle>.enabled(): Expression<Boolean> =
    ExpressionImpl.UnaryInvokeImpl(value = this, operation = UnaryOperation.FunksjonsbryterEnabled)

@JvmName("absoluteValueKroner")
fun Expression<Kroner>.absoluteValue(): Expression<Kroner> =
    ExpressionImpl.UnaryInvokeImpl(value = this, operation = UnaryOperation.AbsoluteValueKroner)

fun <T> Expression<Collection<T>>.isNotEmpty(): Expression<Boolean> =
    not(this.isEmpty())

fun <T : Any, R> Expression<Collection<T>>.map(selector: TemplateModelSelector<T, R>): Expression<Collection<R>> =
    map(UnaryOperation.Select(selector))

fun <T, R> Expression<Collection<T>>.map(mapper: UnaryOperation<T, R>): Expression<Collection<R>> =
    ExpressionImpl.UnaryInvokeImpl(
        value = this,
        operation = UnaryOperation.MapCollection(mapper),
    )

fun <In1, In2, Out> Expression<Collection<In1>>.map(
    mapper: BinaryOperation<In1, In2, Out>,
    second: Expression<In2>
): Expression<Collection<Out>> =
    ExpressionImpl.BinaryInvokeImpl(
        first = this,
        second = second,
        operation = BinaryOperation.MapCollection(mapper)
    )

fun <In1, In2, Out> Expression<Collection<In2>>.map(
    first: Expression<In1>,
    mapper: BinaryOperation<In1, In2, Out>
): Expression<Collection<Out>> =
    ExpressionImpl.BinaryInvokeImpl(
        first = this,
        second = first,
        operation = BinaryOperation.MapCollection(BinaryOperation.Flip(mapper))
    )

fun <In> Expression<Collection<In>>.map(mapper: BinaryOperation<In, Language, String>): Expression<Collection<String>> =
    map(mapper, Expression.FromScope.Language)

fun Expression<Collection<String>>.format(): StringExpression = format(formatter = LocalizedFormatter.CollectionFormat)

