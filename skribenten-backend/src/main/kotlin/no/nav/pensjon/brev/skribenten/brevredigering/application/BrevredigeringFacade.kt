package no.nav.pensjon.brev.skribenten.brevredigering.application

import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.*
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.db.BrevredigeringTable
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.SaksId
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class BrevredigeringFacade(
    private val frigiReservasjon: UseCaseHandler<FrigiReservasjonHandler.Request, Unit, BrevredigeringError>,
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
) : HentBrevService {


    override fun hentBrevInfo(brevId: BrevId): Dto.BrevInfo? =
        transaction { BrevredigeringEntity.findById(brevId)?.toBrevInfo(brevreservasjonPolicy) }

    fun hentBrevForSak(saksId: SaksId): List<Dto.BrevInfo> =
        transaction {
            BrevredigeringEntity.find { BrevredigeringTable.saksId eq saksId }
                .map { it.toBrevInfo(brevreservasjonPolicy) }
        }

    override fun hentBrevForAlleSaker(saksIder: Set<SaksId>): List<Dto.BrevInfo> =
        transaction {
            BrevredigeringEntity.find { BrevredigeringTable.saksId inList saksIder }
                .map { it.toBrevInfo(brevreservasjonPolicy) }
        }

    suspend fun frigiReservasjon(request: FrigiReservasjonHandler.Request): Outcome<Unit, BrevredigeringError>? =
        suspendTransaction {
            frigiReservasjon.invoke(request)?.onError { rollback() }
        }
}