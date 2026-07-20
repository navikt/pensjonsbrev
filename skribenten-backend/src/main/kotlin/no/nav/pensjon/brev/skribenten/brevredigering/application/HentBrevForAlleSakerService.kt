package no.nav.pensjon.brev.skribenten.brevredigering.application

import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.HentBrevForAlleSakerHandler
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.model.Dto

fun interface HentBrevForAlleSakerService {
    suspend operator fun invoke(request: HentBrevForAlleSakerHandler.Request): Outcome<List<Dto.BrevInfo>, Nothing>
}
