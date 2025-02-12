package no.nav.pensjon.brev.converters

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification.FieldType

object TemplateModelSpecificationModule : SimpleModule() {
    private fun readResolve(): Any = TemplateModelSpecificationModule

    init {
        addDeserializer(FieldType::class.java, fieldTypeDeserializer())
    }

    private fun fieldTypeDeserializer() =
        object : StdDeserializer<FieldType>(FieldType::class.java) {
            override fun deserialize(
                p: JsonParser,
                ctxt: DeserializationContext,
            ): FieldType {
                val node = p.codec.readTree<JsonNode>(p)
                val type =
                    when (FieldType.Type.valueOf(node.get("type").textValue())) {
                        FieldType.Type.array -> FieldType.Array::class.java
                        FieldType.Type.scalar -> FieldType.Scalar::class.java
                        FieldType.Type.enum -> FieldType.Enum::class.java
                        FieldType.Type.`object` -> FieldType.Object::class.java
                    }
                return p.codec.treeToValue(node, type)
            }
        }
}
