package no.nav.pensjon.brev.brevoppskrift

import com.fasterxml.jackson.core.StreamReadConstraints
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule

val draftObjectMapper: JsonMapper = JsonMapper.builder()
    .addModule(KotlinModule.Builder().enable(KotlinFeature.StrictNullChecks).build())
    .addModule(JavaTimeModule())
    .enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
    .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    .disable(MapperFeature.ALLOW_COERCION_OF_SCALARS)
    .build()
    .apply {
        factory.setStreamReadConstraints(StreamReadConstraints.builder().maxNestingDepth(100).build())
    }
