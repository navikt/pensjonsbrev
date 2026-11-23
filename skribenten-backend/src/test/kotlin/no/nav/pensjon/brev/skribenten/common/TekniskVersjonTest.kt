package no.nav.pensjon.brev.skribenten.common

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TekniskVersjonTest {
    @Test
    fun `godtar positive versjoner`() {
        assertThat(TekniskVersjon(1).value).isEqualTo(1)
        assertThat(TekniskVersjon(Int.MAX_VALUE).value).isEqualTo(Int.MAX_VALUE)
    }

    @Test
    fun `avviser null og negative versjoner`() {
        listOf(0, -1, Int.MIN_VALUE).forEach { value ->
            assertThrows<IllegalArgumentException> { TekniskVersjon(value) }
        }
    }
}
