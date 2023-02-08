package no.nav.pensjon.brev.template

import com.fasterxml.jackson.annotation.*
import kotlin.reflect.*
import kotlin.reflect.full.primaryConstructor

data class TemplateModelSpecification(val types: Map<String, ObjectTypeSpecification>, val letterModelTypeName: String?)

typealias ObjectTypeSpecification = Map<String, FieldType>

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes(
    JsonSubTypes.Type(FieldType.Scalar::class, name = "scalar"),
    JsonSubTypes.Type(FieldType.Enum::class, name = "enum"),
    JsonSubTypes.Type(FieldType.Array::class, name = "array"),
    JsonSubTypes.Type(FieldType.Object::class, name = "object"),
)
sealed class FieldType {
    data class Scalar(val nullable: Boolean, val kind: Kind) : FieldType() {
        enum class Kind { NUMBER, DOUBLE, STRING, BOOLEAN, DATE }
    }

    data class Enum(val nullable: Boolean, val values: Set<String>) : FieldType()
    data class Array(val nullable: Boolean, val items: FieldType) : FieldType()
    data class Object(val nullable: Boolean, val typeName: String) : FieldType()
}

class LetterModelSpecificationError(msg: String) : Error(msg)

class LetterModelSpecificationFactory(val from: KClass<*>) {
    private val toProcess = mutableListOf<KClass<*>>()

    fun build(): TemplateModelSpecification =
        if (from.objectInstance == Unit) {
            TemplateModelSpecification(emptyMap(), null)
        } else if (from.primaryConstructor == null) {
            throw LetterModelSpecificationError("Cannot create specification of class without primary constructor: ${from.qualifiedName}")
        } else if (!(from.isData || from.isValue)) {
            throw LetterModelSpecificationError("Cannot create specification from a regular class: must be data or value class")
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

    fun validateTypes(spec: TemplateModelSpecification, path: List<String>, type: ObjectTypeSpecification) {
        type.values.forEach {
            if (it is FieldType.Object) {
                if (path.contains(it.typeName)) {
                    throw LetterModelSpecificationError("Recursive types not supported: ${it.typeName}")
                } else if (!spec.types.containsKey(it.typeName)) {
                    throw LetterModelSpecificationError("TemplateModelSpecification is incomplete: ${it.typeName} is missing")
                } else {
                    validateTypes(spec, path + it.typeName, spec.types[it.typeName]!!)
                }
            }
        }
    }

    private fun createObjectTypeSpecification(type: KClass<*>): ObjectTypeSpecification =
        type.primaryConstructor!!.parameters.associate { it.name!! to it.type.toFieldType() }

    private fun KType.toFieldType(): FieldType {
        val theClassifier = classifier
        return if (theClassifier is KClass<*>) {
            when (val qname = theClassifier.qualifiedName) {
                "kotlin.String" ->
                    FieldType.Scalar(isMarkedNullable, FieldType.Scalar.Kind.STRING)

                "kotlin.Int", "kotlin.Long" ->
                    FieldType.Scalar(isMarkedNullable, FieldType.Scalar.Kind.NUMBER)

                "kotlin.Double", "kotlin.Float" ->
                    FieldType.Scalar(isMarkedNullable, FieldType.Scalar.Kind.DOUBLE)

                "kotlin.Boolean" ->
                    FieldType.Scalar(isMarkedNullable, FieldType.Scalar.Kind.BOOLEAN)

                "kotlin.collections.List" -> {
                    FieldType.Array(isMarkedNullable, arguments.first().type!!.toFieldType())
                }

                "java.time.LocalDate" ->
                    FieldType.Scalar(isMarkedNullable, FieldType.Scalar.Kind.DATE)

                else -> {
                    if (theClassifier.isData || theClassifier.isValue) {
                        toProcess.add(theClassifier)
                        FieldType.Object(isMarkedNullable, qname!!)
                    } else if (theClassifier.java.isEnum) {
                        FieldType.Enum(isMarkedNullable, theClassifier.java.enumConstants.map { it.toString() }.toSet())
                    } else {
                        throw LetterModelSpecificationError("Don't know how to handle type: $qname")
                    }
                }
            }
        } else {
            throw LetterModelSpecificationError("Unable to create FieldType of: $this")
        }
    }
}
