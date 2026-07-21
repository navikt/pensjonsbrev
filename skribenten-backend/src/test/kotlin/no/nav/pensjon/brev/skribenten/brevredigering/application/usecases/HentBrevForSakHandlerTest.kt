package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.common.asSuccess
import no.nav.pensjon.brev.skribenten.model.SaksId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class HentBrevForSakHandlerTest : BrevredigeringHandlerTestBase() {

    @Test
    suspend fun `kan hente brev for en sak`() {
        val annenSak = sak1.copy(saksId = SaksId(sak1.saksId.id + 1))
        val forventedeBrev = listOf(
            opprettBrev(sak = sak1),
            opprettBrev(sak = sak1),
        ).map { it.resultOrFail() }.map { it.info }.toSet()

        val ikkeForventetBrev = opprettBrev(sak = annenSak).resultOrFail().info

        val resultat = hentBrevForSak(sak1.saksId)?.asSuccess()?.toSet() ?: emptySet()

        assertThat(resultat).containsAll(forventedeBrev)
        assertThat(resultat).doesNotContain(ikkeForventetBrev)
    }
}
