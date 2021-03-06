package no.nav.pensjon.brev.template.dsl

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.base.PensjonLatex
import no.nav.pensjon.brev.template.dsl.expression.expr
import org.junit.jupiter.api.Test

class IncludeAttachmentTest {
    val testVedlegg = createAttachment<LangNynorsk, Unit>(
        title = newText(
            Language.Nynorsk to "Test vedlegg",
        ),
        includeSakspart = true
    ) {
        paragraph {
            text(Language.Nynorsk to "test")
        }
    }


    @Test
    fun `attachment is included in template`() {
        val expected = LetterTemplate(
            name = "test",
            title = listOf(newText(Language.Nynorsk to "tittel")),
            base = PensjonLatex,
            letterDataType = SomeDto::class,
            language = languages(Language.Nynorsk),
            letterMetadata = testLetterMetadata,
            outline = emptyList(),
            attachments = listOf(
                IncludeAttachment(Unit.expr(), testVedlegg, false.expr())
            )
        )

        val actual = createTemplate(
            name = "test",
            base = PensjonLatex,
            languages = languages(Language.Nynorsk),
            letterDataType = SomeDto::class,
            letterMetadata = testLetterMetadata,
        ) {
            title { text(Language.Nynorsk to "tittel") }
            outline {}
            includeAttachment(testVedlegg, Unit.expr(), false.expr())
        }

        assertThat(expected, equalTo(actual))
    }
}