package no.nav.pensjon.brev.skribenten.brevredigering.application

import no.nav.brev.brevbaker.StableHashCode
import no.nav.pensjon.brev.skribenten.Features
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.letter.updateEditedAttachment
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.override
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.BlockImpl.ParagraphImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl
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
}
