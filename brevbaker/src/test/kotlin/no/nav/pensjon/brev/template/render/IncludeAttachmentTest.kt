package no.nav.pensjon.brev.template.render

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.has
import com.natpryce.hamkrest.isEmpty
import no.nav.brev.brevbaker.FellesFactory
import no.nav.brev.brevbaker.createTemplate
import no.nav.brev.brevbaker.template.render.Letter2Markup
import no.nav.brev.brevbaker.template.render.LetterWithAttachmentsMarkup
import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brev.template.HasModel
import no.nav.pensjon.brev.template.LangNynorsk
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.LetterImpl
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.render.IncludeAttachmentTestSelectors.NullDataSelectors.vedlegg
import org.junit.jupiter.api.Nested
import kotlin.test.Test

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
            val actual = Letter2Markup.render(LetterImpl(testTemplate, NullData(null), Nynorsk, FellesFactory.felles))
            assertThat(
                actual,
                has(LetterWithAttachmentsMarkup::attachments, isEmpty)
            )
        }

        @Test
        fun `attachment is included when using includeAttachmentIfNotNull and attachmentData is not null`() {
            assertThat(
                Letter2Markup.render(LetterImpl(testTemplate, NullData(VedleggData("testtekst")), Nynorsk, FellesFactory.felles)),
                hasAttachments {
                    attachment {
                        title { literal("Test vedlegg") }
                        blocks {
                            paragraph { literal("test") }
                        }
                    }
                }
            )
        }
    }


}