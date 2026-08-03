package no.nav.brev.brevbaker.jackson

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule

/**
 * Den kanoniske [ObjectMapper]-en for *intern* trafikk mellom brevbaker, skribenten og pdf-bygger.
 *
 * Bruk denne – ikke en global/delt mapper – for alt som går på tvers av disse tjenestene. Da er
 * oppsettet likt i begge ender, og [KotlinModule] (som trengs for value classes som
 * `Markup.Personidentifikator` og for default-verdier i konstruktører) påvirker ikke annen
 * Jackson-bruk i konsumentene.
 *
 * - Ukjente felter ignoreres, slik at ny og gammel versjon kan snakke sammen under utrulling.
 * - Datoer skrives som ISO-8601-strenger (`2026-07-09`), likt det kotlinx produserte.
 * - `null` skrives ut eksplisitt, slik at «feltet mangler» og «feltet er null» ikke blir tvetydig.
 *   (`NON_NULL` ville også fungert teknisk, siden begge ender bruker Jackson med [KotlinModule].)
 */
fun internalObjectMapper(): ObjectMapper =
    ObjectMapper()
        .registerModule(KotlinModule.Builder().build())
        .registerModule(JavaTimeModule())
        .registerModule(MarkupJacksonModule)
        .registerModule(LetterMarkupV1JacksonModule)
        .registerModule(TemplateModelSpecificationJacksonModule)
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

/** Delt, trådsikker instans av [internalObjectMapper]. */
val InternalObjectMapper: ObjectMapper by lazy { internalObjectMapper() }
