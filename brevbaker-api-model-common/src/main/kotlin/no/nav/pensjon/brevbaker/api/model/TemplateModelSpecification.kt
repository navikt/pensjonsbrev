package no.nav.pensjon.brevbaker.api.model

typealias ObjectTypeSpecification = Map<String, TemplateModelSpecification.FieldType>

data class TemplateModelSpecification(val types: Map<String, ObjectTypeSpecification>, val letterModelTypeName: String?) {
    sealed class FieldType {
        abstract val nullable: Boolean
        abstract val type: String

        data class Scalar(override val nullable: Boolean, val kind: Kind) : FieldType() {
            override val type = "scalar"

            enum class Kind { NUMBER, DOUBLE, STRING, BOOLEAN, DATE }
        }

        data class Enum(override val nullable: Boolean, val values: Set<String>) : FieldType() {
            override val type = "enum"
        }

        data class Array(override val nullable: Boolean, val items: FieldType) : FieldType() {
            override val type = "array"
        }

        data class Object(override val nullable: Boolean, val typeName: String) : FieldType() {
            override val type = "object"
        }

        @Suppress("EnumEntryName")
        enum class Type { scalar, enum, array, `object` }
    }
}


