package no.nav.pensjon.brev.skribenten.brevredigering.domain

import no.nav.pensjon.brev.skribenten.Features
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.Literal
import no.nav.pensjon.brev.skribenten.letter.EditLetterVisitor
import no.nav.pensjon.brevbaker.api.model.ElementTags

class FerdigRedigertPolicy {

    suspend fun erFerdigRedigert(brev: Brevredigering): Outcome<Unit, IkkeFerdigRedigert> {
        val uredigerteFritekstfelter = brev.redigertBrev.uredigerteFritekstfelter()
        val alleDuplikateAvsnittErHaandtert = (!Features.hindreDuplikateAvsnitt.isEnabled()) || brev.redigertBrev.alleDuplikateAvsnittErHaandtert()
        return if (uredigerteFritekstfelter.none() && alleDuplikateAvsnittErHaandtert) {
            success(Unit)
        } else if (uredigerteFritekstfelter.any()) {
            failure(IkkeFerdigRedigert.FritekstFelterUredigert(uredigerteFritekstfelter))
        } else {
            failure(IkkeFerdigRedigert.DuplikatAvsnittUhaandtert)
        }
    }

    sealed interface IkkeFerdigRedigert : BrevredigeringError {
        data class FritekstFelterUredigert(val ikkeredigerteFritekstfelter: List<String>) : IkkeFerdigRedigert
        data object DuplikatAvsnittUhaandtert : IkkeFerdigRedigert
    }

    private fun VersjonertBrev.uredigerteFritekstfelter(): List<String> = when (this) {
        is VersjonertBrev.V1 -> brev.literals.filter { it.tags.contains(ElementTags.FRITEKST) && it.editedText == null }.map { it.text }
    }

    private fun VersjonertBrev.alleDuplikateAvsnittErHaandtert(): Boolean = when (this) {
        is VersjonertBrev.V1 -> brev.blocks.none { it.missingFromTemplate }
    }
}

private val Edit.Letter.literals: List<Literal>
    get() = object : EditLetterVisitor<Literal>(this) {
        override fun visit(content: Literal) = emit(content)
    }.build()