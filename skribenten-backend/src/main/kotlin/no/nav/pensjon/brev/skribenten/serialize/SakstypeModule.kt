package no.nav.pensjon.brev.skribenten.serialize

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import no.nav.pensjon.brev.api.model.ISakstype

object SakstypeModule : SimpleModule() {
    private fun readResolve(): Any = SakstypeModule

    init {
        addDeserializer(ISakstype::class.java, SakstypeDeserializer)
        addSerializer(ISakstype::class.java, SakstypeSerializer)
    }

    private object SakstypeDeserializer : JsonDeserializer<ISakstype>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): ISakstype =
            Sakstype(ctxt.readValue(parser, String::class.java))
    }

    private object SakstypeSerializer : JsonSerializer<ISakstype>() {
        override fun serialize(value: ISakstype, generator: JsonGenerator, serializers: SerializerProvider) {
            generator.writeString(value.kode)
        }
    }
}

class Sakstype(val name: String) : ISakstype {
    override val kode = name
    override fun equals(other: Any?): Boolean {
        if (other !is ISakstype) return false
        return name == other.kode
    }
    override fun hashCode() = name.hashCode()
    override fun toString() = name
}