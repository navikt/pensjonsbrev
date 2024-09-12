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
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.model.Distribusjonstype
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SchemaUtils.withDataBaseLock
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.json.json
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.time.LocalDate

@Suppress("unused")
object Favourites : Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val userId: Column<String> = varchar("User Id", length = 50)
    val letterCode: Column<String> = varchar("Letter Code", length = 50)
    override val primaryKey = PrimaryKey(id, name = "PK_Favourite_ID")
}

internal val databaseObjectMapper: ObjectMapper = jacksonObjectMapper().apply {
    registerModule(JavaTimeModule())
    registerModule(Edit.JacksonModule)
    registerModule(BrevbakerBrevdataModule)
}

object BrevredigeringTable : LongIdTable() {
    val saksId: Column<Long> = long("saksId").index()
    val brevkode: Column<Brevkode.Redigerbar> = varchar("brevkode", length = 50).transform(Brevkode.Redigerbar::valueOf, Brevkode.Redigerbar::name)
    val spraak: Column<LanguageCode> = varchar("spraak", length = 50).transform(LanguageCode::valueOf, LanguageCode::name)
    val avsenderEnhetId: Column<String?> = varchar("avsenderEnhetId", 50).nullable()
    val saksbehandlerValg = json<BrevbakerBrevdata>("saksbehandlerValg", databaseObjectMapper::writeValueAsString, databaseObjectMapper::readValue)
    val redigertBrev = json<Edit.Letter>("redigertBrev", databaseObjectMapper::writeValueAsString, databaseObjectMapper::readValue)
    val redigertBrevHash: Column<ByteArray> = hashColumn("redigertBrevHash")
    val laastForRedigering: Column<Boolean> = bool("laastForRedigering")
    val distribusjonstype: Column<Distribusjonstype> = varchar("distribusjonstype", length = 50).transform(Distribusjonstype::valueOf, Distribusjonstype::name)
    val redigeresAvNavIdent: Column<String?> = varchar("redigeresAvNavIdent", length = 50).nullable()
    val sistRedigertAvNavIdent: Column<String> = varchar("sistRedigertAvNavIdent", length = 50)
    val opprettetAvNavIdent: Column<String> = varchar("opprettetAvNavIdent", length = 50).index()
    val opprettet: Column<Instant> = timestamp("opprettet")
    val sistredigert: Column<Instant> = timestamp("sistredigert")
    val sistReservert: Column<Instant?> = timestamp("sistReservert").nullable()
    val signaturSignerende: Column<String> = varchar("signaturSignerende", length = 50)
}

class Brevredigering(id: EntityID<Long>) : LongEntity(id) {
    var saksId by BrevredigeringTable.saksId
    var brevkode by BrevredigeringTable.brevkode
    var spraak by BrevredigeringTable.spraak
    var avsenderEnhetId by BrevredigeringTable.avsenderEnhetId
    var saksbehandlerValg by BrevredigeringTable.saksbehandlerValg
    var redigertBrev by BrevredigeringTable.redigertBrev.writeHashTo(BrevredigeringTable.redigertBrevHash)
    val redigertBrevHash by BrevredigeringTable.redigertBrevHash.editLetterHash()
    var laastForRedigering by BrevredigeringTable.laastForRedigering
    var distribusjonstype by BrevredigeringTable.distribusjonstype
    var redigeresAvNavIdent by BrevredigeringTable.redigeresAvNavIdent.wrap(::NavIdent, NavIdent::id)
    var sistRedigertAvNavIdent by BrevredigeringTable.sistRedigertAvNavIdent.wrap(::NavIdent, NavIdent::id)
    var opprettetAvNavIdent by BrevredigeringTable.opprettetAvNavIdent.wrap(::NavIdent, NavIdent::id)
    var opprettet by BrevredigeringTable.opprettet
    var sistredigert by BrevredigeringTable.sistredigert
    var sistReservert by BrevredigeringTable.sistReservert
    var signaturSignerende by BrevredigeringTable.signaturSignerende
    val document by Document referrersOn DocumentTable.brevredigering orderBy (DocumentTable.id to SortOrder.DESC)
    val mottaker by Mottaker optionalBackReferencedOn MottakerTable.id

