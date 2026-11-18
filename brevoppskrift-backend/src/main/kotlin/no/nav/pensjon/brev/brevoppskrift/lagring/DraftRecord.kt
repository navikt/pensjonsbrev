package no.nav.pensjon.brev.brevoppskrift.lagring

import no.nav.pensjon.brev.brevoppskrift.model.Draft
import no.nav.pensjon.brev.brevoppskrift.model.DraftMetadata
import no.nav.pensjon.brev.brevoppskrift.model.DraftId
import no.nav.pensjon.brev.brevoppskrift.model.DraftRevision
import no.nav.pensjon.brev.brevoppskrift.model.SourceId
import no.nav.pensjon.brev.brevoppskrift.auth.AuthorName
import java.time.Instant

enum class DraftStatus { WORK, READY }
enum class ArchiveReason { DONE, RESET, DISCARDED }

data class DraftRecord(
    val id: DraftId,
    val revision: DraftRevision,
    val status: DraftStatus,
    val sourceKey: SourceId?,
    val draft: Draft,
    val createdAt: Instant,
    val updatedAt: Instant,
    val updatedBy: AuthorName,
    val archivedAt: Instant? = null,
    val archiveReason: ArchiveReason? = null,
)

data class DraftSummary(
    val id: DraftId,
    val revision: DraftRevision,
    val status: DraftStatus,
    val sourceKey: SourceId?,
    val metadata: DraftMetadata,
    val updatedAt: Instant,
    val updatedBy: AuthorName,
)

data class CreateDraftRequest(val draft: Draft? = null, val sourceId: SourceId? = null)
data class UpdateDraftRequest(val revision: DraftRevision, val draft: Draft)
data class RevisionRequest(val revision: DraftRevision)
data class StatusRequest(val revision: DraftRevision, val status: DraftStatus)
data class ArchiveRequest(val revision: DraftRevision, val reason: ArchiveReason)
