package no.nav.pensjon.brev.converters

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.pensjon.brev.api.model.PesysBrevkategori
import no.nav.pensjon.brev.api.model.TemplateDescription

// TODO: Denne bør liggje på konsumentsida
object BrevkategoriModule : SimpleModule() {
    private fun readResolve(): Any = BrevkodeModule

    init {
        addDeserializer(TemplateDescription.Brevkategori::class.java, BrevkategoriDeserializer)
    }

    private object BrevkategoriDeserializer : JsonDeserializer<TemplateDescription.Brevkategori>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): TemplateDescription.Brevkategori =
            ctxt.readValue(parser, PesysBrevkategori::class.java)
    }
}