package no.nav.pensjon.brev.brevoppskrift.katalog

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.brevoppskrift.TestPostgres
import no.nav.pensjon.brev.brevoppskrift.auth.AuthorName
import no.nav.pensjon.brev.brevoppskrift.draftObjectMapper
import no.nav.pensjon.brev.brevoppskrift.lagring.DraftRepository
import no.nav.pensjon.brev.brevoppskrift.lagring.SourceCatalogueRepository
import no.nav.pensjon.brev.brevoppskrift.lagring.Transactional
import no.nav.pensjon.brev.brevoppskrift.lagring.openDatabase
import no.nav.pensjon.brev.brevoppskrift.model.*
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.v1.jdbc.Database
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.junit.jupiter.api.parallel.Isolated
import java.io.ByteArrayInputStream
import java.security.MessageDigest

@Isolated
class SourceCatalogueHistoryTest {
    @Test
    fun `trusted imported drafts survive changed and removed catalogue entries after restart`(): Unit = runBlocking {
        openDatabase(postgres.database()).use { pool ->
            val transactions = Transactional(Database.connect(pool))
            val history = SourceCatalogueRepository(transactions)
            val old = requireNotNull(javaClass.getResourceAsStream(BuildSourceCatalogue.RESOURCE)).use {
                draftObjectMapper.readValue(it, CatalogueSnapshot::class.java).entries.first { entry -> entry.summary.editable }
            }
            history.retain(listOf(old))
            history.retain(listOf(old.copy(files = emptyMap())))
            assertThat(history.find(old.reference)).isEqualTo(old)

            val imported = requireNotNull(old.draft)
            val language = imported.title.keys.first()
            val marker = " [synthetic change]"
            val edited = imported.copy(title = imported.title + (language to (imported.title.getValue(language) + marker)))
            val drafts = DraftRepository(transactions)
            val saved = drafts.create(edited, old.reference.id, AuthorName("Synthetic author"))

            val upstream = old.files.getValue(old.templatePath) + "\n// Upstream change\n"
            val digest = Sha256Digest(MessageDigest.getInstance("SHA-256").digest(upstream.toByteArray()).joinToString("") { "%02x".format(it) })
            val newCommit = GitCommit("b".repeat(40))
            val reference = old.reference.copy(commit = newCommit, files = old.reference.files + (old.templatePath to digest))
            val updated = old.copy(
                summary = old.summary.copy(sourceCommit = newCommit),
                reference = reference,
                draft = imported.copy(source = reference),
                files = old.files + (old.templatePath to upstream),
            )
            history.retain(listOf(updated))

            for (entries in listOf(listOf(updated), emptyList())) {
                val restartedHistory = SourceCatalogueRepository(transactions)
                val catalogue = BuildSourceCatalogue.fromResource(
                    ByteArrayInputStream(draftObjectMapper.writeValueAsBytes(CatalogueSnapshot(newCommit, entries))), restartedHistory,
                )
                val restored = requireNotNull(drafts.get(saved.id)).draft
                assertThat(catalogue.checkSource(restored)).isEmpty()
                assertThat(catalogue.trustedEntry(requireNotNull(restored.source))).isEqualTo(old)

                val renamed = restored.copy(metadata = restored.metadata.copy(name = "ForgedName"))
                assertThat(catalogue.checkSource(renamed).map { it.code }).contains("LOCKED_SOURCE_CHANGED")
                val forged = restored.copy(source = old.reference.copy(files = old.reference.files + (old.templatePath to Sha256Digest("0".repeat(64)))))
                assertThat(catalogue.checkSource(forged)).isNotEmpty()
                val copy = AttachmentCopy(AttachmentReference("historical-copy"), DraftRevision(1), restored)
                assertThat(catalogue.checkSource(exampleDraft().copy(attachments = listOf(copy)))).isEmpty()
                val forgedCopy = exampleDraft().copy(attachments = listOf(copy.copy(draft = forged)))
                assertThat(catalogue.checkSource(forgedCopy)).anyMatch { it.path == "attachments[0].draft.source" }
            }
        }
    }

    companion object {
        @JvmField
        @RegisterExtension
        val postgres = TestPostgres()
    }
}
