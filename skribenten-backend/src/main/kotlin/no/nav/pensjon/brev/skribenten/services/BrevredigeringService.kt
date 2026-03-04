package no.nav.pensjon.brev.skribenten.services

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.db.BrevredigeringTable
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.SaksId
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

data class GeneriskRedigerbarBrevdata(
    override val pesysData: FagsystemBrevdata,
    override val saksbehandlerValg: SaksbehandlerValgBrevdata,
) : RedigerbarBrevdata<SaksbehandlerValgBrevdata, FagsystemBrevdata>

sealed class BrevredigeringException(override val message: String) : Exception() {
    class BrevmalFinnesIkke(message: String) : BrevredigeringException(message)
}

interface HentBrevService {
    fun hentBrevForAlleSaker(saksIder: Set<SaksId>): List<Dto.BrevInfo>
}

class BrevredigeringService : HentBrevService {
    private val brevreservasjonPolicy = BrevreservasjonPolicy()

    override fun hentBrevForAlleSaker(saksIder: Set<SaksId>): List<Dto.BrevInfo> =
        transaction {
            BrevredigeringEntity.find { BrevredigeringTable.saksId inList saksIder }
                .map { it.toBrevInfo(brevreservasjonPolicy) }
        }
}
