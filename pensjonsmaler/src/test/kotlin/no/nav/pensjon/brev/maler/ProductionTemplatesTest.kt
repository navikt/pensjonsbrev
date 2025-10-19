package no.nav.pensjon.brev.maler

import no.nav.brev.brevbaker.AllTemplates
import no.nav.brev.brevbaker.TemplatesTest
import no.nav.brev.brevbaker.TestTags
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.example.EksempelbrevRedigerbart
import no.nav.pensjon.brev.maler.example.LetterExample
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.LetterTemplate
import org.junit.jupiter.api.Tag
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

val filterForPDF = listOf(LetterExample.kode)

class ProductionTemplatesTest : TemplatesTest(
    templates = ProductionTemplates,
    auto = Pesysbrevkoder.AutoBrev.entries,
    redigerbare = Pesysbrevkoder.Redigerbar.entries
) {

    @Tag(TestTags.MANUAL_TEST)
    @ParameterizedTest(name = "{1}, {3}")
    @MethodSource("filtrerteMaler")
    override fun <T : Any> testPdf(
        template: LetterTemplate<LanguageSupport, T>,
        brevkode: Brevkode<*>,
        fixtures: T,
        spraak: Language,
    ) = renderPdf(template, brevkode, fixtures, spraak)

    @ParameterizedTest(name = "{1}, {3}")
    @MethodSource("alleMalene")
    override fun <T : Any> testHtml(
        template: LetterTemplate<LanguageSupport, T>,
        brevkode: Brevkode<*>,
        fixtures: T,
        spraak: Language,
    ) = renderHtml(template, brevkode, fixtures, spraak)

    companion object {
        @JvmStatic
        fun filtrerteMaler(): List<Arguments> = finnMaler(filterForPDF)

        @JvmStatic
        fun alleMalene(): List<Arguments> = finnMaler(listOf())

        @JvmStatic
        fun finnMaler(filter: List<Brevkode<*>> = listOf()): List<Arguments> {
            val alleMaler = object : AllTemplates {
                override fun hentAutobrevmaler() = ProductionTemplates.hentAutobrevmaler() + LetterExample
                override fun hentRedigerbareMaler() =
                    ProductionTemplates.hentRedigerbareMaler() + EksempelbrevRedigerbart
            }
            return finnMaler(
                alleMaler, Fixtures, filter
            )
        }
    }
}