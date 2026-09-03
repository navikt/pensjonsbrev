package no.nav.pensjon.brev.skribenten.brevredigering.application.oppslag

import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.db.BrevredigeringTable
import no.nav.pensjon.brev.skribenten.db.Transactional
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.SaksId
import org.jetbrains.exposed.v1.core.eq

class HentBrevForSakHandler(
    private val transactional: Transactional,
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
) {

    data class Request(val saksId: SaksId)

    suspend operator fun invoke(request: Request): Outcome<List<Dto.BrevInfo>, Nothing> =
        transactional.rollbackOnFailure {
            success(
                BrevredigeringEntity.find { BrevredigeringTable.saksId eq request.saksId }
                    .map { it.toBrevInfo(brevreservasjonPolicy) }
            )
        }
}
