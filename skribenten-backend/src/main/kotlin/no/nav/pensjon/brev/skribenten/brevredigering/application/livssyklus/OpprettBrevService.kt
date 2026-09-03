package no.nav.pensjon.brev.skribenten.brevredigering.application.livssyklus

import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.model.Dto

fun interface OpprettBrevService {
    suspend operator fun invoke(request: OpprettBrevHandler.Request): Outcome<Dto.Brevredigering, BrevredigeringError>
}
