package no.nav.pensjon.brev.api.model.maler

import org.junit.Test


class BrevkodeTest {
    @Test
    fun `ingen koder har lengde over 50`() {
        assert(Brevkode.AutoBrev.entries.map { it.name.length }.none { it > 50 })
    }

}