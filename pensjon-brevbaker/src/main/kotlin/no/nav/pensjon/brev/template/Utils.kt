package no.nav.pensjon.brev.template

import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import no.nav.pensjon.brev.converters.BrevbakerBrevdataModule
import no.nav.pensjon.brev.converters.BrevkodeModule
import no.nav.pensjon.brev.converters.LetterMarkupModule
import no.nav.pensjon.brev.converters.PDFRequestModule
import no.nav.pensjon.brev.converters.TemplateModelSpecificationModule
import no.nav.pensjon.brev.pdfbygger.FellesModule

fun ObjectMapper.brevbakerConfig() {
    registerModule(JavaTimeModule())
    registerModule(BrevbakerBrevdataModule)
    registerModule(BrevkodeModule)
    registerModule(LetterMarkupModule)
    registerModule(FellesModule)
    registerModule(PDFRequestModule)
    registerModule(TemplateModelSpecificationModule)
    enable(SerializationFeature.INDENT_OUTPUT)
    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
}

fun jacksonObjectMapper() = com.fasterxml.jackson.module.kotlin.jacksonObjectMapper().apply { brevbakerConfig() }
