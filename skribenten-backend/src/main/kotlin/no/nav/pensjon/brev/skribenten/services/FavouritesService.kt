package no.nav.pensjon.brev.skribenten.services

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class FavouritesService {
    fun getFavourites(userId: String): List<String> =
        transaction {
            Favourites.select { Favourites.userId eq userId }.map { row -> row[Favourites.letterCode] }
        }


    fun addFavourite(userID: String, letterId: String) {
        transaction {
            Favourites.insert {
                it[userId] = userID
                it[letterCode] = letterId
            }
        }
    }

    fun removeFavourite(userID: String, letterId: String) {
        transaction {
            Favourites.deleteWhere {
                userId eq userID
                letterCode eq letterId
            }
        }
    }
}