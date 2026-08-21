package no.nav.pensjon.brev.skribenten

import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevkode
import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.Brevtilgang
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.db.Favourites
import no.nav.pensjon.brev.skribenten.model.NavIdent
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll

class HentFavoritterHandler(private val brevtilgang: Brevtilgang) {

    data class Request(val userId: NavIdent)

    suspend operator fun invoke(request: Request): Outcome<List<RedigerbarBrevkode>, Nothing> =
        brevtilgang.iTransaksjon {
            success(Favourites.selectAll().where { Favourites.userId eq request.userId }.map { row -> row[Favourites.letterCode] })
        }
}

class LeggTilFavorittHandler(private val brevtilgang: Brevtilgang) {

    data class Request(val userId: NavIdent, val brevkode: RedigerbarBrevkode)

    suspend operator fun invoke(request: Request): Outcome<Unit, Nothing> =
        brevtilgang.iTransaksjon {
            Favourites.insert {
                it[userId] = request.userId
                it[letterCode] = request.brevkode
            }
            success(Unit)
        }
}

class FjernFavorittHandler(private val brevtilgang: Brevtilgang) {

    data class Request(val userId: NavIdent, val brevkode: RedigerbarBrevkode)

    suspend operator fun invoke(request: Request): Outcome<Unit, Nothing> =
        brevtilgang.iTransaksjon {
            Favourites.deleteWhere {
                (userId eq request.userId) and (letterCode eq request.brevkode)
            }
            success(Unit)
        }
}
