package no.nav.pensjon.brev.skribenten.saksbehandler

import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.db.NyhetsvarselstatusTable
import no.nav.pensjon.brev.skribenten.db.Transactional
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brev.skribenten.model.NyhetsvarselId
import no.nav.pensjon.brev.skribenten.model.Nyhetsvarselmarkering
import org.jetbrains.exposed.v1.core.Coalesce
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.upsert
import java.time.Instant

class HentNyhetsvarslerHandler(private val transactional: Transactional) {

    data class Request(val navIdent: NavIdent)

    suspend operator fun invoke(request: Request): Outcome<Api.Nyhetsvarsler, Nothing> =
        transactional.rollbackOnFailure {
            val rader = NyhetsvarselstatusTable.selectAll()
                .where { NyhetsvarselstatusTable.navIdent eq request.navIdent }
                .toList()
            success(
                Api.Nyhetsvarsler(
                    settAnnonsen = rader.filter { it[NyhetsvarselstatusTable.settAnnonsen] != null }
                        .map { it[NyhetsvarselstatusTable.nyhetsvarselId].value },
                    gjennomfoertOmvisningen = rader.filter { it[NyhetsvarselstatusTable.gjennomfoertOmvisningen] != null }
                        .map { it[NyhetsvarselstatusTable.nyhetsvarselId].value },
                )
            )
        }
}

class MarkerNyhetsvarselHandler(private val transactional: Transactional) {

    data class Request(val navIdent: NavIdent, val nyhetsvarselId: NyhetsvarselId, val markering: Nyhetsvarselmarkering)

    suspend operator fun invoke(request: Request): Outcome<Unit, Nothing> =
        transactional.rollbackOnFailure {
            val tidspunkt = when (request.markering) {
                Nyhetsvarselmarkering.SETT_ANNONSEN -> NyhetsvarselstatusTable.settAnnonsen
                Nyhetsvarselmarkering.GJENNOMFOERT_OMVISNINGEN -> NyhetsvarselstatusTable.gjennomfoertOmvisningen
            }
            NyhetsvarselstatusTable.upsert(onUpdate = { it[tidspunkt] = Coalesce(tidspunkt, insertValue(tidspunkt)) }) {
                it[navIdent] = request.navIdent
                it[nyhetsvarselId] = request.nyhetsvarselId
                it[tidspunkt] = Instant.now()
            }
            success(Unit)
        }
}
