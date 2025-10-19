package no.nav.brev.brevbaker

import no.nav.pensjon.brev.api.FeatureToggleService
import no.nav.pensjon.brev.api.model.FeatureToggle
import no.nav.pensjon.brev.api.model.FeatureToggleSingleton
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brevbaker.api.model.DisplayText
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.provider.Arguments
import kotlin.collections.filterNot
import kotlin.reflect.KProperty


abstract class TemplatesTest(val templates: AllTemplates, val auto: Collection<Brevkode.Automatisk>, val redigerbare: Collection<Brevkode.Redigerbart>) {
    @Test
    fun `alle autobrev fins i templates`() {
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
            .filterNot { it.template.letterDataType in setOf(EmptyBrevdata::class, EmptyRedigerbarBrevdata::class)  }
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

    companion object {
        // Du som konsument må lage din egen jvmstatic-metode i testklassa di som kallar på denne med riktige argumenter
        @JvmStatic
        fun finnMaler(templates: AllTemplates, letterDataFactory: LetterDataFactory, filter: List<Brevkode<*>> = listOf()): List<Arguments> {
            FeatureToggleSingleton.init(FeatureToggleDummy)
            return listOf(Language.Nynorsk, Language.Bokmal, Language.English).flatMap { spraak ->
                (templates.hentAutobrevmaler() +
                        templates.hentRedigerbareMaler()
                        ).filter { filter.isEmpty() || filter.any { f -> it.kode.kode() == f.kode() } }
                    .map { Arguments.of(it.template, it.kode, letterDataFactory.create(it.template.letterDataType), spraak) }
            }
        }
    }

    abstract fun <T : Any> testPdf(
        template: LetterTemplate<LanguageSupport, T>,
        brevkode: Brevkode<*>,
        fixtures: T,
        spraak: Language,
    )

    abstract fun <T : Any> testHtml(
        template: LetterTemplate<LanguageSupport, T>,
        brevkode: Brevkode<*>,
        fixtures: T,
        spraak: Language,
    )

    protected fun <T: Any> renderPdf(
        template: LetterTemplate<LanguageSupport, T>,
        brevkode: Brevkode<*>,
        fixtures: T,
        spraak: Language,
    ) {
        if (!template.language.supports(spraak)) {
            println("Mal ${template.letterMetadata.displayTitle} med brevkode ${brevkode.kode()} fins ikke på språk ${spraak.javaClass.simpleName.lowercase()}, tester ikke denne")
            return
        }
        val letter = LetterTestImpl(template, fixtures, spraak, Fixtures.felles)

        letter.renderTestPDF(filnavn(brevkode, spraak))
    }

    protected fun <T : Any> renderHtml(
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
            Fixtures.felles,
        ).renderTestHtml(filnavn(brevkode, spraak))
    }

    private fun filnavn(brevkode: Brevkode<*>, spraak: Language) =
        "${brevkode.kode()}_${spraak.javaClass.simpleName}"


    @Test
    fun `alle maler skal bruke en unik brevkode`() {
        val malKoder = (templates.hentAutobrevmaler() + templates.hentRedigerbareMaler())
            .map { it.kode.kode() }

        malKoder.sorted().zipWithNext { a, b ->
            assert(a != b) { "Alle brevmaler må bruke egne unike brevkoder! Brevkode $a brukes i flere brev." }
        }
    }
}

object FeatureToggleDummy : FeatureToggleService {
    private val toggles: MutableMap<FeatureToggle, Boolean> = mutableMapOf()

    override fun isEnabled(toggle: FeatureToggle): Boolean = toggles[toggle] ?: false
    override fun verifiserAtAlleBrytereErDefinert(entries: List<FeatureToggle>) { }

}