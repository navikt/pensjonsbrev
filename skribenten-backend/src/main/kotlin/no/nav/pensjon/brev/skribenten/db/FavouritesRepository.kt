package no.nav.pensjon.brev.skribenten.db

import no.nav.pensjon.brev.skribenten.model.NavIdent
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class FavouritesRepository {
    fun getFavourites(userId: NavIdent): List<String> =
        transaction {
            Favourites.selectAll().where { Favourites.userId eq userId.id }.map { row -> row[Favourites.letterCode] }
        }

    fun addFavourite(
        userID: NavIdent,
        letterId: String,
    ) {
        transaction {
            Favourites.insert {
                it[userId] = userID.id
                it[letterCode] = letterId
            }
        }
    }

    fun removeFavourite(
        userID: NavIdent,
        letterId: String,
    ) {
        transaction {
            Favourites.deleteWhere {
                userId eq userID.id
                letterCode eq letterId
            }
        }
    }
}
