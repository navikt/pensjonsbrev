package no.nav.pensjon.brev.template

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import no.nav.pensjon.brev.converters.BrevbakerBrevdataModule
import no.nav.pensjon.brev.converters.BrevkodeModule
import no.nav.brev.brevbaker.serialization.LetterMarkupV1JacksonModule
import no.nav.brev.brevbaker.serialization.TemplateModelSpecificationJacksonModule

fun ObjectMapper.brevbakerConfig() {
    registerModule(JavaTimeModule())
    registerModule(BrevbakerBrevdataModule)
    registerModule(BrevkodeModule)
    registerModule(LetterMarkupV1JacksonModule)
    registerModule(TemplateModelSpecificationJacksonModule)
    disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
}

fun brevbakerJacksonObjectMapper() = com.fasterxml.jackson.module.kotlin.jacksonObjectMapper().apply { brevbakerConfig() }
