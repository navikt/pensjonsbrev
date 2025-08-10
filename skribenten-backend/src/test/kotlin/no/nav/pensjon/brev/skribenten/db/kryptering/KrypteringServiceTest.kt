package no.nav.pensjon.brev.skribenten.db.kryptering

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.nio.charset.Charset

class KrypteringServiceTest {

    private val service = KrypteringService("PZUgqsFjAc3GViO47HHMVQ==")

    @Test
    fun `krypter data og les tilbake`() {
        val bytes = "Test string".toByteArray(Charset.defaultCharset())

        val kryptertBytes = service.krypterData(DekryptertByteArray(bytes))
        assertThat(kryptertBytes.byteArray).isNotEqualTo(bytes)

        val dekryptertBytes = service.dekrypterData(kryptertBytes)
        assertThat(dekryptertBytes.byteArray).isEqualTo(bytes)
    }
}