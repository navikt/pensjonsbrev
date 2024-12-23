package no.nav.pensjon.brev.maler.legacy.vedlegg

import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.createVedleggTestTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brevbaker.api.model.Bruker
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
        Letter(
            template,
            Unit,
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF("OpplysningerBruktIBeregningUfoereLegacy")
    }
}