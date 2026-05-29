package no.nav.pensjon.brev.converters

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgVerdi

object BrevbakerBrevdataModule : SimpleModule() {
    private fun readResolve(): Any = BrevbakerBrevdataModule

    private class GenericBrevdata : LinkedHashMap<String, Any>(), BrevbakerBrevdata, FagsystemBrevdata, SaksbehandlerValgBrevdata

    private data class GenericRedigerbarBrevdata(override val saksbehandlerValg: GenericBrevdata, override val pesysData: GenericBrevdata) : RedigerbarBrevdata<GenericBrevdata, GenericBrevdata>

    init {
        addDeserializer(BrevbakerBrevdata::class.java, BrevdataDeserializer)
        addDeserializer(RedigerbarBrevdata::class.java, RedigerbarBrevdataDeserializer)
        addDeserializer(SaksbehandlervalgIDSL::class.java, SaksbehandlervalgIDSLDeserializer)
        addDeserializer(SaksbehandlervalgVerdi::class.java, SaksbehandlervalgDeserializer)
    }

    private object BrevdataDeserializer : JsonDeserializer<BrevbakerBrevdata>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): BrevbakerBrevdata = ctxt.readValue(parser, GenericBrevdata::class.java)
    }
    private object RedigerbarBrevdataDeserializer : JsonDeserializer<RedigerbarBrevdata<*,*>>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): RedigerbarBrevdata<*,*> =
            ctxt.readValue(parser, GenericRedigerbarBrevdata::class.java)
    }
    private object SaksbehandlervalgIDSLDeserializer : JsonDeserializer<SaksbehandlervalgIDSL>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): SaksbehandlervalgIDSL {
            val root = parser.codec.readTree<JsonNode>(parser)
            val verdier = root.properties().associate { (key, node) ->
                key to parser.codec.treeToValue(node, SaksbehandlervalgVerdi::class.java)
            }
            return SaksbehandlervalgIDSLImpl(verdier)
        }
    }

    private object SaksbehandlervalgDeserializer : JsonDeserializer<SaksbehandlervalgVerdi>() {
            override fun deserialize(p: JsonParser, ctxt: DeserializationContext): SaksbehandlervalgVerdi {
                val node = p.codec.readTree<JsonNode>(p)
                val type = when (SaksbehandlervalgVerdi.Type.valueOf(node.get("type").textValue())) {
                    SaksbehandlervalgVerdi.Type.BOOL -> SaksbehandlervalgVerdi.Bool::class.java
                    SaksbehandlervalgVerdi.Type.INTEGER -> SaksbehandlervalgVerdi.Integer::class.java
                    SaksbehandlervalgVerdi.Type.ENUM -> SaksbehandlervalgVerdi.Enum::class.java
                }
                return p.codec.treeToValue(node, type)
            }
        }
}

class SaksbehandlervalgIDSLImpl(override val verdier: Map<String, SaksbehandlervalgVerdi>) : SaksbehandlervalgIDSL {
    override fun <T : SaksbehandlervalgVerdi> get(key: String) = verdier[key]!! as T
}