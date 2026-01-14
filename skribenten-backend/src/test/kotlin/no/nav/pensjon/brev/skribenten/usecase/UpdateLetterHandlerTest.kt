package no.nav.pensjon.brev.skribenten.usecase

import no.nav.pensjon.brev.skribenten.Testbrevkoder
import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.db.Hash
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.domain.RedigerBrevPolicy
import no.nav.pensjon.brev.skribenten.isFailure
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.Edit.Block.Paragraph
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.Literal
import no.nav.pensjon.brev.skribenten.letter.editedLetter
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.letter.updateEditedLetter
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.model.SaksbehandlerValg
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.BlockImpl.ParagraphImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.TextImpl.VariableImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.junit.jupiter.api.parallel.Isolated

class UpdateLetterHandlerTest : BrevredigeringTest() {
    private val nyttRedigertBrev = editedLetter(Paragraph(1, true, listOf(Literal(1, text = "red pill", editedText = "blue pill"))))

    private suspend fun oppdaterBrev(
        brevId: Long,
        nyeSaksbehandlerValg: SaksbehandlerValg? = null,
        nyttRedigertbrev: Edit.Letter? = null,
        frigiReservasjon: Boolean = false,
    ): Result<Dto.Brevredigering, BrevredigeringError>? =
        brevredigeringFacade.oppdaterBrev(
            UpdateLetterHandler.Request(
                brevId = brevId,
                nyeSaksbehandlerValg = nyeSaksbehandlerValg,
                nyttRedigertbrev = nyttRedigertbrev,
                frigiReservasjon = frigiReservasjon
            )
        )

    @Test
    suspend fun `kan oppdatere brevredigering`() {
        val saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg1", true) }
        val original = opprettBrev(reserverForRedigering = true, saksbehandlerValg = saksbehandlerValg).resultOrThrow()

        brevbakerService.renderMarkupKall.clear()

        val nyeValg = Api.GeneriskBrevdata().apply { put("valg2", true) }
        val oppdatert = withPrincipal(saksbehandler1Principal) {
            oppdaterBrev(brevId = original.info.id, nyeSaksbehandlerValg = nyeValg, nyttRedigertbrev = nyttRedigertBrev)
        }

        assertThat(brevbakerService.renderMarkupKall.first()).isEqualTo(Testbrevkoder.INFORMASJONSBREV to LanguageCode.ENGLISH)
        assertThat(brevbakerService.renderMarkupKall.size).isEqualTo(1)

        assertThat(oppdatert).isSuccess {
            assertThat(it.saksbehandlerValg).isNotEqualTo(original.saksbehandlerValg)
            assertThat(it.redigertBrev).isEqualTo(nyttRedigertBrev)
        }
    }

    @Test
    suspend fun `kan ikke oppdatere brevredigering som ikke eksisterer`() {
        val saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg1", true) }
        val oppdatert = withPrincipal(saksbehandler1Principal) {
            oppdaterBrev(
                brevId = 1099,
                nyeSaksbehandlerValg = saksbehandlerValg,
                nyttRedigertbrev = nyttRedigertBrev,
            )
        }
        assertThat(oppdatert).isNull()
    }

    @Test
    suspend fun `oppdaterer redigertBrev med fersk rendering fra brevbaker`() {
        val saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg1", true) }
        val original = opprettBrev(saksbehandlerValg = saksbehandlerValg).resultOrThrow()

        val nyeValg = Api.GeneriskBrevdata().apply { put("valg2", true) }
        val freshRender = letter.copy(
            blocks = letter.blocks + ParagraphImpl(2, true, listOf(VariableImpl(21, "ny paragraph")))
        )
        brevbakerService.renderMarkupResultat = { freshRender }

        val oppdatert = withPrincipal(saksbehandler1Principal) {
            oppdaterBrev(
                brevId = original.info.id,
                nyeSaksbehandlerValg = nyeValg,
                nyttRedigertbrev = letter.toEdit(),
            )
        }

        assertThat(oppdatert).isSuccess {
            assertThat(it.redigertBrev).isNotEqualTo(original.redigertBrev)
            assertThat(it.redigertBrev).isEqualTo(original.redigertBrev.updateEditedLetter(freshRender))
        }
    }

