package no.nav.pensjon.brev.maler.legacy.vedlegg

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.createVedleggTestTemplate
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.api.model.maler.legacy.OversiktOverPensjonensStoerrelseGjenlevendepensjonDto
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.languages
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class OversiktOverPensjonensStoerrelseGjenlevendepensjonLegacyTest {

    @Test
    fun testVedlegg() {
        val template = createVedleggTestTemplate(
            vedleggOversiktOverPensjonensStoerrelseGjenlevendepensjon,
            Fixtures.createVedlegg<OversiktOverPensjonensStoerrelseGjenlevendepensjonDto>().expr(),
            languages(Language.Bokmal, Language.Nynorsk, Language.English),
        )
        LetterTestImpl(
            template,
            EmptyAutobrevdata,
            Language.Bokmal,
            Fixtures.fellesAuto,
        ).renderTestPDF("OversiktOverPensjonensStoerrelseGjenlevendepensjonLegacy")
    }
}

