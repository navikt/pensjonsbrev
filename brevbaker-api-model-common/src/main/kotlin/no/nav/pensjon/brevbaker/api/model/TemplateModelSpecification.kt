package no.nav.pensjon.brevbaker.api.model

import java.util.Objects

typealias ObjectTypeSpecification = Map<String, TemplateModelSpecification.FieldType>

class TemplateModelSpecification(val types: Map<String, ObjectTypeSpecification>, val letterModelTypeName: String?) {
    sealed class FieldType {
        abstract val nullable: Boolean
        abstract val type: String
        abstract val displayText: String?

        data class Scalar(override val nullable: Boolean, val kind: Kind, override val displayText: String? = null) : FieldType() {
            override val type = "scalar"

            enum class Kind { NUMBER, DOUBLE, STRING, BOOLEAN, DATE, YEAR }
        }

        data class Enum(override val nullable: Boolean, val values: Set<String>, override val displayText: String? = null) : FieldType() {
            override val type = "enum"
        }

        data class Array(override val nullable: Boolean, val items: FieldType, override val displayText: String? = null) : FieldType() {
            override val type = "array"
        }

        data class Object(override val nullable: Boolean, val typeName: String, override val displayText: String? = null) : FieldType() {
            override val type = "object"
        }

        @Suppress("EnumEntryName")
        enum class Type { scalar, enum, array, `object` }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is TemplateModelSpecification) { return false }
        return types == other.types && letterModelTypeName == other.letterModelTypeName
    }

    override fun hashCode() = Objects.hash(types, letterModelTypeName)

    override fun toString() = "TemplateModelSpecification(types=$types, letterModelTypeName=$letterModelTypeName)"
}


@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
annotation class DisplayText(val text: String)