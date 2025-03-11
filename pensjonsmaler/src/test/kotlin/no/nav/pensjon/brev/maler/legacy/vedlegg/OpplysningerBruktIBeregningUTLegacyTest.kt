package no.nav.pensjon.brev.maler.legacy.vedlegg

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.createVedleggTestTemplate
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.languages
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class OpplysningerBruktIBeregningUTLegacyTest {

    @Test
    fun testVedlegg() {
        val template = createVedleggTestTemplate(
            vedleggOpplysningerBruktIBeregningUTLegacy,
            Fixtures.create(PE::class).expr(),
            languages(Language.Bokmal, Language.Nynorsk, Language.English),
        )
        LetterTestImpl(
            template,
            Unit,
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF("OpplysningerBruktIBeregningUfoereLegacy")
    }
}