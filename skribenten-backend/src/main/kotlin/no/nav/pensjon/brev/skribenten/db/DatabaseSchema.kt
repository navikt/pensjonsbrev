package no.nav.pensjon.brev.skribenten.db

import com.fasterxml.jackson.core.JacksonException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.typesafe.config.Config
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import no.nav.brev.Landkode
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevkode
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.model.Distribusjonstype
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brev.skribenten.model.SaksbehandlerValg
import no.nav.pensjon.brev.skribenten.services.LetterMarkupModule
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.json.json
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import java.time.Instant
import java.time.LocalDate
import javax.sql.DataSource

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
    registerModule(LetterMarkupModule)
    disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
}

class DatabaseJsonDeserializeException(cause: JacksonException): Exception("Failed to deserialize json-column from database", cause)

private inline fun <reified T> readJsonColumn(json: String): T =
    try {
        databaseObjectMapper.readValue<T>(json)
    } catch (e: JacksonException) {
        throw DatabaseJsonDeserializeException(e)
    }

object BrevredigeringTable : LongIdTable() {
    val saksId: Column<Long> = long("saksId").index()
    val vedtaksId: Column<Long?> = long("vedtaksId").nullable()
    val brevkode: Column<Brevkode.Redigerbart> = varchar("brevkode", length = 50).transform({ RedigerbarBrevkode(it) }, Brevkode.Redigerbart::kode)
    val spraak: Column<LanguageCode> = varchar("spraak", length = 50).transform(LanguageCode::valueOf, LanguageCode::name)
    val avsenderEnhetId: Column<String?> = varchar("avsenderEnhetId", 50).nullable()
    val saksbehandlerValg = json<SaksbehandlerValg>("saksbehandlerValg", databaseObjectMapper::writeValueAsString, ::readJsonColumn)
    val redigertBrev = json<Edit.Letter>("redigertBrev", databaseObjectMapper::writeValueAsString, ::readJsonColumn)
    val redigertBrevHash: Column<ByteArray> = hashColumn("redigertBrevHash")
    val laastForRedigering: Column<Boolean> = bool("laastForRedigering")
    val distribusjonstype: Column<Distribusjonstype> = varchar("distribusjonstype", length = 50).transform(Distribusjonstype::valueOf, Distribusjonstype::name)
    val redigeresAvNavIdent: Column<String?> = varchar("redigeresAvNavIdent", length = 50).nullable()
    val sistRedigertAvNavIdent: Column<String> = varchar("sistRedigertAvNavIdent", length = 50)
    val opprettetAvNavIdent: Column<String> = varchar("opprettetAvNavIdent", length = 50).index()
    val opprettet: Column<Instant> = timestamp("opprettet")
    val sistredigert: Column<Instant> = timestamp("sistredigert")
    val sistReservert: Column<Instant?> = timestamp("sistReservert").nullable()
    // TODO: fjern signatur kolonner (samt fra api+frontend), de skal ligge i redigertBrev fra nå av.
    val signaturSignerende: Column<String> = varchar("signaturSignerende", length = 50)
    val signaturAttestant: Column<String?> = varchar("signaturAttestant", length = 50).nullable()
    val journalpostId: Column<Long?> = long("journalpostId").nullable()
    val attestertAvNavIdent: Column<String?> = varchar("attestertAvNavIdent", length = 50).nullable()
}

class Brevredigering(id: EntityID<Long>) : LongEntity(id) {
    var saksId by BrevredigeringTable.saksId
    // Det er forventet at vedtaksId kun har verdi om brevet er Vedtaksbrev
    var vedtaksId by BrevredigeringTable.vedtaksId
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
    var journalpostId by BrevredigeringTable.journalpostId
    val document by Document referrersOn DocumentTable.brevredigering orderBy (DocumentTable.id to SortOrder.DESC)
    val mottaker by Mottaker optionalBackReferencedOn MottakerTable.id
    var attestertAvNavIdent by BrevredigeringTable.attestertAvNavIdent.wrap(::NavIdent, NavIdent::id)
    var signaturAttestant by BrevredigeringTable.signaturAttestant

    companion object : LongEntityClass<Brevredigering>(BrevredigeringTable) {
        fun findByIdAndSaksId(id: Long, saksId: Long?) =
            if (saksId == null) {
                findById(id)
            } else {
                find { (BrevredigeringTable.id eq id) and (BrevredigeringTable.saksId eq saksId) }.firstOrNull()
            }
    }

    val isVedtaksbrev get() = vedtaksId != null
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
    var tssId by MottakerTable.tssId
    var navn by MottakerTable.navn
    var postnummer by MottakerTable.postnummer
    var poststed by MottakerTable.poststed
    var adresselinje1 by MottakerTable.adresselinje1
    var adresselinje2 by MottakerTable.adresselinje2
    var adresselinje3 by MottakerTable.adresselinje3
    var landkode by MottakerTable.landkode.wrap(::Landkode, Landkode::landkode)

    companion object : LongEntityClass<Mottaker>(MottakerTable)
}

fun initDatabase(config: Config) =
    config.getConfig("database").let {
        initDatabase(createJdbcUrl(it), it.getString("username"), it.getString("password"))
    }

fun initDatabase(jdbcUrl: String, username: String, password: String) =
    HikariDataSource(HikariConfig().apply {
        this.jdbcUrl = jdbcUrl
        this.username = username
        this.password = password
        this.initializationFailTimeout = 6000
        maximumPoolSize = 2
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

private object BrevbakerBrevdataModule : SimpleModule() {
    private fun readResolve(): Any = BrevbakerBrevdataModule

    init {
        addDeserializer(BrevbakerBrevdata::class.java, BrevdataDeserializer)
    }

    private object BrevdataDeserializer : JsonDeserializer<BrevbakerBrevdata>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): BrevbakerBrevdata = ctxt.readValue(parser, SaksbehandlerValg::class.java)
    }
}