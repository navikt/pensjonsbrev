package no.nav.pensjon.brev.skribenten.brevredigering.application

import no.nav.brev.brevbaker.StableHashCode
import no.nav.pensjon.brev.skribenten.Features
import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.brevredigering.application.vedlegg.EndreRedigertVedleggHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.vedlegg.HentRedigerbareVedleggHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.vedlegg.HentRedigertVedleggHandler
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.letter.updateEditedAttachment
import no.nav.pensjon.brev.skribenten.letter.updateEditedLetter
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.override
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.BlockImpl.ParagraphImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.TextImpl.VariableImpl
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ForhindreOppdateringAvKladdTest : BrevredigeringHandlerTestBase() {

    private val uendretVedlegg = VedleggId("uendret")
    private val endretVedlegg = VedleggId("endret")
    private val vedleggUtenHash = VedleggId("utenHash")

    @BeforeEach
    fun slaaPaaToggle() {
        Features.override(Features.forhindreOppdateringAvKladd, true)
    }

    @AfterEach
    fun slaaAvToggle() {
        Features.override(Features.forhindreOppdateringAvKladd, false)
    }

    private fun malVedlegg(tekst: String, hash: Int): LetterMarkup.Attachment =
        LetterMarkupImpl.AttachmentImpl(
            title = listOf(LiteralImpl(1, "Tittel")),
            blocks = listOf(ParagraphImpl(2, true, listOf(LiteralImpl(21, tekst)))),
            includeSakspart = false,
            stableHashCode = StableHashCode(hash),
        )

    private fun lagreVedlegg(brevId: BrevId, vedleggId: VedleggId, mal: LetterMarkup.Attachment, lagreMalHash: Boolean = true) =
        transaction {
            BrevredigeringEntity[brevId].apply {
                settRedigertVedlegg(vedleggId, mal.toEdit())
                if (lagreMalHash) settBrevmalForRedigertVedlegg(vedleggId, mal)
            }
        }

    private fun lagretVedlegg(brevId: BrevId, vedleggId: VedleggId): Edit.Attachment =
        transaction { BrevredigeringEntity[brevId].hentRedigertVedlegg(vedleggId)!! }

    private suspend fun brevMedVedleggOgNyeMaler(): Pair<Dto.Brevredigering, Map<VedleggId, LetterMarkup.Attachment>> {
        val brev = opprettBrev().resultOrFail()
        lagreVedlegg(brev.info.id, uendretVedlegg, malVedlegg("uendret", 1))
        lagreVedlegg(brev.info.id, endretVedlegg, malVedlegg("endret", 2))
        lagreVedlegg(brev.info.id, vedleggUtenHash, malVedlegg("uten hash", 3), lagreMalHash = false)

        val nyeMaler = mapOf(
            uendretVedlegg to malVedlegg("uendret ny tekst", 1),
            endretVedlegg to malVedlegg("endret ny tekst", 22),
            vedleggUtenHash to malVedlegg("uten hash ny tekst", 3),
        )
        brevbakerService.renderRedigerbareVedleggResultat = nyeMaler
        return brev to nyeMaler
    }

    private fun assertVedleggFlettetPerVedlegg(brevId: BrevId, nyeMaler: Map<VedleggId, LetterMarkup.Attachment>) {
        assertThat(lagretVedlegg(brevId, uendretVedlegg))
            .isEqualTo(malVedlegg("uendret", 1).toEdit().updateEditedAttachment(nyeMaler.getValue(uendretVedlegg)))
        assertThat(lagretVedlegg(brevId, vedleggUtenHash))
            .isEqualTo(malVedlegg("uten hash", 3).toEdit().updateEditedAttachment(nyeMaler.getValue(vedleggUtenHash)))
        assertThat(lagretVedlegg(brevId, endretVedlegg)).isEqualTo(malVedlegg("endret", 2).toEdit())
    }

    @Test
    suspend fun `hent brev fletter bare vedlegg med uendret mal`() {
        val (brev, nyeMaler) = brevMedVedleggOgNyeMaler()

        assertThat(hentBrev(brev.info.id, reserverForRedigering = true)).isSuccess()

        assertVedleggFlettetPerVedlegg(brev.info.id, nyeMaler)
    }

    @Test
    suspend fun `vedlegg som er fjernet fra malen hindrer ikke fletting av andre vedlegg`() {
        val (brev, nyeMaler) = brevMedVedleggOgNyeMaler()
        brevbakerService.renderRedigerbareVedleggResultat = nyeMaler - endretVedlegg

        assertThat(hentBrev(brev.info.id, reserverForRedigering = true)).isSuccess()

        assertThat(lagretVedlegg(brev.info.id, uendretVedlegg))
            .isEqualTo(malVedlegg("uendret", 1).toEdit().updateEditedAttachment(nyeMaler.getValue(uendretVedlegg)))
    }

    @Test
    suspend fun `oppdater brev fletter bare vedlegg med uendret mal`() {
        val (brev, nyeMaler) = brevMedVedleggOgNyeMaler()

        assertThat(oppdaterBrev(brev.info.id)).isSuccess()

        assertVedleggFlettetPerVedlegg(brev.info.id, nyeMaler)
    }

    @Test
    suspend fun `hent redigerbare vedlegg fletter bare vedlegg med uendret mal`() {
        val (brev, nyeMaler) = brevMedVedleggOgNyeMaler()

        assertThat(withPrincipal(saksbehandler1Principal) {
            hentRedigerbareVedlegg(HentRedigerbareVedleggHandler.Request(brevId = brev.info.id, saksId = sak1.saksId))
        }).isSuccess()

        assertVedleggFlettetPerVedlegg(brev.info.id, nyeMaler)
    }

    @Test
    suspend fun `hent redigert vedlegg fletter bare naar vedleggsmalen er uendret`() {
        val (brev, nyeMaler) = brevMedVedleggOgNyeMaler()

        assertThat(hentVedlegg(brev.info.id, endretVedlegg)).isSuccess {
            assertThat(it).isEqualTo(malVedlegg("endret", 2).toEdit())
        }
        assertThat(hentVedlegg(brev.info.id, uendretVedlegg)).isSuccess {
            assertThat(it).isEqualTo(malVedlegg("uendret", 1).toEdit().updateEditedAttachment(nyeMaler.getValue(uendretVedlegg)))
        }
    }

    @Test
    suspend fun `lagre redigert vedlegg fletter bare naar vedleggsmalen er uendret`() {
        val (brev, nyeMaler) = brevMedVedleggOgNyeMaler()

        assertThat(endreVedlegg(brev.info.id, endretVedlegg, malVedlegg("endret", 2).toEdit())).isSuccess {
            assertThat(it).isEqualTo(malVedlegg("endret", 2).toEdit())
        }
        assertThat(lagretVedlegg(brev.info.id, endretVedlegg)).isEqualTo(malVedlegg("endret", 2).toEdit())

        assertThat(endreVedlegg(brev.info.id, uendretVedlegg, malVedlegg("uendret", 1).toEdit())).isSuccess {
            assertThat(it).isEqualTo(malVedlegg("uendret", 1).toEdit().updateEditedAttachment(nyeMaler.getValue(uendretVedlegg)))
        }
    }

    private suspend fun hentVedlegg(brevId: BrevId, vedleggId: VedleggId) =
        withPrincipal(saksbehandler1Principal) {
            hentRedigertVedlegg(HentRedigertVedleggHandler.Request(brevId = brevId, saksId = sak1.saksId, vedleggId = vedleggId))
        }

    private suspend fun endreVedlegg(brevId: BrevId, vedleggId: VedleggId, vedlegg: Edit.Attachment) =
        withPrincipal(saksbehandler1Principal) {
            endreRedigertVedlegg(
                EndreRedigertVedleggHandler.Request(brevId = brevId, saksId = sak1.saksId, vedleggId = vedleggId, redigertVedlegg = vedlegg)
            )
        }

    private val nyttRendretBrev
        get() = letter.copy(blocks = letter.blocks + ParagraphImpl(2, true, listOf(VariableImpl(21, "ny paragraph"))))

    @Test
    suspend fun `hent brev fletter inn rendret brev naar brevmalen er uendret`() {
        val opprettet = opprettBrev().resultOrFail()
        brevbakerService.renderMarkupResultat = { nyttRendretBrev }

        assertThat(hentBrev(opprettet.info.id, reserverForRedigering = true)).isSuccess {
            assertThat(it.redigertBrev).isEqualTo(opprettet.redigertBrev.updateEditedLetter(nyttRendretBrev))
        }
    }

    @Test
    suspend fun `hent brev fletter ikke inn rendret brev naar brevmalen er endret`() {
        val opprettet = opprettBrev().resultOrFail()
        brevbakerService.renderMarkupStableHashCode = StableHashCode(456)
        brevbakerService.renderMarkupResultat = { nyttRendretBrev }

        assertThat(hentBrev(opprettet.info.id, reserverForRedigering = true)).isSuccess {
            assertThat(it.redigertBrev).isEqualTo(opprettet.redigertBrev)
        }
    }

    @Test
    suspend fun `oppdater brev fletter ikke inn rendret brev naar brevmalen er endret`() {
        val opprettet = opprettBrev().resultOrFail()
        brevbakerService.renderMarkupStableHashCode = StableHashCode(456)
        brevbakerService.renderMarkupResultat = { nyttRendretBrev }

        assertThat(oppdaterBrev(opprettet.info.id)).isSuccess {
            assertThat(it.redigertBrev).isEqualTo(opprettet.redigertBrev)
        }
    }

    @Test
    suspend fun `hent brev rendrer ikke vedlegg naar brevmalen er endret`() {
        val (brev, _) = brevMedVedleggOgNyeMaler()
        brevbakerService.renderMarkupStableHashCode = StableHashCode(456)
        brevbakerService.renderRedigerbartVedleggKall.clear()

        assertThat(hentBrev(brev.info.id, reserverForRedigering = true)).isSuccess()

        assertThat(brevbakerService.renderRedigerbartVedleggKall).isEmpty()
    }
}
