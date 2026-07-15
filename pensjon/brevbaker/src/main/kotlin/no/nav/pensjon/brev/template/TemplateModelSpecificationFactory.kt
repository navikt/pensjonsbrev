package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyVedleggData
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgVerdi
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Broek
import no.nav.pensjon.brevbaker.api.model.DisplayText
import no.nav.pensjon.brevbaker.api.model.ObjectTypeSpecification
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification.FieldType
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification.FieldType.Scalar.Kind
import java.time.Period
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor

class TemplateModelSpecificationError(msg: String) : Error(msg)

class TemplateModelSpecificationFactory(private val from: KClass<*>) {
    private val toProcess = mutableListOf<KClass<*>>()

    @OptIn(BrevbakerDSLInternal::class)
    fun build(saksbehandlervalg: Map<String, SaksbehandlervalgVerdi<*>>?): TemplateModelSpecification =
        if (from.objectInstance == Unit || from.objectInstance in setOf(EmptyAutobrevdata, EmptyRedigerbarBrevdata, EmptyVedleggData)) {
            TemplateModelSpecification(emptyMap(), null)
        } else if (from.primaryConstructor == null) {
            throw TemplateModelSpecificationError("Cannot create specification of class without primary constructor: ${from.qualifiedName}")
        } else if (!(from.isData || from.isValue)) {
            throw TemplateModelSpecificationError("Cannot create specification from a regular class: must be data or value class")
        } else {
            val objectTypes = mutableMapOf(from.qualifiedName!! to createObjectTypeSpecification(from))

            while (toProcess.isNotEmpty()) {
                val current = toProcess.removeFirst()
                val name = current.qualifiedName!!
                if (current.isSubclassOf(SaksbehandlervalgIDSL::class)) {
                    if (saksbehandlervalg == null) {
                        throw IllegalArgumentException("saksbehandlervalg must be provided when building specification for ${from.qualifiedName}")
                    }
                    objectTypes[name] = saksbehandlervalg.entries.associate { (key, verdi) -> key to verdi.toFieldType() }
                } else if (!objectTypes.containsKey(name)) {
                    objectTypes[name] = createObjectTypeSpecification(current)
                }
            }

            validate(
                TemplateModelSpecification(
                    types = objectTypes,
                    letterModelTypeName = from.qualifiedName,
                )
            )
        }

    @OptIn(BrevbakerDSLInternal::class)
    private fun SaksbehandlervalgVerdi<*>.toFieldType(): FieldType = when (this) {
        is SaksbehandlervalgVerdi.Bool -> FieldType.Scalar(nullable = true, kind = Kind.BOOLEAN, displayText = displayText)
        is SaksbehandlervalgVerdi.Integer -> FieldType.Scalar(nullable = true, kind = Kind.NUMBER, displayText = displayText)
        is SaksbehandlervalgVerdi.Text -> FieldType.Scalar(nullable = true, kind = Kind.STRING, displayText = displayText)
        is SaksbehandlervalgVerdi.Enum<*> -> FieldType.Enum(nullable = true, clazz.java.enumConstants.map { FieldType.EnumEntry(it.name, (it as SaksbehandlerValgEnum).displayText) }.toSet())
    }

    private fun validate(spec: TemplateModelSpecification): TemplateModelSpecification {
        spec.types.forEach { (name, fieldType) -> validateTypes(spec, listOf(name), fieldType) }

        return spec
    }

    private fun validateTypes(spec: TemplateModelSpecification, path: List<String>, type: ObjectTypeSpecification) {
        type.values.forEach {
            if (it is FieldType.Object) {
                if (path.contains(it.typeName)) {
                    throw TemplateModelSpecificationError("Recursive types not supported: ${it.typeName}")
                } else if (!spec.types.containsKey(it.typeName)) {
                    throw TemplateModelSpecificationError("TemplateModelSpecification is incomplete: ${it.typeName} is missing")
                } else {
                    validateTypes(spec, path + it.typeName, spec.types[it.typeName]!!)
                }
            }
        }
    }

    private fun createObjectTypeSpecification(type: KClass<*>): ObjectTypeSpecification =
        type.primaryConstructor?.parameters?.associate { it.name!! to it.type.toFieldType(it.annotations, type.isSubclassOf(SaksbehandlerValgBrevdata::class), it.name!!) }
            ?: emptyMap()

    private fun KType.toFieldType(annotations: List<Annotation>, paakrevDisplayText: Boolean, name: String): FieldType {
        val theClassifier = classifier
        return if (theClassifier is KClass<*>) {
            val displayText = annotations.filterIsInstance<DisplayText>().map { it.text }
            val displayedText = displayText.firstOrNull()
            if (paakrevDisplayText && displayedText == null) {
                throw TemplateModelSpecificationError("Missing required DisplayText annotation on $name")
            }

            when (val qname = theClassifier.qualifiedName) {
                "kotlin.String" -> FieldType.Scalar(isMarkedNullable, Kind.STRING, displayText = displayedText)

                "kotlin.Int", "kotlin.Long" -> FieldType.Scalar(isMarkedNullable, Kind.NUMBER, displayText = displayedText)

                "kotlin.Double", "kotlin.Float" -> FieldType.Scalar(isMarkedNullable, Kind.DOUBLE, displayText = displayedText)

                "kotlin.Boolean" -> FieldType.Scalar(isMarkedNullable, Kind.BOOLEAN, displayText = displayedText)

                "kotlin.collections.List" -> FieldType.Array(isMarkedNullable, arguments.first().type!!.toFieldType(listOf(), false, name))

                "java.time.LocalDate" -> FieldType.Scalar(isMarkedNullable, Kind.DATE, displayText = displayedText)

                "no.nav.pensjon.brev.api.model.maler.EmptyBrevdata", "no.nav.pensjon.brev.api.model.maler.EmptyVedlegg" -> {
                    toProcess.add(theClassifier)
                    FieldType.Object(isMarkedNullable, qname, displayText = displayedText)
                }

                Broek::class.qualifiedName, Period::class.qualifiedName -> {
                    toProcess.add(theClassifier)
                    FieldType.Object(isMarkedNullable, qname!!, displayText = displayedText)
                }

                else -> {
                    if (theClassifier.isValue) {
                        val parameter = theClassifier.primaryConstructor!!.parameters.first()
                        parameter.type.toFieldType(annotations, paakrevDisplayText, parameter.name!!)
                            .takeIf { it is FieldType.Scalar } ?: throw TemplateModelSpecificationError("Expected value class to be scalar, but was not")
                    } else if (theClassifier.isData || theClassifier.java.isInterface) {
                        toProcess.add(theClassifier)
                        FieldType.Object(isMarkedNullable, qname!!, displayText = displayedText)
                    } else if (theClassifier.java.isEnum) {
                        FieldType.Enum(isMarkedNullable, enumVerdier(theClassifier, paakrevDisplayText), displayText = displayedText)
                    } else {
                        throw TemplateModelSpecificationError("Don't know how to handle type: $qname")
                    }
                }
            }
        } else {
            throw TemplateModelSpecificationError("Unable to create FieldType of: $this")
        }
    }

    private fun enumVerdier(theClassifier: KClass<*>, paakrevDisplayText: Boolean) =
        theClassifier.java.fields.map {
            val filterIsInstance = it.annotations.filterIsInstance<DisplayText>()
            val displayText = if (paakrevDisplayText) { filterIsInstance.first() } else { filterIsInstance.firstOrNull() }
            FieldType.EnumEntry(it.name, displayText?.text)
        }.toSet()
}