    @Test
    suspend fun `saksbehandler kan ikke redigere brev som er laastForRedigering`() {
        val brev = opprettBrev().resultOrThrow()

        val result = withPrincipal(saksbehandler1Principal) {
            delvisOppdaterBrev(brev, laastForRedigering = true)

            oppdaterBrev(
                brevId = brev.info.id,
                nyeSaksbehandlerValg = null,
                nyttRedigertbrev = nyttRedigertBrev,
            )
        }

        assertThat(result).isFailure<RedigerBrevPolicy.KanIkkeRedigere.LaastBrev, _, _>()
    }

    @Test
    suspend fun `attestant kan redigere brev som er laastForRedigering`() {
        val brev = opprettBrev(brevkode = Testbrevkoder.VEDTAKSBREV, vedtaksId = 123L).resultOrThrow()

        withPrincipal(saksbehandler1Principal) {
            delvisOppdaterBrev(brev, laastForRedigering = true)
        }

        val resultat = withPrincipal(attestantPrincipal) {
            oppdaterBrev(
                brevId = brev.info.id,
                nyttRedigertbrev = nyttRedigertBrev,
            )
        }

        assertThat(resultat).isSuccess {
            assertThat(it.redigertBrev).isEqualTo(nyttRedigertBrev)
        }
    }

    @Test
    suspend fun `brev kan ikke endres om det er arkivert`() {
        val brev = opprettBrev(reserverForRedigering = true).resultOrThrow()

        withPrincipal(saksbehandler1Principal) {
            val pdf = hentEllerOpprettPdf(brev)
            assertThat(pdf).isNotNull()
            delvisOppdaterBrev(brev, laastForRedigering = true)
        }

        penService.sendBrevResponse = Pen.BestillBrevResponse(
            991,
            Pen.BestillBrevResponse.Error(null, "Distribuering feilet", null)
        )

        sendBrev(brev)

        withPrincipal(saksbehandler1Principal) {
            assertThat(
                oppdaterBrev(
                    brevId = brev.info.id,
                    nyttRedigertbrev = nyttRedigertBrev,
                )
            ).isFailure<RedigerBrevPolicy.KanIkkeRedigere.ArkivertBrev, _, _>()
        }
    }

    @Test
    suspend fun `brev kan ikke oppdateres av andre enn den som har reservert det for redigering`() {
        val brev = opprettBrev(reserverForRedigering = true).resultOrThrow()

        withPrincipal(saksbehandler2Principal) {
            assertThat(
                oppdaterBrev(
                    brevId = brev.info.id,
                    nyeSaksbehandlerValg = brev.saksbehandlerValg,
                    nyttRedigertbrev = nyttRedigertBrev,
                )
            ).isFailure<BrevreservasjonPolicy.ReservertAvAnnen, _, _>()
        }
    }

    @Test
    suspend fun `brevreservasjon frigis ikke ved oppdatering`() {
        val brev = opprettBrev(reserverForRedigering = true).resultOrThrow()

        val oppdatertBrev = withPrincipal(saksbehandler1Principal) {
            oppdaterBrev(
                brevId = brev.info.id,
                nyeSaksbehandlerValg = brev.saksbehandlerValg,
                frigiReservasjon = false,
            )
        }
        assertThat(oppdatertBrev).isSuccess {
            assertThat(it.info.redigeresAv).isEqualTo(saksbehandler1Principal.navIdent)
        }
    }

    @Test
    suspend fun `brevreservasjon kan frigis ved oppdatering`() {
        val brev = opprettBrev(principal = saksbehandler1Principal, reserverForRedigering = true).resultOrThrow()

        val oppdatertBrev = withPrincipal(saksbehandler1Principal) {
            oppdaterBrev(
                brevId = brev.info.id,
                nyeSaksbehandlerValg = brev.saksbehandlerValg,
                frigiReservasjon = true,
            )
        }

        assertThat(oppdatertBrev).isSuccess {
            assertThat(it.info.redigeresAv).isNull()
        }
    }

    @Test
    suspend fun `oppdatering av redigertBrev endrer ogsaa redigertBrevHash`() {
        val brev = opprettBrev(reserverForRedigering = true).resultOrThrow()

        val result = withPrincipal(saksbehandler1Principal) {
            oppdaterBrev(
                brevId = brev.info.id,
                nyttRedigertbrev = nyttRedigertBrev,
            )
        }
        assertThat(result).isSuccess {
            assertThat(it.redigertBrevHash).isNotEqualTo(brev.redigertBrevHash)
            assertThat(it.redigertBrevHash).isEqualTo(Hash.read(nyttRedigertBrev))
        }
    }

}