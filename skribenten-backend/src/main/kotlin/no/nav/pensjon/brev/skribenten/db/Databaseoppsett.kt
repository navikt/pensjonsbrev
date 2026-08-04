package no.nav.pensjon.brev.skribenten.db

import com.fasterxml.jackson.core.JacksonException
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.*
import com.typesafe.config.Config
import com.zaxxer.hikari.*
import no.nav.pensjon.brev.skribenten.SkribentenConfig
import no.nav.pensjon.brev.skribenten.db.kryptering.EncryptedByteArray
import no.nav.pensjon.brev.skribenten.serialize.LetterMarkupJacksonModule
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.v1.core.*
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import java.util.concurrent.atomic.AtomicBoolean
import javax.sql.DataSource
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

val databaseReady: AtomicBoolean = AtomicBoolean(false)


internal val databaseObjectMapper: ObjectMapper = jacksonObjectMapper().apply {
    registerModule(JavaTimeModule())
    registerModule(LetterMarkupJacksonModule)
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

fun dataSourceFactory(config: SkribentenConfig): HikariDataSource = with(config.database) {
    initDatabase(
        jdbcUrl = "jdbc:postgresql://$host:$port/$name",
        username = username,
        password = password,
        maxPoolSize = maxPoolSize,
    )
}

fun initDatabase(jdbcUrl: String, username: String, password: String, maxPoolSize: Int = 2): HikariDataSource =
    HikariDataSource(HikariConfig().apply {
        this.jdbcUrl = jdbcUrl
        this.username = username
        this.password = password
        this.initializationFailTimeout = 6000
        maximumPoolSize = maxPoolSize
        // Cloud SQL/proxy kan lukke inaktive forbindelser før Hikari sin default maxLifetime (30 min),
        // som gir "Failed to validate connection ... This connection has been closed".
        // Poolen er bevisst fixed-size (minimumIdle == maximumPoolSize), så idleTimeout settes ikke.
        maxLifetime = 10.minutes.inWholeMilliseconds
        keepaliveTime = 2.minutes.inWholeMilliseconds
        connectionTimeout = 5.seconds.inWholeMilliseconds
        validationTimeout = 3.seconds.inWholeMilliseconds
        validate()
    }).also { konfigurerFlyway(it) }


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