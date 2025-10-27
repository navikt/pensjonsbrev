package no.nav.pensjon.brev.template.render

import no.nav.brev.brevbaker.Fixtures
import no.nav.brev.brevbaker.createTemplate
import no.nav.brev.brevbaker.template.render.Letter2Markup
import no.nav.pensjon.brev.api.model.maler.Vedlegg
import no.nav.pensjon.brev.template.LangNynorsk
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.LetterImpl
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.render.CreateAttachmentTestSelectors.LittInnholdSelectors.test1
import no.nav.pensjon.brev.template.render.CreateAttachmentTestSelectors.LittInnholdSelectors.test2
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateAttachmentTest {
    @TemplateModelHelpers
    val vedlegg = createAttachment<LangNynorsk, LittInnhold>(
        title = {
            text(
                nynorsk { +"Test vedlegg" }
            )
            ifNotNull(test1) { eval(it) }
            eval(test2.format())
            showIf(test2.greaterThan(5)) {
                text(nynorsk { +"parameteret er større enn 5" })
            }
        },
        includeSakspart = true
    ) {
        paragraph {
            text(nynorsk { +"test" })
        }
    }
    @Test
    fun `title may have several elements`() {
        val testVedlegg = vedlegg

        val testTemplate = createTemplate(
            letterDataType = LittInnhold::class,
            languages = languages(Nynorsk),
            letterMetadata = testLetterMetadata,
        ) {
            title { text(nynorsk { +"tittel" }) }
            outline {}
            includeAttachment(testVedlegg, argument)
        }

        val tittel =
            Letter2Markup.render(LetterImpl(testTemplate, LittInnhold("testtekst", 10), Nynorsk, Fixtures.felles)).attachments
                .first()
                .title
        assertEquals(4, tittel.size)
    }

    data class LittInnhold(val test1: String?, val test2: Int) : Vedlegg
}