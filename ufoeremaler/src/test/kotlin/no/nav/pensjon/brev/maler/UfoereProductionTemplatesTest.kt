package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.UfoereProductionTemplates
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.Ufoerebrevkoder
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.collections.filterNot

class UfoereProductionTemplatesTest {

    @Test
    fun `alle autobrev fins i production templates`() {
        val brukteKoder = UfoereProductionTemplates.hentAutobrevmaler().map { it.kode }
        val ubrukteKoder = Ufoerebrevkoder.AutoBrev.entries.filterNot { brukteKoder.contains(it) }
        Assertions.assertEquals(ubrukteKoder, listOf<Brevkode.Automatisk>())
    }

    @Test
    fun `alle redigerbare brev fins i production templates`() {
        val brukteKoder = UfoereProductionTemplates.hentRedigerbareMaler().map { it.kode }
        val ubrukteKoder = Ufoerebrevkoder.Redigerbar.entries.filterNot { brukteKoder.contains(it) }
        Assertions.assertEquals(ubrukteKoder, listOf<Brevkode.Redigerbart>())
    }
}