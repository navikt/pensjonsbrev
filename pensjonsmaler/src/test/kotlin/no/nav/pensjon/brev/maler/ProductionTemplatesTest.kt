package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ProductionTemplatesTest {

    @Test
    fun `alle autobrev fins i production templates`() {
        val brukteKoder = ProductionTemplates.hentAutobrevmaler().map { it.kode }
        val ubrukteKoder = Pesysbrevkoder.AutoBrev.entries.filterNot { brukteKoder.contains(it) }
        Assertions.assertEquals(ubrukteKoder, listOf<Brevkode.Automatisk>())
    }

    @Test
    fun `alle redigerbare brev fins i production templates`() {
        val brukteKoder = ProductionTemplates.hentRedigerbareMaler().map { it.kode }
        val ubrukteKoder = Pesysbrevkoder.Redigerbar.entries.filterNot { brukteKoder.contains(it) }
        Assertions.assertEquals(ubrukteKoder, listOf<Brevkode.Redigerbart>())
    }
}