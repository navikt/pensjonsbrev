package no.nav.pensjon.brev.converters

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
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
    }

    private object BrevdataDeserializer : JsonDeserializer<BrevbakerBrevdata>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): BrevbakerBrevdata = ctxt.readValue(parser, GenericBrevdata::class.java)
    }
    private object RedigerbarBrevdataDeserializer : JsonDeserializer<RedigerbarBrevdata<*,*>>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): RedigerbarBrevdata<*,*> =
            ctxt.readValue(parser, GenericRedigerbarBrevdata::class.java)
    }
    private object SaksbehandlervalgIDSLDeserializer : JsonDeserializer<SaksbehandlervalgIDSL>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): SaksbehandlervalgIDSL =
            SaksbehandlervalgIDSLImpl(ctxt.readValue(parser, Map::class.java) as Map<String, SaksbehandlervalgVerdi>)
    }
}

class SaksbehandlervalgIDSLImpl(override val verdier: Map<String, SaksbehandlervalgVerdi>) : SaksbehandlervalgIDSL