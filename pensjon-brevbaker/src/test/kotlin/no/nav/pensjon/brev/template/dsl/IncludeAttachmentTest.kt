package no.nav.pensjon.brev.template.dsl

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.expression.expr
import org.junit.jupiter.api.Test

class IncludeAttachmentTest {
    private val testVedlegg = createAttachment<LangNynorsk, Unit>(
        title = newText(
            Nynorsk to "Test vedlegg",
        ),
        includeSakspart = true
    ) {
        paragraph {
            text(Nynorsk to "test")
        }
    }

    @Test
    fun `attachment is included in template`() {
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

        assertThat(expected, equalTo(actual))
    }
}