package no.nav.pensjon.brev.skribenten.db

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.typesafe.config.Config
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.skribenten.letter.Edit
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.json.json
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

@Suppress("unused")
object Favourites : Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val userId: Column<String> = varchar("User Id", length = 50)
    val letterCode: Column<String> = varchar("Letter Code", length = 50)
    override val primaryKey = PrimaryKey(id, name = "PK_Favourite_ID")
}

val objectMapper: ObjectMapper = jacksonObjectMapper().apply {
    registerModule(JavaTimeModule())
    registerModule(Edit.JacksonModule)
    registerModule(BrevbakerBrevdataModule)
}

object BrevredigeringTable : LongIdTable() {
    val saksId : Column<Long> = long("saksId") //TODO: Om vi skal slå opp redigineringer på sak, legg til index
    val brevkode: Column<String> = varchar("brevkode", length = 50)
    val saksbehandlerValg = json<BrevbakerBrevdata>("saksbehandlerValg", objectMapper::writeValueAsString, objectMapper::readValue)
    val redigertBrev = json<Edit.Letter>("redigertBrev", objectMapper::writeValueAsString, objectMapper::readValue)
    val laastForRedigering: Column<Boolean> = bool("laastForRedigering")
    // TODO: introdusere value class for NavIdent?
    val redigeresAvNavIdent: Column<String?> = varchar("redigeresAvNavIdent", length = 50).nullable()
    val sistRedigertAvNavIdent : Column<String> = varchar("sistRedigertAvNavIdent", length = 50)
    val opprettetAvNavIdent: Column<String> = varchar("opprettetAvNavIdent", length = 50).index()
    val opprettet: Column<LocalDateTime> = datetime("opprettet")
    val sistredigert: Column<LocalDateTime> = datetime("sistredigert")
}

class Brevredigering(id: EntityID<Long>) : LongEntity(id) {
    var saksId  by BrevredigeringTable.saksId
    var brevkode by BrevredigeringTable.brevkode
    var saksbehandlerValg  by BrevredigeringTable.saksbehandlerValg
    var redigertBrev  by BrevredigeringTable.redigertBrev
    var laastForRedigering by BrevredigeringTable.laastForRedigering
    var redigeresAvNavIdent by BrevredigeringTable.redigeresAvNavIdent
    var sistRedigertAvNavIdent by BrevredigeringTable.sistRedigertAvNavIdent
    var opprettetAvNavIdent by BrevredigeringTable.opprettetAvNavIdent
    var opprettet by BrevredigeringTable.opprettet
    var sistredigert by BrevredigeringTable.sistredigert

    companion object: LongEntityClass<Brevredigering>(BrevredigeringTable)
}

fun initDatabase(config: Config) =
    config.getConfig("database").let {
        initDatabase(createJdbcUrl(it), it.getString("username"), it.getString("password"))
    }

fun initDatabase(jdbcUrl: String, username: String, password: String) {
    val database = Database.connect(
        HikariDataSource(HikariConfig().apply {
            this.jdbcUrl = jdbcUrl
            this.username = username
            this.password = password
            maximumPoolSize = 4
            validate()
        }),
    )
    transaction(database) {
        SchemaUtils.create(BrevredigeringTable, Favourites)
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
    private fun readResolve(): Any = BrevbakerBrevdataModule

    private class GenericBrevdata : LinkedHashMap<String, Any>(), BrevbakerBrevdata

    init {
        addDeserializer(BrevbakerBrevdata::class.java, BrevdataDeserializer)
    }

    private object BrevdataDeserializer : JsonDeserializer<BrevbakerBrevdata>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): BrevbakerBrevdata = ctxt.readValue(parser, GenericBrevdata::class.java)
    }
}