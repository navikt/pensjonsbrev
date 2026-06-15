package no.nav.pensjon.brev.template.render

import no.nav.brev.brevbaker.FellesFactory
import no.nav.brev.brevbaker.createTemplate
import no.nav.brev.brevbaker.template.render.Letter2Markup
import no.nav.brev.brevbaker.template.toScope
import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brev.api.model.maler.EmptyVedleggData
import no.nav.pensjon.brev.template.LangNynorsk
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.LetterImpl
import no.nav.pensjon.brev.template.SimpleSelector
import no.nav.pensjon.brev.template.VedleggId
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.RedigerbartVedlegg
import no.nav.pensjon.brev.template.dsl.expression.select
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@OptIn(RedigerbartVedlegg::class)
class EditableAttachmentOverrideTest {

    data class TomtBrev(val tekst: String = "") : AutobrevData

    data class PredikatBrev(val visVedlegg: Boolean) : AutobrevData

    private val originaltVedlegg = createAttachment<LangNynorsk, EmptyVedleggData>(
        title = { text(nynorsk { +"Original tittel" }) },
    ) {
        paragraph { text(nynorsk { +"Originalt innhold" }) }
    }

    private val annetVedlegg = createAttachment<LangNynorsk, EmptyVedleggData>(
        title = { text(nynorsk { +"Overstyrt tittel" }) },
    ) {
        paragraph { text(nynorsk { +"Overstyrt innhold" }) }
    }

    private val ikkeRedigerbartVedlegg = createAttachment<LangNynorsk, EmptyVedleggData>(
        title = { text(nynorsk { +"Ikke-redigerbar tittel" }) },
    ) {
        paragraph { text(nynorsk { +"Ikke-redigerbart innhold" }) }
    }

    private val template = createTemplate(
        letterDataType = TomtBrev::class,
        languages = languages(Nynorsk),
        letterMetadata = testLetterMetadata,
    ) {
        title { text(nynorsk { +"tittel" }) }
        outline {}
        includeAttachmentRedigerbar(VedleggId("vedlegg1"), originaltVedlegg)
    }

    private val templateMedFlereVedlegg = createTemplate(
        letterDataType = TomtBrev::class,
        languages = languages(Nynorsk),
        letterMetadata = testLetterMetadata,
    ) {
        title { text(nynorsk { +"tittel" }) }
        outline {}
        includeAttachmentRedigerbar(VedleggId("vedlegg1"), originaltVedlegg)
        includeAttachment(ikkeRedigerbartVedlegg)
        includeAttachmentRedigerbar(VedleggId("vedlegg2"), annetVedlegg)
    }

    private val scopeMedFlereVedlegg =
        LetterImpl(templateMedFlereVedlegg, TomtBrev(), Nynorsk, FellesFactory.felles).toScope()

    private val scope = LetterImpl(template, TomtBrev(), Nynorsk, FellesFactory.felles).toScope()

    private val overstyring = run {
        val overrideTemplate = createTemplate(
            letterDataType = TomtBrev::class,
            languages = languages(Nynorsk),
            letterMetadata = testLetterMetadata,
        ) {
            title { text(nynorsk { +"tittel" }) }
            outline {}
            includeAttachmentRedigerbar(VedleggId("vedlegg1"), annetVedlegg)
        }
        val overrideScope = LetterImpl(overrideTemplate, TomtBrev(), Nynorsk, FellesFactory.felles).toScope()
        Letter2Markup.renderAttachmentsOnly(overrideScope, overrideTemplate).first()
    }

    private val predikatTemplate = createTemplate(
        letterDataType = PredikatBrev::class,
        languages = languages(Nynorsk),
        letterMetadata = testLetterMetadata,
    ) {
        title { text(nynorsk { +"tittel" }) }
        outline {}
        includeAttachmentRedigerbar(
            VedleggId("vedlegg1"),
            originaltVedlegg,
            predicate = argument.select(
                SimpleSelector(
                    className = PredikatBrev::class.qualifiedName!!,
                    propertyName = "visVedlegg",
                    propertyType = "kotlin.Boolean",
                    selector = PredikatBrev::visVedlegg,
                )
            ),
        )
    }

    private fun predikatScope(visVedlegg: Boolean) =
        LetterImpl(predikatTemplate, PredikatBrev(visVedlegg), Nynorsk, FellesFactory.felles).toScope()

