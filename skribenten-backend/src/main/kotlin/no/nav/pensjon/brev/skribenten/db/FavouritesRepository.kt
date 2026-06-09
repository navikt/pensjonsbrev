package no.nav.pensjon.brev.skribenten.db

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.skribenten.model.NavIdent
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class FavouritesRepository {
    fun getFavourites(userId: NavIdent): List<Brevkode.Redigerbart> =
        transaction {
            Favourites.selectAll().where { Favourites.userId eq userId }.map { row -> row[Favourites.letterCode] }
        }


    fun addFavourite(userID: NavIdent, letterId: Brevkode.Redigerbart) {
        transaction {
            Favourites.insert {
                it[userId] = userID
                it[letterCode] = letterId
            }
        }
    }

    fun removeFavourite(userID: NavIdent, letterId: Brevkode.Redigerbart) {
        transaction {
            Favourites.deleteWhere {
                userId eq userID
                letterCode eq letterId
            }
        }
    }
}