package no.nav.pensjon.brev.skribenten.brevredigering.domain

import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.letter.updateEditedAttachment
import no.nav.pensjon.brevbaker.api.model.LetterMarkup

@JsonDeserialize(using = VersjonertVedleggDeserializer::class)
sealed interface VersjonertVedlegg {
    val markupVersjon: MarkupVersjon
    val tittel: String

    fun innholdEndresAv(rendret: RendretVedleggMarkup): Boolean
    fun merge(rendret: RendretVedleggMarkup): VersjonertVedlegg
    fun asV1(): Edit.Attachment

    data class V1(@get:JsonValue val vedlegg: Edit.Attachment) : VersjonertVedlegg {
        override val markupVersjon get() = MarkupVersjon.V1
        override val tittel get() =
            vedlegg.title.text.joinToString("") { if (it is Edit.ParagraphContent.Text.Literal) it.editedText ?: it.text else it.text }

        override fun innholdEndresAv(rendret: RendretVedleggMarkup) =
            vedlegg.updateEditedAttachment(rendret.asV1()).blocks != vedlegg.blocks

        override fun merge(rendret: RendretVedleggMarkup) = V1(vedlegg.updateEditedAttachment(rendret.asV1()))

        override fun asV1() = vedlegg

        private fun RendretVedleggMarkup.asV1(): LetterMarkup.Attachment =
            (this as? RendretVedleggMarkup.V1)?.markup ?: throw FeilMarkupVersjonException(this@V1.markupVersjon, markupVersjon)
    }
}

sealed interface RendretVedleggMarkup {
    val markupVersjon: MarkupVersjon

    fun toEdit(): VersjonertVedlegg

    data class V1(val markup: LetterMarkup.Attachment) : RendretVedleggMarkup {
        override val markupVersjon get() = MarkupVersjon.V1
        override fun toEdit() = VersjonertVedlegg.V1(markup.toEdit())
    }
}

class VersjonertVedleggDeserializer : StdDeserializer<VersjonertVedlegg>(VersjonertVedlegg::class.java) {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): VersjonertVedlegg {
        val node = ctxt.readTree(p)
        return when (node.markupVersjon()) {
            MarkupVersjon.V1 -> VersjonertVedlegg.V1(ctxt.readTreeAsValue(node, Edit.Attachment::class.java))
        }
    }
}
