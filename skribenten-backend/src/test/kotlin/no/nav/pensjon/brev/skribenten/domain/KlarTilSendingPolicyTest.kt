package no.nav.pensjon.brev.skribenten.domain

import no.nav.pensjon.brev.skribenten.db.Hash
import no.nav.pensjon.brev.skribenten.isFailure
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.editedLetter
import no.nav.pensjon.brevbaker.api.model.ElementTags
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class KlarTilSendingPolicyTest {

    private data class RedigertBrevStub(override val redigertBrev: Edit.Letter) : BrevredigeringStub() {
        override val redigertBrevHash: Hash<Edit.Letter> get() = Hash.read(redigertBrev)
    }

    private val fritekst = setOf(ElementTags.FRITEKST)
    private val policy = KlarTilSendingPolicy()

    @Test
    fun `tomt brev er klar til sending`() {
        val brev = RedigertBrevStub(editedLetter())
        assertThat(policy.kanSettesTilKlar(brev)).isSuccess()
    }

    @Test
    fun `brev uten fritekst er klar til sending`() {
        val brev = RedigertBrevStub(
            editedLetter(Edit.Block.Paragraph(null, true, listOf(Edit.ParagraphContent.Text.Literal(null, "lit1"))))
        )
        assertThat(policy.kanSettesTilKlar(brev)).isSuccess()
    }

    @Test
    fun `brev med redigert fritekst er klar til sending`() {
        val brev = RedigertBrevStub(
            editedLetter(
                Edit.Block.Paragraph(
                    null, true, listOf(Edit.ParagraphContent.Text.Literal(null, "lit1", editedText = "hei", tags = fritekst))
                )
            )
        )
        assertThat(policy.kanSettesTilKlar(brev)).isSuccess()
    }

    @Test
    fun `brev med ikke redigert fritekst er ikke klar til sending`() {
        val brev = RedigertBrevStub(
            editedLetter(
                Edit.Block.Paragraph(
                    null, true, listOf(
                        Edit.ParagraphContent.Text.Literal(null, "lit1", tags = fritekst), Edit.ParagraphContent.Text.Literal(null, "lit2")
                    )
                )
            )
        )
        assertThat(policy.kanSettesTilKlar(brev)).isFailure<KlarTilSendingPolicy.IkkeKlarTilSending.FritekstFelterUredigert, _, _>()
    }
}