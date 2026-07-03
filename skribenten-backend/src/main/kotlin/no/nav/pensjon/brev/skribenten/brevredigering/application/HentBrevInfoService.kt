package no.nav.pensjon.brev.skribenten.brevredigering.application

import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.db.BrevredigeringTable
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.SaksId
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

interface HentBrevInfoService {
    fun hentBrevForAlleSaker(saksIder: Set<SaksId>): List<Dto.BrevInfo>
    fun hentBrevInfo(brevId: BrevId): Dto.BrevInfo?
    fun hentBrevForSak(saksId: SaksId): List<Dto.BrevInfo>
}

class HentBrevInfoServiceImpl(
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
    private val database: Database,
) : HentBrevInfoService {

    override fun hentBrevInfo(brevId: BrevId): Dto.BrevInfo? =
        transaction(db = database) { BrevredigeringEntity.findById(brevId)?.toBrevInfo(brevreservasjonPolicy) }

    override fun hentBrevForSak(saksId: SaksId): List<Dto.BrevInfo> =
        transaction(db = database) {
            BrevredigeringEntity.find { BrevredigeringTable.saksId eq saksId }
                .map { it.toBrevInfo(brevreservasjonPolicy) }
        }

    override fun hentBrevForAlleSaker(saksIder: Set<SaksId>): List<Dto.BrevInfo> =
        transaction(db = database) {
            BrevredigeringEntity.find { BrevredigeringTable.saksId inList saksIder }
                .map { it.toBrevInfo(brevreservasjonPolicy) }
        }
}