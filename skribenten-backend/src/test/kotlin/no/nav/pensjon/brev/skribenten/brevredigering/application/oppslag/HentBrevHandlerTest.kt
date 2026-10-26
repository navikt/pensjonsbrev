package no.nav.pensjon.brev.skribenten.brevredigering.application.oppslag
import no.nav.pensjon.brev.skribenten.brevredigering.application.BrevredigeringHandlerTestBase

import no.nav.pensjon.brev.skribenten.Testbrevkoder
import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.BrevmalendringType
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.db.BrevredigeringTable
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
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.junit.jupiter.api.Test

class HentBrevHandlerTest : BrevredigeringHandlerTestBase() {

    @Test
    suspend fun `kladd uten lagret brevmalHash rapporteres ikke som endret, og faar hash satt`() {
        val opprettet = opprettBrev(reserverForRedigering = false).resultOrFail()
        transaction { BrevredigeringTable.update({ BrevredigeringTable.id eq opprettet.info.id }) { it[brevmalHash] = null } }

        val hentet = hentBrev(brevId = opprettet.info.id, reserverForRedigering = true)

        assertThat(hentet).isSuccess {
            assertThat(it.brevmalEndret).isEqualTo(BrevmalendringType.INGEN_ENDRING)
        }
        assertThat(transaction { BrevredigeringEntity[opprettet.info.id].brevmalHash }).isNotNull
    }

    @Test
    suspend fun `kladd med lagret brevmalHash som avviker fra rendret rapporteres som endret`() {
        val opprettet = opprettBrev(reserverForRedigering = false).resultOrFail()

        assertThat(hentBrev(brevId = opprettet.info.id, reserverForRedigering = true)).isSuccess {
            assertThat(it.brevmalEndret).isEqualTo(BrevmalendringType.MARKUP_ENDRET)
        }
    }

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
            assertThat(it.redigertBrev).isEqualTo(letter.toEdit())
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
            assertThat(it.redigertBrev).isEqualTo(opprettet.redigertBrev.updateEditedLetter(freshRender))
        }
    }

    @Test
    suspend fun `returnerer null når brev ikke finnes`() {
        val hentet = hentBrev(brevId = BrevId(9999), reserverForRedigering = false)

        assertThat(hentet).isNull()
    }
}
