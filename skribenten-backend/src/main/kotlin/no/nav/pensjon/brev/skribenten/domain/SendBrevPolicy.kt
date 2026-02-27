package no.nav.pensjon.brev.skribenten.domain

import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.usecase.Outcome
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.success

class SendBrevPolicy(private val ferdigRedigertPolicy: FerdigRedigertPolicy) {

    fun kanSende(brev: Brevredigering, document: Dto.Document): Outcome<Unit, BrevredigeringError> {
        ferdigRedigertPolicy.erFerdigRedigert(brev).onError { return failure(it) }

        if (!brev.laastForRedigering) {
            return failure(KanIkkeSende.IkkeLaastForRedigering(brev.id.value))
        }

        if (document.redigertBrevHash != brev.redigertBrevHash) {
            return failure(KanIkkeSende.DocumentIkkeForGjeldendeRedigertBrev(brev.id.value))
        }

        if (brev.isVedtaksbrev && brev.attestertAvNavIdent == null) {
            return failure(KanIkkeSende.VedtaksbrevIkkeAttestert(brev.id.value))
        }

        return success(Unit)
    }

    sealed interface KanIkkeSende : BrevredigeringError {
        data class IkkeLaastForRedigering(val brevId: BrevId) : KanIkkeSende
        data class DocumentIkkeForGjeldendeRedigertBrev(val brevId: BrevId) : KanIkkeSende
        data class VedtaksbrevIkkeAttestert(val brevId: BrevId) : KanIkkeSende
    }
}


