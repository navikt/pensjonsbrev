package no.nav.pensjon.brev.template

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import no.nav.brev.brevbaker.serialization.internalJacksonConfig
import no.nav.pensjon.brev.converters.BrevbakerBrevdataModule
import no.nav.pensjon.brev.converters.BrevkodeModule

fun ObjectMapper.brevbakerConfig() {
    internalJacksonConfig()
    registerModule(BrevbakerBrevdataModule)
    registerModule(BrevkodeModule)
    enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
}

fun brevbakerJacksonObjectMapper(): ObjectMapper = ObjectMapper().apply { brevbakerConfig() }
