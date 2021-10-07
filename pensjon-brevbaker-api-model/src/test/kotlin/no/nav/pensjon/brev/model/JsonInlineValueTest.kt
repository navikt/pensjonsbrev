package no.nav.pensjon.brev.model

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.pensjon.brev.api.model.JsonInlineValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

class JsonInlineValueTest {

    val jackson = jacksonObjectMapper()

    private data class SpecialNumber(override val value: Int): JsonInlineValue<Int>
    private data class Data(val counter: SpecialNumber)

    private val data = Data(SpecialNumber(42))

    private val dataJson = """{"counter":42}"""

    @Test
    fun `can serialize to json scalar value`() {
        val actual = jackson.writeValueAsString(data).also { println(it) }
        assertEquals(dataJson, actual)
    }

    @Test
    fun `can deserialize from json scalar value`() {
        val actual = jackson.readValue(dataJson, Data::class.java)

        assertEquals(data, actual)
    }

}