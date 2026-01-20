package no.nav.pensjon.brev.skribenten.domain

import no.nav.pensjon.brev.skribenten.letter.alleFritekstFelterErRedigert
import no.nav.pensjon.brev.skribenten.usecase.Outcome
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.success

class KlarTilSendingPolicy {

    fun kanSettesTilKlar(brev: Brevredigering): Outcome<Boolean, IkkeKlarTilSending> =
        if (brev.redigertBrev.alleFritekstFelterErRedigert()) {
            success(true)
        } else {
            failure(IkkeKlarTilSending.FritekstFelterUredigert)
        }

    sealed interface IkkeKlarTilSending : BrevredigeringError {
        data object FritekstFelterUredigert : IkkeKlarTilSending
    }
}