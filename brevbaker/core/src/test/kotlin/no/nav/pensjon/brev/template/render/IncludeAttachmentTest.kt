package no.nav.pensjon.brev.template.render

import no.nav.brev.brevbaker.FellesFactory
import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.createTemplate
import no.nav.brev.brevbaker.template.render.Letter2Markup
import no.nav.brev.brevbaker.template.toScope
import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brev.template.HasModel
import no.nav.pensjon.brev.template.LangNynorsk
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.TemplateRootScope.RedigerbartVedlegg
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.render.selectors.includeAttachmentTest.nullData.*
import no.nav.pensjon.brev.template.render.selectors.includeAttachmentTest.vedleggData.test
import no.nav.pensjon.brev.template.render.MatcherDslAsserter.Companion.assertThat
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class IncludeAttachmentTest {
    data class NullData(val vedlegg: VedleggData?) : AutobrevData
    data class VedleggData(val test: String?) : no.nav.pensjon.brev.api.model.maler.VedleggData

    @TemplateModelHelpers
    object Helpers : HasModel<NullData>

    @Nested
    inner class IncludeIfNotNull {
        private val testVedlegg = createAttachment<LangNynorsk, VedleggData>(
            title = {
                text(
                    nynorsk { +"Test vedlegg" },
                )
            },
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
            includeAttachmentIfNotNull(testVedlegg, vedlegg)
        }

        @Test
        fun `attachment is not included when using includeAttachmentIfNotNull and attachmentData is null`() {
            val actual =
                Letter2Markup.render(LetterTestImpl(testTemplate, NullData(null), Nynorsk, FellesFactory.felles))
            assertThat(actual.attachments).isEmpty()
        }

        @Test
        fun `attachment is included when using includeAttachmentIfNotNull and attachmentData is not null`() {
            assertThat(
                Letter2Markup.render(
                    LetterTestImpl(
                        testTemplate,
                        NullData(VedleggData("testtekst")),
                        Nynorsk,
                        FellesFactory.felles
                    )
                )
            ).hasAttachments {
                attachment {
                    title { literal("Test vedlegg") }
                    blocks {
                        paragraph { literal("test") }
                    }
                }
            }
        }
    }
    @Nested
    @OptIn(RedigerbartVedlegg::class)
    inner class IncludeRedigerbarIfNotNull {
        private val vedleggId = VedleggId("testVedlegg")

        private val testVedlegg = createAttachment<LangNynorsk, VedleggData>(
            title = { text(nynorsk { +"Test vedlegg" }) },
            includeSakspart = true,
        ) {
            paragraph {
                text(nynorsk { +argument.test.ifNull("") })
            }
        }

        private val testTemplate = createTemplate(
            letterDataType = NullData::class,
            languages = languages(Nynorsk),
            letterMetadata = testLetterMetadata,
        ) {
            title { text(nynorsk { +"tittel" }) }
            outline {}
            includeAttachmentRedigerbarIfNotNull(vedleggId, testVedlegg, vedlegg)
        }

        @Test
        fun `editable attachment is not included when attachmentData is null`() {
            val letter = LetterImpl(testTemplate, NullData(null), Nynorsk, FellesFactory.felles)

            assertThat(Letter2Markup.render(letter).attachments).isEmpty()
            assertThat(Letter2Markup.renderEditableAttachmentTitles(letter.toScope(), testTemplate)).isEmpty()
        }

        @Test
        fun `editable attachment is included with data and editable id when attachmentData is not null`() {
            val letter = LetterImpl(testTemplate, NullData(VedleggData("testtekst")), Nynorsk, FellesFactory.felles)

            assertThat(Letter2Markup.render(letter)).hasAttachments {
                attachment {
                    title { literal("Test vedlegg") }
                    blocks {
                        paragraph { variable("testtekst") }
                    }
                }
            }
            val titles = Letter2Markup.renderEditableAttachmentTitles(letter.toScope(), testTemplate)
            assertThat(titles.keys).containsExactly(vedleggId)
            assertThat(titles.getValue(vedleggId).joinToString("") { it.text }).isEqualTo("Test vedlegg")
        }
    }

}
