package no.nav.pensjon.brev.brevoppskrift.model

import no.nav.pensjon.brev.brevoppskrift.draftObjectMapper
import no.nav.pensjon.brev.brevoppskrift.auth.AuthorName
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.Test
import java.time.Instant

class ReviewStamperTest {
    private val now = Instant.parse("2026-11-11T12:00:00Z")
    private val comment = DraftComment(ReviewEventId("comment"), CommentAnchor("BLOCK", blockId = BlockId("paragraph")), "Kontroller teksten.")

    @Test
    fun `new review events use authenticated author and server clock`() {
        val next = exampleDraft().copy(comments = listOf(comment.copy(
            author = AuthorName("forged"),
            at = Instant.EPOCH,
            replies = listOf(CommentReply(ReviewEventId("reply"), "Svar", AuthorName("forged"), Instant.EPOCH)),
            states = listOf(CommentState(ReviewEventId("state"), true, AuthorName("forged"), Instant.EPOCH)),
        )))
        val stamped = ReviewStamper.stamp(null, next, AuthorName("authenticated"), now)
        assertThat(stamped.comments.single().author).isEqualTo(AuthorName("authenticated"))
        assertThat(stamped.comments.single().at).isEqualTo(now)
        assertThat(stamped.comments.single().replies.single().author).isEqualTo(AuthorName("authenticated"))
        assertThat(stamped.comments.single().states.single().at).isEqualTo(now)
    }

    @Test
    fun `existing events cannot be edited deleted reordered or impersonated`() {
        val previous = ReviewStamper.stamp(null, exampleDraft().copy(comments = listOf(comment)), AuthorName("first"), now)
        val existing = previous.comments.single()
        listOf(
            previous.copy(comments = emptyList()),
            previous.copy(comments = listOf(existing.copy(text = "Endret"))),
            previous.copy(comments = listOf(existing.copy(author = AuthorName("another")))),
            previous.copy(comments = listOf(existing.copy(anchor = CommentAnchor("DRAFT")))),
            previous.copy(comments = listOf(comment.copy(id = ReviewEventId("new")), existing)),
        ).forEach { tampered ->
            assertThatIllegalArgumentException().isThrownBy { ReviewStamper.stamp(previous, tampered, AuthorName("second"), now.plusSeconds(1)) }
        }
    }

    @Test
    fun `replies and resolved states are append only`() {
        val previous = ReviewStamper.stamp(null, exampleDraft().copy(comments = listOf(comment.copy(
            replies = listOf(CommentReply(ReviewEventId("reply"), "Svar")),
            states = listOf(CommentState(ReviewEventId("state"), true)),
        ))), AuthorName("first"), now)
        val old = previous.comments.single()
        val next = previous.copy(comments = listOf(old.copy(
            replies = old.replies + CommentReply(ReviewEventId("second-reply"), "Nytt svar"),
            states = old.states + CommentState(ReviewEventId("reopen"), false),
        )))
        val stamped = ReviewStamper.stamp(previous, next, AuthorName("second"), now.plusSeconds(1))
        assertThat(stamped.comments.single().replies.first()).isEqualTo(old.replies.first())
        assertThat(stamped.comments.single().states.last().author).isEqualTo(AuthorName("second"))
        assertThatIllegalArgumentException().isThrownBy {
            ReviewStamper.stamp(previous, previous.copy(comments = listOf(old.copy(states = emptyList()))), AuthorName("second"), now)
        }
        assertThatIllegalArgumentException().isThrownBy {
            val changedReply = old.replies.single().copy(text = "Endret")
            val tampered = previous.copy(comments = listOf(old.copy(replies = listOf(changedReply))))
            ReviewStamper.stamp(previous, tampered, AuthorName("second"), now)
        }
    }

    @Test
    fun `ordinary PUT cannot create remove or tamper with baseline`() {
        val baseline = DraftBaseline(exampleDraft().forBaseline(), "APPROVED", now, AuthorName("first"))
        val previous = exampleDraft().copy(baseline = baseline)
        assertThat(ReviewStamper.stamp(previous, previous, AuthorName("second"), now)).isEqualTo(previous)
        assertThatIllegalArgumentException().isThrownBy { ReviewStamper.stamp(previous, previous.copy(baseline = null), AuthorName("second"), now) }
        assertThatIllegalArgumentException().isThrownBy { ReviewStamper.stamp(null, previous, AuthorName("second"), now) }
    }

    @Test
    fun `expanded contract round trips JSON and baseline strips review content`() {
        val draft = exampleDraft().copy(
            source = SourceReference(SourceId("source"), GitCommit("a".repeat(40)), mapOf(RepositoryPath("ufoere/maler/X.kt") to Sha256Digest("b".repeat(64)))),
            comments = listOf(comment),
            scenarios = listOf(TestScenario(ScenarioId("scenario"), "Syntetisk", mapOf(FieldId("amount") to draftObjectMapper.readTree("12")))),
        )
        val document = draft.copy(baseline = DraftBaseline(draft.forBaseline(), "IMPORTED"))
        assertThat(draftObjectMapper.readValue(draftObjectMapper.writeValueAsBytes(document), Draft::class.java)).isEqualTo(document)
        assertThat(document.baseline?.draft?.comments).isEmpty()
        assertThat(DraftValidator.validate(document)).isEmpty()
    }
}
