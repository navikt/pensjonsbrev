package no.nav.pensjon.brev.skribenten.brevredigering.application.oppslag

import no.nav.pensjon.brev.skribenten.brevredigering.domain.asV2
import no.nav.pensjon.brev.skribenten.brevredigering.domain.MarkupVersjon
import no.nav.pensjon.brev.skribenten.letter.v2.MarkupV2
import no.nav.pensjon.brev.skribenten.letter.v2.letter as letterV2
import no.nav.pensjon.brev.skribenten.brevredigering.domain.asV1
import no.nav.pensjon.brev.skribenten.brevredigering.application.BrevredigeringHandlerTestBase

import no.nav.pensjon.brev.skribenten.Testbrevkoder
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.letter.updateEditedLetter
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.SaksbehandlervalgMap
import no.nav.pensjon.brev.skribenten.model.SaksbehandlervalgVerdi
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.BlockImpl.ParagraphImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.TextImpl.VariableImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class HentBrevHandlerTest : BrevredigeringHandlerTestBase() {

    @Test
    suspend fun `kan hente brev uten reservasjon`() {
        val saksbehandlerValg = SaksbehandlervalgMap().apply { put("valg1", SaksbehandlervalgVerdi.Boolean(true)) }
        val opprettet = opprettBrev(reserverForRedigering = false, saksbehandlerValg = saksbehandlerValg).resultOrFail()

        brevbakerService.renderMarkupKall.clear()

        val hentet = hentBrev(brevId = opprettet.info.id, reserverForRedigering = false)

        assertThat(brevbakerService.renderMarkupKall).isEmpty()
        assertThat(hentet).isSuccess {
            assertThat(it.info.id).isEqualTo(opprettet.info.id)
            assertThat(it.saksbehandlerValg).isEqualTo(saksbehandlerValg)
            assertThat(it.redigertBrev).isEqualTo(opprettet.redigertBrev)
        }
    }

    @Test
    suspend fun `kan hente brev med reservasjon`() {
        val saksbehandlerValg = SaksbehandlervalgMap().apply { put("valg1", SaksbehandlervalgVerdi.Boolean(true)) }
        val opprettet = opprettBrev(reserverForRedigering = false, saksbehandlerValg = saksbehandlerValg).resultOrFail()

        brevbakerService.renderMarkupKall.clear()

        val hentet = hentBrev(brevId = opprettet.info.id, reserverForRedigering = true)

        assertThat(brevbakerService.renderMarkupKall.first()).isEqualTo(Testbrevkoder.INFORMASJONSBREV to LanguageCode.ENGLISH)
        assertThat(brevbakerService.renderMarkupKall.size).isEqualTo(1)

        assertThat(hentet).isSuccess {
            assertThat(it.info.id).isEqualTo(opprettet.info.id)
            assertThat(it.saksbehandlerValg).isEqualTo(saksbehandlerValg)
            assertThat(it.redigertBrev.asV1()).isEqualTo(letter.toEdit())
            assertThat(it.info.redigeresAv).isEqualTo(saksbehandler1Principal.navIdent)
        }
    }

    @Test
    suspend fun `hent brev med reservasjon merger inn rendret brev`() {
        val opprettet = opprettBrev(reserverForRedigering = false).resultOrFail()
        val freshRender = letter.copy(
            blocks = letter.blocks + ParagraphImpl(2, true, listOf(VariableImpl(21, "ny paragraph")))
        )
        brevbakerService.renderMarkupResultat = { freshRender }

        val hentet = hentBrev(brevId = opprettet.info.id, reserverForRedigering = true)

        assertThat(hentet).isSuccess {
            assertThat(it.redigertBrev.asV1()).isEqualTo(opprettet.redigertBrev.asV1().updateEditedLetter(freshRender))
        }
    }

    @Test
    suspend fun `hent v2-brev med reservasjon merger inn rendret brev`() {
        stageMarkupV2()
        val opprettet = opprettBrev(reserverForRedigering = false, markupVersjon = MarkupVersjon.V2).resultOrFail()
        val freshBlocks = arrayOf(
            MarkupV2.paragraph(1, MarkupV2.literal(1, "red pill")),
            MarkupV2.paragraph(2, MarkupV2.variable(21, "ny paragraph")),
        )
        stageMarkupV2(*freshBlocks)

        val hentet = hentBrev(brevId = opprettet.info.id, reserverForRedigering = true)

        assertThat(hentet).isSuccess {
            assertThat(it.redigertBrev.asV2().blocks).hasSize(2)
            assertThat(it.redigertBrev.asV2().blocks).isEqualTo(opprettet.redigertBrev.asV2().merge(letterV2(*freshBlocks)).blocks)
        }
    }

    @Test
    suspend fun `returnerer null når brev ikke finnes`() {
        val hentet = hentBrev(brevId = BrevId(9999), reserverForRedigering = false)

        assertThat(hentet).isNull()
    }
}
