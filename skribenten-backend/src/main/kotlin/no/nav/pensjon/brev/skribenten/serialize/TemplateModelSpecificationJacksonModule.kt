package no.nav.pensjon.brev.skribenten.serialize

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification

object TemplateModelSpecificationJacksonModule : SimpleModule() {
    // Kreves pga Serializable
    @Suppress("unused")
    private fun readResolve(): Any = TemplateModelSpecificationJacksonModule

    init {
        addDeserializer(TemplateModelSpecification.FieldType::class.java, fieldTypeDeserializer())
    }

    private fun fieldTypeDeserializer() =
        object : StdDeserializer<TemplateModelSpecification.FieldType>(TemplateModelSpecification.FieldType::class.java) {
            override fun deserialize(p: JsonParser, ctxt: DeserializationContext): TemplateModelSpecification.FieldType {
                val node = p.codec.readTree<JsonNode>(p)
                val type = when (TemplateModelSpecification.FieldType.Type.valueOf(node.get("type").textValue())) {
                    TemplateModelSpecification.FieldType.Type.array -> TemplateModelSpecification.FieldType.Array::class.java
                    TemplateModelSpecification.FieldType.Type.scalar -> TemplateModelSpecification.FieldType.Scalar::class.java
                    TemplateModelSpecification.FieldType.Type.enum -> TemplateModelSpecification.FieldType.Enum::class.java
                    TemplateModelSpecification.FieldType.Type.`object` -> TemplateModelSpecification.FieldType.Object::class.java
                }
                return p.codec.treeToValue(node, type)
            }
        }

}