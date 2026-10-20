package no.nav.pensjon.brev.skribenten.brevredigering.application.redigering

import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.brevredigering.application.BrevredigeringHandlerTestBase
import no.nav.pensjon.brev.skribenten.letter.letter
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.BlockImpl.ParagraphImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DiffBrevHandlerTest : BrevredigeringHandlerTestBase() {

    private val diffBrev by lazy { DiffBrevHandler(brevtilgang, brevdataService, brevmalService) }

    @Test
    suspend fun `diff bruker rendret brev fra siste merge og ikke gjeldende mal`() {
        val brev = opprettBrev().resultOrFail()
        val rendretVedOpprettelse = letter.toEdit()
        assertThat(brev.redigertBrev).isEqualTo(rendretVedOpprettelse)

        brevbakerService.renderMarkupResultat = {
            letter(ParagraphImpl(1, true, listOf(LiteralImpl(1, "ny tekst i malen")))).medSignatur(
                saksbehandler = saksbehandler1Principal.fullName, attestant = null
            )
        }

        val diff = withPrincipal(saksbehandler1Principal) {
            diffBrev(DiffBrevHandler.Request(brev.info.id, sak1.saksId, brev.redigertBrev))
        }

        assertThat(diff).isNotNull()
        val unified = diff!!.resultOrFail() as DiffBrevHandler.Response.Unified
        assertThat(unified.editedBlocks).isEmpty()
        assertThat(unified.deletedBlocks).isEmpty()
    }
}
