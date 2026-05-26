package no.nav.pensjon.brev.skribenten.brevredigering.domain

import no.nav.pensjon.brev.skribenten.Features
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.literals
import no.nav.pensjon.brevbaker.api.model.ElementTags

class FerdigRedigertPolicy {

    suspend fun erFerdigRedigert(brev: Brevredigering): Outcome<Unit, IkkeFerdigRedigert> {
        val ikkeredigerteFritekstfelter = brev.redigertBrev.ikkeredigerteFritekstfelter()
        val alleDuplikateAvsnittErHaandtert = (!Features.hindreDuplikateAvsnitt.isEnabled()) || brev.redigertBrev.alleDuplikateAvsnittErHaandtert()
        return if (ikkeredigerteFritekstfelter.none() && alleDuplikateAvsnittErHaandtert) {
            success(Unit)
        } else if (ikkeredigerteFritekstfelter.isNotEmpty()) {
            failure(IkkeFerdigRedigert.FritekstFelterUredigert(ikkeredigerteFritekstfelter))
        } else {
            failure(IkkeFerdigRedigert.DuplikatAvsnittUhaandtert)
        }
    }

    sealed interface IkkeFerdigRedigert : BrevredigeringError {
        data class FritekstFelterUredigert(val ikkeredigerteFritekstfelter: List<Edit.ParagraphContent.Text.Literal>) : IkkeFerdigRedigert
        data object DuplikatAvsnittUhaandtert : IkkeFerdigRedigert
    }

    private fun Edit.Letter.ikkeredigerteFritekstfelter(): List<Edit.ParagraphContent.Text.Literal> =
        literals.filterNot { !it.tags.contains(ElementTags.FRITEKST) || it.editedText != null }

    private fun Edit.Letter.alleDuplikateAvsnittErHaandtert(): Boolean =
        blocks.all { it.missingFromTemplate != true }
}