package no.nav.pensjon.brev.skribenten.model

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class PenOrganisasjonTest {
    @Test
    fun `ni siffer er gyldig`() {
        Pen.SendRedigerbartBrevRequest.Mottaker.Organisasjon(OrgNr("987654321"))
    }

    @Test
    fun `organisasjonens orgNr serialiseres fortsatt som en streng`() {
        val json = jacksonObjectMapper().writeValueAsString(
            Pen.SendRedigerbartBrevRequest.Mottaker.Organisasjon(OrgNr("987654321")),
        )

        assertThat(jacksonObjectMapper().readTree(json)["orgNr"].asText()).isEqualTo("987654321")
    }

    @Test
    fun `normaliserer TSS orgnummer med 00-prefiks`() {
        assertThat(OrgNr.normaliser("00987654321")).isEqualTo(OrgNr("987654321"))
    }

    @ParameterizedTest
    @ValueSource(strings = ["", " ", "12345678", "1234567890", "12345678a", " 987654321", "987 654 321"])
    fun `ugyldig orgnummer avvises`(orgNr: String) {
        assertThrows<IllegalArgumentException> {
            OrgNr(orgNr)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["", " ", "12345678", "1234567890", "01987654321", "000987654321", "00abcdef123"])
    fun `normalisering avviser ugyldige orgnummer`(orgNr: String) {
        assertThat(OrgNr.normaliser(orgNr)).isNull()
    }
}
