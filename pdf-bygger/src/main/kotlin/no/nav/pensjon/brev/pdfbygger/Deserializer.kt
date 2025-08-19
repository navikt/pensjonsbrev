package no.nav.pensjon.brev.pdfbygger

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.module.SimpleModule


internal inline fun <reified T, reified V : T> SimpleModule.addInterfaceDeserializer() =
    addDeserializer(T::class.java, object : FellesDeserializer<T, V>(V::class.java) {})

internal abstract class FellesDeserializer<T, V : T>(private val v: Class<V>) : JsonDeserializer<T>() {
    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): T =
        parser.codec.treeToValue(parser.codec.readTree<JsonNode>(parser), v)
}