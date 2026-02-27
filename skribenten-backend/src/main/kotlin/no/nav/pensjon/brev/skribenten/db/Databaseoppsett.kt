package no.nav.pensjon.brev.skribenten.db

import com.fasterxml.jackson.core.JacksonException
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.typesafe.config.Config
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import no.nav.pensjon.brev.skribenten.db.kryptering.EncryptedByteArray
import no.nav.pensjon.brev.skribenten.serialize.BrevkodeJacksonModule
import no.nav.pensjon.brev.skribenten.serialize.EditLetterJacksonModule
import no.nav.pensjon.brev.skribenten.serialize.LetterMarkupJacksonModule
import no.nav.pensjon.brev.skribenten.serialize.SakstypeModule
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.columnTransformer
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.jdbc.Database
import javax.sql.DataSource

internal val databaseObjectMapper: ObjectMapper = jacksonObjectMapper().apply {
    registerModule(JavaTimeModule())
    registerModule(EditLetterJacksonModule)
    registerModule(LetterMarkupJacksonModule)
    registerModule(SakstypeModule)
    registerModule(BrevkodeJacksonModule)
    disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
}

class DatabaseJsonDeserializeException(cause: JacksonException): Exception("Failed to deserialize json-column from database", cause)

internal inline fun <reified T> IdTable<*>.readJsonString(json: String): T =
    try {
        databaseObjectMapper.readValue<T>(json)
    } catch (e: JacksonException) {
        throw DatabaseJsonDeserializeException(e)
    }

internal fun Table.encryptedBinary(name: String): Column<EncryptedByteArray> =
    binary(name).transform(columnTransformer(unwrap = EncryptedByteArray::bytes, wrap = ::EncryptedByteArray))

internal inline fun <reified T> IdTable<*>.readJsonBinary(json: ByteArray): T =
    try {
        databaseObjectMapper.readValue<T>(json)
    } catch (e: JacksonException) {
        throw DatabaseJsonDeserializeException(e)
    }


fun initDatabase(config: Config) =
    config.getConfig("database").let {
        initDatabase(createJdbcUrl(it), it.getString("username"), it.getString("password"))
    }

fun initDatabase(jdbcUrl: String, username: String, password: String, maxPoolSize: Int = 2) =
    HikariDataSource(HikariConfig().apply {
        this.jdbcUrl = jdbcUrl
        this.username = username
        this.password = password
        this.initializationFailTimeout = 6000
        maximumPoolSize = maxPoolSize
        validate()
    })
        .also { konfigurerFlyway(it) }
        .also { Database.connect(it) }

private fun konfigurerFlyway(dataSource: DataSource) = Flyway
    .configure()
    .dataSource(dataSource)
    .baselineOnMigrate(true)
    .validateMigrationNaming(true)
    .load()
    .migrate()


private fun createJdbcUrl(config: Config): String =
    with(config) {
        val url = getString("host")
        val port = getString("port")
        val dbName = getString("name")
        return "jdbc:postgresql://$url:$port/$dbName"
    }