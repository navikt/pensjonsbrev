package no.nav.pensjon.brev.skribenten

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.module.SimpleModule
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// TODO: Når alle brevredigeringer har `sistreservert` nyere enn 2025-07-30 (da LetterMarkup.Sakspart.dokumentDato ble endret til LocalDate),
//       kan denne modulen fjernes og `JavaTimeModule` brukes direkte.
object FlexibleLocalDateModule : SimpleModule() {

    // Kreves av Serializable (siden dette er et object)
    @Suppress("unused")
    private fun readResolve(): Any = FlexibleLocalDateModule

    init {
        addDeserializer(LocalDate::class.java, flexibleLocalDateDeserializer())
    }

    private fun flexibleLocalDateDeserializer() = object : JsonDeserializer<LocalDate>() {
        private val isoFormatter = DateTimeFormatter.ISO_LOCAL_DATE
        private val norwegianShortFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): LocalDate {
            val value = parser.text.trim()
            return try {
                LocalDate.parse(value, isoFormatter)
            } catch (isoFormatException: Exception) {
                try {
                    LocalDate.parse(value, norwegianShortFormatter)
                } catch (norwegianFormatException: Exception) {
                    throw InvalidFormatException(
                        parser,
                        "Date must be ISO (yyyy-MM-dd) or Norwegian short (dd.MM.yyyy)",
                        value,
                        LocalDate::class.java
                    ).also {
                        it.addSuppressed(isoFormatException)
                        it.addSuppressed(norwegianFormatException)
                    }
                }
            }
        }
    }
}