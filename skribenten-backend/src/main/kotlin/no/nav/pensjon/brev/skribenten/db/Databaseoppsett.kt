package no.nav.pensjon.brev.skribenten.db

import com.fasterxml.jackson.core.JacksonException
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.typesafe.config.Config
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import no.nav.pensjon.brev.skribenten.db.kryptering.EncryptedByteArray
import no.nav.pensjon.brev.skribenten.serialize.LetterMarkupJacksonModule
import no.nav.pensjon.brevbaker.api.model.AlltidValgbartVedleggBrevkode
import no.nav.pensjon.brevbaker.api.model.AlltidValgbartVedleggKode
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.columnTransformer
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.jdbc.Database
import java.util.concurrent.atomic.AtomicBoolean
import javax.sql.DataSource
import kotlin.jvm.java

val databaseReady: AtomicBoolean = AtomicBoolean(false)


object ValgteVedleggModule : SimpleModule() {
    @Suppress("unused")
    private fun readResolve(): Any = ValgteVedleggModule

    init {
        addDeserializer(AlltidValgbartVedleggKode::class.java, alltidValgbartVedleggKodeDeserializer())
        addDeserializer(AlltidValgbartVedleggBrevkode::class.java, alltidValgbartVedleggBrevkodeDeserializer())
    }

    fun alltidValgbartVedleggKodeDeserializer() = object : JsonDeserializer<AlltidValgbartVedleggKode>() {
        override fun deserialize(p: com.fasterxml.jackson.core.JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): AlltidValgbartVedleggKode {
            val node = p.codec.readTree<com.fasterxml.jackson.databind.JsonNode>(p)
            val kode = node.get("kode").textValue()
            val visningstekst = node.get("visningstekst").textValue()
            val spraak = setOf(LanguageCode.BOKMAL, LanguageCode.ENGLISH)
            return AlltidValgbartVedleggBrevkode(kode = kode, visningstekst = visningstekst, spraak = spraak)
        }
    }

    fun alltidValgbartVedleggBrevkodeDeserializer() = object : JsonDeserializer<AlltidValgbartVedleggBrevkode>() {
        override fun deserialize(p: com.fasterxml.jackson.core.JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): AlltidValgbartVedleggBrevkode {
            val node = p.codec.readTree<com.fasterxml.jackson.databind.JsonNode>(p)
            val kode = node.get("kode").textValue()
            val visningstekst = node.get("visningstekst").textValue()
            val spraak = setOf(LanguageCode.BOKMAL, LanguageCode.ENGLISH)
            return AlltidValgbartVedleggBrevkode(kode = kode, visningstekst = visningstekst, spraak = spraak)
        }
    }
}

internal val databaseObjectMapper: ObjectMapper = jacksonObjectMapper().apply {
    registerModule(JavaTimeModule())
    registerModule(LetterMarkupJacksonModule)
    registerModule(ValgteVedleggModule)
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
        initDatabase(
            jdbcUrl = createJdbcUrl(it),
            username = it.getString("username"),
            password = it.getString("password"),
            maxPoolSize = it.getInt("maxPoolSize"),
        )
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
        .also { databaseReady.set(true) }

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