package no.nav.pensjon.brev.maler.vedlegg

import no.nav.brev.brevbaker.*
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.languages
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class MaanedligPensjonFoerSkattITest {
    private val maanedligPensjonFoerSkattData = Fixtures.create(MaanedligPensjonFoerSkattDto::class)

    val template = createVedleggTestTemplate(
        vedleggMaanedligPensjonFoerSkatt,
        maanedligPensjonFoerSkattData.expr(),
        languages(Bokmal, Nynorsk, English),
    )


    @Test
    fun testPdf() {
        LetterTestImpl(template, Unit, Bokmal, Fixtures.fellesAuto).renderTestPDF("MaanedligPensjonFoerSkatt")
    }

    @Test
    fun testHtml() {
        LetterTestImpl(template, Unit, Bokmal, Fixtures.fellesAuto).renderTestHtml("MaanedligPensjonFoerSkatt")
    }


}