    @Nested
    inner class RenderAttachmentsOnly {
        @Test
        fun `vedlegget rendres fra mal naar det ikke finnes noen overstyring`() {
            val attachments = Letter2Markup.renderAttachmentsOnly(scope, template)

            assertThat(attachments).hasSize(1)
            assertThat(attachments.first()).isNotEqualTo(overstyring)
        }

        @Test
        fun `overstyringen brukes i sin helhet naar vedleggId matcher`() {
            val attachments = Letter2Markup.renderAttachmentsOnly(scope, template, mapOf(VedleggId("vedlegg1") to overstyring))

            assertThat(attachments).hasSize(1)
            assertThat(attachments.first()).isEqualTo(overstyring)
        }

        @Test
        fun `overstyring for en annen vedleggId ignoreres`() {
            val utenOverstyring = Letter2Markup.renderAttachmentsOnly(scope, template)
            val medFeilId = Letter2Markup.renderAttachmentsOnly(scope, template, mapOf(VedleggId("annetVedlegg") to overstyring))

            assertThat(medFeilId).isEqualTo(utenOverstyring)
        }
    }

    @Nested
    inner class RenderEditableAttachmentTitles {
        @Test
        fun `returnerer tittelen for et redigerbart vedlegg`() {
            val titler = Letter2Markup.renderEditableAttachmentTitles(scope, template)

            assertThat(titler.keys).containsExactly(VedleggId("vedlegg1"))
            assertThat(titler.getValue(VedleggId("vedlegg1")).joinToString("") { it.text }).isEqualTo("Original tittel")
        }

        @Test
        fun `returnerer titler for alle redigerbare vedlegg`() {
            val titler = Letter2Markup.renderEditableAttachmentTitles(scopeMedFlereVedlegg, templateMedFlereVedlegg)

            assertThat(titler.keys).containsExactlyInAnyOrder(VedleggId("vedlegg1"), VedleggId("vedlegg2"))
            assertThat(titler.getValue(VedleggId("vedlegg1")).joinToString("") { it.text }).isEqualTo("Original tittel")
            assertThat(titler.getValue(VedleggId("vedlegg2")).joinToString("") { it.text }).isEqualTo("Overstyrt tittel")
        }

        @Test
        fun `utelater ikke-redigerbare vedlegg`() {
            val titler = Letter2Markup.renderEditableAttachmentTitles(scopeMedFlereVedlegg, templateMedFlereVedlegg)

            assertThat(titler.values.flatten().map { it.text }).doesNotContain("Ikke-redigerbar tittel")
        }

        @Test
        fun `gir tom map naar ingen vedlegg er redigerbare`() {
            val utenRedigerbareTemplate = createTemplate(
                letterDataType = TomtBrev::class,
                languages = languages(Nynorsk),
                letterMetadata = testLetterMetadata,
            ) {
                title { text(nynorsk { +"tittel" }) }
                outline {}
                includeAttachment(ikkeRedigerbartVedlegg)
            }
            val utenRedigerbareScope =
                LetterImpl(utenRedigerbareTemplate, TomtBrev(), Nynorsk, FellesFactory.felles).toScope()

            val titler = Letter2Markup.renderEditableAttachmentTitles(utenRedigerbareScope, utenRedigerbareTemplate)

            assertThat(titler).isEmpty()
        }
    }

    @Nested
    inner class MedPredikat {
        @Test
        fun `redigerbart vedlegg rendres naar predikatet er sant`() {
            val attachments = Letter2Markup.renderAttachmentsOnly(predikatScope(visVedlegg = true), predikatTemplate)

            assertThat(attachments).hasSize(1)
            assertThat(attachments.first().title.joinToString("") { it.text }).isEqualTo("Original tittel")
        }

        @Test
        fun `redigerbart vedlegg utelates naar predikatet er usant`() {
            val attachments = Letter2Markup.renderAttachmentsOnly(predikatScope(visVedlegg = false), predikatTemplate)

            assertThat(attachments).isEmpty()
        }

        @Test
        fun `renderEditableAttachmentTitles tar med redigerbart vedlegg naar predikatet er sant`() {
            val titler = Letter2Markup.renderEditableAttachmentTitles(predikatScope(visVedlegg = true), predikatTemplate)

            assertThat(titler.keys).containsExactly(VedleggId("vedlegg1"))
            assertThat(titler.getValue(VedleggId("vedlegg1")).joinToString("") { it.text }).isEqualTo("Original tittel")
        }

        @Test
        fun `renderEditableAttachmentTitles utelater redigerbart vedlegg naar predikatet er usant`() {
            val titler = Letter2Markup.renderEditableAttachmentTitles(predikatScope(visVedlegg = false), predikatTemplate)

            assertThat(titler).isEmpty()
        }
    }
}
