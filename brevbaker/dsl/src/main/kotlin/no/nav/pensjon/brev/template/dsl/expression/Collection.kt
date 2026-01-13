package no.nav.pensjon.brev.template.dsl.expression

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.api.model.FeatureToggle
import no.nav.pensjon.brevbaker.api.model.Kroner

fun <T> Expression<Collection<T>>.isEmpty(): Expression<Boolean> =
    UnaryOperation.IsEmpty(this)

fun <T> Expression<Collection<T>>.size(): Expression<Int> =
    UnaryOperation.SizeOf(this)

fun Expression<Int>.absoluteValue(): Expression<Int> =
    UnaryOperation.AbsoluteValue(this)

fun Expression<FeatureToggle>.enabled(): Expression<Boolean> =
    UnaryOperation.FunksjonsbryterEnabled(this)

@JvmName("absoluteValueKroner")
fun Expression<Kroner>.absoluteValue(): Expression<Kroner> =
    UnaryOperation.AbsoluteValueKroner(this)

fun <T> Expression<Collection<T>>.isNotEmpty(): Expression<Boolean> =
    not(this.isEmpty())

fun <T : Any, R> Expression<Collection<T>>.map(selector: TemplateModelSelector<T, R>): Expression<Collection<R>> =
    map(UnaryOperation.Select(selector))

fun <T, R> Expression<Collection<T>>.map(mapper: UnaryOperation<T, R>): Expression<Collection<R>> =
    UnaryOperation.MapCollection(mapper).invoke(this)

fun <In1, In2, Out> Expression<Collection<In1>>.map(
    mapper: BinaryOperation<In1, In2, Out>,
    second: Expression<In2>
): Expression<Collection<Out>> =
    BinaryOperation.MapCollection(mapper).invoke(this, second)

fun <In1, In2, Out> Expression<Collection<In2>>.map(
    first: Expression<In1>,
    mapper: BinaryOperation<In1, In2, Out>
): Expression<Collection<Out>> =
    BinaryOperation.MapCollection(BinaryOperation.Flip(mapper)).invoke(this, first)

fun <In> Expression<Collection<In>>.map(mapper: BinaryOperation<In, Language, String>): Expression<Collection<String>> =
    map(mapper, Expression.FromScope.Language)

@JvmName("formatCollection")
fun Expression<Collection<String>>.format(): StringExpression = format(formatter = LocalizedFormatter.CollectionFormat)
@JvmName("formatCollectionNullable")
fun Expression<Collection<String>?>.format(): Expression<String?> = format(formatter = LocalizedFormatter.CollectionFormat)

