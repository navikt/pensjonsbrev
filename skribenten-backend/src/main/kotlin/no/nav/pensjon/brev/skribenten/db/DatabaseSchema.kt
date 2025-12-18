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
import no.nav.brev.Landkode
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevkode
import no.nav.pensjon.brev.skribenten.db.kryptering.EncryptedByteArray
import no.nav.pensjon.brev.skribenten.db.kryptering.KrypteringService
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.Distribusjonstype
import no.nav.pensjon.brev.skribenten.model.Dto.Mottaker.ManueltAdressertTil
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brev.skribenten.model.NorskPostnummer
import no.nav.pensjon.brev.skribenten.model.SaksbehandlerValg
import no.nav.pensjon.brev.skribenten.serialize.BrevkodeJacksonModule
import no.nav.pensjon.brev.skribenten.serialize.EditLetterJacksonModule
import no.nav.pensjon.brev.skribenten.serialize.LetterMarkupJacksonModule
import no.nav.pensjon.brev.skribenten.services.BrevdataResponse
import no.nav.pensjon.brevbaker.api.model.AlltidValgbartVedleggKode
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.json.json
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
    registerModule(EditLetterJacksonModule)
    registerModule(LetterMarkupJacksonModule)
    registerModule(BrevkodeJacksonModule)
    disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
}

class DatabaseJsonDeserializeException(cause: JacksonException): Exception("Failed to deserialize json-column from database", cause)

private inline fun <reified T> readJsonString(json: String): T =
    try {
        databaseObjectMapper.readValue<T>(json)
    } catch (e: JacksonException) {
        throw DatabaseJsonDeserializeException(e)
    }

fun Table.encryptedBinary(name: String): Column<EncryptedByteArray> =
    binary(name).transform(columnTransformer(unwrap = EncryptedByteArray::bytes, wrap = ::EncryptedByteArray))

private inline fun <reified T> readJsonBinary(json: ByteArray): T =
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
    val saksbehandlerValg = json<SaksbehandlerValg>("saksbehandlerValg", databaseObjectMapper::writeValueAsString, ::readJsonString)
    val redigertBrevKryptert: Column<Edit.Letter> = encryptedBinary("redigertBrevKryptert")
        .transform(KrypteringService::dekrypter, KrypteringService::krypter)
        .transform(::readJsonBinary, databaseObjectMapper::writeValueAsBytes)
    val redigertBrevKryptertHash: Column<Hash<Edit.Letter>> = hashColumn("redigertBrevKryptertHash")
    val laastForRedigering: Column<Boolean> = bool("laastForRedigering")
    val distribusjonstype: Column<Distribusjonstype> = varchar("distribusjonstype", length = 50).transform(Distribusjonstype::valueOf, Distribusjonstype::name)
    val redigeresAvNavIdent: Column<String?> = varchar("redigeresAvNavIdent", length = 50).nullable()
    val sistRedigertAvNavIdent: Column<String> = varchar("sistRedigertAvNavIdent", length = 50)
    val opprettetAvNavIdent: Column<String> = varchar("opprettetAvNavIdent", length = 50).index()
    val opprettet: Column<Instant> = timestamp("opprettet")
    val sistredigert: Column<Instant> = timestamp("sistredigert")
    val sistReservert: Column<Instant?> = timestamp("sistReservert").nullable()
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
    var redigertBrev by BrevredigeringTable.redigertBrevKryptert.writeHashTo(BrevredigeringTable.redigertBrevKryptertHash)
    val redigertBrevHash by BrevredigeringTable.redigertBrevKryptertHash
    var laastForRedigering by BrevredigeringTable.laastForRedigering
    var distribusjonstype by BrevredigeringTable.distribusjonstype
    var redigeresAvNavIdent by BrevredigeringTable.redigeresAvNavIdent.wrap(::NavIdent, NavIdent::id)
    var sistRedigertAvNavIdent by BrevredigeringTable.sistRedigertAvNavIdent.wrap(::NavIdent, NavIdent::id)
    var opprettetAvNavIdent by BrevredigeringTable.opprettetAvNavIdent.wrap(::NavIdent, NavIdent::id)
    var opprettet by BrevredigeringTable.opprettet
    var sistredigert by BrevredigeringTable.sistredigert
    var sistReservert by BrevredigeringTable.sistReservert
    var journalpostId by BrevredigeringTable.journalpostId
    val document by Document referrersOn DocumentTable.brevredigering orderBy (DocumentTable.id to SortOrder.DESC)
    val mottaker by Mottaker optionalBackReferencedOn MottakerTable.id
    val p1Data by P1Data optionalBackReferencedOn P1DataTable.id
    val valgteVedlegg by ValgteVedlegg optionalBackReferencedOn ValgteVedleggTable.id
    var attestertAvNavIdent by BrevredigeringTable.attestertAvNavIdent.wrap(::NavIdent, NavIdent::id)

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
    val pdfKryptert: Column<ByteArray> = encryptedBinary("pdfKryptert")
        .transform(KrypteringService::dekrypter, KrypteringService::krypter)
    val redigertBrevHash: Column<Hash<Edit.Letter>> = hashColumn("redigertBrevHash")
    val brevdataHash: Column<Hash<BrevdataResponse.Data>> = hashColumn("brevdataHash")
}

