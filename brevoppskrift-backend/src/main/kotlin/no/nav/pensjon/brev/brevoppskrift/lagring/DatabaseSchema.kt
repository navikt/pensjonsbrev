package no.nav.pensjon.brev.brevoppskrift.lagring

import no.nav.pensjon.brev.brevoppskrift.draftObjectMapper
import no.nav.pensjon.brev.brevoppskrift.model.Draft
import no.nav.pensjon.brev.brevoppskrift.model.DraftMetadata
import no.nav.pensjon.brev.brevoppskrift.model.DraftId
import no.nav.pensjon.brev.brevoppskrift.model.DraftRevision
import no.nav.pensjon.brev.brevoppskrift.model.SourceId
import no.nav.pensjon.brev.brevoppskrift.auth.AuthorName
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.java.javaUUID
import org.jetbrains.exposed.v1.javatime.timestamp
import org.jetbrains.exposed.v1.json.jsonb

object DraftTable : Table("draft") {
    val id = javaUUID("id").transform(::DraftId, DraftId::value)
    val revision = long("revision").transform(::DraftRevision, DraftRevision::value)
    val status = enumerationByName<DraftStatus>("status", 16)
    val sourceKey = text("source_key").transform(::SourceId, SourceId::value).nullable()
    val document = jsonb<Draft>("document", serialize = draftObjectMapper::writeValueAsString, deserialize = { draftObjectMapper.readValue(it, Draft::class.java) })
    val metadata = jsonb<DraftMetadata>("metadata", serialize = draftObjectMapper::writeValueAsString, deserialize = { draftObjectMapper.readValue(it, DraftMetadata::class.java) })
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")
    val createdBy = text("created_by").transform(::AuthorName, AuthorName::value)
    val updatedBy = text("updated_by").transform(::AuthorName, AuthorName::value)
    val archivedAt = timestamp("archived_at").nullable()
    val archiveReason = enumerationByName<ArchiveReason>("archive_reason", 16).nullable()
    override val primaryKey = PrimaryKey(id)
}
