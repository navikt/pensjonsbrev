package no.nav.pensjon.brev.converters

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata

object BrevbakerBrevdataModule : SimpleModule() {
    private fun readResolve(): Any = BrevbakerBrevdataModule

    private class GenericBrevdata : LinkedHashMap<String, Any>(), BrevbakerBrevdata

    private class GenericRedigerbarBrevdata : LinkedHashMap<String, Any>(), RedigerbarBrevdata<BrevbakerBrevdata, BrevbakerBrevdata> {
        override val saksbehandlerValg: BrevbakerBrevdata
            get() = get("saksbehandlerValg") as BrevbakerBrevdata
        override val pesysData: BrevbakerBrevdata
            get() = get("pesysData") as BrevbakerBrevdata
    }

    init {
        addDeserializer(BrevbakerBrevdata::class.java, BrevdataDeserializer)
        addDeserializer(RedigerbarBrevdata::class.java, RedigerbarBrevdataDeserializer)
    }

    private object BrevdataDeserializer : JsonDeserializer<BrevbakerBrevdata>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): BrevbakerBrevdata = ctxt.readValue(parser, GenericBrevdata::class.java)
    }
    private object RedigerbarBrevdataDeserializer : JsonDeserializer<RedigerbarBrevdata<*,*>>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): RedigerbarBrevdata<*,*> {
            return ctxt.readValue(parser, GenericRedigerbarBrevdata::class.java)
        }
    }
}