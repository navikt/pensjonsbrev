package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.typesafe.config.Config
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.serialization.json.Json
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.json.json
import org.jetbrains.exposed.sql.transactions.transaction

@Suppress("unused")
object Favourites : Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val userId: Column<String> = varchar("User Id", length = 50)
    val letterCode: Column<String> = varchar("Letter Code", length = 50)
    override val primaryKey = PrimaryKey(id, name = "PK_Favourite_ID")
}

val format = Json { prettyPrint = true }
val objectMapper: ObjectMapper = jacksonObjectMapper().apply {
    registerModule(JavaTimeModule())
    registerModule(RenderedLetterMarkdownModule)
    registerModule(BrevbakerBrevdataModule)
}


@kotlinx.serialization.Serializable
data class RedigerbarBrevdata(val json: String)

object Brevredigering : Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val eksternId: Column<String> = varchar("eksternId", length = 50)
    val saksid : Column<String> = varchar("saksid", length = 50)
    val brevkode: Column<String> = varchar("brevkode", length = 50)
    val saksbehandlerValg = json<BrevbakerBrevdata>("saksbehandlerValg", objectMapper::writeValueAsString, objectMapper::readValue)
    val redigertBrev = json<LetterMarkup>("redigertBrev", format)
    val laastForRedigering: Column<Boolean> = bool("laastForRedigering")
    val redigeresAvNavident: Column<String?> = varchar("brevkode", length = 50).nullable()
    val opprettetAvNavident: Column<String> = varchar("opprettetAvNavident", length = 50)
    val opprettet: Column<String> = varchar("opprettet", length = 50)
    val sistredigert: Column<String> = varchar("sistredigert", length = 50)

    override val primaryKey = PrimaryKey(id, name = "PK_Brevredigering_ID")
}

fun initDatabase(config: Config) {
    // Creates db connection and registers it globally.
    val dbConfig = config.getConfig("database")
    val database = Database.connect(
        HikariDataSource(HikariConfig().apply {
            jdbcUrl = createJdbcUrl(dbConfig)
            username = dbConfig.getString("username")
            password = dbConfig.getString("password")
            maximumPoolSize = 4
            validate()
        }),
    )
    transaction(database) {
        SchemaUtils.create(Favourites)
    }
}

private fun createJdbcUrl(config: Config): String =
    with(config) {
        val url = getString("host")
        val port = getString("port")
        val dbName = getString("name")
        return "jdbc:postgresql://$url:$port/$dbName"
    }



private object BrevbakerBrevdataModule : SimpleModule() {

    private class GenericBrevdata : LinkedHashMap<String, Any>(), BrevbakerBrevdata

    init {
        addDeserializer(BrevbakerBrevdata::class.java, BrevdataDeserializer)
    }

    private object BrevdataDeserializer : JsonDeserializer<BrevbakerBrevdata>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): BrevbakerBrevdata = ctxt.readValue(parser, GenericBrevdata::class.java)
    }
}