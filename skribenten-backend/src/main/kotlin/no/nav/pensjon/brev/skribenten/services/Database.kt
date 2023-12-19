package no.nav.pensjon.brev.skribenten.services

import com.typesafe.config.Config
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

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