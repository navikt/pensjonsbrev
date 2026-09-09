package no.nav.pensjon.brev.skribenten.openapi

import io.ktor.openapi.AdditionalProperties
import io.ktor.openapi.JsonSchema
import io.ktor.openapi.JsonType
import io.ktor.openapi.ReferenceOr
import no.nav.pensjon.brev.skribenten.model.RedigerbarSaksbehandlervalgMap
import no.nav.pensjon.brev.skribenten.model.SaksbehandlervalgMap
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class JacksonReflectionJsonSchemaInferenceTest {

    private val inference = JacksonReflectionJsonSchemaInference(JacksonSchemaReflectionAdapter(null))

    /**
     * [RedigerbarSaksbehandlervalgMap] is a concrete class that fixes its `Map` value type
     * (`SaksbehandlervalgVerdi?`) in its supertype declaration (`: LinkedHashMap<String, SaksbehandlervalgVerdi?>()`)
     * rather than declaring its own type parameters. Schema inference must resolve the value type
     * via the resolved `Map` supertype in that case - not just the property's own (empty) type
     * arguments - otherwise the OpenAPI schema (and generated TypeScript type) silently degrades to
     * an unconstrained `unknown`.
     */
    @Test
    fun `resolves map value type for a concrete non-generic Map subclass`() {
        val schema = inference.schemaForClass(RedigerbarSaksbehandlervalgMap::class)

        assertThat(schema.additionalProperties).isInstanceOf(AdditionalProperties.PSchema::class.java)
        val valueSchema = (((schema.additionalProperties as AdditionalProperties.PSchema).value) as ReferenceOr.Value<JsonSchema>).value

        // SaksbehandlervalgVerdi is a nullable sealed interface with 3 scalar subtypes (Boolean/Int/String),
        // i.e. equivalent to the TypeScript union `boolean | number | string | null`.
        val oneOfTypes = valueSchema.oneOf.orEmpty().map { (it as ReferenceOr.Value<JsonSchema>).value.type }
        assertThat(oneOfTypes).containsExactlyInAnyOrder(JsonType.BOOLEAN, JsonType.INTEGER, JsonType.STRING, JsonType.NULL)
    }

    /**
     * [SaksbehandlervalgMap]'s value type is genuinely unconstrained (`Any?`), so
     * `additionalProperties` should stay permissive rather than pointing at a bogus schema.
     */
    @Test
    fun `keeps genuinely unconstrained Any value type permissive`() {
        val schema = inference.schemaForClass(SaksbehandlervalgMap::class)

        assertThat(schema.additionalProperties).isEqualTo(AdditionalProperties.Allowed(true))
    }
}
