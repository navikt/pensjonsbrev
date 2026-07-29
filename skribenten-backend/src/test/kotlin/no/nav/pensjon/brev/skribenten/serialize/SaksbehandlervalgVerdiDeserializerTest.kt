package no.nav.pensjon.brev.skribenten.serialize

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import no.nav.pensjon.brev.skribenten.model.RedigerbarSaksbehandlervalgMap
import no.nav.pensjon.brev.skribenten.model.SaksbehandlervalgVerdi
import no.nav.pensjon.brev.skribenten.skribentenServerJackson
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SaksbehandlervalgVerdiDeserializerTest {

    private val mapper = jacksonObjectMapper().skribentenServerJackson()

    @Test
    fun `deserialiserer tekst, tall, boolsk og null`() {
        val result = mapper.readValue<RedigerbarSaksbehandlervalgMap>(
            """{"tekst": "hei", "tall": 42, "boolsk": true, "nullverdi": null}"""
        )

        assertThat(result).containsExactlyInAnyOrderEntriesOf(
            mapOf(
                "tekst" to SaksbehandlervalgVerdi.String("hei"),
                "tall" to SaksbehandlervalgVerdi.Int(42),
                "boolsk" to SaksbehandlervalgVerdi.Boolean(true),
                "nullverdi" to null,
            )
        )
    }

    @Test
    fun `kaster en skikkelig deserialiseringsfeil for verdier som ikke er tekst, tall eller boolsk`() {
        val exception = assertThrows<MismatchedInputException> {
            mapper.readValue<RedigerbarSaksbehandlervalgMap>(
                """{"felt": {"nested": "verdi"}}"""
            )
        }

        assertThat(exception.message).contains("SaksbehandlervalgVerdi")
    }
}
