package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.common.asSuccess
import no.nav.pensjon.brev.skribenten.model.BrevId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class HentBrevInfoHandlerTest : BrevredigeringHandlerTestBase() {

    @Test
    suspend fun `kan hente brevinfo for eksisterende brev`() {
        val opprettet = opprettBrev().resultOrFail()

        val hentet = hentBrevInfo(opprettet.info.id)

        assertThat(hentet?.asSuccess()?.value).isEqualTo(opprettet.info)
    }

    @Test
    suspend fun `returnerer null naar brev ikke finnes`() {
        val hentet = hentBrevInfo(BrevId(9999))

        assertThat(hentet).isNull()
    }
}
