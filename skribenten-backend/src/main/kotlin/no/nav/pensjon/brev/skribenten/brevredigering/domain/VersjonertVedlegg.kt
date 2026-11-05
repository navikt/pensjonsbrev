package no.nav.pensjon.brev.skribenten.brevredigering.domain

import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import no.nav.brev.brevbaker.markup.Attachment as AttachmentV2
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.letter.updateEditedAttachment
import no.nav.pensjon.brev.skribenten.letter.v2.EditV2
import no.nav.pensjon.brev.skribenten.letter.v2.toEdit as toEditV2
import no.nav.pensjon.brev.skribenten.letter.v2.updateEditedAttachment as updateEditedAttachmentV2
import no.nav.pensjon.brevbaker.api.model.LetterMarkup

@JsonDeserialize(using = VersjonertVedleggDeserializer::class)
sealed interface VersjonertVedlegg {
    val markupVersjon: MarkupVersjon
    val tittel: String

    fun innholdEndresAv(rendret: VersjonertVedleggMarkup): Boolean
    fun merge(rendret: VersjonertVedleggMarkup): VersjonertVedlegg
    fun asV1(): Edit.Attachment
    fun asV2(): EditV2.Attachment

    data class V1(@get:JsonValue val vedlegg: Edit.Attachment) : VersjonertVedlegg {
        override val markupVersjon get() = MarkupVersjon.V1
        override val tittel get() =
            vedlegg.title.text.joinToString("") { if (it is Edit.ParagraphContent.Text.Literal) it.editedText ?: it.text else it.text }

        override fun innholdEndresAv(rendret: VersjonertVedleggMarkup) =
            vedlegg.updateEditedAttachment(rendret.asV1()).blocks != vedlegg.blocks

        override fun merge(rendret: VersjonertVedleggMarkup) = V1(vedlegg.updateEditedAttachment(rendret.asV1()))

        override fun asV1() = vedlegg

        override fun asV2() = throw FeilMarkupVersjonException(MarkupVersjon.V2, markupVersjon)

        private fun VersjonertVedleggMarkup.asV1(): LetterMarkup.Attachment =
            (this as? VersjonertVedleggMarkup.V1)?.markup ?: throw FeilMarkupVersjonException(this@V1.markupVersjon, markupVersjon)
    }

    data class V2(@get:JsonValue val vedlegg: EditV2.Attachment) : VersjonertVedlegg {
        override val markupVersjon get() = MarkupVersjon.V2
        override val tittel get() =
            vedlegg.title.text.joinToString("") { if (it is EditV2.Text.Literal) it.editedText ?: it.text else it.text }

        override fun innholdEndresAv(rendret: VersjonertVedleggMarkup) =
            vedlegg.updateEditedAttachmentV2(rendret.asV2()).blocks != vedlegg.blocks

        override fun merge(rendret: VersjonertVedleggMarkup) = V2(vedlegg.updateEditedAttachmentV2(rendret.asV2()))

        override fun asV1() = throw FeilMarkupVersjonException(MarkupVersjon.V1, markupVersjon)

        override fun asV2() = vedlegg

        private fun VersjonertVedleggMarkup.asV2(): AttachmentV2 =
            (this as? VersjonertVedleggMarkup.V2)?.markup ?: throw FeilMarkupVersjonException(this@V2.markupVersjon, markupVersjon)
    }
}

sealed interface VersjonertVedleggMarkup {
    val markupVersjon: MarkupVersjon

    fun toEdit(): VersjonertVedlegg

    data class V1(val markup: LetterMarkup.Attachment) : VersjonertVedleggMarkup {
        override val markupVersjon get() = MarkupVersjon.V1
        override fun toEdit() = VersjonertVedlegg.V1(markup.toEdit())
    }

    data class V2(val markup: AttachmentV2) : VersjonertVedleggMarkup {
        override val markupVersjon get() = MarkupVersjon.V2
        override fun toEdit() = VersjonertVedlegg.V2(markup.toEditV2())
    }
}

class VersjonertVedleggDeserializer : StdDeserializer<VersjonertVedlegg>(VersjonertVedlegg::class.java) {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): VersjonertVedlegg {
        val node = ctxt.readTree(p)
        return when (node.markupVersjon()) {
            MarkupVersjon.V1 -> VersjonertVedlegg.V1(ctxt.readTreeAsValue(node, Edit.Attachment::class.java))
            MarkupVersjon.V2 -> VersjonertVedlegg.V2(ctxt.readTreeAsValue(node, EditV2.Attachment::class.java))
        }
    }
}
