package no.nav.pensjon.brev.brevoppskrift.model

import no.nav.pensjon.brev.brevoppskrift.auth.AuthorName
import java.time.Instant

/** Server-owned review metadata. Existing events and their ordering are immutable. */
object ReviewStamper {
    fun stamp(previous: Draft?, next: Draft, author: AuthorName, now: Instant): Draft {
        require(author.value.isNotBlank()) { "Forfatter mangler." }
        require(next.baseline == previous?.baseline) { "Grunnlinje kan bare endres gjennom godkjenning." }
        val old = previous?.comments.orEmpty()
        require(next.comments.size >= old.size) { "Kommentarhistorikk kan ikke slettes." }
        val eventIds = mutableSetOf<ReviewEventId>()
        fun event(id: ReviewEventId) {
            require(id.value.isNotBlank() && eventIds.add(id)) { "Gjennomgangshendelser må ha unike ID-er." }
        }
        val stamped = next.comments.mapIndexed { index, comment ->
            event(comment.id)
            comment.replies.forEach { event(it.id) }
            comment.states.forEach { event(it.id) }
            val existing = old.getOrNull(index)
            if (existing == null) {
                comment.copy(
                    author = author,
                    at = now,
                    replies = comment.replies.map { it.copy(author = author, at = now) },
                    states = comment.states.map { it.copy(author = author, at = now) },
                )
            } else {
                require(comment.copy(replies = existing.replies, states = existing.states) == existing) {
                    "Eksisterende kommentarer kan ikke endres eller flyttes."
                }
                require(comment.replies.take(existing.replies.size) == existing.replies) { "Svarhistorikk kan ikke endres." }
                require(comment.states.take(existing.states.size) == existing.states) { "Statushistorikk kan ikke endres." }
                comment.copy(
                    replies = existing.replies + comment.replies.drop(existing.replies.size).map { it.copy(author = author, at = now) },
                    states = existing.states + comment.states.drop(existing.states.size).map { it.copy(author = author, at = now) },
                )
            }
        }
        val attachments = next.attachments.map { attachment ->
            val oldAttachment = previous?.attachments?.find { it.id == attachment.id }
            attachment.copy(draft = stamp(oldAttachment?.draft, attachment.draft, author, now))
        }
        return next.copy(comments = stamped, attachments = attachments)
    }
}
