package no.nav.pensjon.brev.pdfbygger

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.module.SimpleModule
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// TODO: Kan fjernes så snart deployment er ferdig. Det er kun Brevbaker som per nå sender LetterMarkup,
//       og den vil serialisere LocalDate som `yyyy-MM-dd` så snart den er deployet.
object FlexibleLocalDateModule : SimpleModule() {

    // Kreves av Serializable (siden dette er et object)
    @Suppress("unused")
    private fun readResolve(): Any = FlexibleLocalDateModule

    init {
        addDeserializer(LocalDate::class.java, flexibleLocalDateDeserializer())
    }

    private fun flexibleLocalDateDeserializer() = object : JsonDeserializer<LocalDate>() {
            private val isoFormatter = DateTimeFormatter.ISO_LOCAL_DATE
            private val noShortFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

            override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): LocalDate {
                val value = parser.text.trim()
                return try {
                    LocalDate.parse(value, isoFormatter)
                } catch (_: Exception) {
                    try {
                        LocalDate.parse(value, noShortFormatter)
                    } catch (_: Exception) {
                        throw InvalidFormatException(
                            parser,
                            "Date must be ISO (yyyy-MM-dd) or Norwegian short (dd.MM.yyyy)",
                            value,
                            LocalDate::class.java
                        )
                    }
                }
            }
        }
}