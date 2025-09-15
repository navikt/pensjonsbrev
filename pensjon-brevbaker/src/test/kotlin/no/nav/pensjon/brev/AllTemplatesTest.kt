package no.nav.pensjon.brev

import no.nav.pensjon.brev.api.FeatureToggleService
import no.nav.pensjon.brev.api.model.FeatureToggle
import no.nav.pensjon.brev.api.model.FeatureToggleSingleton
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.maler.ProductionTemplates
import no.nav.pensjon.brev.maler.example.EksempelbrevRedigerbart
import no.nav.pensjon.brev.maler.example.LetterExample
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.brevbakerJacksonObjectMapper
import no.nav.pensjon.brev.template.render.TemplateDocumentationRenderer
import no.nav.pensjon.brev.template.render.modelSpecification
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.nio.file.Path

class AllTemplatesTest {

    val templateDocumentationTestPath = Path.of("build", "test_template_documentation").also { it.toFile().mkdirs() }
    private val mapper = brevbakerJacksonObjectMapper().writerWithDefaultPrettyPrinter()

    @Test
    fun `alle maler skal bruke en unik brevkode`() {
        val malKoder = (pensjonOgUfoereProductionTemplates.hentAutobrevmaler() + pensjonOgUfoereProductionTemplates.hentRedigerbareMaler())
            .map { it.kode.kode() }

        malKoder.sorted().zipWithNext { a, b ->
            assert(a != b) { "Alle brevmaler må bruke egne unike brevkoder! Brevkode $a brukes i flere brev." }
        }
    }

    @ParameterizedTest(name = "{1}, {3}")
    @MethodSource("alleMalene")
    fun <T : Any> testTemplateDocumentation(
        template: LetterTemplate<LanguageSupport, T>,
        brevkode: Brevkode<*>,
        spraak: Language,
    ) {
        if (!template.language.supports(spraak)) {
            println("Mal ${template.name} fins ikke på språk ${spraak.javaClass.simpleName.lowercase()}, tester ikke denne")
            return
        }
        val templateDocumentation =
            TemplateDocumentationRenderer.render(template, spraak, template.modelSpecification())

        templateDocumentationTestPath.resolve(filnavn(brevkode, spraak))
            .toFile().writeText(mapper.writeValueAsString(templateDocumentation))

    }

    private fun filnavn(brevkode: Brevkode<*>, spraak: Language) =
        "${brevkode.kode()}_${spraak.javaClass.simpleName}.json"


    companion object {
        @JvmStatic
        fun alleMalene(): List<Arguments> = finnMaler(listOf())

        @JvmStatic
        fun finnMaler(filter: List<Brevkode<*>> = listOf()): List<Arguments> {
            FeatureToggleSingleton.init(FeatureToggleDummy)
            return listOf(Language.Nynorsk, Language.Bokmal, Language.English).flatMap { spraak ->
                (ProductionTemplates.hentAutobrevmaler() +
                        ProductionTemplates.hentRedigerbareMaler()
                        + LetterExample
                        + EksempelbrevRedigerbart
                        ).filter { filter.isEmpty() || filter.any { f -> it.kode.kode() == f.kode() } }
                    .map { Arguments.of(it.template, it.kode, spraak) }
            }
        }
    }
}


object FeatureToggleDummy : FeatureToggleService {
    private val toggles: MutableMap<FeatureToggle, Boolean> = mutableMapOf()

    override fun isEnabled(toggle: FeatureToggle): Boolean = toggles[toggle] ?: false
    override fun verifiserAtAlleBrytereErDefinert(entries: List<FeatureToggle>) { }

}