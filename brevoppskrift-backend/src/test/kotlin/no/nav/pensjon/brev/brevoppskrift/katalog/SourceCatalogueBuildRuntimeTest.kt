package no.nav.pensjon.brev.brevoppskrift.katalog

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.brevoppskrift.draftObjectMapper
import no.nav.pensjon.brev.brevoppskrift.model.TargetModule
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.File

class SourceCatalogueBuildRuntimeTest {
    @Test
    fun `Git executable must be an absolute executable file`() {
        val root = File(requireNotNull(System.getProperty("authoring.repository")))
        assertThatThrownBy { readCommittedSources(root, File("git")) }
            .isInstanceOf(IllegalArgumentException::class.java)
        assertThatThrownBy { readCommittedSources(root, root) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `production builder imports actual commit and packages visible templates`(): Unit = runBlocking {
        val root = File(requireNotNull(System.getProperty("authoring.repository")))
        // Production Git reading and compiler initialization, not a handwritten test-only catalogue.
        val input = readCommittedSources(root)
        val snapshot = KotlinSourceImporter().use { it.build(input.commit, input.sources) }
        val catalogue = BuildSourceCatalogue.fromResource(ByteArrayInputStream(draftObjectMapper.writeValueAsBytes(snapshot)))
        assertThat(snapshot.entries).isNotEmpty()
        assertThat(snapshot.entries.map { it.summary.targetModule }.toSet()).isEqualTo(TargetModule.entries.map { it.name }.toSet())
        assertThat(snapshot.commit.value).matches("[a-f0-9]{40,64}")
        assertThat(snapshot.entries.flatMap { it.summary.diagnostics }.filter { it.code == "UNRESOLVED_TEMPLATE" })
            .describedAs("Every registered template must resolve to a source symbol at the same commit").isEmpty()
        assertThat(snapshot.entries.filter { it.summary.editable }).describedAs("Actual committed editable-import corpus").isNotEmpty()
        for (entry in snapshot.entries) {
            if (!entry.summary.editable) {
                assertThat(entry.summary.diagnostics).isNotEmpty()
                assertThat(catalogue.import(entry.summary.id)).isNull()
                continue
            }
            val draft = requireNotNull(catalogue.import(entry.summary.id))
            assertThat(catalogue.checkSource(draft)).describedAs(entry.summary.id.value).isEmpty()
            val original = entry.files.getValue(entry.templatePath)
            assertThat(original).isEqualTo(input.sources.getValue(entry.templatePath))
            assertThat(draft).isEqualTo(entry.draft)
        }
        val summary = catalogueSummary(snapshot)
        assertThat(summary["visibleTemplates"]).isEqualTo(catalogue.list().size)
        println("AUTHORING_CATALOGUE_SUPPORT ${draftObjectMapper.writeValueAsString(summary)}")
    }
}
