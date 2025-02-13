package no.nav.pensjon.brev.maler.vedlegg

import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.createVedleggTestTemplate
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.languages
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class OpplysningerBruktIBeregningUTTest {

    @Test
    fun testVedlegg() {
        val template = createVedleggTestTemplate(
            createVedleggOpplysningerBruktIBeregningUT(skalViseMinsteytelse = true, skalViseBarnetillegg = true),
            Fixtures.create(OpplysningerBruktIBeregningUTDto::class).expr(),
            languages(Language.Bokmal, Language.Nynorsk, Language.English),
        )
        Letter(
            template,
            Unit,
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF("OpplysningerBruktIBeregningUfoere")

    }
}