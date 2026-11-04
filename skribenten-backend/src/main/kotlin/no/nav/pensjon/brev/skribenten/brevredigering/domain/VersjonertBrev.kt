package no.nav.pensjon.brev.skribenten.brevredigering.domain

import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import no.nav.brev.brevbaker.markup.Attachment as AttachmentV2
import no.nav.brev.brevbaker.markup.LetterMarkup as MarkupV2
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.letter.updateEditedAttachment
import no.nav.pensjon.brev.skribenten.letter.updateEditedLetter
import no.nav.pensjon.brev.skribenten.letter.updateSakspartOgSignatur
import no.nav.pensjon.brev.skribenten.letter.v2.EditV2
import no.nav.pensjon.brev.skribenten.letter.v2.toEdit as toEditV2
import no.nav.pensjon.brev.skribenten.letter.v2.updateEditedAttachment as updateEditedAttachmentV2
import no.nav.pensjon.brev.skribenten.letter.v2.updateEditedLetter as updateEditedLetterV2
import no.nav.pensjon.brev.skribenten.letter.v2.updateSaksinformasjonOgSignatur
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsage
import java.time.LocalDate

enum class MarkupVersjon(val versjon: Int) {
    V1(1),
    V2(2);

    companion object {
        fun fra(versjon: Int): MarkupVersjon =
            entries.firstOrNull { it.versjon == versjon } ?: throw IllegalArgumentException("Ukjent markup-versjon: $versjon")
    }
}

@JsonDeserialize(using = VersjonertBrevDeserializer::class)
sealed interface VersjonertBrev {
    val markupVersjon: MarkupVersjon

    data class V1(@get:JsonValue val brev: Edit.Letter) : VersjonertBrev {
        override val markupVersjon get() = MarkupVersjon.V1
    }

    data class V2(@get:JsonValue val brev: EditV2.Letter) : VersjonertBrev {
        override val markupVersjon get() = MarkupVersjon.V2
    }
}

class FeilMarkupVersjonException(forventet: MarkupVersjon, faktisk: MarkupVersjon) :
    IllegalStateException("Forventet markup-versjon $forventet, men fikk $faktisk")

sealed interface RendretMarkup {
    val markupVersjon: MarkupVersjon

    data class V1(val markup: LetterMarkup) : RendretMarkup {
        override val markupVersjon get() = MarkupVersjon.V1
    }

    data class V2(val markup: MarkupV2) : RendretMarkup {
        override val markupVersjon get() = MarkupVersjon.V2
    }

    fun toEdit(): VersjonertBrev = when (this) {
        is V1 -> VersjonertBrev.V1(markup.toEdit())
        is V2 -> VersjonertBrev.V2(markup.toEditV2())
    }
}

data class RendretBrev(
    val markup: RendretMarkup,
    val letterDataUsage: Set<LetterMarkupWithDataUsage.Property>,
)

sealed interface RendretVedleggMarkup {
    val markupVersjon: MarkupVersjon

    data class V1(val markup: LetterMarkup.Attachment) : RendretVedleggMarkup {
        override val markupVersjon get() = MarkupVersjon.V1
    }

    data class V2(val markup: AttachmentV2) : RendretVedleggMarkup {
        override val markupVersjon get() = MarkupVersjon.V2
    }

    fun toEdit(): VersjonertVedlegg = when (this) {
        is V1 -> VersjonertVedlegg.V1(markup.toEdit())
        is V2 -> VersjonertVedlegg.V2(markup.toEditV2())
    }
}

val VersjonertBrev.saksbehandlerNavn: String?
    get() = when (this) {
        is VersjonertBrev.V1 -> brev.signatur.saksbehandlerNavn
        is VersjonertBrev.V2 -> brev.signatur.saksbehandlerNavn
    }

val VersjonertBrev.attesterendeSaksbehandlerNavn: String?
    get() = when (this) {
        is VersjonertBrev.V1 -> brev.signatur.attesterendeSaksbehandlerNavn
        is VersjonertBrev.V2 -> brev.signatur.attesterendeSaksbehandlerNavn
    }

val VersjonertBrev.tittel: String
    get() = when (this) {
        is VersjonertBrev.V1 -> brev.title.text.joinToString(" ") { it.text }
        is VersjonertBrev.V2 -> brev.title.text.joinToString(" ") { it.text }
    }.trim()

val VersjonertVedlegg.tittel: String
    get() = when (this) {
        is VersjonertVedlegg.V1 -> vedlegg.title.text.joinToString("") { if (it is Edit.ParagraphContent.Text.Literal) it.editedText ?: it.text else it.text }
        is VersjonertVedlegg.V2 -> vedlegg.title.text.joinToString("") { if (it is EditV2.Text.Literal) it.editedText ?: it.text else it.text }
    }

fun VersjonertBrev.innholdEndresAv(rendret: RendretMarkup): Boolean = when (val merget = merge(rendret)) {
    is VersjonertBrev.V1 -> merget.brev.blocks != (this as VersjonertBrev.V1).brev.blocks
    is VersjonertBrev.V2 -> merget.brev.blocks != (this as VersjonertBrev.V2).brev.blocks
}

