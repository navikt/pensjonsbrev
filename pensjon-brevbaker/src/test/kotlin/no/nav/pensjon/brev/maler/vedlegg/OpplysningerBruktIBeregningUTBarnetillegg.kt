package no.nav.pensjon.brev.maler.vedlegg

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.PDF_BUILDER_URL
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto.BarnetilleggGjeldende.Fellesbarn
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningUT.vedleggOpplysningerBruktIBeregningUTBarnetillegg
import no.nav.pensjon.brev.template.Language
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
class OpplysningerBruktIBeregningUTTest {

    @Test
    fun testVedlegg() {
        val template = createTemplate(
            name = "test-template",
            letterDataType = Unit::class,
            languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
            letterMetadata = LetterMetadata(
                "test mal",
                isSensitiv = false,
                distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
            ),
        ) {
            title {
                text(
                    Language.Bokmal to "Tittel",
                    Language.Nynorsk to "Tittel",
                    Language.English to "Title"
                )
            }

            outline {
            }
// TODO Virker ikke etter hensikt
                val dto = Fixtures.create(OpplysningerBruktIBeregningUTDto::class)
                dto.copy(
                    barnetilleggGjeldende = OpplysningerBruktIBeregningUTDto.BarnetilleggGjeldende(
                        fellesbarn = Fixtures.create(Fellesbarn::class),
                        saerkullsbarn = null,
                        totaltAntallBarn = 2,
                    )
                )

            includeAttachment(
                vedleggOpplysningerBruktIBeregningUTBarnetillegg,
                Fixtures.create(OpplysningerBruktIBeregningUTDto::class).expr()
                )
        }
        Letter(
            template,
            Unit,
            Language.English,
            Fixtures.fellesAuto
        )
            .let { PensjonLatexRenderer.render(it) }
            .let { runBlocking { LaTeXCompilerService(PDF_BUILDER_URL).producePDF(it, "test").base64PDF } }
            .also { writeTestPDF("OpplysningerBruktIBeregningUfoere", it) }

    }
}