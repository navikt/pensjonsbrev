package no.nav.pensjon.brev.template.dsl

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.NullDataSelectors.test
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import org.junit.jupiter.api.Test

class IncludeAttachmentTest {
    @Test
    fun `attachment is included in template`() {
        val testVedlegg = createAttachment<LangNynorsk, Unit>(
            title = newText(
                Nynorsk to "Test vedlegg",
            ),
            includeSakspart = true
        ) {
            paragraph {
                text(Nynorsk to "test")
            }
        }

        val expected = LetterTemplate(
            name = "test",
            title = listOf(newText(Nynorsk to "tittel")),
            letterDataType = SomeDto::class,
            language = languages(Nynorsk),
            outline = emptyList(),
            attachments = listOf(
                IncludeAttachment(Unit.expr(), testVedlegg, false.expr())
            ),
            letterMetadata = testLetterMetadata
        )

        val actual = createTemplate(
            name = "test",
            letterDataType = SomeDto::class,
            languages = languages(Nynorsk),
            letterMetadata = testLetterMetadata,
        ) {
            title { text(Nynorsk to "tittel") }
            outline {}
            includeAttachment(testVedlegg, Unit.expr(), false.expr())
        }

        assertThat(actual, equalTo(expected))
    }

    data class NullData(val test: String?)

    @TemplateModelHelpers
    object Helpers : HasModel<NullData>

    @Test
    fun `attachment is not included when using includeAttachmentIfNotNull and attachmentData is null`() {
        val selector: Expression<String?> = Expression.FromScope(ExpressionScope<NullData, *>::argument).test
        val testVedlegg = createAttachment<LangNynorsk, String>(
            title = newText(
                Nynorsk to "Test vedlegg",
            ),
            includeSakspart = true
        ) {
            paragraph {
                text(Nynorsk to "test")
            }
        }

        @Suppress("UNCHECKED_CAST")
        val expected = LetterTemplate(
            name = "test",
            title = listOf(newText(Nynorsk to "tittel")),
            letterDataType = NullData::class,
            language = languages(Nynorsk),
            outline = emptyList(),
            attachments = listOf(
                IncludeAttachment(selector as Expression<String>, testVedlegg, selector.notNull())
            ), letterMetadata = testLetterMetadata
        )
        val actual = createTemplate(
            name = "test",
            letterDataType = NullData::class,
            languages = languages(Nynorsk),
            letterMetadata = testLetterMetadata,
        ) {
            title { text(Nynorsk to "tittel") }
            outline {}
            includeAttachmentIfNotNull(testVedlegg, test)
        }

        assertThat(actual, equalTo(expected))
    }
}