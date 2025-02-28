package no.nav.pensjon.brev.template

import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brev.api.model.FeatureToggle
import no.nav.pensjon.brev.api.model.FeatureToggleSingleton
import no.nav.pensjon.brev.template.expression.ExpressionMapper
import no.nav.pensjon.brev.template.render.fulltNavn
import no.nav.pensjon.brevbaker.api.model.Bruker
import no.nav.pensjon.brevbaker.api.model.Kroner
import kotlin.math.absoluteValue

abstract class AbstractOperation : Operation {
    // Since most operations don't have fields, and hence can't be data classes,
    // we override equals+hashCode to compare by class.
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }

    override fun hashCode(): Int = stableHashCode()
    override fun toString(): String = "${this::class.simpleName}"
}

@InterneDataklasser
sealed class UnaryOperationImpl<In, out Out> : UnaryOperation<In, Out>, AbstractOperation() {

    @InterneDataklasser
    object AbsoluteValueImpl : UnaryOperation.AbsoluteValue, StableHash by StableHash.of("UnaryOperation.AbsoluteValue") {
        override fun apply(input: Int): Int = input.absoluteValue
    }

    @InterneDataklasser
    object AbsoluteValueKronerImpl : UnaryOperation.AbsoluteValueKroner, StableHash by StableHash.of("UnaryOperation.AbsoluteValueKroner") {
        override fun apply(input: Kroner): Kroner = Kroner(input.value.absoluteValue)
    }

    @InterneDataklasser
    object BrukerFulltNavnImpl : UnaryOperation.BrukerFulltNavn, AbstractOperation(), StableHash by StableHash.of("UnaryOperation.BrukerFulltNavn") {
        override fun apply(input: Bruker): String = input.fulltNavn()
    }

    @InterneDataklasser
    object FunksjonsbryterEnabledImpl : UnaryOperation.FunksjonsbryterEnabled, AbstractOperation(), StableHash by StableHash.of("UnaryOperation.Enabled") {
        override fun apply(input: FeatureToggle): Boolean = FeatureToggleSingleton.isEnabled(input)
    }

    @InterneDataklasser
    object IsEmptyImpl : UnaryOperation.IsEmpty, AbstractOperation(), StableHash by StableHash.of("UnaryOperation.IsEmpty") {
        override fun apply(input: Collection<*>): Boolean = input.isEmpty()
    }

    @InterneDataklasser
    data class MapValueImpl<In, Out>(override val mapper: ExpressionMapper<In, Out>) : UnaryOperation.MapValue<In, Out>, AbstractOperation(), StableHash {
        override fun apply(input: In): Out = mapper.apply(input)
        override fun stableHashCode(): Int = StableHash.of(StableHash.of("UnaryOperation.MapValue"), mapper).stableHashCode()
    }

    @InterneDataklasser
    data class MapCollectionImpl<In, Out>(override val mapper: UnaryOperation<In, Out>) : UnaryOperation.MapCollection<In, Out>, AbstractOperation() {
        override fun apply(input: Collection<In>): Collection<Out> = input.map { mapper.apply(it) }
        override fun stableHashCode(): Int = StableHash.of(StableHash.of("UnaryOperation.MapCollection"), mapper).stableHashCode()
    }

    @InterneDataklasser
    object Not : UnaryOperation<Boolean, Boolean>, AbstractOperation(), StableHash by StableHash.of("UnaryOperation.Not") {
        override fun apply(input: Boolean): Boolean = input.not()
    }

    @InterneDataklasser
    class SafeCall<In : Any, Out>(override val selector: TemplateModelSelector<In, Out>) : UnaryOperation.SafeCall<In, Out>, AbstractOperation() {
        override fun apply(input: In?): Out? = input?.let { selector.selector(it) }
        override fun stableHashCode(): Int = StableHash.of(StableHash.of("UnaryOperation.SafeCall"), selector).stableHashCode()
    }

    @InterneDataklasser
    class Select<In : Any, Out>(override val selector: TemplateModelSelector<In, Out>) : UnaryOperation.Select<In, Out>, AbstractOperation() {
        override fun apply(input: In): Out = selector.selector(input)
        override fun stableHashCode(): Int = StableHash.of(StableHash.of("UnaryOperation.Select"), StableHash.of(selector)).stableHashCode()
    }

    @InterneDataklasser
    object SizeOf : UnaryOperation.SizeOf, AbstractOperation(), StableHash by StableHash.of("UnaryOperation.SizeOf") {
        override fun apply(input: Collection<*>): Int = input.size
    }

    @InterneDataklasser
    object ToString : UnaryOperation.ToString, AbstractOperation(), StableHash by StableHash.of("UnaryOperation.ToString") {
        override fun apply(input: Any): String = input.toString()
    }
}