fun VersjonertVedlegg.innholdEndresAv(rendret: RendretVedleggMarkup): Boolean = when (val merget = merge(rendret)) {
    is VersjonertVedlegg.V1 -> merget.vedlegg.blocks != (this as VersjonertVedlegg.V1).vedlegg.blocks
    is VersjonertVedlegg.V2 -> merget.vedlegg.blocks != (this as VersjonertVedlegg.V2).vedlegg.blocks
}

fun VersjonertBrev.withSignaturAttestant(attesterendeSaksbehandlerNavn: String?): VersjonertBrev = when (this) {
    is VersjonertBrev.V1 -> VersjonertBrev.V1(brev.withSignaturAttestant(attesterendeSaksbehandlerNavn))
    is VersjonertBrev.V2 -> VersjonertBrev.V2(brev.withSignaturAttestant(attesterendeSaksbehandlerNavn))
}

fun VersjonertBrev.withAnnenMottakerNavn(annenMottakerNavn: String?): VersjonertBrev = when (this) {
    is VersjonertBrev.V1 -> VersjonertBrev.V1(brev.withSakspart(annenMottakerNavn = annenMottakerNavn))
    is VersjonertBrev.V2 -> VersjonertBrev.V2(brev.withSaksinformasjon(annenMottakerNavn = annenMottakerNavn))
}

fun VersjonertBrev.withDokumentDato(dokumentDato: LocalDate): VersjonertBrev = when (this) {
    is VersjonertBrev.V1 -> VersjonertBrev.V1(brev.withSakspart(dokumentDato = dokumentDato))
    is VersjonertBrev.V2 -> VersjonertBrev.V2(brev.withSaksinformasjon(dokumentDato = dokumentDato))
}

fun VersjonertBrev.merge(rendret: RendretMarkup): VersjonertBrev = when {
    this is VersjonertBrev.V1 && rendret is RendretMarkup.V1 -> VersjonertBrev.V1(brev.updateEditedLetter(rendret.markup))
    this is VersjonertBrev.V2 && rendret is RendretMarkup.V2 -> VersjonertBrev.V2(brev.updateEditedLetterV2(rendret.markup))
    else -> throw FeilMarkupVersjonException(markupVersjon, rendret.markupVersjon)
}

fun VersjonertBrev.oppdaterSakspartOgSignatur(rendret: RendretMarkup): VersjonertBrev = when {
    this is VersjonertBrev.V1 && rendret is RendretMarkup.V1 -> VersjonertBrev.V1(brev.updateSakspartOgSignatur(rendret.markup))
    this is VersjonertBrev.V2 && rendret is RendretMarkup.V2 -> VersjonertBrev.V2(brev.updateSaksinformasjonOgSignatur(rendret.markup))
    else -> throw FeilMarkupVersjonException(markupVersjon, rendret.markupVersjon)
}

fun VersjonertVedlegg.merge(rendret: RendretVedleggMarkup): VersjonertVedlegg = when {
    this is VersjonertVedlegg.V1 && rendret is RendretVedleggMarkup.V1 -> VersjonertVedlegg.V1(vedlegg.updateEditedAttachment(rendret.markup))
    this is VersjonertVedlegg.V2 && rendret is RendretVedleggMarkup.V2 -> VersjonertVedlegg.V2(vedlegg.updateEditedAttachmentV2(rendret.markup))
    else -> throw FeilMarkupVersjonException(markupVersjon, rendret.markupVersjon)
}

@JsonDeserialize(using = VersjonertVedleggDeserializer::class)
sealed interface VersjonertVedlegg {
    val markupVersjon: MarkupVersjon

    data class V1(@get:JsonValue val vedlegg: Edit.Attachment) : VersjonertVedlegg {
        override val markupVersjon get() = MarkupVersjon.V1
    }

    data class V2(@get:JsonValue val vedlegg: EditV2.Attachment) : VersjonertVedlegg {
        override val markupVersjon get() = MarkupVersjon.V2
    }
}

private fun JsonNode.markupVersjon(): MarkupVersjon =
    get("version")?.takeUnless { it.isNull }?.let { MarkupVersjon.fra(it.asInt()) } ?: MarkupVersjon.V1

class VersjonertBrevDeserializer : StdDeserializer<VersjonertBrev>(VersjonertBrev::class.java) {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): VersjonertBrev {
        val node = ctxt.readTree(p)
        return when (node.markupVersjon()) {
            MarkupVersjon.V1 -> VersjonertBrev.V1(ctxt.readTreeAsValue(node, Edit.Letter::class.java))
            MarkupVersjon.V2 -> VersjonertBrev.V2(ctxt.readTreeAsValue(node, EditV2.Letter::class.java))
        }
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

fun VersjonertVedlegg.somV1(): Edit.Attachment =
    (this as? VersjonertVedlegg.V1)?.vedlegg ?: throw FeilMarkupVersjonException(MarkupVersjon.V1, markupVersjon)

fun VersjonertVedlegg.somV2(): EditV2.Attachment =
    (this as? VersjonertVedlegg.V2)?.vedlegg ?: throw FeilMarkupVersjonException(MarkupVersjon.V2, markupVersjon)
