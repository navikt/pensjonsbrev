package no.nav.pensjon.brev.skribenten.saksbehandler

import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.db.BekreftetNyhetsvarselTable
import no.nav.pensjon.brev.skribenten.db.SettNyhetsvarselTable
import no.nav.pensjon.brev.skribenten.db.Transactional
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brev.skribenten.model.NyhetsvarselId
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insertIgnore
import org.jetbrains.exposed.v1.jdbc.select
import java.time.Instant

class HentBekreftedeNyhetsvarslerHandler(private val transactional: Transactional) {

    data class Request(val navIdent: NavIdent)

    suspend operator fun invoke(request: Request): Outcome<List<NyhetsvarselId>, Nothing> =
        transactional.rollbackOnFailure {
            success(
                BekreftetNyhetsvarselTable.select(BekreftetNyhetsvarselTable.nyhetsvarselId)
                    .where { BekreftetNyhetsvarselTable.navIdent eq request.navIdent }
                    .map { it[BekreftetNyhetsvarselTable.nyhetsvarselId].value }
            )
        }
}

class BekreftNyhetsvarselHandler(private val transactional: Transactional) {

    data class Request(val navIdent: NavIdent, val nyhetsvarselId: NyhetsvarselId)

    suspend operator fun invoke(request: Request): Outcome<Unit, Nothing> =
        transactional.rollbackOnFailure {
            BekreftetNyhetsvarselTable.insertIgnore {
                it[navIdent] = request.navIdent
                it[nyhetsvarselId] = request.nyhetsvarselId
                it[bekreftet] = Instant.now()
            }
            success(Unit)
        }
}

class HentSetteNyhetsvarslerHandler(private val transactional: Transactional) {

    data class Request(val navIdent: NavIdent)

    suspend operator fun invoke(request: Request): Outcome<List<NyhetsvarselId>, Nothing> =
        transactional.rollbackOnFailure {
            success(
                SettNyhetsvarselTable.select(SettNyhetsvarselTable.nyhetsvarselId)
                    .where { SettNyhetsvarselTable.navIdent eq request.navIdent }
                    .map { it[SettNyhetsvarselTable.nyhetsvarselId].value }
            )
        }
}

class MarkerNyhetsvarselSomSettHandler(private val transactional: Transactional) {

    data class Request(val navIdent: NavIdent, val nyhetsvarselId: NyhetsvarselId)

    suspend operator fun invoke(request: Request): Outcome<Unit, Nothing> =
        transactional.rollbackOnFailure {
            SettNyhetsvarselTable.insertIgnore {
                it[navIdent] = request.navIdent
                it[nyhetsvarselId] = request.nyhetsvarselId
                it[sett] = Instant.now()
            }
            success(Unit)
        }
}
