package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyVedleggData
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brevbaker.api.model.Broek
import no.nav.pensjon.brevbaker.api.model.DisplayText
import no.nav.pensjon.brevbaker.api.model.ObjectTypeSpecification
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification.FieldType
import java.time.Period
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor

class TemplateModelSpecificationError(msg: String) : Error(msg)

class TemplateModelSpecificationFactory(private val from: KClass<*>) {
    private val toProcess = mutableListOf<KClass<*>>()

    fun build(): TemplateModelSpecification =
        if (from.objectInstance == Unit || from.objectInstance in setOf(EmptyBrevdata, EmptyRedigerbarBrevdata, EmptyVedleggData)) {
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
                if (!objectTypes.containsKey(name)) {
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
        type.primaryConstructor?.parameters?.associate { it.name!! to it.type.toFieldType(it.annotations, type.isSubclassOf(SaksbehandlerValgBrevdata::class)) }
            ?: emptyMap()

    private fun KType.toFieldType(annotations: List<Annotation>, paakrevDisplayText: Boolean): FieldType {
        val theClassifier = classifier
        return if (theClassifier is KClass<*>) {
            val displayText = annotations.filterIsInstance<DisplayText>().map { it.text }
            val displayedText = if (paakrevDisplayText) {
                displayText.first()
            } else {
                displayText.firstOrNull()
            }

            when (val qname = theClassifier.qualifiedName) {
                "kotlin.String" ->
                    FieldType.Scalar(isMarkedNullable, FieldType.Scalar.Kind.STRING, displayText = displayedText)

                "kotlin.Int", "kotlin.Long" ->
                    FieldType.Scalar(isMarkedNullable, FieldType.Scalar.Kind.NUMBER, displayText = displayedText)

                "kotlin.Double", "kotlin.Float" ->
                    FieldType.Scalar(isMarkedNullable, FieldType.Scalar.Kind.DOUBLE, displayText = displayedText)

                "kotlin.Boolean" ->
                    FieldType.Scalar(isMarkedNullable, FieldType.Scalar.Kind.BOOLEAN, displayText = displayedText)

                "kotlin.collections.List" -> {
                    FieldType.Array(isMarkedNullable, arguments.first().type!!.toFieldType(listOf(), paakrevDisplayText))
                }

                "java.time.LocalDate" ->
                    FieldType.Scalar(isMarkedNullable, FieldType.Scalar.Kind.DATE, displayText = displayedText)

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
                        theClassifier.primaryConstructor!!.parameters.first().type.toFieldType(annotations, paakrevDisplayText)
                            .takeIf { it is FieldType.Scalar } ?: throw TemplateModelSpecificationError("Expected value class to be scalar, but was not")
                    }
                    else if (theClassifier.isData || theClassifier.java.isInterface) {
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
