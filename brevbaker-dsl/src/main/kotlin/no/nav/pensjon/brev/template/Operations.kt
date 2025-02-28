package no.nav.pensjon.brev.template

import no.nav.pensjon.brevbaker.api.model.Bruker
import no.nav.pensjon.brev.api.model.FeatureToggle
import no.nav.pensjon.brev.template.expression.ExpressionMapper
import no.nav.pensjon.brevbaker.api.model.Kroner

interface Operation : StableHash

sealed interface UnaryOperation<In, out Out> : Operation {
    fun apply(input: In): Out

    interface AbsoluteValue : UnaryOperation<Int, Int>

    interface AbsoluteValueKroner : UnaryOperation<Kroner, Kroner>

    interface BrukerFulltNavn : UnaryOperation<Bruker, String>

    interface FunksjonsbryterEnabled : UnaryOperation<FeatureToggle, Boolean>

    interface IsEmpty : UnaryOperation<Collection<*>, Boolean>

    interface MapValue<In, Out> : UnaryOperation<In, Out> {
        val mapper: ExpressionMapper<In, Out>
    }

    interface MapCollection<In, Out> : UnaryOperation<Collection<In>, Collection<Out>> {
        val mapper: UnaryOperation<In, Out>
    }

    interface Not : UnaryOperation<Boolean, Boolean>

    interface SafeCall<In : Any, Out> : UnaryOperation<In?, Out?> {
        val selector: TemplateModelSelector<In, Out>
    }

    interface Select<In : Any, Out> : UnaryOperation<In, Out> {
        val selector: TemplateModelSelector<In, Out>
    }

    interface SizeOf : UnaryOperation<Collection<*>, Int>

    interface ToString : UnaryOperation<Any, String>
}

interface BinaryOperation<in In1, in In2, out Out> : Operation {
    val doc: Documentation?
        get() = null

    interface Documentation {
        val name: String
        val syntax: Notation
        enum class Notation { PREFIX, INFIX, POSTFIX, FUNCTION }
    }

    fun apply(first: In1, second: In2): Out

    interface Equal<In> : BinaryOperation<In, In, Boolean>

    interface GreaterThan<in T : Comparable<T>> : BinaryOperation<T, T, Boolean>

    interface GreaterThanOrEqual<in T : Comparable<T>> : BinaryOperation<T, T, Boolean>

    interface LessThanOrEqual<in T : Comparable<T>> : BinaryOperation<T, T, Boolean>

    interface LessThan<in T : Comparable<T>> : BinaryOperation<T, T, Boolean>

    interface Or : BinaryOperation<Boolean, Boolean, Boolean>

    interface And : BinaryOperation<Boolean, Boolean, Boolean>

    interface Concat : BinaryOperation<String, String, String>

    interface IntMinus : BinaryOperation<Int, Int, Int>

    interface IntPlus : BinaryOperation<Int, Int, Int>

    interface IfNull<T : Any> : BinaryOperation<T?, T, T>

    interface MapCollection<In1, In2, Out> : BinaryOperation<Collection<In1>, In2, Collection<Out>> {
        val mapper: BinaryOperation<In1, In2, Out>
    }

    interface EnumInList<EnumType : Enum<*>> : BinaryOperation<EnumType, List<EnumType>, Boolean>

    interface GetElementOrNull<ListType> : BinaryOperation<List<ListType>?, Int, ListType?>

    interface IfElse<Out> : BinaryOperation<Boolean, Pair<Out, Out>, Out>

    interface Tuple<Out> : BinaryOperation<Out, Out, Pair<Out, Out>>

    interface Flip<In1, In2, Out> : BinaryOperation<In1, In2, Out> {
        val operation: BinaryOperation<In2, In1, Out>
    }

    interface SafeCall<In1, In2, Out> : BinaryOperation<In1, In2, Out?> {
        val operation: BinaryOperation<In1 & Any, In2 & Any, Out>
    }
}