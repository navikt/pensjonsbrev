package no.nav.pensjon.brev.skribenten.brevredigering.application.redigering

import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.brevredigering.application.BrevredigeringHandlerTestBase
import no.nav.pensjon.brev.skribenten.brevredigering.domain.MarkupVersjon
import no.nav.pensjon.brev.skribenten.letter.letter
import no.nav.pensjon.brev.skribenten.letter.v2.MarkupV2
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.BlockImpl.ParagraphImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class DiffBrevHandlerTest : BrevredigeringHandlerTestBase() {

    private val diffBrev by lazy { DiffBrevHandler(brevtilgang, brevdataService, brevmalService) }

    @BeforeEach
    fun stageV2() = stageMarkupV2()

    @ParameterizedTest
    @EnumSource(MarkupVersjon::class)
    suspend fun `diff bruker rendret brev fra siste merge og ikke gjeldende mal`(versjon: MarkupVersjon) {
        val brev = opprettBrev(markupVersjon = versjon).resultOrFail()
        assertThat(brev.redigertBrev).isEqualTo(rendretVedOpprettelse(versjon))

        brevbakerService.renderMarkupResultat = {
            letter(ParagraphImpl(1, true, listOf(LiteralImpl(1, "ny tekst i malen")))).medSignatur(
                saksbehandler = saksbehandler1Principal.fullName, attestant = null
            )
        }
        stageMarkupV2(MarkupV2.paragraph(1, MarkupV2.literal(1, "ny tekst i malen")))

        val diff = withPrincipal(saksbehandler1Principal) {
            diffBrev(DiffBrevHandler.Request(brev.info.id, sak1.saksId, brev.redigertBrev))
        }

        assertThat(diff).isNotNull()
        when (val resultat = diff!!.resultOrFail()) {
            is DiffBrevHandler.Resultat.V1 -> {
                assertThat(versjon).isEqualTo(MarkupVersjon.V1)
                val unified = resultat.response as DiffBrevHandler.Response.Unified
                assertThat(unified.editedBlocks).isEmpty()
                assertThat(unified.deletedBlocks).isEmpty()
            }
            is DiffBrevHandler.Resultat.V2 -> {
                assertThat(versjon).isEqualTo(MarkupVersjon.V2)
                val unified = resultat.response as DiffBrevHandler.ResponseV2.Unified
                assertThat(unified.editedBlocks).isEmpty()
                assertThat(unified.deletedBlocks).isEmpty()
            }
        }
    }
}
