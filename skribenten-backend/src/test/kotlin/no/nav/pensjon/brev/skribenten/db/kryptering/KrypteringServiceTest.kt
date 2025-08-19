package no.nav.pensjon.brev.skribenten.db.kryptering

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class KrypteringServiceTest {

    private val service = KrypteringService("PZUgqsFjAc3GViO47HHMVQ==")

    @Test
    fun `krypter data og les tilbake`() {
        val bytes = "Test string".encodeToByteArray()

        val kryptertBytes = service.krypter(bytes)
        assertThat(kryptertBytes).isNotEqualTo(bytes)

        val dekryptertBytes = service.dekrypter(kryptertBytes)
        assertThat(dekryptertBytes).isEqualTo(bytes)
    }
}