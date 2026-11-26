package no.nav.pensjon.brev.skribenten.db

import com.fasterxml.jackson.databind.node.ObjectNode
import com.zaxxer.hikari.HikariDataSource
import io.ktor.client.engine.cio.CIO
import kotlinx.coroutines.runBlocking
import no.nav.brev.brevbaker.StableHashCode
import no.nav.pensjon.brev.skribenten.SharedPostgres
import no.nav.pensjon.brev.skribenten.NoAuthClientConfig
import no.nav.pensjon.brev.skribenten.Testbrevkoder
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.VersjonertBrev
import no.nav.pensjon.brev.skribenten.common.TekniskVersjon
import no.nav.pensjon.brev.skribenten.common.oneShotJobs
import no.nav.pensjon.brev.skribenten.common.updateBrevredigeringJson
import no.nav.pensjon.brev.skribenten.db.kryptering.EncryptedByteArray
import no.nav.pensjon.brev.skribenten.db.kryptering.KrypteringService
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.editedLetter
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brev.skribenten.model.SaksbehandlervalgMap
import no.nav.pensjon.brev.skribenten.services.EnhetId
import no.nav.pensjon.brev.skribenten.services.NaisLeaderService
import no.nav.pensjon.brev.skribenten.services.LeaderSettings
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.parallel.Isolated
import org.testcontainers.postgresql.PostgreSQLContainer
import java.time.Instant
import java.time.temporal.ChronoUnit

