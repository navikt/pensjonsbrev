package no.nav.pensjon.brev.skribenten.brevredigering.domain

import no.nav.pensjon.brev.skribenten.Features
import no.nav.pensjon.brev.skribenten.db.Hash
import no.nav.pensjon.brev.skribenten.isFailure
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.editedLetter
import no.nav.pensjon.brevbaker.api.model.ElementTags
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FerdigRedigertPolicyTest {

    private data class RedigertBrevStub(override val redigertBrev: Edit.Letter) : BrevredigeringStub() {
        override val redigertBrevHash: Hash<Edit.Letter> get() = Hash.read(redigertBrev)
    }

    private val fritekst = setOf(ElementTags.FRITEKST)
    private val policy = FerdigRedigertPolicy()

    @Test
    suspend fun `tomt brev er klar til sending`() {
        val brev = RedigertBrevStub(editedLetter())
        assertThat(policy.erFerdigRedigert(brev)).isSuccess()
    }

    @Test
    suspend fun `brev uten fritekst er klar til sending`() {
        val brev = RedigertBrevStub(
            editedLetter(Edit.Block.Paragraph(null, true, listOf(Edit.ParagraphContent.Text.Literal(null, "lit1"))))
        )
        assertThat(policy.erFerdigRedigert(brev)).isSuccess()
    }

    @Test
    suspend fun `brev med redigert fritekst er klar til sending`() {
        val brev = RedigertBrevStub(
            editedLetter(
                Edit.Block.Paragraph(
                    null,
                    true,
                    listOf(Edit.ParagraphContent.Text.Literal(null, "lit1", editedText = "hei", tags = fritekst))
                )
            )
        )
        assertThat(policy.erFerdigRedigert(brev)).isSuccess()
    }

    @Test
    suspend fun `brev med ikke redigert fritekst er ikke klar til sending`() {
        val brev = RedigertBrevStub(
            editedLetter(
                Edit.Block.Paragraph(
                    null, true, listOf(
                        Edit.ParagraphContent.Text.Literal(null, "lit1", tags = fritekst),
                        Edit.ParagraphContent.Text.Literal(null, "lit2")
                    )
                )
            )
        )
        assertThat(policy.erFerdigRedigert(brev)).isFailure<FerdigRedigertPolicy.IkkeFerdigRedigert.FritekstFelterUredigert, _, _>()
    }

    @Test
    suspend fun `brev uten duplikat avsnitt er klar til sending`() {
        Features.override(Features.hindreDuplikateAvsnitt, true)
        val brev = RedigertBrevStub(
            editedLetter(
                Edit.Block.Paragraph(id = 1, editable = true, content = emptyList(), missingFromTemplate = false),
                Edit.Block.Paragraph(id = 2, editable = true, content = emptyList(), missingFromTemplate = null),
            )
        )
        assertThat(policy.erFerdigRedigert(brev)).isSuccess()
    }

    @Test
    suspend fun `brev med duplikat avsnitt er ikke klar til sending`() {
        Features.override(Features.hindreDuplikateAvsnitt, true)
        val brev = RedigertBrevStub(
            editedLetter(
                Edit.Block.Paragraph(id = 1, editable = true, content = emptyList(), missingFromTemplate = true),
                Edit.Block.Paragraph(id = 2, editable = true, content = emptyList(), missingFromTemplate = null),
            )
        )
        assertThat(policy.erFerdigRedigert(brev)).isFailure<FerdigRedigertPolicy.IkkeFerdigRedigert.DuplikatAvsnittUhaandtert, _, _>()
    }
}