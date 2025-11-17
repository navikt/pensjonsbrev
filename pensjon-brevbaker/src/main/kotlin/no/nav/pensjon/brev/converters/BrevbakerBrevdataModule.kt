package no.nav.pensjon.brev.converters

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata

object BrevbakerBrevdataModule : SimpleModule() {
    private fun readResolve(): Any = BrevbakerBrevdataModule

    private class GenericBrevdata : LinkedHashMap<String, Any>(), BrevbakerBrevdata, FagsystemBrevdata, SaksbehandlerValgBrevdata

    private class GenericSaksbehandlervalg : LinkedHashMap<String, Any>(), SaksbehandlerValgBrevdata

    private class GenericFagsystemBrevdata : LinkedHashMap<String, Any>(), FagsystemBrevdata

    private class GenericAutobrevdata : LinkedHashMap<String, Any>(), AutobrevData

    private data class GenericRedigerbarBrevdata(override val saksbehandlerValg: GenericSaksbehandlervalg, override val pesysData: GenericFagsystemBrevdata) : RedigerbarBrevdata<GenericSaksbehandlervalg, GenericFagsystemBrevdata>

    init {
        addDeserializer(BrevbakerBrevdata::class.java, BrevdataDeserializer)
        addDeserializer(AutobrevData::class.java, AutobrevdataDeserializer)
        addDeserializer(RedigerbarBrevdata::class.java, RedigerbarBrevdataDeserializer)
    }

    private object BrevdataDeserializer : JsonDeserializer<BrevbakerBrevdata>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): BrevbakerBrevdata = ctxt.readValue(parser, GenericBrevdata::class.java)
    }

    private object AutobrevdataDeserializer : JsonDeserializer<AutobrevData>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): AutobrevData = ctxt.readValue(parser, GenericAutobrevdata::class.java)
    }
    private object RedigerbarBrevdataDeserializer : JsonDeserializer<RedigerbarBrevdata<*,*>>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): RedigerbarBrevdata<*,*> =
            ctxt.readValue(parser, GenericRedigerbarBrevdata::class.java)
    }
}