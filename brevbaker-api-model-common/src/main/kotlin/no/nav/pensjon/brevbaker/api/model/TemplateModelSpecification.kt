package no.nav.pensjon.brevbaker.api.model

typealias ObjectTypeSpecification = Map<String, TemplateModelSpecification.FieldType>

data class TemplateModelSpecification(val types: Map<String, ObjectTypeSpecification>, val letterModelTypeName: String?) {
    sealed class FieldType {
        abstract val type: String

        data class Scalar(val nullable: Boolean, val kind: Kind) : FieldType() {
            override val type = "scalar"

            enum class Kind { NUMBER, DOUBLE, STRING, BOOLEAN, DATE }
        }

        data class Enum(val nullable: Boolean, val values: Set<String>) : FieldType() {
            override val type = "enum"
        }

        data class Array(val nullable: Boolean, val items: FieldType) : FieldType() {
            override val type = "array"
        }

        data class Object(val nullable: Boolean, val typeName: String) : FieldType() {
            override val type = "object"
        }
    }
}


