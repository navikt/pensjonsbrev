package no.nav.pensjon.brev.template.render

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import no.nav.brev.brevbaker.createTemplate
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.IncludeAttachment
import no.nav.pensjon.brev.template.LangNynorsk
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.render.IncludeAttachmentTestSelectors.NullDataSelectors.test
import no.nav.pensjon.brev.template.render.dsl.SomeDto
import no.nav.pensjon.brev.template.render.dsl.testLetterMetadata
import org.junit.jupiter.api.Nested
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
                text(nynorsk { +"test" })
            }
        }

        val expected = LetterTemplate(
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
            letterDataType = SomeDto::class,
            languages = languages(Nynorsk),
            letterMetadata = testLetterMetadata,
        ) {
            title { text(nynorsk { +"tittel" }) }
            outline {}
            includeAttachment(testVedlegg, Unit.expr(), false.expr())
        }

        assertThat(actual, equalTo(expected))
    }

    data class NullData(val test: String?)

    @Nested
    inner class IncludeIfNotNull{
        private val testVedlegg = createAttachment<LangNynorsk, String>(
            title = newText(
                Nynorsk to "Test vedlegg",
            ),
            includeSakspart = true
        ) {
            paragraph {
                text(nynorsk { +"test" })
            }
        }

        private val testTemplate = createTemplate(
            letterDataType = NullData::class,
            languages = languages(Nynorsk),
            letterMetadata = testLetterMetadata,
        ) {
            title { text(nynorsk { +"tittel" }) }
            outline {}
            includeAttachmentIfNotNull(testVedlegg, test)
        }

        @Test
        fun `attachment is included with notnull condition`() {
            val selector: Expression<String?> = Expression.FromScope.Argument<NullData>().test


            @Suppress("UNCHECKED_CAST")
            val expected = LetterTemplate(
                title = listOf(newText(Nynorsk to "tittel")),
                letterDataType = NullData::class,
                language = languages(Nynorsk),
                outline = emptyList(),
                attachments = listOf(
                    IncludeAttachment(selector as Expression<String>, testVedlegg, selector.notNull() and (true.expr().ifNull(true)))
                ), letterMetadata = testLetterMetadata
            )
            assertThat(testTemplate, equalTo(expected))
        }
    }


}