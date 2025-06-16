package no.nav.pensjon.brevbaker.api.model

import java.util.Objects

typealias ObjectTypeSpecification = Map<String, TemplateModelSpecification.FieldType>

class TemplateModelSpecification(val types: Map<String, ObjectTypeSpecification>, val letterModelTypeName: String?) {
    sealed class FieldType {
        abstract val nullable: Boolean
        abstract val type: String
        abstract val displayText: String?

        class Scalar(override val nullable: Boolean, val kind: Kind, override val displayText: String? = null) : FieldType() {
            override val type = "scalar"

            enum class Kind { NUMBER, DOUBLE, STRING, BOOLEAN, DATE, YEAR }

            override fun equals(other: Any?): Boolean {
                if (other !is Scalar) return false
                return nullable == other.nullable && kind == other.kind && displayText == other.displayText && type == other.type
            }

            override fun hashCode() = Objects.hash(nullable, kind, displayText, type)

            override fun toString() = "Scalar(nullable=$nullable, kind=$kind, displayText=$displayText, type='$type')"
        }

        class Enum(override val nullable: Boolean, val values: List<EnumEntry>, override val displayText: String? = null) : FieldType() {
            override val type = "enum"

            override fun equals(other: Any?): Boolean {
                if (other !is Enum) return false
                return nullable == other.nullable && values == other.values && displayText == other.displayText && type == other.type
            }

            override fun hashCode() = Objects.hash(nullable, values, displayText, type)

            override fun toString() = "Enum(nullable=$nullable, values=$values, displayText=$displayText, type='$type')"
        }

        class EnumEntry(val value: String, val displayText: String? = null) {
            override fun equals(other: Any?): Boolean {
                if (other !is EnumEntry) return false
                return value == other.value && displayText == other.displayText
            }

            override fun hashCode() = Objects.hash(value, displayText)

            override fun toString() = "EnumEntry(value=$value, displayText=$displayText)"
        }

        class Array(override val nullable: Boolean, val items: FieldType, override val displayText: String? = null) : FieldType() {
            override val type = "array"

            override fun equals(other: Any?): Boolean {
                if (other !is Array) return false
                return nullable == other.nullable && items == other.items && displayText == other.displayText && type == other.type
            }

            override fun hashCode() = Objects.hash(nullable, items, displayText, type)

            override fun toString() = "Array(nullable=$nullable, items=$items, displayText=$displayText, type='$type')"
        }

        class Object(override val nullable: Boolean, val typeName: String, override val displayText: String? = null) : FieldType() {
            override val type = "object"

            override fun equals(other: Any?): Boolean {
                if (other !is Object) return false
                return nullable == other.nullable && typeName == other.typeName && displayText == other.displayText && type == other.type
            }

            override fun hashCode() = Objects.hash(nullable, typeName, displayText, type)

            override fun toString() = "Object(nullable=$nullable, typeName='$typeName', displayText=$displayText, type='$type')"
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

interface EnumMedDisplayText {
    fun displayText(): String
}