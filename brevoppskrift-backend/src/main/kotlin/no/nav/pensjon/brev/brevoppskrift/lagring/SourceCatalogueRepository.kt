package no.nav.pensjon.brev.brevoppskrift.lagring

import no.nav.pensjon.brev.brevoppskrift.katalog.CatalogueEntry
import no.nav.pensjon.brev.brevoppskrift.model.SourceReference
import org.jetbrains.exposed.v1.core.*
import org.jetbrains.exposed.v1.jdbc.insertIgnore
import org.jetbrains.exposed.v1.jdbc.selectAll

class SourceCatalogueRepository(private val transactions: Transactional) {
    suspend fun retain(entries: List<CatalogueEntry>) = transactions.transaction {
        entries.filter { it.summary.editable }.forEach { entry ->
            SourceCatalogueTable.insertIgnore {
                it[sourceId] = entry.reference.id
                it[commit] = entry.reference.commit
                it[document] = entry
            }
        }
    }

    suspend fun find(reference: SourceReference): CatalogueEntry? = transactions.transaction {
        SourceCatalogueTable.selectAll()
            .where { (SourceCatalogueTable.sourceId eq reference.id) and (SourceCatalogueTable.commit eq reference.commit) }
            .singleOrNull()?.get(SourceCatalogueTable.document)?.takeIf { it.reference == reference }
    }
}
