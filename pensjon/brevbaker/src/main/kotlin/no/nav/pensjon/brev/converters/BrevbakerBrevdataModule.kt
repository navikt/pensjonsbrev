package no.nav.pensjon.brev.converters

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.brev.InternKonstruktoer
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSLImpl
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgVerdi
import no.nav.pensjon.brev.template.BrevbakerDSLInternal

object BrevbakerBrevdataModule : SimpleModule() {
    private fun readResolve(): Any = BrevbakerBrevdataModule

    private class GenericBrevdata : LinkedHashMap<String, Any>(), BrevbakerBrevdata, FagsystemBrevdata, SaksbehandlerValgBrevdata

    private data class GenericRedigerbarBrevdata(override val saksbehandlerValg: GenericBrevdata, override val pesysData: GenericBrevdata) : RedigerbarBrevdata<GenericBrevdata, GenericBrevdata>

    init {
        addDeserializer(BrevbakerBrevdata::class.java, BrevdataDeserializer)
        addDeserializer(RedigerbarBrevdata::class.java, RedigerbarBrevdataDeserializer)
        addDeserializer(SaksbehandlervalgIDSL::class.java, SaksbehandlervalgIDSLDeserializer)
        @Suppress("UNCHECKED_CAST")
        addDeserializer(SaksbehandlervalgVerdi::class.java as Class<SaksbehandlervalgVerdi<*>>, SaksbehandlervalgDeserializer)
        @Suppress("UNCHECKED_CAST")
        addDeserializer(SaksbehandlervalgVerdi.Enum::class.java as Class<SaksbehandlervalgVerdi.Enum<*>>, SaksbehandlervalgEnumDeserializer)
    }

    private object BrevdataDeserializer : JsonDeserializer<BrevbakerBrevdata>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): BrevbakerBrevdata = ctxt.readValue(parser, GenericBrevdata::class.java)
    }
    private object RedigerbarBrevdataDeserializer : JsonDeserializer<RedigerbarBrevdata<*,*>>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): RedigerbarBrevdata<*,*> =
            ctxt.readValue(parser, GenericRedigerbarBrevdata::class.java)
    }

    @OptIn(BrevbakerDSLInternal::class)
    private object SaksbehandlervalgIDSLDeserializer : JsonDeserializer<SaksbehandlervalgIDSL>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): SaksbehandlervalgIDSL {
            val root = parser.codec.readTree<JsonNode>(parser)
            val verdier = root.properties().associate { (key, node) ->
                key to parser.codec.treeToValue(node, SaksbehandlervalgVerdi::class.java) as SaksbehandlervalgVerdi<*>
            }
            return SaksbehandlervalgIDSLImpl(verdier, mapOf()) // TODO andre param her
        }
    }

    @Suppress("UNCHECKED_CAST")
    private object SaksbehandlervalgDeserializer : JsonDeserializer<SaksbehandlervalgVerdi<*>>() {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): SaksbehandlervalgVerdi<*> {
                val node = p.codec.readTree<JsonNode>(p)
                val type = when (SaksbehandlervalgVerdi.Type.valueOf(node.get("type").textValue())) {
                    SaksbehandlervalgVerdi.Type.BOOL -> SaksbehandlervalgVerdi.Bool::class.java
                    SaksbehandlervalgVerdi.Type.INTEGER -> SaksbehandlervalgVerdi.Integer::class.java
                    SaksbehandlervalgVerdi.Type.ENUM -> SaksbehandlervalgVerdi.Enum::class.java
                    SaksbehandlervalgVerdi.Type.TEXT -> SaksbehandlervalgVerdi.Text::class.java
                }
                return p.codec.treeToValue(node, type)
            }
        }

    private object SaksbehandlervalgEnumDeserializer : JsonDeserializer<SaksbehandlervalgVerdi.Enum<*>>() {
        @OptIn(InternKonstruktoer::class)
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): SaksbehandlervalgVerdi.Enum<*> {
            val node = p.codec.readTree<JsonNode>(p)
            val className = node.get("clazz")?.textValue()
                ?: throw IllegalArgumentException("Missing 'clazz' for SaksbehandlervalgVerdi.Enum")

            require(className.startsWith("no.nav")) {
                "Illegal enum class for saksbehandlervalg: $className, pakkenavnet må starte med no.nav"
            }

            val rawClass = Class.forName(className, false, BrevbakerBrevdataModule::class.java.classLoader)
            require(rawClass.isEnum && SaksbehandlerValgEnum::class.java.isAssignableFrom(rawClass)) {
                "Illegal enum class for saksbehandlervalg: $className"
            }

            @Suppress("UNCHECKED_CAST")
            val enumClass = rawClass as Class<out Enum<*>>

            return SaksbehandlervalgVerdi.Enum(
                defaultValue = SaksbehandlervalgVerdi.Enum.parse(enumClass, node.get("enum")?.textValue()),
                displayText = node.get("displayText").textValue(),
                clazz = enumClass,
            )
        }
    }
}