package no.nav.pensjon.brev.maler.vedlegg

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.PDF_BUILDER_URL
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.render.PensjonLatexRenderer
import no.nav.pensjon.brev.writeTestPDF
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.PDF_BYGGER)
class OrienteringOmRettigheterUfoereTest {

    @Test
    fun testVedlegg() {
        val template = createTemplate(
            name = "test-template",
            letterDataType = Unit::class,
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata = LetterMetadata(
                "test mal",
                isSensitiv = false,
                distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
            ),
        ) {
            title {
                text(
                    Bokmal to "Tittel",
                    Nynorsk to "Tittel",
                    English to "Title"
                )
            }
            outline {

            }


            includeAttachment(
                vedleggOrienteringOmRettigheterOgPlikterUfoere,
                Fixtures.create(OrienteringOmRettigheterUfoereDto::class).expr()
            )
        }
            Letter(
                template,
                Unit,
                English,
                Fixtures.fellesAuto
            )
                .let { PensjonLatexRenderer.render(it) }
                .let { runBlocking { LaTeXCompilerService(PDF_BUILDER_URL).producePDF(it, "test").base64PDF } }
                .also { writeTestPDF("OrienteringOmRettigheterUfoere", it) }

    }
}



