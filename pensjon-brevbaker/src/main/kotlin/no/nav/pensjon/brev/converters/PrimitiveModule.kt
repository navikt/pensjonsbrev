package no.nav.pensjon.brev.converters

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.node.IntNode
import com.fasterxml.jackson.databind.node.TextNode
import no.nav.pensjon.brevbaker.api.model.Year
import no.nav.pensjon.brevbaker.api.model.YearWrapper

object PrimitiveModule : SimpleModule() {
    private fun readResolve(): Any = PrimitiveModule

    init {
        addDeserializer(Year::class.java, yearDeserializer())
    }

    private fun yearDeserializer() =
        object : StdDeserializer<Year>(Year::class.java) {
            override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Year {
                val node = p.codec.readTree<JsonNode>(p)
                return if (node is IntNode) {
                    Year(p.codec.treeToValue(node, Int::class.java))
                } else if (node is TextNode) {
                    Year(p.codec.treeToValue(node, String::class.java).toInt())
                } else {
                    Year(p.codec.treeToValue(node, YearWrapper::class.java).value)
                }
            }
        }
}