@Isolated
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OneShotJobMigrationTest {
    private lateinit var postgres: PostgreSQLContainer
    private lateinit var dataSource: HikariDataSource
    private val engine = CIO.create()
    private val config = object : LeaderSettings {
        override val leader: NoAuthClientConfig? = null
    }
    private val edited = editedLetter()
    private val rendered = edited.copy(title = Edit.Title(listOf(Edit.ParagraphContent.Text.Literal(null, "Rendret"))))
    private val staleHash = Hash.fromBytes<VersjonertBrev>(ByteArray(32))
    private val jobName = "migration-test"

    @BeforeAll
    fun initDb() {
        postgres = SharedPostgres.createStandaloneContainer().also { it.start() }
        dataSource = initDatabase(postgres.jdbcUrl, postgres.username, postgres.password)
        Database.connect(dataSource)
        KrypteringService.init("ZBn9yGLDluLZVVGXKZxvnPun3kPQ2ccF")
    }

    @BeforeEach
    fun clearDb() {
        transaction { exec("TRUNCATE brevredigering, oneshotjob RESTART IDENTITY CASCADE") }
    }

    @AfterAll
    fun closeDb() {
        engine.close()
        dataSource.close()
        postgres.stop()
    }

    @Test
    fun `migrerer eldre JSON og hash men ikke gjeldende eller nyere versjoner`() {
        val old = createLetter(TekniskVersjon(1))
        val withoutRendered = createLetter(TekniskVersjon(1), withRendered = false)
        val current = createLetter(TekniskVersjon(2))
        val newer = createLetter(TekniskVersjon(3))

        assertThat(runJob()).isEqualTo(TekniskVersjon(2))

        assertMigrated(old, TekniskVersjon(2), rendered)
        assertMigrated(withoutRendered, TekniskVersjon(2), null)
        assertUnchanged(current, TekniskVersjon(2))
        assertUnchanged(newer, TekniskVersjon(3))
        assertThat(jobCompleted()).isTrue()
    }

    @Test
    fun `reserverte brev migreres ved nytt forsøk med samme versjon`() {
        val old = createLetter(TekniskVersjon(1))
        val reserved = createLetter(TekniskVersjon(1), reservedAt = Instant.now())

        val version = requireNotNull(runJob())
        assertMigrated(old, version, rendered)
        assertUnchanged(reserved, TekniskVersjon(1))
        assertThat(jobCompleted()).isFalse()
        val ciphertext = rawLetter(old)[RawLetterTable.redigertBrev]

        transaction {
            BrevredigeringTable.update({ BrevredigeringTable.id eq reserved }) {
                it[sistReservert] = Instant.now().minus(16, ChronoUnit.MINUTES)
            }
        }
        assertThat(runJob()).isEqualTo(version)
        assertMigrated(reserved, version, rendered)
        assertThat(rawLetter(old)[RawLetterTable.redigertBrev]).isEqualTo(ciphertext)
        assertThat(jobCompleted()).isTrue()
        assertThat(runJob()).isNull()
    }

    private fun runJob(): TekniskVersjon? {
        var version: TekniskVersjon? = null
        runBlocking {
            oneShotJobs(NaisLeaderService(config, engine)) {
                job(jobName) {
                    version = it
                    updateBrevredigeringJson(it)
                }
            }
        }
        return version
    }

    private fun jobCompleted() = transaction {
        OneShotJobTable.selectAll().where { OneShotJobTable.id eq jobName }.single()[OneShotJobTable.completedAt] != null
    }

    private fun createLetter(version: TekniskVersjon, withRendered: Boolean = true, reservedAt: Instant? = null): BrevId = transaction {
        val id = BrevredigeringEntity.opprettBrev(
            saksId = SaksId(123),
            vedtaksId = null,
            opprettetAv = NavIdent("test"),
            brevkode = Testbrevkoder.TESTBREV,
            spraak = LanguageCode.BOKMAL,
            avsenderEnhetId = EnhetId("1111"),
            saksbehandlerValg = SaksbehandlervalgMap(),
            redigertBrev = VersjonertBrev.V1(edited),
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
            brevmalHash = StableHashCode(1),
        ).id.value
        RawLetterTable.update({ RawLetterTable.id eq id.id }) {
            it[redigertBrev] = legacyJson(edited)
            it[rendretBrev] = if (withRendered) legacyJson(rendered) else null
        }
        BrevredigeringTable.update({ BrevredigeringTable.id eq id }) {
            it[redigertBrevKryptertHash] = staleHash
            it[tekniskVersjon] = version
            it[sistReservert] = reservedAt
        }
        id
    }

    private fun assertMigrated(id: BrevId, version: TekniskVersjon, expectedRendered: Edit.Letter?) {
        transaction {
            val row = BrevredigeringTable.selectAll().where { BrevredigeringTable.id eq id }.single()
            assertThat(row[BrevredigeringTable.tekniskVersjon]).isEqualTo(version)
            assertThat(row[BrevredigeringTable.redigertBrevKryptert]).isEqualTo(VersjonertBrev.V1(edited))
            assertThat(row[BrevredigeringTable.rendretBrevKryptert]).isEqualTo(expectedRendered?.let(VersjonertBrev::V1))
            assertThat(row[BrevredigeringTable.redigertBrevKryptertHash]).isEqualTo(Hash.read(VersjonertBrev.V1(edited)))
            val raw = rawLetter(id)
            assertThat(decryptJson(raw[RawLetterTable.redigertBrev])).isEqualTo(databaseObjectMapper.valueToTree(edited))
            val renderedBytes = raw[RawLetterTable.rendretBrev]
            if (expectedRendered == null) {
                assertThat(renderedBytes).isNull()
            } else {
                assertThat(decryptJson(renderedBytes!!)).isEqualTo(databaseObjectMapper.valueToTree(expectedRendered))
            }
        }
    }

    private fun assertUnchanged(id: BrevId, version: TekniskVersjon) {
        transaction {
            val row = BrevredigeringTable.selectAll().where { BrevredigeringTable.id eq id }.single()
            assertThat(row[BrevredigeringTable.tekniskVersjon]).isEqualTo(version)
            assertThat(row[BrevredigeringTable.redigertBrevKryptertHash]).isEqualTo(staleHash)
            val raw = rawLetter(id)
            assertThat(decryptJson(raw[RawLetterTable.redigertBrev])).isEqualTo(legacyTree(edited))
            assertThat(decryptJson(raw[RawLetterTable.rendretBrev]!!)).isEqualTo(legacyTree(rendered))
        }
    }

    private fun rawLetter(id: BrevId) = transaction {
        RawLetterTable.selectAll().where { RawLetterTable.id eq id.id }.single()
    }

    private fun legacyJson(letter: Edit.Letter): ByteArray =
        KrypteringService.krypter(databaseObjectMapper.writeValueAsBytes(legacyTree(letter))).bytes

    private fun legacyTree(letter: Edit.Letter): ObjectNode =
        databaseObjectMapper.valueToTree<ObjectNode>(letter).put("legacyField", "obsolete")

    private fun decryptJson(bytes: ByteArray) =
        databaseObjectMapper.readTree(KrypteringService.dekrypter(EncryptedByteArray(bytes)))
}

private object RawLetterTable : Table("brevredigering") {
    val id = long("id")
    val redigertBrev = binary("redigertBrevKryptert")
    val rendretBrev = binary("rendretBrevKryptert").nullable()
}
