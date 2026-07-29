package no.nav.pensjon.brev.skribenten.serialize

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import no.nav.pensjon.brev.skribenten.model.SaksbehandlervalgVerdi

class SaksbehandlervalgVerdiDeserializer : JsonDeserializer<SaksbehandlervalgVerdi>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): SaksbehandlervalgVerdi {
        val node: JsonNode = p.codec.readTree(p)
        return when {
            node.isTextual -> SaksbehandlervalgVerdi.String(node.asText())
            node.isNumber -> SaksbehandlervalgVerdi.Int(node.asInt())
            node.isBoolean -> SaksbehandlervalgVerdi.Boolean(node.asBoolean())
            else -> ctxt.reportInputMismatch(
                SaksbehandlervalgVerdi::class.java,
                "Kan ikke deserialisere '%s' som SaksbehandlervalgVerdi - må være tekst, tall eller boolsk verdi",
                node,
            )
        }
    }
}