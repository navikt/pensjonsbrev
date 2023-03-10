package no.nav.pensjon.brev.maler.example

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere.OpplysningerOmBarnetillegg
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.render.PensjonHTMLRenderer
import no.nav.pensjon.brev.writeTestHTML
import org.junit.jupiter.api.Test

class TmpTest {
    private data class TestSomething(val test: String)
    @Test
    fun tempTestHtml() {
        testTemplate(true, true, "test_harfleretillegg_endringInntekt_NB", Bokmal)
        testTemplate(false, true, "test_endringInntekt_NB", Bokmal)
        testTemplate(true, false, "test_harfleretillegg_NB", Bokmal)
        testTemplate(false, false, "test_NB", Bokmal)

        testTemplate(true, true, "test_harfleretillegg_endringInntekt_NN", Nynorsk)
        testTemplate(false, true, "test_endringInntekt_NN", Nynorsk)
        testTemplate(true, false, "test_harfleretillegg_NN", Nynorsk)
        testTemplate(false, false, "test_NN", Nynorsk)

        testTemplate(true, true, "test_harfleretillegg_endringInntekt_EN", English)
        testTemplate(false, true, "test_endringInntekt_EN", English)
        testTemplate(true, false, "test_harfleretillegg_EN", English)
        testTemplate(false, false, "test_EN", English)
    }

    private fun testTemplate(
        harFlereTillegg: Boolean, harKravaarsakEndringInntekt: Boolean, htmlName: String, language: Language
    ) {
        val template = createTemplate(
            name = "test",
            letterDataType = TestSomething::class,
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata = testLetterMetadata,
        ) {
            title {
                text(
                    Bokmal to "test",
                    Nynorsk to "test",
                    English to "test",
                )
            }
            outline {
                includePhrase(
                OpplysningerOmBarnetillegg.PeriodisertInntektInnledning(
                    sivilstand = Sivilstand.GIFT.expr(),
                    harFlereTillegg = harFlereTillegg.expr(),
                    harKravaarsakEndringInntekt = harKravaarsakEndringInntekt.expr(),
                )
            ) }

        }
        Letter(
            template,
            TestSomething("asdfg"),
            language,
            Fixtures.fellesAuto
        ).let { PensjonHTMLRenderer.render(it) }
            .also { writeTestHTML(htmlName, it) }
    }
}