    companion object : LongEntityClass<Brevredigering>(BrevredigeringTable) {
        fun findByIdAndSaksId(id: Long, saksId: Long?) =
            if (saksId == null) {
                findById(id)
            } else {
                find { (BrevredigeringTable.id eq id) and (BrevredigeringTable.saksId eq saksId) }.firstOrNull()
            }
    }
}

object DocumentTable : LongIdTable() {
    val brevredigering: Column<EntityID<Long>> = reference("brevredigering", BrevredigeringTable.id, onDelete = ReferenceOption.CASCADE).uniqueIndex()
    val dokumentDato: Column<LocalDate> = date("dokumentDato")
    val pdf: Column<ExposedBlob> = blob("brevpdf")
    val redigertBrevHash: Column<ByteArray> = hashColumn("redigertBrevHash")
}

class Document(id: EntityID<Long>) : LongEntity(id) {
    var brevredigering by Brevredigering referencedOn DocumentTable.brevredigering
    var dokumentDato by DocumentTable.dokumentDato
    var pdf by DocumentTable.pdf
    var redigertBrevHash by DocumentTable.redigertBrevHash.editLetterHash()

    companion object : LongEntityClass<Document>(DocumentTable)
}

object MottakerTable : IdTable<Long>() {
    override val id: Column<EntityID<Long>> = reference("brevredigeringId", BrevredigeringTable.id, onDelete = ReferenceOption.CASCADE).uniqueIndex()
    val type: Column<MottakerType> = varchar("type", 50).transform(MottakerType::valueOf, MottakerType::name)
    val tssId: Column<String?> = varchar("tssId", 50).nullable()
    val navn: Column<String?> = varchar("navn", 50).nullable()
    val postnummer: Column<String?> = varchar("postnummer", 50).nullable()
    val poststed: Column<String?> = text("poststed").nullable()
    val adresselinje1: Column<String?> = text("adresselinje1").nullable()
    val adresselinje2: Column<String?> = text("adresselinje2").nullable()
    val adresselinje3: Column<String?> = text("adresselinje3").nullable()
    val landkode: Column<String?> = varchar("landkode", 2).nullable()

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

enum class MottakerType { SAMHANDLER, NORSK_ADRESSE, UTENLANDSK_ADRESSE }

class Mottaker(brevredigeringId: EntityID<Long>) : LongEntity(brevredigeringId) {
    var type by MottakerTable.type
        private set
    var tssId by MottakerTable.tssId
        private set
    var navn by MottakerTable.navn
        private set
    var postnummer by MottakerTable.postnummer
        private set
    var poststed by MottakerTable.poststed
        private set
    var adresselinje1 by MottakerTable.adresselinje1
        private set
    var adresselinje2 by MottakerTable.adresselinje2
        private set
    var adresselinje3 by MottakerTable.adresselinje3
        private set
    var landkode by MottakerTable.landkode
        private set

    fun samhandler(tssId: String) {
        type = MottakerType.SAMHANDLER
        this.tssId = tssId
    }

    fun norskAdresse(navn: String, postnummer: String, poststed: String, adresselinje1: String?, adresselinje2: String?, adresselinje3: String?) {
        type = MottakerType.NORSK_ADRESSE
        this.navn = navn
        this.postnummer = postnummer
        this.poststed = poststed
        this.adresselinje1 = adresselinje1
        this.adresselinje2 = adresselinje2
        this.adresselinje3 = adresselinje3
    }

    fun utenlandskAdresse(
        navn: String,
        postnummer: String?,
        poststed: String?,
        adresselinje1: String,
        adresselinje2: String?,
        adresselinje3: String?,
        landkode: String
    ) {
        type = MottakerType.UTENLANDSK_ADRESSE
        this.navn = navn
        this.postnummer = postnummer
        this.poststed = poststed
        this.adresselinje1 = adresselinje1
        this.adresselinje2 = adresselinje2
        this.adresselinje3 = adresselinje3
        this.landkode = landkode
    }

    companion object : LongEntityClass<Mottaker>(MottakerTable)
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
            maximumPoolSize = 2
            validate()
        }),
    )
    transaction(database) {
        withDataBaseLock {
            SchemaUtils.createMissingTablesAndColumns(BrevredigeringTable, DocumentTable, Favourites, MottakerTable)
        }
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