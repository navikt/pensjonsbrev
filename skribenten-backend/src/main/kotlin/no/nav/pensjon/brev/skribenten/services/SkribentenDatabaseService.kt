package no.nav.pensjon.brev.skribenten.services

import com.typesafe.config.Config
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction


class SkribentenDatabaseService {
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


fun initDatabase(config: Config) {
    // Creates db connection and registers it globally.
    val dbConfig = config.getConfig("database")
    val database = Database.connect(
        HikariDataSource(HikariConfig().apply {
            jdbcUrl = createJdbcUrl(dbConfig)
            username = dbConfig.getString("username")
            password = dbConfig.getString("password")
            validate()
        }),
    )
    transaction(database) {
        SchemaUtils.create(Favourites)
    }
}

fun createJdbcUrl(config: Config): String =
    with(config) {
        val url = getString("host")
        val port = getString("port")
        val dbName = getString("name")
        return "jdbc:postgresql://$url:$port/$dbName"
    }