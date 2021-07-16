package no.nav.pensjon.brev.template

// TODO: Fjern
//sealed class ArgumentTransformer<Param : ParameterType<In>, In, out Out> {
//    val name: String = this::class.java.name
//
//    companion object {
//        @JsonCreator
//        @JvmStatic
//        fun creator(name: String): ArgumentTransformer<*, *, *>? =
//            ArgumentTransformer::class.findSealedObjects().firstOrNull { it.name == name }
//    }
//
//    abstract fun value(input: In): Out
//    abstract fun unsafe(input: Any): Out
//
//    sealed class ToString<Param : ParameterType<In>, In> : ArgumentTransformer<Param, In, String>() {
//
//        companion object {
//            @JsonCreator
//            @JvmStatic
//            fun creator(name: String): ToString<*, *>? =
//                ToString::class.findSealedObjects().firstOrNull { it.name == name }
//        }
//
//        object Number : ToString<NumberParameter, kotlin.Number>() {
//            override fun value(input: kotlin.Number): String = input.toString()
//
//            override fun unsafe(input: Any): String {
//                if (input is kotlin.Number) {
//                    return input.toString()
//                }
//                throw IllegalArgumentException("Cannot transform input of type: ${input::class.java.name}")
//            }
//        }
//
//        object Boolean : ToString<BooleanParameter, kotlin.Boolean>() {
//            override fun value(input: kotlin.Boolean): String = input.toString()
//
//            override fun unsafe(input: Any): String {
//                if (input is kotlin.Boolean) {
//                    return input.toString()
//                }
//                throw IllegalArgumentException("Cannot transform input of type: ${input::class.java.name}")
//            }
//
//        }
//
//        object SaksNr : ToString<no.nav.pensjon.brev.latex.model.SaksNr, kotlin.Number>() {
//            override fun value(input: kotlin.Number): String {
//                TODO("Not yet implemented")
//            }
//
//            override fun unsafe(input: Any): String {
//                TODO("Not yet implemented")
//            }
//        }
//
//    }
//
//}

abstract class Operation {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

sealed class UnaryOperation<In, out Out> : Operation() {
    abstract fun apply(input: In): Out

    object NumberToString : UnaryOperation<Number, String>() {
        override fun apply(input: Number): String = input.toString()
    }

    class ToString<T : Any> : UnaryOperation<T, String>() {
        override fun apply(input: T): String = input.toString()
    }
}

sealed class BinaryOperation<in In1, in In2, out Out> : Operation() {


    abstract fun apply(first: In1, second: In2): Out

    class Equal<In : Comparable<In>> : BinaryOperation<In, In, Boolean>() {
        override fun apply(first: In, second: In): Boolean = first == second
    }

    class GreaterThan<In : Comparable<In>> : BinaryOperation<In, In, Boolean>() {
        override fun apply(first: In, second: In): Boolean = first > second
    }

    class GreaterThanOrEqual<In : Comparable<In>> : BinaryOperation<In, In, Boolean>() {
        override fun apply(first: In, second: In): Boolean = first >= second
    }

    class LesserThanOr<In : Comparable<In>> : BinaryOperation<In, In, Boolean>() {
        override fun apply(first: In, second: In): Boolean = first < second
    }

    class LesserThanOrEqual<In : Comparable<In>> : BinaryOperation<In, In, Boolean>() {
        override fun apply(first: In, second: In): Boolean = first <= second
    }

}