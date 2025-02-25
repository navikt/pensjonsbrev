package no.nav.brev.brevbaker

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule

fun ObjectMapper.brevbakerConfig() {
    registerModule(JavaTimeModule())
    enable(SerializationFeature.INDENT_OUTPUT)
    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
}

fun jacksonObjectMapper() = com.fasterxml.jackson.module.kotlin.jacksonObjectMapper().apply { brevbakerConfig() }
