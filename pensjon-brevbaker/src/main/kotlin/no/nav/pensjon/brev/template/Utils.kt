package no.nav.pensjon.brev.template

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import no.nav.pensjon.brev.converters.BrevbakerBrevdataModule
import no.nav.pensjon.brev.converters.BrevkodeModule
import no.nav.pensjon.brev.converters.LetterMarkupModule
import no.nav.pensjon.brev.converters.PrimitiveModule
import no.nav.pensjon.brev.converters.TemplateModelSpecificationModule

fun ObjectMapper.brevbakerConfig() {
    registerModule(JavaTimeModule())
    registerModule(BrevbakerBrevdataModule)
    registerModule(BrevkodeModule)
    registerModule(LetterMarkupModule)
    registerModule(TemplateModelSpecificationModule)
    registerModule(PrimitiveModule)
    disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
}

fun brevbakerJacksonObjectMapper() = com.fasterxml.jackson.module.kotlin.jacksonObjectMapper().apply { brevbakerConfig() }
