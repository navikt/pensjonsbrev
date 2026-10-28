package no.nav.pensjon.brev.skribenten.model

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class PenAvtalelandTest {
    @Test
    fun `tre-bokstavs landkode fra PEN er gyldig`() {
        val avtaleland = jacksonObjectMapper().readValue<List<Pen.Avtaleland>>(
            """[{"navn": "Storbritannia", "kode": "GBR"}, {"navn": "Sverige", "kode": "SWE"}]"""
        )
        assertThat(avtaleland.map { it.kode }).containsExactly("GBR", "SWE")
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "  ", "GB", "GBRR"])
    fun `ugyldig landkode avvises`(kode: String) {
        assertThrows<IllegalArgumentException> {
            Pen.Avtaleland("Storbritannia", kode)
        }
    }
}
