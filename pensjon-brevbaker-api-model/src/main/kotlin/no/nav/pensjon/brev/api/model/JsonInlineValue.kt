package no.nav.pensjon.brev.api.model

import com.fasterxml.jackson.annotation.JsonValue

/**
 * Interface for wrapping json scalar values (String, Number, etc.) in a class.
 * Given:
 *      data class SpecialNumber(override val value: Int): JsonInlineValue<Int>
 *      data class Data(val counter: SpecialNumber)
 *
 *      val data = Data(SpecialNumber(42))
 *
 * will be serialized to, and can deserialize:
 *      {
 *          "counter": 42
 *      }
 *
 * TODO: Can be replaced by kotlin value class when pesys uses a newer kotlin version.
 */
interface JsonInlineValue<T> {
    @get:JsonValue
    val value: T
}
