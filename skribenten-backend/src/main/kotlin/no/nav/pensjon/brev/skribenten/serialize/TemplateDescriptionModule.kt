package no.nav.pensjon.brev.skribenten.serialize

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.skribenten.model.Pen
import kotlin.jvm.java

object TemplateDescriptionModule : SimpleModule() {
    private fun readResolve(): Any = TemplateDescriptionModule

    init {
        addSerializer(TemplateDescription.IBrevkategori::class.java, BrevkategoriSerializer())
        addDeserializer(TemplateDescription.IBrevkategori::class.java, BrevkategoriDeserializer())
    }

    private class BrevkategoriSerializer : JsonSerializer<TemplateDescription.IBrevkategori>() {
        override fun serialize(brevkategori: TemplateDescription.IBrevkategori, generator: JsonGenerator, serializers: SerializerProvider?) {
            generator.writeString(brevkategori.kode())
        }
    }

    private class BrevkategoriDeserializer : JsonDeserializer<TemplateDescription.IBrevkategori>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): TemplateDescription.IBrevkategori = Pen.Brevkategori.valueOf(ctxt.readValue(parser, String::class.java))
    }

}