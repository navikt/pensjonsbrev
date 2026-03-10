package no.nav.pensjon.brev.skribenten.brevredigering.domain

import no.nav.pensjon.brev.skribenten.Features
import no.nav.pensjon.brev.skribenten.UnleashToggle
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.literals
import no.nav.pensjon.brevbaker.api.model.ElementTags

class FerdigRedigertPolicy {

    suspend fun erFerdigRedigert(brev: Brevredigering): Outcome<Unit, IkkeFerdigRedigert> =
        if (brev.redigertBrev.alleFritekstFelterErRedigert() && ((!Features.hindreDuplikateAvsnitt.isEnabled()) || (Features.hindreDuplikateAvsnitt.isEnabled() && brev.redigertBrev.alleDuplikateAvsnittErHaandtert()))) {
            success(Unit)
        } else {
            failure(IkkeFerdigRedigert.FritekstFelterUredigert)
        }

    sealed interface IkkeFerdigRedigert : BrevredigeringError {
        data object FritekstFelterUredigert : IkkeFerdigRedigert
    }

    private fun Edit.Letter.alleFritekstFelterErRedigert(): Boolean =
        literals.all { !it.tags.contains(ElementTags.FRITEKST) || it.editedText != null }

    private fun Edit.Letter.alleDuplikateAvsnittErHaandtert(): Boolean =
        blocks.all { it.missingFromTemplate != true }
}