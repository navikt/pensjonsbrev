package no.nav.pensjon.brev.skribenten.brevredigering.domain

import no.nav.pensjon.brev.skribenten.Features
import no.nav.pensjon.brev.skribenten.db.Hash
import no.nav.pensjon.brev.skribenten.isFailure
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.editedLetter
import no.nav.pensjon.brev.skribenten.letter.v2.EditV2
import no.nav.pensjon.brev.skribenten.letter.v2.editedLetter as editedLetterV2
import no.nav.pensjon.brev.skribenten.override
import no.nav.pensjon.brevbaker.api.model.ElementTags
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FerdigRedigertPolicyTest {

    private data class RedigertBrevStub(override val redigertBrev: VersjonertBrev) : BrevredigeringStub() {
        constructor(brev: Edit.Letter) : this(VersjonertBrev.V1(brev))
        constructor(brev: EditV2.Letter) : this(VersjonertBrev.V2(brev))

        override val redigertBrevHash: Hash<VersjonertBrev> get() = Hash.read(redigertBrev)
    }

    private val fritekst = setOf(ElementTags.FRITEKST)
    private val policy = FerdigRedigertPolicy().also {
        Features.override(Features.hindreDuplikateAvsnitt, false)
    }

    @Test
    suspend fun `tomt brev er klar til sending`() {
        val brev = RedigertBrevStub(editedLetter { })
        assertThat(policy.erFerdigRedigert(brev)).isSuccess()
    }

    @Test
    suspend fun `brev uten fritekst er klar til sending`() {
        val brev = RedigertBrevStub(editedLetter { paragraph { literal(text = "lit1") } })
        assertThat(policy.erFerdigRedigert(brev)).isSuccess()
    }

    @Test
    suspend fun `brev med redigert fritekst er klar til sending`() {
        val brev = RedigertBrevStub(
            editedLetter { paragraph { literal(text = "lit1", editedText = "hei", tags = fritekst) } }
        )
        assertThat(policy.erFerdigRedigert(brev)).isSuccess()
    }

    @Test
    suspend fun `brev med ikke redigert fritekst er ikke klar til sending`() {
        val brev = RedigertBrevStub(
            editedLetter {
                paragraph {
                    literal(text = "lit1", tags = fritekst)
                    literal(text = "lit2")
                }
            }
        )
        assertThat(policy.erFerdigRedigert(brev)).isFailure<FerdigRedigertPolicy.IkkeFerdigRedigert.FritekstFelterUredigert, _, _>()
    }

    @Test
    suspend fun `brev uten duplikat avsnitt er klar til sending`() {
        Features.override(Features.hindreDuplikateAvsnitt, true)
        val brev = RedigertBrevStub(
            editedLetter {
                paragraph(id = 1) { }
                paragraph(id = 2) { }
            }
        )
        assertThat(policy.erFerdigRedigert(brev)).isSuccess()
    }

    @Test
    suspend fun `brev med duplikat avsnitt er ikke klar til sending`() {
        Features.override(Features.hindreDuplikateAvsnitt, true)
        val brev = RedigertBrevStub(
            editedLetter {
                paragraph(id = 1, missingFromTemplate = true) { }
                paragraph(id = 2) { }
            }
        )
        assertThat(policy.erFerdigRedigert(brev)).isFailure<FerdigRedigertPolicy.IkkeFerdigRedigert.DuplikatAvsnittUhaandtert, _, _>()
    }

    @Test
    suspend fun `v2-brev med redigert fritekst er klar til sending`() {
        val brev = RedigertBrevStub(
            editedLetterV2 { paragraph { literal(text = "lit1", editedText = "hei", editBehaviour = EditV2.EditBehaviour.FRITEKST) } }
        )
        assertThat(policy.erFerdigRedigert(brev)).isSuccess()
    }

    @Test
    suspend fun `v2-brev med ikke redigert fritekst er ikke klar til sending`() {
        val brev = RedigertBrevStub(
            editedLetterV2 {
                paragraph {
                    literal(text = "lit1", editBehaviour = EditV2.EditBehaviour.FRITEKST)
                    literal(text = "lit2")
                }
            }
        )
        assertThat(policy.erFerdigRedigert(brev)).isFailure<FerdigRedigertPolicy.IkkeFerdigRedigert.FritekstFelterUredigert, _, _>()
    }

    @Test
    suspend fun `v2-brev med duplikat avsnitt er ikke klar til sending`() {
        Features.override(Features.hindreDuplikateAvsnitt, true)
        val brev = RedigertBrevStub(
            editedLetterV2 {
                paragraph(id = 1, missingFromTemplate = true) { }
                paragraph(id = 2) { }
            }
        )
        assertThat(policy.erFerdigRedigert(brev)).isFailure<FerdigRedigertPolicy.IkkeFerdigRedigert.DuplikatAvsnittUhaandtert, _, _>()
    }
}
