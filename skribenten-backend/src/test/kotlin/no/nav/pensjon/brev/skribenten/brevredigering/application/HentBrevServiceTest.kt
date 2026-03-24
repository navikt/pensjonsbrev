package no.nav.pensjon.brev.skribenten.brevredigering.application

import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.BrevredigeringHandlerTestBase
import no.nav.pensjon.brev.skribenten.model.SaksId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

// Utvider BrevredigeringHandlerTestBase for å kunne gjenbruke fixtures derfra.
class HentBrevServiceTest : BrevredigeringHandlerTestBase() {

    private val hentBrevService: HentBrevService = brevredigeringFacade

    @Test
    suspend fun `kan hente brev for flere saker`() {
        val sak2 = sak1.copy(saksId = SaksId(sak1.saksId.id + 1))
        val sak3 = sak1.copy(saksId = SaksId(sak2.saksId.id + 1))
        val forventedeBrev = listOf(
            opprettBrev(sak = sak1),
            opprettBrev(sak = sak1),
            opprettBrev(sak = sak2)
        ).map { it.resultOrFail() }.map { it.info }.toSet()

        val ikkeForventetBrev = opprettBrev(sak = sak3).resultOrFail().info

        val resultat = hentBrevService.hentBrevForAlleSaker(setOf(sak1.saksId, sak2.saksId)).toSet()
        assertThat(resultat).containsAll(forventedeBrev)
        assertThat(resultat).doesNotContain(ikkeForventetBrev)
    }
}