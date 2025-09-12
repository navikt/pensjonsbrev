package no.nav.pensjon.brev.maler.vedlegg.pdf

import no.nav.pensjon.brev.fixtures.createP1Dto
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class P1SomDSLTest {

    @Test
    fun P1SomDSL() {
        val somDSL = createP1Dto().somPDFVedlegg()
        assertEquals(8, somDSL.sider.size)
        assertFalse { somDSL.sider.map { it.felt }.any { it.isEmpty()} }
    }

}