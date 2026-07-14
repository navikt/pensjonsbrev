package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.SharedPostgres
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brevbaker.api.model.AlltidValgbartVedleggBrevkode
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class HentAlltidValgbareVedleggHandlerTest : BrevredigeringHandlerTestBase() {
    private val handler by lazy { HentAlltidValgbareVedleggHandler(brevbakerService, SharedPostgres.database) }

    @Test
    suspend fun `henter alltid valgbare vedlegg sortert med spraaktilgjengelighet`() {
        val brev = opprettBrev().resultOrFail()
        brevbakerService.alltidValgbareVedleggResultat = setOf(
            AlltidValgbartVedleggBrevkode("kode-2", "B vedlegg", setOf(LanguageCode.ENGLISH)),
            AlltidValgbartVedleggBrevkode("kode-1", "A vedlegg", setOf(LanguageCode.BOKMAL)),
            AlltidValgbartVedleggBrevkode("kode-3", "C vedlegg", setOf(LanguageCode.BOKMAL, LanguageCode.ENGLISH)),
        )

        assertThat(handler(HentAlltidValgbareVedleggHandler.Request(brev.info.id))).isSuccess {
            assertThat(it).containsExactly(
                ValgbartVedlegg("kode-1", "A vedlegg", setOf(LanguageCode.BOKMAL), false),
                ValgbartVedlegg("kode-2", "B vedlegg", setOf(LanguageCode.ENGLISH), true),
                ValgbartVedlegg("kode-3", "C vedlegg", setOf(LanguageCode.BOKMAL, LanguageCode.ENGLISH), true),
            )
        }
    }
}
