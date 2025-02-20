package no.nav.pensjon.brev.maler.vedlegg

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.createVedleggTestTemplate
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.languages
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class OrienteringOmRettigheterUfoereTest {

    @Test
    fun testVedlegg() {
        val template = createVedleggTestTemplate(
            vedleggDineRettigheterOgPlikterUfoere,
            Fixtures.create(OrienteringOmRettigheterUfoereDto::class).expr(),
            languages(Bokmal, Nynorsk, English),
        )
        LetterTestImpl(
            template,
            Unit,
            Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF("OrienteringOmRettigheterUfoere")

    }
}



