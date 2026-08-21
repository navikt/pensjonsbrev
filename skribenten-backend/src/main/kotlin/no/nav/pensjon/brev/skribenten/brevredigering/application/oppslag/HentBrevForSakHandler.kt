package no.nav.pensjon.brev.skribenten.brevredigering.application.oppslag

import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.Brevtilgang
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.db.BrevredigeringTable
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.SaksId
import org.jetbrains.exposed.v1.core.eq

class HentBrevForSakHandler(private val brevtilgang: Brevtilgang) {

    data class Request(val saksId: SaksId)

    suspend operator fun invoke(request: Request): Outcome<List<Dto.BrevInfo>, Nothing>? =
        brevtilgang.iTransaksjon {
            success(
                BrevredigeringEntity.find { BrevredigeringTable.saksId eq request.saksId }
                    .map { it.tilBrevInfo() }
            )
        }
}
