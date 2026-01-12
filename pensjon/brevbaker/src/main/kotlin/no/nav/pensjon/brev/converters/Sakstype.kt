package no.nav.pensjon.brev.converters

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.JsonDeserializer
import no.nav.pensjon.brev.api.model.ISakstype

class Sakstype(val name: String) : ISakstype {
    override fun kode() = name
    override fun equals(other: Any?): Boolean {
        if (other !is ISakstype) return false
        return name == other.kode()
    }
    override fun hashCode() = name.hashCode()
    override fun toString() = name
}

object SakstypeModule : SimpleModule() {
    private fun readResolve(): Any = SakstypeModule

    init {
        addDeserializer(ISakstype::class.java, SakstypeDeserializer)
    }

    private object SakstypeDeserializer : JsonDeserializer<ISakstype>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): ISakstype =
            Sakstype(ctxt.readValue(parser, String::class.java))
    }
}