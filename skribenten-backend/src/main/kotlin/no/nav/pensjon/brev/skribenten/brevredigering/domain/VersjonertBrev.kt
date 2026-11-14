package no.nav.pensjon.brev.skribenten.brevredigering.domain

import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import no.nav.brev.brevbaker.markup.Attachment as AttachmentV2
import no.nav.brev.brevbaker.markup.LetterMarkup as MarkupV2
import no.nav.brev.brevbaker.StableHashCode
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.letter.toMarkup
import no.nav.pensjon.brev.skribenten.letter.updateEditedLetter
import no.nav.pensjon.brev.skribenten.letter.updateSakspartOgSignatur
import no.nav.pensjon.brev.skribenten.letter.v2.EditV2
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsage

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
    val saksbehandlerNavn: String?
    val attesterendeSaksbehandlerNavn: String?
    val tittel: String

    fun innholdEndresAv(rendret: VersjonertMarkup): Boolean
    fun merge(rendret: VersjonertMarkup): VersjonertBrev
    fun oppdaterSakspartOgSignatur(rendret: VersjonertMarkup): VersjonertBrev
    fun withSignaturAttestant(attesterendeSaksbehandlerNavn: String?): VersjonertBrev
    fun withAnnenMottakerNavn(annenMottakerNavn: String?): VersjonertBrev

    data class V1(@get:JsonValue val brev: Edit.Letter) : VersjonertBrev {
        override val markupVersjon get() = MarkupVersjon.V1
        override val saksbehandlerNavn get() = brev.signatur.saksbehandlerNavn
        override val attesterendeSaksbehandlerNavn get() = brev.signatur.attesterendeSaksbehandlerNavn
        override val tittel get() = brev.title.text.joinToString(" ") { it.text }.trim()

        override fun innholdEndresAv(rendret: VersjonertMarkup) =
            brev.updateEditedLetter(rendret.asV1()).blocks != brev.blocks

        override fun merge(rendret: VersjonertMarkup) = V1(brev.updateEditedLetter(rendret.asV1()))

        override fun oppdaterSakspartOgSignatur(rendret: VersjonertMarkup) = V1(brev.updateSakspartOgSignatur(rendret.asV1()))

        override fun withSignaturAttestant(attesterendeSaksbehandlerNavn: String?) =
            V1(brev.withSignaturAttestant(attesterendeSaksbehandlerNavn))

        override fun withAnnenMottakerNavn(annenMottakerNavn: String?) = V1(brev.withSakspart(annenMottakerNavn = annenMottakerNavn))

        fun vedleggMarkup(vedlegg: List<Dto.RedigertVedlegg>): Map<VedleggId, LetterMarkup.Attachment> =
            vedlegg.associate { it.vedleggId to it.redigertVedlegg.asV1().toMarkup() }

        private fun VersjonertMarkup.asV1(): LetterMarkup =
            (this as? VersjonertMarkup.V1)?.markup ?: throw FeilMarkupVersjonException(this@V1.markupVersjon, markupVersjon)
    }

    data class V2(@get:JsonValue val brev: EditV2.Letter) : VersjonertBrev {
        override val markupVersjon get() = MarkupVersjon.V2
        override val saksbehandlerNavn get() = brev.signatur.saksbehandlerNavn
        override val attesterendeSaksbehandlerNavn get() = brev.signatur.attesterendeSaksbehandlerNavn
        override val tittel get() = brev.title.text.joinToString(" ") { it.text }.trim()

        override fun innholdEndresAv(rendret: VersjonertMarkup) =
            brev.merge(rendret.asV2()).blocks != brev.blocks

        override fun merge(rendret: VersjonertMarkup) = V2(brev.merge(rendret.asV2()))

        override fun oppdaterSakspartOgSignatur(rendret: VersjonertMarkup) = V2(brev.updateSaksinformasjonOgSignatur(rendret.asV2()))

        override fun withSignaturAttestant(attesterendeSaksbehandlerNavn: String?) =
            V2(brev.withSignaturAttestant(attesterendeSaksbehandlerNavn))

        override fun withAnnenMottakerNavn(annenMottakerNavn: String?) = V2(brev.withSaksinformasjon(annenMottakerNavn = annenMottakerNavn))

        fun vedleggMarkup(vedlegg: List<Dto.RedigertVedlegg>): Map<VedleggId, AttachmentV2> =
            vedlegg.associate { it.vedleggId to it.redigertVedlegg.asV2().toMarkup() }

        private fun VersjonertMarkup.asV2(): MarkupV2 =
            (this as? VersjonertMarkup.V2)?.markup ?: throw FeilMarkupVersjonException(this@V2.markupVersjon, markupVersjon)
    }
}

class FeilMarkupVersjonException(forventet: MarkupVersjon, faktisk: MarkupVersjon) :
    IllegalStateException("Forventet markup-versjon $forventet, men fikk $faktisk")

sealed interface VersjonertMarkup {
    val markupVersjon: MarkupVersjon

    fun toEdit(): VersjonertBrev

    data class V1(val markup: LetterMarkup) : VersjonertMarkup {
        override val markupVersjon get() = MarkupVersjon.V1
        override fun toEdit() = VersjonertBrev.V1(markup.toEdit())
    }

    data class V2(val markup: MarkupV2) : VersjonertMarkup {
        override val markupVersjon get() = MarkupVersjon.V2
        override fun toEdit() = VersjonertBrev.V2(EditV2.Letter.from(markup))
    }
}

data class RendretBrev(
    val markup: VersjonertMarkup,
    val letterDataUsage: Set<LetterMarkupWithDataUsage.Property>,
    val stableHashCode: StableHashCode,
)

internal fun JsonNode.markupVersjon(): MarkupVersjon =
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
