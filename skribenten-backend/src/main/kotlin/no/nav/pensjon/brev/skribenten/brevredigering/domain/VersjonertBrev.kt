package no.nav.pensjon.brev.skribenten.brevredigering.domain

import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.letter.updateEditedAttachment
import no.nav.pensjon.brev.skribenten.letter.updateEditedLetter
import no.nav.pensjon.brev.skribenten.letter.updateSakspartOgSignatur
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsage
import java.time.LocalDate

enum class MarkupVersjon(val versjon: Int) {
    V1(1);

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

}

class FeilMarkupVersjonException(forventet: MarkupVersjon, faktisk: MarkupVersjon) :
    IllegalStateException("Forventet markup-versjon $forventet, men fikk $faktisk")

sealed interface RendretMarkup {
    val markupVersjon: MarkupVersjon

    data class V1(val markup: LetterMarkup) : RendretMarkup {
        override val markupVersjon get() = MarkupVersjon.V1
    }

    fun toEdit(): VersjonertBrev = when (this) {
        is V1 -> VersjonertBrev.V1(markup.toEdit())
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

    fun toEdit(): VersjonertVedlegg = when (this) {
        is V1 -> VersjonertVedlegg.V1(markup.toEdit())
    }
}

val VersjonertBrev.saksbehandlerNavn: String?
    get() = when (this) {
        is VersjonertBrev.V1 -> brev.signatur.saksbehandlerNavn
    }

val VersjonertBrev.attesterendeSaksbehandlerNavn: String?
    get() = when (this) {
        is VersjonertBrev.V1 -> brev.signatur.attesterendeSaksbehandlerNavn
    }

val VersjonertBrev.tittel: String
    get() = when (this) {
        is VersjonertBrev.V1 -> brev.title.text.joinToString(" ") { it.text }
    }.trim()

val VersjonertVedlegg.tittel: String
    get() = when (this) {
        is VersjonertVedlegg.V1 -> vedlegg.title.text.joinToString("") { if (it is Edit.ParagraphContent.Text.Literal) it.editedText ?: it.text else it.text }
    }

fun VersjonertBrev.innholdEndresAv(rendret: RendretMarkup): Boolean = when (val merget = merge(rendret)) {
    is VersjonertBrev.V1 -> merget.brev.blocks != (this as VersjonertBrev.V1).brev.blocks
}

fun VersjonertVedlegg.innholdEndresAv(rendret: RendretVedleggMarkup): Boolean = when (val merget = merge(rendret)) {
    is VersjonertVedlegg.V1 -> merget.vedlegg.blocks != (this as VersjonertVedlegg.V1).vedlegg.blocks
}

fun VersjonertBrev.withSignaturAttestant(attesterendeSaksbehandlerNavn: String?): VersjonertBrev = when (this) {
    is VersjonertBrev.V1 -> VersjonertBrev.V1(brev.withSignaturAttestant(attesterendeSaksbehandlerNavn))
}

fun VersjonertBrev.withAnnenMottakerNavn(annenMottakerNavn: String?): VersjonertBrev = when (this) {
    is VersjonertBrev.V1 -> VersjonertBrev.V1(brev.withSakspart(annenMottakerNavn = annenMottakerNavn))
}

fun VersjonertBrev.withDokumentDato(dokumentDato: LocalDate): VersjonertBrev = when (this) {
    is VersjonertBrev.V1 -> VersjonertBrev.V1(brev.withSakspart(dokumentDato = dokumentDato))
}

fun VersjonertBrev.merge(rendret: RendretMarkup): VersjonertBrev = when {
    this is VersjonertBrev.V1 && rendret is RendretMarkup.V1 -> VersjonertBrev.V1(brev.updateEditedLetter(rendret.markup))
    else -> throw FeilMarkupVersjonException(markupVersjon, rendret.markupVersjon)
}

fun VersjonertBrev.oppdaterSakspartOgSignatur(rendret: RendretMarkup): VersjonertBrev = when {
    this is VersjonertBrev.V1 && rendret is RendretMarkup.V1 -> VersjonertBrev.V1(brev.updateSakspartOgSignatur(rendret.markup))
    else -> throw FeilMarkupVersjonException(markupVersjon, rendret.markupVersjon)
}

fun VersjonertVedlegg.merge(rendret: RendretVedleggMarkup): VersjonertVedlegg = when {
    this is VersjonertVedlegg.V1 && rendret is RendretVedleggMarkup.V1 -> VersjonertVedlegg.V1(vedlegg.updateEditedAttachment(rendret.markup))
    else -> throw FeilMarkupVersjonException(markupVersjon, rendret.markupVersjon)
}

@JsonDeserialize(using = VersjonertVedleggDeserializer::class)
sealed interface VersjonertVedlegg {
    val markupVersjon: MarkupVersjon

    data class V1(@get:JsonValue val vedlegg: Edit.Attachment) : VersjonertVedlegg {
        override val markupVersjon get() = MarkupVersjon.V1
    }

}

private fun JsonNode.markupVersjon(): MarkupVersjon =
    get("version")?.takeUnless { it.isNull }?.let { MarkupVersjon.fra(it.asInt()) } ?: MarkupVersjon.V1

class VersjonertBrevDeserializer : StdDeserializer<VersjonertBrev>(VersjonertBrev::class.java) {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): VersjonertBrev {
        val node = ctxt.readTree(p)
        return when (node.markupVersjon()) {
            MarkupVersjon.V1 -> VersjonertBrev.V1(ctxt.readTreeAsValue(node, Edit.Letter::class.java))
        }
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

fun VersjonertVedlegg.somV1(): Edit.Attachment =
    (this as? VersjonertVedlegg.V1)?.vedlegg ?: throw FeilMarkupVersjonException(MarkupVersjon.V1, markupVersjon)
