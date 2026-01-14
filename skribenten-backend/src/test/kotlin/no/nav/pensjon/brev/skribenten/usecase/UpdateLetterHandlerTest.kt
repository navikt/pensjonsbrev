package no.nav.pensjon.brev.skribenten.usecase

import no.nav.pensjon.brev.skribenten.Testbrevkoder
import no.nav.pensjon.brev.skribenten.assertFailure
import no.nav.pensjon.brev.skribenten.assertSuccess
import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.domain.BrevedigeringError
import no.nav.pensjon.brev.skribenten.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.domain.RedigerBrevPolicy
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
import no.nav.pensjon.brev.skribenten.services.brev.BrevdataService
import no.nav.pensjon.brev.skribenten.services.brev.RenderService
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.BlockImpl.ParagraphImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.TextImpl.VariableImpl
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.junit.jupiter.api.parallel.Isolated

@Execution(ExecutionMode.SAME_THREAD)
@Isolated
class UpdateLetterHandlerTest : BrevredigeringTest() {

    val oppdaterBrev = UpdateLetterHandler(
        redigerBrevPolicy = RedigerBrevPolicy(),
        brevreservasjonPolicy = BrevreservasjonPolicy(),
        renderService = RenderService(brevbakerService),
        brevdataService = BrevdataService(penService),
    )

    private val nyttRedigertBrev = editedLetter(Paragraph(1, true, listOf(Literal(1, text = "red pill", editedText = "blue pill"))))

    private suspend fun oppdaterBrev(
        brevId: Long,
        nyeSaksbehandlerValg: SaksbehandlerValg? = null,
        nyttRedigertbrev: Edit.Letter? = null,
        frigiReservasjon: Boolean = false,
    ): Result<Dto.Brevredigering, BrevedigeringError>? = newSuspendedTransaction {
        oppdaterBrev.handle(
            UpdateLetterHandler.Request(
                brevId = brevId,
                nyeSaksbehandlerValg = nyeSaksbehandlerValg,
                nyttRedigertbrev = nyttRedigertbrev,
                frigiReservasjon = frigiReservasjon
            )
        )
    }

    @Test
    suspend fun `kan oppdatere brevredigering`() {
        val saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg1", true) }
        val original = opprettBrev(reserverForRedigering = true, saksbehandlerValg = saksbehandlerValg)

        brevbakerService.renderMarkupKall.clear()

        val nyeValg = Api.GeneriskBrevdata().apply { put("valg2", true) }
        val oppdatert = withPrincipal(saksbehandler1Principal) {
            oppdaterBrev(brevId = original.info.id, nyeSaksbehandlerValg = nyeValg, nyttRedigertbrev = nyttRedigertBrev)
        }

        assertThat(brevbakerService.renderMarkupKall.first()).isEqualTo(Testbrevkoder.INFORMASJONSBREV to LanguageCode.ENGLISH)
        assertThat(brevbakerService.renderMarkupKall.size).isEqualTo(1)

        assertSuccess(oppdatert) {
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
        val original = opprettBrev(saksbehandlerValg = saksbehandlerValg)

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

        assertSuccess(oppdatert) {
            assertThat(it.redigertBrev).isNotEqualTo(original.redigertBrev)
            assertThat(it.redigertBrev).isEqualTo(original.redigertBrev.updateEditedLetter(freshRender))
        }
    }

    @Test
    suspend fun `saksbehandler kan ikke redigere brev som er laastForRedigering`() {
        val brev = opprettBrev()

        withPrincipal(saksbehandler1Principal) {
            delvisOppdaterBrev(brev, laastForRedigering = true)

            assertFailure<RedigerBrevPolicy.KanIkkeRedigere.LaastBrev>(
                oppdaterBrev(
                    brevId = brev.info.id,
                    nyeSaksbehandlerValg = null,
                    nyttRedigertbrev = nyttRedigertBrev,
                )
            )
        }
    }

    @Test
    suspend fun `attestant kan redigere brev som er laastForRedigering`() {
        val brev = opprettBrev(brevkode = Testbrevkoder.VEDTAKSBREV, vedtaksId = 123L)

        withPrincipal(saksbehandler1Principal) {
            delvisOppdaterBrev(brev, laastForRedigering = true)
        }

        val resultat = withPrincipal(attestantPrincipal) {
            oppdaterBrev(
                brevId = brev.info.id,
                nyttRedigertbrev = nyttRedigertBrev,
            )
        }

        assertSuccess(resultat) {
            assertThat(it.redigertBrev).isEqualTo(nyttRedigertBrev)
        }
    }

    @Test
    suspend fun `brev kan ikke endres om det er arkivert`() {
        val brev = opprettBrev(reserverForRedigering = true)

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
            assertFailure<RedigerBrevPolicy.KanIkkeRedigere.ArkivertBrev>(
                oppdaterBrev(
                    brevId = brev.info.id,
                    nyttRedigertbrev = nyttRedigertBrev,
                )
            )
        }
    }

    @Test
    suspend fun `brev kan ikke oppdateres av andre enn den som har reservert det for redigering`() {
        val brev = opprettBrev(reserverForRedigering = true)

        withPrincipal(saksbehandler2Principal) {
            assertFailure<BrevreservasjonPolicy.ReservertAvAnnen>(
                oppdaterBrev(
                    brevId = brev.info.id,
                    nyeSaksbehandlerValg = brev.saksbehandlerValg,
                    nyttRedigertbrev = nyttRedigertBrev,
                )
            )
        }
    }

    @Test
    suspend fun `brevreservasjon frigis ikke ved oppdatering`() {
        val brev = opprettBrev(reserverForRedigering = true)

        val oppdatertBrev = withPrincipal(saksbehandler1Principal) {
            oppdaterBrev(
                brevId = brev.info.id,
                nyeSaksbehandlerValg = brev.saksbehandlerValg,
                frigiReservasjon = false,
            )
        }
        assertSuccess(oppdatertBrev) {
            assertThat(it.info.redigeresAv).isEqualTo(saksbehandler1Principal.navIdent)
        }
    }

    @Test
    suspend fun `brevreservasjon kan frigis ved oppdatering`() {
        val brev = opprettBrev(principal = saksbehandler1Principal, reserverForRedigering = true)

        val oppdatertBrev = withPrincipal(saksbehandler1Principal) {
            oppdaterBrev(
                brevId = brev.info.id,
                nyeSaksbehandlerValg = brev.saksbehandlerValg,
                frigiReservasjon = true,
            )
        }

        assertSuccess(oppdatertBrev) {
            assertThat(it.info.redigeresAv).isNull()
        }
    }
}