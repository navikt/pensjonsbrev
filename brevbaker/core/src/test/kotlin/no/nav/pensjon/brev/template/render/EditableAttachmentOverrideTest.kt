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
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class EditableAttachmentOverrideTest {

    data class TomtBrev(val tekst: String = "") : AutobrevData

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

    private val template = createTemplate(
        letterDataType = TomtBrev::class,
        languages = languages(Nynorsk),
        letterMetadata = testLetterMetadata,
    ) {
        title { text(nynorsk { +"tittel" }) }
        outline {}
        includeAttachmentRedigerbar("vedlegg1", originaltVedlegg)
    }

    private val scope = LetterImpl(template, TomtBrev(), Nynorsk, FellesFactory.felles).toScope()

    private val overstyring = run {
        val overrideTemplate = createTemplate(
            letterDataType = TomtBrev::class,
            languages = languages(Nynorsk),
            letterMetadata = testLetterMetadata,
        ) {
            title { text(nynorsk { +"tittel" }) }
            outline {}
            includeAttachment(annetVedlegg)
        }
        val overrideScope = LetterImpl(overrideTemplate, TomtBrev(), Nynorsk, FellesFactory.felles).toScope()
        Letter2Markup.renderAttachmentsOnly(overrideScope, overrideTemplate).first()
    }

    @Test
    fun `vedlegget rendres fra mal naar det ikke finnes noen overstyring`() {
        val attachments = Letter2Markup.renderAttachmentsOnly(scope, template)

        assertThat(attachments).hasSize(1)
        assertThat(attachments.first()).isNotEqualTo(overstyring)
    }

    @Test
    fun `overstyringen brukes i sin helhet naar vedleggId matcher`() {
        val attachments = Letter2Markup.renderAttachmentsOnly(scope, template, mapOf("vedlegg1" to overstyring))

        assertThat(attachments).hasSize(1)
        assertThat(attachments.first()).isEqualTo(overstyring)
    }

    @Test
    fun `overstyring for en annen vedleggId ignoreres`() {
        val utenOverstyring = Letter2Markup.renderAttachmentsOnly(scope, template)
        val medFeilId = Letter2Markup.renderAttachmentsOnly(scope, template, mapOf("annetVedlegg" to overstyring))

        assertThat(medFeilId).isEqualTo(utenOverstyring)
    }
}
