package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.DisplayText
import no.nav.pensjon.brevbaker.api.model.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.ObjectTypeSpecification
import no.nav.pensjon.brevbaker.api.model.Telefonnummer
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification.FieldType
import no.nav.pensjon.brevbaker.api.model.Year
import kotlin.reflect.*
import kotlin.reflect.full.primaryConstructor

class TemplateModelSpecificationError(msg: String) : Error(msg)

class TemplateModelSpecificationFactory(val from: KClass<*>) {
    private val toProcess = mutableListOf<KClass<*>>()

    fun build(): TemplateModelSpecification =
        if (from.objectInstance == Unit || from.objectInstance in setOf(EmptyBrevdata, EmptyRedigerbarBrevdata)) {
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
        type.primaryConstructor?.parameters?.associate { it.name!! to it.type.toFieldType(it.annotations) }
            ?: emptyMap()

    private fun KType.toFieldType(annotations: List<Annotation>): FieldType {
        val theClassifier = classifier
        return if (theClassifier is KClass<*>) {
            val displayText = annotations.filterIsInstance<DisplayText>().map { it.text }
            when (val qname = theClassifier.qualifiedName) {
                "kotlin.String" ->
                    FieldType.Scalar(isMarkedNullable, FieldType.Scalar.Kind.STRING, displayText = displayText.firstOrNull())

                "kotlin.Int", "kotlin.Long" ->
                    FieldType.Scalar(isMarkedNullable, FieldType.Scalar.Kind.NUMBER, displayText = displayText.firstOrNull())

                "kotlin.Double", "kotlin.Float" ->
                    FieldType.Scalar(isMarkedNullable, FieldType.Scalar.Kind.DOUBLE, displayText = displayText.firstOrNull())

                "kotlin.Boolean" ->
                    FieldType.Scalar(isMarkedNullable, FieldType.Scalar.Kind.BOOLEAN, displayText = displayText.firstOrNull())

                "kotlin.collections.List" -> {
                    FieldType.Array(isMarkedNullable, arguments.first().type!!.toFieldType(listOf()))
                }

                "java.time.LocalDate" ->
                    FieldType.Scalar(isMarkedNullable, FieldType.Scalar.Kind.DATE, displayText = displayText.firstOrNull())

                Year::class.qualifiedName ->
                    FieldType.Scalar(isMarkedNullable, FieldType.Scalar.Kind.YEAR, displayText = displayText.firstOrNull())

                "no.nav.pensjon.brev.api.model.maler.EmptyBrevdata" -> {
                    toProcess.add(theClassifier)
                    FieldType.Object(isMarkedNullable, qname, displayText = displayText.firstOrNull())
                }

                Telefonnummer::class.qualifiedName, Foedselsnummer::class.qualifiedName -> {
                    toProcess.add(theClassifier)
                    FieldType.Object(isMarkedNullable, qname!!, displayText = displayText.firstOrNull())
                }

                else -> {
                    if (theClassifier.isData || theClassifier.isValue || theClassifier.java.isInterface) {
                        toProcess.add(theClassifier)
                        FieldType.Object(isMarkedNullable, qname!!, displayText = displayText.firstOrNull())
                    } else if (theClassifier.java.isEnum) {
                        FieldType.Enum(isMarkedNullable, enumVerdier(theClassifier), displayText = displayText.firstOrNull())
                    } else {
                        throw TemplateModelSpecificationError("Don't know how to handle type: $qname")
                    }
                }
            }
        } else {
            throw TemplateModelSpecificationError("Unable to create FieldType of: $this")
        }
    }

    private fun enumVerdier(theClassifier: KClass<*>) =
        theClassifier.java.fields.map {
            FieldType.EnumEntry(it.name, it.annotations.filterIsInstance<DisplayText>().firstOrNull()?.text)
        }.toSet()
}
