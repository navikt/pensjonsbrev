package no.nav.pensjon.brev.template

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import no.nav.pensjon.brev.something.Fagdelen
import kotlin.reflect.KClass

interface ParameterType<out T>


sealed class Parameter(@JsonIgnore val dataType: KClass<*>) {
    val name: String = this::class.java.name
    val schema: String = "TODO: add schema description"

    companion object {
        @JsonCreator
        @JvmStatic
        fun creator(name: String): Parameter? =
            Parameter::class.findSealedObjects().firstOrNull { it.name == name }
    }

}

// TODO: Finn en fornuftig måte å skille ut pensjon-spesifikke parametre fra template-pakken.

sealed class NumberParameter : Parameter(Number::class), ParameterType<Number>
sealed class BooleanParameter : Parameter(Boolean::class), ParameterType<Boolean>
sealed class StringParameter : Parameter(String::class), ParameterType<String>
sealed class EnumParameter<T : Enum<T>>(enum: KClass<T>) : Parameter(enum), ParameterType<T>
sealed class ComplexParameter<out T: Any>(dataType: KClass<T>) : Parameter(dataType), ParameterType<T>

object SaksNr : NumberParameter()
object EtHelTall : Parameter(Int::class), ParameterType<Int>
object PensjonInnvilget : BooleanParameter()
object KortNavn : StringParameter()
object Sak : EnumParameter<Fagdelen.SakTypeKode>(Fagdelen.SakTypeKode::class)
object ReturAdresse : ComplexParameter<Fagdelen.ReturAdresse>(Fagdelen.ReturAdresse::class)
object Penger : ComplexParameter<Int>(Int::class)

object FornavnMottaker : StringParameter()
object EtternavnMottaker : StringParameter()
object GatenavnMottaker : StringParameter()
object HusnummerMottaker : ComplexParameter<Int>(Int::class)
object PostnummerMottaker : ComplexParameter<Int>(Int::class)
object PoststedMottaker : StringParameter()
object NorskIdentifikator : NumberParameter()
object LetterTitle : StringParameter()