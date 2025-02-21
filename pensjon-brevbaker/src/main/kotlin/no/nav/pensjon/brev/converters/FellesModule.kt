package no.nav.pensjon.brev.converters

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brevbaker.api.model.NAVEnhet
import no.nav.pensjon.brevbaker.api.model.NavEnhetImpl

@OptIn(InterneDataklasser::class)
object FellesModule : SimpleModule() {
    private fun readResolve(): Any = FellesModule

    init {
        addDeserializer(NAVEnhet::class.java, NavEnhetDeserializer)
    }


    private object NavEnhetDeserializer : JsonDeserializer<NAVEnhet>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): NAVEnhet =
            parser.codec.treeToValue(parser.codec.readTree<JsonNode>(parser), NavEnhetImpl::class.java)
    }
}