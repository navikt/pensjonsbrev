package no.nav.pensjon.brev.skribenten.brevredigering.domain

import com.fasterxml.jackson.module.kotlin.readValue
import no.nav.pensjon.brev.skribenten.db.Hash
import no.nav.pensjon.brev.skribenten.db.databaseObjectMapper
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.editedLetter
import no.nav.pensjon.brev.skribenten.letter.letter
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.letter.updateEditedLetter
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.BlockImpl.ParagraphImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.time.LocalDate

class VersjonertBrevTest {

    private val v1Brev = editedLetter(dokumentDato = LocalDate.of(2025, 1, 2)) {
        title1 { literal(null, "Tittel") }
        paragraph {
            literal(null, "hei")
            variable(null, "joda")
            itemList { item { literal(null, "punkt") } }
        }
    }

    @Test
    fun `v1 serialiseres byte-identisk med Edit Letter`() {
        assertThat(databaseObjectMapper.writeValueAsBytes(VersjonertBrev.V1(v1Brev)))
            .isEqualTo(databaseObjectMapper.writeValueAsBytes(v1Brev))
    }

    @Test
    fun `hash av v1 er uendret`() {
        assertThat(Hash.read<VersjonertBrev>(VersjonertBrev.V1(v1Brev)).toString())
            .isEqualTo(Hash.read(v1Brev).toString())
    }

    @Test
    fun `eksisterende v1-json uten version deserialiseres som v1`() {
        val json = databaseObjectMapper.writeValueAsBytes(v1Brev)
        assertThat(databaseObjectMapper.readValue<VersjonertBrev>(json)).isEqualTo(VersjonertBrev.V1(v1Brev))
    }

    @Test
    fun `vedlegg dispatcher paa version`() {
        val v1 = Edit.Attachment(v1Brev.title, v1Brev.blocks, emptyList(), false)

        assertThat(databaseObjectMapper.writeValueAsBytes(VersjonertVedlegg.V1(v1))).isEqualTo(databaseObjectMapper.writeValueAsBytes(v1))
        assertThat(databaseObjectMapper.readValue<VersjonertVedlegg>(databaseObjectMapper.writeValueAsBytes(v1))).isEqualTo(VersjonertVedlegg.V1(v1))
    }

    @Test
    fun `ukjent version feiler`() {
        val json = """{"version":2}"""
        assertThatThrownBy { databaseObjectMapper.readValue<VersjonertBrev>(json) }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `innhold endres ikke av samme rendring naar brevet har blokk som er ny i malen`() {
        val forrigeRendring = letter(ParagraphImpl(1, true, listOf(LiteralImpl(11, "Første"))))
        val rendring = letter(
            ParagraphImpl(1, true, listOf(LiteralImpl(11, "Første"))),
            ParagraphImpl(2, true, listOf(LiteralImpl(21, "Andre"))),
        )
        val brev = VersjonertBrev.V1(forrigeRendring.toEdit().updateEditedLetter(rendring, previousRender = forrigeRendring.toEdit()))

        assertThat(brev.brev.blocks.last().templateStatus).isEqualTo(Edit.Block.TemplateStatus.NEW_IN_TEMPLATE)
        assertThat(brev.innholdEndresAv(VersjonertMarkup.V1(rendring))).isFalse()
    }

    @Test
    fun `innhold endres naar redigert blokk ikke lenger er i malen`() {
        val brev = VersjonertBrev.V1(editedLetter {
            paragraph(id = 1) { literal(id = 11, text = "Første", editedText = "Redigert") }
        })

        assertThat(brev.innholdEndresAv(VersjonertMarkup.V1(letter(ParagraphImpl(2, true, listOf(LiteralImpl(21, "Andre"))))))).isTrue()
    }
}
