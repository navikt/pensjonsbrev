package no.nav.pensjon.brev.skribenten.services

import com.typesafe.config.Config
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.ds.PGSimpleDataSource


class SkribentenDatabaseService(private val database: Database) {
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


@Suppress("unused")
object Favourites : Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val userId: Column<String> = varchar("User Id", length = 50)
    val letterCode: Column<String> = varchar("Letter Code", length = 50)
    override val primaryKey = PrimaryKey(id, name = "PK_Favourite_ID")
}


fun initDatabase(config: Config): Database {
    val database = Database.connect(
        PGSimpleDataSource().apply {
            setURL(config.getString("database.url"))
            user = "postgres"
            password = "pass"
        }
    )
    transaction(database) {
        SchemaUtils.create(Favourites)
    }

    return database
}