package no.nav.pensjon.brev.api.model.maler

import org.junit.Test
import kotlin.test.assertTrue


class PesysbrevkodeTest {
    @Test
    fun `ingen auto-koder har lengde over 50`() {
        Ufoerebrevkoder.AutoBrev.entries.filter { it.name.length > 50 }.let {
            assertTrue(it.isEmpty(), "Alle brevkoder må være under 50 lange for å kunne arkiveres. Disse feila: $it")
        }
    }

    @Test
    fun `ingen redigerbare koder har lengde over 50`() {
        Ufoerebrevkoder.Redigerbar.entries.filter { it.name.length > 50 }.let {
            assertTrue(it.isEmpty(), "Alle brevkoder må være under 50 lange for å kunne arkiveres. Disse feila: $it")
        }

    }

}