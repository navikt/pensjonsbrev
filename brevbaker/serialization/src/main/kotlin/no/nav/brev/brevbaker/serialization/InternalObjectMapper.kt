package no.nav.brev.brevbaker.serialization

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule

/**
 *  [ObjectMapper]-en for *intern* trafikk mellom brevbaker, skribenten og pdf-bygger.
 *
 * - Ukjente felter ignoreres, slik at ny og gammel versjon kan snakke sammen under utrulling.
 * - Datoer skrives som ISO-8601-strenger (`2026-07-09`), likt det kotlinx produserte.
 * - `null` skrives ut eksplisitt, slik at «feltet mangler» og «feltet er null» ikke blir tvetydig.
 *   (`NON_NULL` ville også fungert teknisk, siden begge ender bruker Jackson med [KotlinModule].)
 */
fun internalObjectMapper(): ObjectMapper = ObjectMapper().internalJacksonConfig()

fun ObjectMapper.internalJacksonConfig(): ObjectMapper =
    registerModule(KotlinModule.Builder().build())
        .registerModule(JavaTimeModule())
        .registerModule(MarkupJacksonModule)
        .registerModule(LetterMarkupV1JacksonModule)
        .registerModule(TemplateModelSpecificationJacksonModule)
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
