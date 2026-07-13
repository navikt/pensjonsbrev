package no.nav.pensjon.brev.converters

import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL

object BrevbakerBrevdataModule : SimpleModule() {
    private fun readResolve(): Any = BrevbakerBrevdataModule

    private class GenericBrevdata : LinkedHashMap<String, Any>(), BrevbakerBrevdata, FagsystemBrevdata, SaksbehandlerValgBrevdata

    private data class GenericRedigerbarBrevdata(override val saksbehandlerValg: GenericBrevdata, override val pesysData: GenericBrevdata) : RedigerbarBrevdata<GenericBrevdata, GenericBrevdata>

    private class SaksbehandlervalgMap : SaksbehandlervalgIDSL, LinkedHashMap<String, Any?>()

    init {
        addAbstractTypeMapping<BrevbakerBrevdata, GenericBrevdata>()
        addAbstractTypeMapping<RedigerbarBrevdata<*, *>, GenericRedigerbarBrevdata>()
        addAbstractTypeMapping<SaksbehandlervalgIDSL, SaksbehandlervalgMap>()
    }

}
