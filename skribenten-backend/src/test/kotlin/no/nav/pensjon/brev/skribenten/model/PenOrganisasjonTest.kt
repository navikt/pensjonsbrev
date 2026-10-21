package no.nav.pensjon.brev.skribenten.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class PenOrganisasjonTest {
    @Test
    fun `ni siffer er gyldig`() {
        Pen.SendRedigerbartBrevRequest.Mottaker.Organisasjon("987654321")
    }

    @Test
    fun `normaliserer TSS orgnummer med 00-prefiks`() {
        assertThat(Pen.SendRedigerbartBrevRequest.Mottaker.Organisasjon.normaliser("00987654321"))
            .isEqualTo("987654321")
    }

    @ParameterizedTest
    @ValueSource(strings = ["", " ", "12345678", "1234567890", "12345678a", " 987654321", "987 654 321"])
    fun `ugyldig orgnummer avvises`(orgNr: String) {
        assertThrows<IllegalArgumentException> {
            Pen.SendRedigerbartBrevRequest.Mottaker.Organisasjon(orgNr)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["", " ", "12345678", "1234567890", "01987654321", "000987654321", "00abcdef123"])
    fun `normalisering avviser ugyldige orgnummer`(orgNr: String) {
        assertThat(Pen.SendRedigerbartBrevRequest.Mottaker.Organisasjon.normaliser(orgNr)).isNull()
    }
}
