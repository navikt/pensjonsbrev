package no.nav.pensjon.brev.skribenten

import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevkode
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.TransactionHandler
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.db.Favourites
import no.nav.pensjon.brev.skribenten.model.NavIdent
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll

class HentFavoritterHandler(database: Database) : TransactionHandler<HentFavoritterHandler.Request, List<RedigerbarBrevkode>, Nothing>(database) {

    data class Request(val userId: NavIdent)

    override suspend fun execute(request: Request): Outcome<List<RedigerbarBrevkode>, Nothing> =
        success(Favourites.selectAll().where { Favourites.userId eq request.userId }.map { row -> row[Favourites.letterCode] })
}

class LeggTilFavorittHandler(database: Database) : TransactionHandler<LeggTilFavorittHandler.Request, Unit, Nothing>(database) {

    data class Request(val userId: NavIdent, val brevkode: RedigerbarBrevkode)

    override suspend fun execute(request: Request): Outcome<Unit, Nothing> {
        Favourites.insert {
            it[userId] = request.userId
            it[letterCode] = request.brevkode
        }
        return success(Unit)
    }
}

class FjernFavorittHandler(database: Database) : TransactionHandler<FjernFavorittHandler.Request, Unit, Nothing>(database) {

    data class Request(val userId: NavIdent, val brevkode: RedigerbarBrevkode)

    override suspend fun execute(request: Request): Outcome<Unit, Nothing> {
        Favourites.deleteWhere {
            (userId eq request.userId) and (letterCode eq request.brevkode)
        }
        return success(Unit)
    }
}
