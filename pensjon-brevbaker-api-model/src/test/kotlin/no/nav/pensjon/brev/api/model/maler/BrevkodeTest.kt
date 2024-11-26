package no.nav.pensjon.brev.api.model.maler

import org.junit.Test
import kotlin.test.assertTrue


class BrevkodeTest {
    @Test
    fun `ingen koder har lengde over 50`() {
        assertTrue(Pesysbrevkoder.AutoBrev.entries.map { it.name.length }.none { it > 50 }, "Alle brevkoder må være under 50 lange for å kunne arkiveres.")
    }

}