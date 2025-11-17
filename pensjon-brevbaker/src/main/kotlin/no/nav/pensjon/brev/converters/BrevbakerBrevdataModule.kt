package no.nav.pensjon.brev.converters

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata

object BrevbakerBrevdataModule : SimpleModule() {
    private fun readResolve(): Any = BrevbakerBrevdataModule

    private class GenericSaksbehandlervalg : LinkedHashMap<String, Any>(), SaksbehandlerValgBrevdata

    private class GenericFagsystemBrevdata : LinkedHashMap<String, Any>(), FagsystemBrevdata

    private class GenericAutobrevdata : LinkedHashMap<String, Any>(), AutobrevData

    private data class GenericRedigerbarBrevdata(override val saksbehandlerValg: GenericSaksbehandlervalg, override val pesysData: GenericFagsystemBrevdata) : RedigerbarBrevdata<GenericSaksbehandlervalg, GenericFagsystemBrevdata>

    init {
        addDeserializer(AutobrevData::class.java, AutobrevdataDeserializer)
        addDeserializer(RedigerbarBrevdata::class.java, RedigerbarBrevdataDeserializer)
    }

    private object AutobrevdataDeserializer : JsonDeserializer<AutobrevData>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): AutobrevData = ctxt.readValue(parser, GenericAutobrevdata::class.java)
    }
    private object RedigerbarBrevdataDeserializer : JsonDeserializer<RedigerbarBrevdata<*,*>>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): RedigerbarBrevdata<*,*> =
            ctxt.readValue(parser, GenericRedigerbarBrevdata::class.java)
    }
}