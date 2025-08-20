package no.nav.pensjon.brev

import org.junit.jupiter.api.Test

class AllTemplatesTest {
    @Test
    fun `alle maler skal bruke en unik brevkode`() {
        val malKoder = (pensjonOgUfoereProductionTemplates.hentAutobrevmaler() + pensjonOgUfoereProductionTemplates.hentRedigerbareMaler())
            .map { it.kode.kode() }

        malKoder.sorted().zipWithNext { a, b ->
            assert(a != b) { "Alle brevmaler må bruke egne unike brevkoder! Brevkode $a brukes i flere brev." }
        }
    }

}