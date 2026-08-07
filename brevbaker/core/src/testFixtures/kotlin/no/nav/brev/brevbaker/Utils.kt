package no.nav.brev.brevbaker

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth

fun ObjectMapper.brevbakerConfig() {
    registerModule(JavaTimeModule())
    enable(SerializationFeature.INDENT_OUTPUT)
    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
}

fun jacksonObjectMapper() = com.fasterxml.jackson.module.kotlin.jacksonObjectMapper().apply { brevbakerConfig() }

val vilkaarligDato = LocalDate.of(2025, Month.NOVEMBER, 20)

val vilkaarligMaaned = YearMonth.of(2025, Month.OCTOBER)

fun writeJsonToFile(path: Path, content: Any) {
    Files.writeString(path, jacksonObjectMapper().writeValueAsString(content))
}

fun toPrettyJson(content: Any): String =
    jacksonObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(content)