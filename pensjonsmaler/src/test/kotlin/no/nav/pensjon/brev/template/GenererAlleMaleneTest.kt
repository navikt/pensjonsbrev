package no.nav.pensjon.brev.template

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.FeatureToggleService
import no.nav.pensjon.brev.api.model.FeatureToggle
import no.nav.pensjon.brev.api.model.FeatureToggleSingleton
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.maler.ProductionTemplates
import no.nav.pensjon.brev.maler.example.EksempelbrevRedigerbart
import no.nav.pensjon.brev.maler.example.LetterExample
import org.junit.jupiter.api.Tag
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource


val filterForPDF = listOf("SAMLET")

class GenererAlleMaleneTest {

    @Tag(TestTags.MANUAL_TEST)
    @ParameterizedTest(name = "{1}, {3}")
    @MethodSource("filtrerteMaler")
    fun <T : Any> testPdf(
        template: LetterTemplate<LanguageSupport, T>,
        brevkode: Brevkode<*>,
        fixtures: T,
        spraak: Language,
    ) {
        if (!template.language.supports(spraak)) {
            println("Mal ${template.name} fins ikke på språk $spraak, tester ikke denne")
            return
        }
        val letter = LetterTestImpl(template, fixtures, spraak, Fixtures.felles)

        letter.renderTestPDF(filnavn(brevkode, spraak))
    }

    @ParameterizedTest(name = "{1}, {3}")
    @MethodSource("alleMalene")
    fun <T : Any> testHtml(
        template: LetterTemplate<LanguageSupport, T>,
        brevkode: Brevkode<*>,
        fixtures: T,
        spraak: Language,
    ) {
        if (!template.language.supports(spraak)) {
            println("Mal ${template.name} fins ikke på språk ${spraak.javaClass.simpleName}, tester ikke denne")
            return
        }
        LetterTestImpl(
            template,
            fixtures,
            spraak,
            Fixtures.felles,
        ).renderTestHtml(filnavn(brevkode, spraak))
    }

    private fun filnavn(brevkode: Brevkode<*>, spraak: Language) =
        "${brevkode.kode()}_${spraak.javaClass.simpleName}"

    companion object {
        @JvmStatic
        fun filtrerteMaler(): List<Arguments> = finnMaler(filterForPDF)

        @JvmStatic
        fun alleMalene(): List<Arguments> = finnMaler(listOf())

        @JvmStatic
        fun finnMaler(filter: List<String> = listOf()): List<Arguments> {
            FeatureToggleSingleton.init(FeatureToggleDummy)
            return listOf(Language.Nynorsk, Language.Bokmal, Language.English).flatMap { spraak ->
                (ProductionTemplates.hentAutobrevmaler() +
                        ProductionTemplates.hentRedigerbareMaler()
                        + LetterExample
                        + EksempelbrevRedigerbart
                        ).filter {
                        filter.isEmpty() || filter.any { f ->
                            it.template.name.lowercase().contains(f.lowercase())
                        }
                    }
                    .map { Arguments.of(it.template, it.kode, Fixtures.create(it.template.letterDataType), spraak) }
            }
        }
    }
}

object FeatureToggleDummy : FeatureToggleService {
    private val toggles: MutableMap<FeatureToggle, Boolean> = mutableMapOf()

    override fun isEnabled(toggle: FeatureToggle): Boolean = toggles[toggle] ?: false

}