package no.nav.pensjon.brev.model.alder

import org.junit.Test
import kotlin.test.assertTrue

class AldersbrevkodeTest {
    @Test
    fun `ingen auto-koder har lengde over 50`() {
        Aldersbrevkoder.AutoBrev.entries.filter { it.name.length > 50 }.let {
            assertTrue(
                it.isEmpty(),
                "Alle brevkoder må være under 50 lange for å kunne arkiveres. Disse feila: $it",
            )
        }
    }

    @Test
    fun `ingen redigerbare koder har lengde over 50`() {
        Aldersbrevkoder.Redigerbar.entries.filter { it.name.length > 50 }.let {
            assertTrue(
                it.isEmpty(),
                "Alle brevkoder må være under 50 lange for å kunne arkiveres. Disse feila: $it",
            )
        }
    }
}
