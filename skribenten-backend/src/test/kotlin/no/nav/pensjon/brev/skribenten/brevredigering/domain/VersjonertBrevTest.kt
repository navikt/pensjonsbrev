package no.nav.pensjon.brev.skribenten.brevredigering.domain

import com.fasterxml.jackson.module.kotlin.readValue
import no.nav.pensjon.brev.skribenten.db.Hash
import no.nav.pensjon.brev.skribenten.db.databaseObjectMapper
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.editedLetter
import no.nav.pensjon.brev.skribenten.letter.toMarkup
import no.nav.pensjon.brev.skribenten.letter.v2.EditV2
import no.nav.pensjon.brev.skribenten.letter.v2.MarkupV2
import no.nav.pensjon.brev.skribenten.letter.v2.letter as letterV2
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

    private val v2Brev = EditV2.Letter(
        title = EditV2.Title(listOf(EditV2.Text.Literal(1, "Tittel"))),
        saksinformasjon = EditV2.Saksinformasjon("Test Testeson", "12345678910", null, "1234", LocalDate.of(2025, 1, 2)),
        blocks = listOf(
            EditV2.Block.Paragraph(2, listOf(EditV2.Text.Literal(3, "hei", parentId = 2))),
            EditV2.Block.ItemList(4, listOf(EditV2.Item(5, listOf(EditV2.Text.Literal(6, "punkt", parentId = 5)), parentId = 4))),
        ),
        signatur = EditV2.Signatur("Saksbehandler", null, "Nav Porsgrunn"),
        deletedBlocks = emptyList(),
    )

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
    fun `v2 round-tripper og har version 2`() {
        val json = databaseObjectMapper.writeValueAsBytes(VersjonertBrev.V2(v2Brev))
        assertThat(databaseObjectMapper.readTree(json).get("version").asInt()).isEqualTo(2)
        assertThat(databaseObjectMapper.readValue<VersjonertBrev>(json)).isEqualTo(VersjonertBrev.V2(v2Brev))
    }

    @Test
    fun `vedlegg dispatcher paa version`() {
        val v1 = Edit.Attachment(v1Brev.title, v1Brev.blocks, emptyList(), false)
        val v2 = EditV2.Attachment(v2Brev.title, v2Brev.blocks, emptyList(), false)

        assertThat(databaseObjectMapper.writeValueAsBytes(VersjonertVedlegg.V1(v1))).isEqualTo(databaseObjectMapper.writeValueAsBytes(v1))
        assertThat(databaseObjectMapper.readValue<VersjonertVedlegg>(databaseObjectMapper.writeValueAsBytes(v1))).isEqualTo(VersjonertVedlegg.V1(v1))
        assertThat(databaseObjectMapper.readValue<VersjonertVedlegg>(databaseObjectMapper.writeValueAsBytes(VersjonertVedlegg.V2(v2)))).isEqualTo(VersjonertVedlegg.V2(v2))
    }

    @Test
    fun `ukjent version feiler`() {
        val json = databaseObjectMapper.writeValueAsString(VersjonertBrev.V2(v2Brev)).replace("\"version\":2", "\"version\":3")
        assertThatThrownBy { databaseObjectMapper.readValue<VersjonertBrev>(json) }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `brev merges bare med rendret markup av samme versjon`() {
        val v1Markup = VersjonertMarkup.V1(v1Brev.toMarkup())
        val v2Markup = VersjonertMarkup.V2(v2Brev.toMarkup())

        assertThat(VersjonertBrev.V1(v1Brev).merge(v1Markup, previousRender = null)).isInstanceOf(VersjonertBrev.V1::class.java)
        assertThat(VersjonertBrev.V2(v2Brev).merge(v2Markup, previousRender = null)).isInstanceOf(VersjonertBrev.V2::class.java)
        assertThatThrownBy { VersjonertBrev.V1(v1Brev).merge(v2Markup, previousRender = null) }.isInstanceOf(FeilMarkupVersjonException::class.java)
        assertThatThrownBy { VersjonertBrev.V2(v2Brev).merge(v1Markup, previousRender = null) }.isInstanceOf(FeilMarkupVersjonException::class.java)
    }

    @Test
    fun `vedlegg merges bare med rendret markup av samme versjon`() {
        val v1 = Edit.Attachment(v1Brev.title, v1Brev.blocks, emptyList(), false)
        val v2 = EditV2.Attachment(v2Brev.title, v2Brev.blocks, emptyList(), false)
        val v1Markup = VersjonertVedleggMarkup.V1(v1.toMarkup())
        val v2Markup = VersjonertVedleggMarkup.V2(v2.toMarkup())

        assertThat(VersjonertVedlegg.V1(v1).merge(v1Markup)).isInstanceOf(VersjonertVedlegg.V1::class.java)
        assertThat(VersjonertVedlegg.V2(v2).merge(v2Markup)).isInstanceOf(VersjonertVedlegg.V2::class.java)
        assertThatThrownBy { VersjonertVedlegg.V1(v1).merge(v2Markup) }.isInstanceOf(FeilMarkupVersjonException::class.java)
        assertThatThrownBy { VersjonertVedlegg.V2(v2).merge(v1Markup) }.isInstanceOf(FeilMarkupVersjonException::class.java)
    }

    @Test
    fun `innhold endres ikke av samme rendring naar brevet har blokk som er ny i malen`() {
        val forrigeRendring = letter(ParagraphImpl(1, true, listOf(LiteralImpl(11, "Første"))))
        val rendring = letter(
            ParagraphImpl(1, true, listOf(LiteralImpl(11, "Første"))),
            ParagraphImpl(2, true, listOf(LiteralImpl(21, "Andre"))),
        )
        val brev = VersjonertBrev.V1(forrigeRendring.toEdit().updateEditedLetter(rendring, previousRender = forrigeRendring.toEdit()))

        assertThat(brev.brev.blocks.last().decision?.type).isEqualTo(Edit.Block.DecisionRequest.Type.NEW_IN_RENDERED)
        assertThat(brev.innholdEndresAv(VersjonertMarkup.V1(rendring))).isFalse()
    }

    @Test
    fun `innhold endres naar redigert blokk ikke lenger er i malen`() {
        val brev = VersjonertBrev.V1(editedLetter {
            paragraph(id = 1) { literal(id = 11, text = "Første", editedText = "Redigert") }
        })

        assertThat(brev.innholdEndresAv(VersjonertMarkup.V1(letter(ParagraphImpl(2, true, listOf(LiteralImpl(21, "Andre"))))))).isTrue()
    }

    @Test
    fun `v2-innhold endres ikke av samme rendring naar brevet har blokk som er ny i malen`() {
        val forrigeRendring = letterV2(MarkupV2.paragraph(1, MarkupV2.literal(11, "Første")))
        val rendring = letterV2(
            MarkupV2.paragraph(1, MarkupV2.literal(11, "Første")),
            MarkupV2.itemList(2, MarkupV2.item(21, MarkupV2.literal(211, "Punkt"))),
        )
        val forrigeBrev = EditV2.Letter.from(forrigeRendring)
        val brev = VersjonertBrev.V2(forrigeBrev).merge(VersjonertMarkup.V2(rendring), previousRender = VersjonertBrev.V2(forrigeBrev))

        assertThat((brev as VersjonertBrev.V2).brev.blocks.last().decision).isEqualTo(EditV2.Block.DecisionRequest.newInRenderedPending)
        assertThat(brev.innholdEndresAv(VersjonertMarkup.V2(rendring))).isFalse()
    }

    @Test
    fun `v2-merge feiler naar forrige rendring har annen markup-versjon`() {
        val rendring = VersjonertMarkup.V2(v2Brev.toMarkup())

        assertThatThrownBy { VersjonertBrev.V2(v2Brev).merge(rendring, previousRender = VersjonertBrev.V1(v1Brev)) }
            .isInstanceOf(FeilMarkupVersjonException::class.java)
    }
}
