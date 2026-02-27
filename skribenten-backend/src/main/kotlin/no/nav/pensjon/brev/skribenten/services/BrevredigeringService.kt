package no.nav.pensjon.brev.skribenten.services

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.skribenten.db.BrevredigeringTable
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.model.*
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


    /**
     * Slett brev med id.
     * @return `true` om brevet ble slettet, false om brevet ikke eksisterer,
     */
    fun slettBrev(saksId: SaksId, brevId: BrevId): Boolean {
        return transaction {
            val brev = BrevredigeringEntity.findByIdAndSaksId(brevId, saksId)
            if (brev != null) {
                brev.delete()
                true
            } else {
                false
            }
        }
    }

    override fun hentBrevForAlleSaker(saksIder: Set<SaksId>): List<Dto.BrevInfo> =
        transaction {
            BrevredigeringEntity.find { BrevredigeringTable.saksId inList saksIder }
                .map { it.toBrevInfo(brevreservasjonPolicy) }
        }
}
