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
        val alleFritekstFelterErRedigert = brev.redigertBrev.alleFritekstFelterErRedigert()
        val alleDuplikateAvsnittErHaandtert = (!Features.hindreDuplikateAvsnitt.isEnabled()) || brev.redigertBrev.alleDuplikateAvsnittErHaandtert()
        return if (alleFritekstFelterErRedigert && alleDuplikateAvsnittErHaandtert) {
            success(Unit)
        } else if (!alleFritekstFelterErRedigert) {
            failure(IkkeFerdigRedigert.FritekstFelterUredigert)
        } else {
            failure(IkkeFerdigRedigert.DuplikatAvsnittUhaandtert)
        }
    }

    sealed interface IkkeFerdigRedigert : BrevredigeringError {
        data object FritekstFelterUredigert : IkkeFerdigRedigert
        data object DuplikatAvsnittUhaandtert : IkkeFerdigRedigert
    }

    private fun Edit.Letter.alleFritekstFelterErRedigert(): Boolean =
        literals.all { !it.tags.contains(ElementTags.FRITEKST) || it.editedText != null }

    private fun Edit.Letter.alleDuplikateAvsnittErHaandtert(): Boolean =
        blocks.all { it.missingFromTemplate != true }
}