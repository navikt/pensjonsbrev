package no.nav.pensjon.brev.skribenten.brevredigering.domain

import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import no.nav.brev.brevbaker.StableHashCode
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.letter.updateEditedLetter
import no.nav.pensjon.brev.skribenten.letter.updateSakspartOgSignatur
import no.nav.pensjon.brev.skribenten.letter.withTemplateStatus
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsage

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
    val saksbehandlerNavn: String?
    val attesterendeSaksbehandlerNavn: String?
    val tittel: String

    fun innholdEndresAv(rendret: VersjonertMarkup): Boolean
    fun merge(rendret: VersjonertMarkup, previousRender: VersjonertBrev?): VersjonertBrev
    fun oppdaterSakspartOgSignatur(rendret: VersjonertMarkup): VersjonertBrev
    fun withSignaturAttestant(attesterendeSaksbehandlerNavn: String?): VersjonertBrev
    fun withAnnenMottakerNavn(annenMottakerNavn: String?): VersjonertBrev

    data class V1(@get:JsonValue val brev: Edit.Letter) : VersjonertBrev {
        override val markupVersjon get() = MarkupVersjon.V1
        override val saksbehandlerNavn get() = brev.signatur.saksbehandlerNavn
        override val attesterendeSaksbehandlerNavn get() = brev.signatur.attesterendeSaksbehandlerNavn
        override val tittel get() = brev.title.text.joinToString(" ") { it.text }.trim()

        // NEW_IN_TEMPLATE only marks a block for the most recent merge, so it is not a change in content.
        override fun innholdEndresAv(rendret: VersjonertMarkup) =
            brev.updateEditedLetter(rendret.asV1()).blocks.utenNyIMal() != brev.blocks.utenNyIMal()

        override fun merge(rendret: VersjonertMarkup, previousRender: VersjonertBrev?) =
            V1(brev.updateEditedLetter(rendret.asV1(), previousRender.asV1()))

        override fun oppdaterSakspartOgSignatur(rendret: VersjonertMarkup) = V1(brev.updateSakspartOgSignatur(rendret.asV1()))

        override fun withSignaturAttestant(attesterendeSaksbehandlerNavn: String?) =
            V1(brev.withSignaturAttestant(attesterendeSaksbehandlerNavn))

        override fun withAnnenMottakerNavn(annenMottakerNavn: String?) = V1(brev.withSakspart(annenMottakerNavn = annenMottakerNavn))

        private fun VersjonertMarkup.asV1(): LetterMarkup =
            (this as? VersjonertMarkup.V1)?.markup ?: throw FeilMarkupVersjonException(this@V1.markupVersjon, markupVersjon)

        private fun List<Edit.Block>.utenNyIMal(): List<Edit.Block> =
            map { if (it.templateStatus == Edit.Block.TemplateStatus.NEW_IN_TEMPLATE) it.withTemplateStatus(Edit.Block.TemplateStatus.IN_TEMPLATE) else it }

        private fun VersjonertBrev?.asV1(): Edit.Letter? =
            this?.let { (it as? V1)?.brev ?: throw FeilMarkupVersjonException(this@V1.markupVersjon, it.markupVersjon) }
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
        }
    }
}
