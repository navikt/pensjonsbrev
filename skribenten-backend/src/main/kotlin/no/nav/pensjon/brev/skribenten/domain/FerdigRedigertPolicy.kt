package no.nav.pensjon.brev.skribenten.domain

import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.literals
import no.nav.pensjon.brev.skribenten.usecase.Outcome
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.success
import no.nav.pensjon.brevbaker.api.model.ElementTags

class FerdigRedigertPolicy {

    fun erFerdigRedigert(brev: Brevredigering): Outcome<Unit, IkkeFerdigRedigert> =
        if (brev.redigertBrev.alleFritekstFelterErRedigert()) {
            success(Unit)
        } else {
            failure(IkkeFerdigRedigert.FritekstFelterUredigert)
        }

    sealed interface IkkeFerdigRedigert : BrevredigeringError {
        data object FritekstFelterUredigert : IkkeFerdigRedigert
    }

    // Sjekker om brevet er klar til sending. Foreløpig er det kun at alle fritekst felter er redigert som sjekkes,
    // om det legges til flere valideringer så må feilmeldingen i BrevredigeringsService.validerErKlarTilSending endres.
    private fun Edit.Letter.alleFritekstFelterErRedigert(): Boolean =
        literals.all { !it.tags.contains(ElementTags.FRITEKST) || it.editedText != null }
}