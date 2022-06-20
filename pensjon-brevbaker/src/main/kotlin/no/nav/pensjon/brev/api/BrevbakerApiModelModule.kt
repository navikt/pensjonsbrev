package no.nav.pensjon.brev.api

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.pensjon.brev.api.model.Foedselsnummer
import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.Telefonnummer

object BrevbakerApiModelModule : SimpleModule() {
    init {
        addDeserializer(Kroner::class.java, IntValueClassDeserializer(Kroner::class.java, ::Kroner))
        addDeserializer(Foedselsnummer::class.java, StringValueClass(Foedselsnummer::class.java, ::Foedselsnummer))
        addDeserializer(Telefonnummer::class.java, StringValueClass(Telefonnummer::class.java, ::Telefonnummer))
    }

    class IntValueClassDeserializer<T: Any>(clazz: Class<T>, val factory: (Int) -> T) : StdDeserializer<T>(clazz) {
        override fun deserialize(p0: JsonParser?, p1: DeserializationContext?): T {
            val value = p0?.codec?.readValue(p0, Int::class.java)
            return factory(value!!)
        }
    }

    class StringValueClass<T: Any>(clazz: Class<T>, val factory: (String) -> T) : StdDeserializer<T>(clazz) {
        override fun deserialize(p0: JsonParser?, p1: DeserializationContext?): T {
            val value = p0?.codec?.readValue(p0, String::class.java)
            return factory(value!!)
        }
    }
}