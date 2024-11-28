package no.nav.pensjon.brev.converters

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.pensjon.brev.api.model.PesysSakstype
import no.nav.pensjon.brev.api.model.TemplateDescription

// TODO: Denne bør liggje på konsumentsida
internal object SakstypeModule : SimpleModule() {
    private fun readResolve(): Any = BrevkodeModule

    init {
        addDeserializer(TemplateDescription.Sakstype::class.java, SakstypeDeserializer)
    }

    private object SakstypeDeserializer : JsonDeserializer<TemplateDescription.Sakstype>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): TemplateDescription.Sakstype =
            ctxt.readValue(parser, PesysSakstype::class.java)
    }
}