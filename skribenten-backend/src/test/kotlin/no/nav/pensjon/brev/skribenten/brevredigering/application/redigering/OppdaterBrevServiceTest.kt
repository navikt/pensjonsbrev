package no.nav.pensjon.brev.skribenten.brevredigering.application.redigering

import no.nav.brev.brevbaker.StableHashCode
import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.brevredigering.application.BrevredigeringHandlerTestBase
import no.nav.pensjon.brev.skribenten.brevredigering.application.oppslag.HentBrevHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.BrevmalendringType
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.letter.letter
import no.nav.pensjon.brev.skribenten.letter.updateEditedLetter
import no.nav.pensjon.brev.skribenten.letter.updateVariables
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.BlockImpl.ParagraphImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.TextImpl.VariableImpl
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.junit.jupiter.api.Test

class OppdaterBrevServiceTest : BrevredigeringHandlerTestBase() {

    private val hentBrevMedSjekk by lazy {
        HentBrevHandler(brevtilgang, brevmalService, brevdataService, OppdaterBrevService(brevmalService) { true })
    }

    private fun mal(beloep: String, vararg ekstra: LetterMarkup.Block) =
        letter(ParagraphImpl(1, true, listOf(LiteralImpl(11, "Beløp: "), VariableImpl(12, beloep))), *ekstra)

    private val nyttAvsnitt = ParagraphImpl(2, true, listOf(LiteralImpl(21, "nytt avsnitt i malen")))

    private suspend fun opprettMedMal(mal: LetterMarkup): Dto.Brevredigering {
        brevbakerService.renderMarkupResultat = { mal }
        return opprettBrev(reserverForRedigering = false).resultOrFail()
    }

    private suspend fun hentMedSjekk(brevId: BrevId) = withPrincipal(saksbehandler1Principal) {
        hentBrevMedSjekk(HentBrevHandler.Request(brevId = brevId, saksId = sak1.saksId, reserverForRedigering = true))
    }

    private fun lagretHash(brevId: BrevId): StableHashCode? = transaction { BrevredigeringEntity[brevId].brevmalHash }

    @Test
    suspend fun `uendret mal merger inn rendret brev`() {
        val opprettet = opprettMedMal(mal("100 kr"))
        val opprinneligHash = lagretHash(opprettet.info.id)

        val nyRendering = mal("200 kr")
        brevbakerService.renderMarkupResultat = { nyRendering }

        assertThat(hentMedSjekk(opprettet.info.id)).isSuccess {
            assertThat(it.redigertBrev).isEqualTo(opprettet.redigertBrev.updateEditedLetter(nyRendering))
            assertThat(it.brevmalEndret).isEqualTo(BrevmalendringType.INGEN_ENDRING)
        }
        assertThat(lagretHash(opprettet.info.id)).isEqualTo(opprinneligHash)
    }

    @Test
    suspend fun `endret mal oppdaterer kun variabler naar sjekk er paa`() {
        val opprettet = opprettMedMal(mal("100 kr"))
        val opprinneligHash = lagretHash(opprettet.info.id)

        val nyRendering = mal("200 kr", nyttAvsnitt)
        brevbakerService.renderMarkupResultat = { nyRendering }

        assertThat(hentMedSjekk(opprettet.info.id)).isSuccess {
            assertThat(it.redigertBrev).isEqualTo(opprettet.redigertBrev.updateVariables(nyRendering))
            assertThat(it.redigertBrev.blocks).hasSameSizeAs(opprettet.redigertBrev.blocks)
            assertThat(it.brevmalEndret).isEqualTo(BrevmalendringType.MARKUP_ENDRET)
        }
        assertThat(lagretHash(opprettet.info.id)).isEqualTo(opprinneligHash)
    }

    @Test
    suspend fun `endret mal merger inn rendret brev og oppdaterer hash naar sjekk er av`() {
        val opprettet = opprettMedMal(mal("100 kr"))
        val opprinneligHash = lagretHash(opprettet.info.id)

        val nyRendering = mal("200 kr", nyttAvsnitt)
        brevbakerService.renderMarkupResultat = { nyRendering }

        assertThat(hentBrev(brevId = opprettet.info.id, reserverForRedigering = true)).isSuccess {
            assertThat(it.redigertBrev).isEqualTo(opprettet.redigertBrev.updateEditedLetter(nyRendering))
            assertThat(it.brevmalEndret).isEqualTo(BrevmalendringType.MARKUP_ENDRET)
        }
        assertThat(lagretHash(opprettet.info.id)).isNotNull.isNotEqualTo(opprinneligHash)
    }

    @Test
    suspend fun `variabel som forsvinner fra uendret mal rapporteres`() {
        val opprettet = opprettMedMal(mal("100 kr"))

        brevbakerService.renderMarkupResultat = { letter(ParagraphImpl(1, true, listOf(LiteralImpl(11, "Beløp: ")))) }

        assertThat(hentMedSjekk(opprettet.info.id)).isSuccess {
            assertThat(it.brevmalEndret).isEqualTo(BrevmalendringType.KUNNE_IKKE_OPPDATERE_ALLE_VARIABLER)
        }
    }
}