class Document(id: EntityID<Long>) : LongEntity(id) {
    var brevredigering by Brevredigering referencedOn DocumentTable.brevredigering
    var dokumentDato by DocumentTable.dokumentDato
    var pdf by DocumentTable.pdfKryptert
    var redigertBrevHash by DocumentTable.redigertBrevHash
    var brevdataHash by DocumentTable.brevdataHash

    companion object : LongEntityClass<Document>(DocumentTable)
}

object MottakerTable : IdTable<Long>() {
    override val id: Column<EntityID<Long>> = reference("brevredigeringId", BrevredigeringTable.id, onDelete = ReferenceOption.CASCADE).uniqueIndex()
    val type: Column<MottakerType> = varchar("type", 50).transform(MottakerType::valueOf, MottakerType::name)
    val tssId: Column<String?> = varchar("tssId", 50).nullable()
    val navn: Column<String?> = varchar("navn", 128).nullable()
    val postnummer: Column<String?> = varchar("postnummer", 4).nullable()
    val poststed: Column<String?> = varchar("poststed", 50).nullable()
    val adresselinje1: Column<String?> = varchar("adresselinje1", 128).nullable()
    val adresselinje2: Column<String?> = varchar("adresselinje2", 128).nullable()
    val adresselinje3: Column<String?> = varchar("adresselinje3", 128).nullable()
    val landkode: Column<String?> = varchar("landkode", 2).nullable()
    val manueltAdressertTil: Column<ManueltAdressertTil> = varchar("manueltAdressertTil", 50)
        .transform(ManueltAdressertTil::valueOf, ManueltAdressertTil::name)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

enum class MottakerType { SAMHANDLER, NORSK_ADRESSE, UTENLANDSK_ADRESSE }

class Mottaker(brevredigeringId: EntityID<Long>) : LongEntity(brevredigeringId) {
    var type by MottakerTable.type
    var tssId by MottakerTable.tssId
    var navn by MottakerTable.navn
    var postnummer by MottakerTable.postnummer.wrap(::NorskPostnummer, NorskPostnummer::value)
    var poststed by MottakerTable.poststed
    var adresselinje1 by MottakerTable.adresselinje1
    var adresselinje2 by MottakerTable.adresselinje2
    var adresselinje3 by MottakerTable.adresselinje3
    var manueltAdressertTil by MottakerTable.manueltAdressertTil
    var landkode by MottakerTable.landkode.wrap(::Landkode, Landkode::landkode)

    companion object : LongEntityClass<Mottaker>(MottakerTable)
}

object P1DataTable : IdTable<Long>() {
    override val id: Column<EntityID<Long>> = reference("brevredigeringId", BrevredigeringTable.id, onDelete = ReferenceOption.CASCADE).uniqueIndex()
    val p1data: Column<Api.GeneriskBrevdata> = encryptedBinary("p1data")
        .transform(KrypteringService::dekrypter, KrypteringService::krypter)
        .transform(::readJsonBinary, databaseObjectMapper::writeValueAsBytes)


    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

class P1Data(brevredigeringId: EntityID<Long>) : LongEntity(brevredigeringId) {
    var p1data by P1DataTable.p1data
    companion object : LongEntityClass<P1Data>(P1DataTable)
}

object OneShotJobTable : IdTable<String>() {
    override val id: Column<EntityID<String>> = varchar("name", 255).entityId()
    val completedAt: Column<Instant> = timestamp("completedAt")
    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

object ValgteVedleggTable : IdTable<Long>() {
    override val id: Column<EntityID<Long>> = reference("brevredigeringId", BrevredigeringTable.id, onDelete = ReferenceOption.CASCADE).uniqueIndex()
    val valgteVedlegg = json<List<AlltidValgbartVedleggKode>>("valgtevedlegg", databaseObjectMapper::writeValueAsString, ::readJsonString)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

class ValgteVedlegg(brevredigeringId: EntityID<Long>) : LongEntity(brevredigeringId) {
    var valgteVedlegg by ValgteVedleggTable.valgteVedlegg
    companion object : LongEntityClass<ValgteVedlegg>(ValgteVedleggTable)
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