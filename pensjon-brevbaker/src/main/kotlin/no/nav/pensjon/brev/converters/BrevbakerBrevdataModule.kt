package no.nav.pensjon.brev.converters

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata

object BrevbakerBrevdataModule : SimpleModule() {
    private fun readResolve(): Any = BrevbakerBrevdataModule

    private class GenericBrevdata : LinkedHashMap<String, Any>(), BrevbakerBrevdata

    init {
        addDeserializer(BrevbakerBrevdata::class.java, BrevdataDeserializer)
    }

    private object BrevdataDeserializer : JsonDeserializer<BrevbakerBrevdata>() {
        override fun deserialize(
            parser: JsonParser,
            ctxt: DeserializationContext,
        ): BrevbakerBrevdata = ctxt.readValue(parser, GenericBrevdata::class.java)
    }
}
