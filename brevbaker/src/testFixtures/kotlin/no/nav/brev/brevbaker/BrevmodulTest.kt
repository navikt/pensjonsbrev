package no.nav.brev.brevbaker

import no.nav.pensjon.brev.api.FeatureToggleService
import no.nav.pensjon.brev.api.model.FeatureToggle
import no.nav.pensjon.brev.api.model.FeatureToggleSingleton
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.AutomatiskBrevkode
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevkode
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.api.model.maler.VedleggData
import no.nav.pensjon.brev.template.AttachmentTemplate
import no.nav.pensjon.brev.template.BrevTemplate
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.UnaryOperation
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brevbaker.api.model.DisplayText
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Collectors
import java.util.stream.IntStream
import kotlin.reflect.KProperty


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class BrevmodulTest(
    val templates: AllTemplates,
    val auto: Collection<Brevkode.Automatisk>,
    val redigerbare: Collection<Brevkode.Redigerbart>,
    val fixtures: LetterDataFactory,
    val filterForPDF: List<Brevkode<*>>
) {
    @Test
    open fun `alle autobrev fins i templates`() {
        val brukteKoder = templates.hentAutobrevmaler().map { it.kode }
        val ubrukteKoder = auto.filterNot { brukteKoder.contains(it) }
        assertEquals(ubrukteKoder, listOf<Brevkode.Automatisk>())
    }

    @Test
    fun `alle redigerbare brev fins i templates`() {
        val brukteKoder = templates.hentRedigerbareMaler().map { it.kode }
        val ubrukteKoder = redigerbare.filterNot { brukteKoder.contains(it) }
        assertEquals(ubrukteKoder, listOf<Brevkode.Redigerbart>())
    }

    @Test
    fun `alle redigerbare brev har displaytext for alle saksbehandlervalg`() {
        templates.hentRedigerbareMaler().map { mal ->
            val clazz = mal.template.letterDataType.java
            val saksbehandlervalg = clazz.declaredFields.map { it.type }.filter { field -> SaksbehandlerValgBrevdata::class.java.isAssignableFrom(field) }.map { it.kotlin }
            saksbehandlervalg.flatMap { it.members }.filter { it is KProperty<*> }.forEach { field ->
                val hasDisplayText = field.annotations.filterIsInstance<DisplayText>().any()
                assertTrue(hasDisplayText, "Alle saksbehandlervalg må ha displaytext, ${field.name} i klasse ${clazz.name} mangler det")
            }
        }
    }

    @Test
    fun `alle maler med brevdata har annotasjon som gjoer at vi genererer selectors`() {
        (templates.hentAutobrevmaler() + templates.hentRedigerbareMaler())
            .filterNot { it.template.letterDataType in setOf(EmptyAutobrevdata::class, EmptyRedigerbarBrevdata::class)  }
            .forEach {
                assertTrue(
                    it.javaClass.declaredAnnotations.any { annotation -> annotation.annotationClass == TemplateModelHelpers::class },
                    "Alle maler annoteres med @TemplateModelHelpers, for å få generert selectors. Det har ikke ${it.javaClass.simpleName}"
                )
            }
    }

    // Dette er ei hypotese vi på brevteamet har. Oppdater eller fjern testen hvis dere skulle finne et scenario hvor hypotesa ikke holder
    @Test
    fun `brev som er deklarert med brevtype vedtaksbrev skal ha brevkontekst vedtak`() {
        assertEquals(
            emptyList<String>(),
            templates.hentRedigerbareMaler()
                .filter { it.template.letterMetadata.brevtype == LetterMetadata.Brevtype.VEDTAKSBREV }
                .filterNot { it.brevkontekst == TemplateDescription.Brevkontekst.VEDTAK }
                .map { it.javaClass.simpleName }
            ,
        )
    }

    @Suppress("unused") // Brukt i MethodSource
    private fun filtrerPdf() = finnMaler( { filterForPDF.isEmpty() || filterForPDF.contains(it.kode) })

    @Suppress("unused") // Brukt i MethodSource
    private fun filtrerVedlegg() = (templates.hentAutobrevmaler() + templates.hentRedigerbareMaler())
        .map { it.template }
        .flatMap { template ->
            template.language.all().flatMap { spraak ->
                template.attachments.map { vedlegg ->
                    val dto =
                        (((vedlegg.data as? Expression.UnaryInvoke<*, *>)?.operation as? UnaryOperation.Select<*, *>)?.selector?.propertyType)?.let {
                            Class.forName(it.removeSuffix("?"))
                        }
                    dto?.let { Arguments.of(vedlegg.template, fixtures.create(dto.kotlin), spraak, dto.simpleName) }
                }
            }
        }
        .filterNotNull()
        .distinctBy { it.get()[2].toString() + it.get()[3] }


    @Suppress("unused") // Brukt i MethodSource
    protected fun alleMalene() = finnMaler({ true })

    protected fun finnMaler(shouldInclude: (BrevTemplate<BrevbakerBrevdata, out Brevkode<*>>) -> Boolean = { true }): List<Arguments> {
        FeatureToggleSingleton.init(FeatureToggleDummy)
        return listOf(Language.Nynorsk, Language.Bokmal, Language.English)
            .flatMap { spraak ->
                (templates.hentAutobrevmaler() + templates.hentRedigerbareMaler())
                    .filter { shouldInclude(it) }
                    .map { Arguments.of(it.template, it.kode, fixtures.create(it.template.letterDataType), spraak) }
            }
    }

    @Tag(TestTags.MANUAL_TEST)
    @ParameterizedTest(name = "{1}, {3}")
    @MethodSource("filtrerPdf")
    fun <T : BrevbakerBrevdata> testPdf(
        template: LetterTemplate<LanguageSupport, T>,
        brevkode: Brevkode<*>,
        fixtures: T,
        spraak: Language,
    ) {
        if (!template.language.supports(spraak)) {
            println("Mal ${template.letterMetadata.displayTitle} med brevkode ${brevkode.kode()} fins ikke på språk ${spraak.javaClass.simpleName.lowercase()}, tester ikke denne")
            return
        }
        val letter = LetterTestImpl(template, fixtures, spraak, FellesFactory.felles)

        letter.renderTestPDF(filnavn(brevkode, spraak), pdfByggerService = LaTeXCompilerService(PDFByggerTestContainer.mappedUrl()))
    }

    @ParameterizedTest(name = "{1}, {3}")
    @MethodSource("alleMalene")
    fun <T : BrevbakerBrevdata> testHtml(
        template: LetterTemplate<LanguageSupport, T>,
        brevkode: Brevkode<*>,
        fixtures: T,
        spraak: Language,
    ) {
        if (!template.language.supports(spraak)) {
            println("Mal ${template.letterMetadata.displayTitle} med brevkode ${brevkode.kode()} fins ikke på språk ${spraak.javaClass.simpleName.lowercase()}, tester ikke denne")
            return
        }
        LetterTestImpl(
            template,
            fixtures,
            spraak,
            FellesFactory.felles,
        ).renderTestHtml(filnavn(brevkode, spraak))
    }

    @ParameterizedTest(name = "{3}, {2}")
    @MethodSource("filtrerVedlegg")
    fun <T : VedleggData> testVedlegg(
        template: AttachmentTemplate<LanguageSupport, T>,
        fixtures: T,
        spraak: Language,
        clazzName: String,
    ) {
        createVedleggTestTemplate(template, fixtures.expr(), languages(spraak))
            .let {
                LetterTestImpl(
                    it,
                    fixtures,
                    spraak,
                    Fixtures.felles,
                ).renderTestHtml("${clazzName}_$spraak", "test_vedlegg")
            }
    }

    protected fun filnavn(brevkode: Brevkode<*>, spraak: Language) =
        "${brevkode.kode()}_${spraak.javaClass.simpleName}"


    @Test
    fun `alle maler skal bruke en unik brevkode`() {
        val malKoder = (templates.hentAutobrevmaler() + templates.hentRedigerbareMaler())
            .map { it.kode.kode() }

        malKoder.sorted().zipWithNext { a, b ->
            assert(a != b) { "Alle brevmaler må bruke egne unike brevkoder! Brevkode $a brukes i flere brev." }
        }
    }

    @Test
    fun `tittel paa under 50 tegn er OK for redigerbar`() {
        val langTittel = IntStream.range(0, 15).mapToObj { "a" }.collect(Collectors.joining())
        RedigerbarBrevkode(langTittel)
    }
    @Test
    fun `tittel paa over 50 tegn feiler for redigerbar`() {
        val langTittel = IntStream.range(0, 51).mapToObj { "b" }.collect(Collectors.joining())
        assertThrows(IllegalArgumentException::class.java) { RedigerbarBrevkode(langTittel) }
    }


    @Test
    fun `tittel paa under 50 tegn er OK for automatisk`() {
        val langTittel = IntStream.range(0, 15).mapToObj { "c" }.collect(Collectors.joining())
        AutomatiskBrevkode(langTittel)
    }
    @Test
    fun `tittel paa over 50 tegn feiler for automatisk`() {
        val langTittel = IntStream.range(0, 51).mapToObj { "d" }.collect(Collectors.joining())
        assertThrows(IllegalArgumentException::class.java) { AutomatiskBrevkode(langTittel) }
    }

    @Test
    fun `alle brevkoder skal være unike`(){
        val redigerbareBrev = redigerbare.map { it.toString() }
        val autobrev = auto.map { it.toString() }

        assert(redigerbareBrev.intersect(autobrev.toSet()).isEmpty())
    }

    @Test
    fun `ingen auto-koder har lengde over 50`() {
        auto.filter { it.kode().length > 50 }.let {
            assertTrue(it.isEmpty(), "Alle brevkoder må være under 50 lange for å kunne arkiveres. Disse feila: $it")
        }
    }

    @Test
    fun `ingen redigerbare koder har lengde over 50`() {
        redigerbare.filter { it.kode().length > 50 }.let {
            assertTrue(it.isEmpty(), "Alle brevkoder må være under 50 lange for å kunne arkiveres. Disse feila: $it")
        }
    }
}

object FeatureToggleDummy : FeatureToggleService {
    private val toggles: MutableMap<FeatureToggle, Boolean> = mutableMapOf()

    override fun isEnabled(toggle: FeatureToggle): Boolean = toggles[toggle] ?: false
    override fun verifiserAtAlleBrytereErDefinert(entries: List<FeatureToggle>) { }

}