package no.nav.pensjon.brev.template.dsl.expression

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.expression.*
import no.nav.pensjon.brevbaker.api.model.Kroner

fun <T> Expression<Collection<T>>.containsAll(vararg items: T): Expression<Boolean> =
    Expression.BinaryInvoke(
        first = Collections.ContainsAll(items.toSet()).expr(),
        second = this,
        operation = BinaryOperation.ValidatePredicate(),
    )

fun <T> Expression<Collection<T>>.isEmpty(): Expression<Boolean> =
    Expression.BinaryInvoke(
        first = Collections.IsEmpty.expr(),
        second = this,
        operation = BinaryOperation.ValidatePredicate(),
    )

fun <T> Expression<Collection<T>>.size(): Expression<Int> =
    Expression.UnaryInvoke(value = this, operation = UnaryOperation.SizeOf)

fun Expression<Int>.absoluteValue(): Expression<Int> =
    Expression.UnaryInvoke(value = this, operation = UnaryOperation.AbsoluteValue)

@JvmName("absoluteValueKroner")
fun Expression<Kroner>.absoluteValue(): Expression<Kroner> =
    Expression.UnaryInvoke(value = this, operation = UnaryOperation.AbsoluteValueKroner)

fun <T> Expression<Collection<T>>.isNotEmpty(): Expression<Boolean> =
    not(this.isEmpty())

fun <T : Any, R> Expression<Collection<T>>.map(selector: TemplateModelSelector<T, R>): Expression<Collection<R>> =
    map(UnaryOperation.Select(selector))

fun <T, R> Expression<Collection<T>>.map(mapper: UnaryOperation<T, R>): Expression<Collection<R>> =
    Expression.UnaryInvoke(
        value = this,
        operation = UnaryOperation.MapCollection(mapper),
    )

fun <In1, In2, Out> Expression<Collection<In1>>.map(
    mapper: BinaryOperation<In1, In2, Out>,
    second: Expression<In2>
): Expression<Collection<Out>> =
    Expression.BinaryInvoke(
        first = this,
        second = second,
        operation = BinaryOperation.MapCollection(mapper)
    )

fun <In1, In2, Out> Expression<Collection<In2>>.map(
    first: Expression<In1>,
    mapper: BinaryOperation<In1, In2, Out>
): Expression<Collection<Out>> =
    Expression.BinaryInvoke(
        first = this,
        second = first,
        operation = BinaryOperation.MapCollection(BinaryOperation.Flip(mapper))
    )

fun <In> Expression<Collection<In>>.map(mapper: BinaryOperation<In, Language, String>): Expression<Collection<String>> =
    map(mapper, Expression.FromScope.Language)

fun Expression<Collection<String>>.format(): StringExpression = format(formatter = BinaryOperation.LocalizedCollectionFormat)

/**
 * Collection contains at least one of the listed items.
 */
fun <T> Expression<Collection<T>>.containsAny(vararg items: T): Expression<Boolean> =
    Expression.BinaryInvoke(
        first = Collections.ContainsAny(items.toSet()).expr(),
        second = this,
        operation = BinaryOperation.ValidatePredicate(),
    )

/**
 * Collection exclusively contains the listed items.
 */
fun <T> Expression<Collection<T>>.containsExclusively(body: Collections.ContainsExclusively.Builder<T>.() -> Unit): Expression<Boolean> =
    Expression.BinaryInvoke(
        first = Collections.ContainsExclusively.Builder<T>().apply(body).build().expr(),
        second = this,
        operation = BinaryOperation.ValidatePredicate(),
    )
