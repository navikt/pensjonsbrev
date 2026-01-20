package no.nav.pensjon.brev.skribenten.serialize

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import no.nav.pensjon.brev.api.model.ISakstype
import no.nav.pensjon.brev.skribenten.model.Pen

object SakstypeModule : SimpleModule() {
    private fun readResolve(): Any = SakstypeModule

    init {
        addDeserializer(ISakstype::class.java, SakstypeDeserializer)
        addSerializer(ISakstype::class.java, SakstypeSerializer)
    }

    private object SakstypeDeserializer : JsonDeserializer<ISakstype>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): ISakstype =
            // TODO: Dette er fram til vi finn ein betre måte å gjera isRelevantRegelverk-sjekken på. Vil helst ha sakstype som eit generelt objekt her, på samme måte som i pensjon-brevbaker
            Pen.BrevbakerSakstype.valueOf(ctxt.readValue(parser, String::class.java))
    }

    private object SakstypeSerializer : JsonSerializer<ISakstype>() {
        override fun serialize(value: ISakstype, generator: JsonGenerator, serializers: SerializerProvider) {
            generator.writeString(value.kode())
        }
    }
}

class Sakstype(val name: String) : ISakstype {
    override fun kode() = name
    override fun equals(other: Any?): Boolean {
        if (other !is ISakstype) return false
        return name == other.kode()
    }
    override fun hashCode() = name.hashCode()
    override fun toString() = name
}