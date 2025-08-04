package no.nav.pensjon.brev.skribenten

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.deser.std.StringDeserializer
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.databind.module.SimpleModule

// TODO: Vi bør kunne rydde bort denne igjen etter at pesys er oppdatert
object PrimitiveModule : SimpleModule() {
    private fun readResolve(): Any = PrimitiveModule

    init {
        addDeserializer(String::class.java, stringDeser())
    }

    private val standardStringDeserializer = StringDeserializer()

    // TODO: Denne kan vi fjerne når både Gjenny og Pesys bruker ny nok versjon av biblioteket, så dei sender
    // fødselsnummer, telefonnummer og kroner som flat tekst og ikkje som innpakka objekt
    private fun stringDeser() = object : StdDeserializer<String>(String::class.java) {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): String? {
            try {
                return standardStringDeserializer.deserialize(p, ctxt)
            } catch (e: MismatchedInputException) {
                val node = p.codec.readTree<JsonNode>(p)
                try {
                    val treeToValue = p.codec.treeToValue(node, Map::class.java)
                    require(treeToValue.size == 1,  { "custom-deserialisering kun for wrapper-klasser" })
                    require(treeToValue.keys.contains("value"))
                    return treeToValue["value"].toString()
                } catch (e2: Exception) {
                    throw e.also { it.addSuppressed(e2) }
                }
            }
        }
    }
}