package no.nav.pensjon.brev.converters

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.pensjon.brev.api.model.TemplateDescription

object TemplateDescriptionModule : SimpleModule() {
    private fun readResolve(): Any = TemplateDescriptionModule

    init {
        addSerializer(TemplateDescription.IBrevkategori::class.java, BrevkategoriSerializer())
        addDeserializer(TemplateDescription.IBrevkategori::class.java, BrevkategoriDeserializer())
    }

    private class BrevkategoriSerializer : JsonSerializer<TemplateDescription.IBrevkategori>() {
        override fun serialize(brevkategori: TemplateDescription.IBrevkategori, generator: JsonGenerator, serializers: SerializerProvider?) {
            generator.writeStartObject()
            generator.writeFieldName("kode")
            generator.writeString(brevkategori.kode())
            generator.writeFieldName("kategoritekst")
            generator.writeString(brevkategori.kategoritekst)
            generator.writeEndObject()
        }
    }

    private class BrevkategoriDeserializer : JsonDeserializer<TemplateDescription.IBrevkategori>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): TemplateDescription.IBrevkategori = parser.readValueAs(Brevkategori::class.java)
    }

}

data class Brevkategori(override val kategoritekst: String, val kode: String) : TemplateDescription.IBrevkategori {
    override fun kode() = kode
}