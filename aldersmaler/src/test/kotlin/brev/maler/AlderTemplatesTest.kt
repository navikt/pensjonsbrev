package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.AlderTemplates
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.model.alder.Aldersbrevkoder
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brevbaker.api.model.DisplayText
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.reflect.KProperty

class AlderTemplatesTest {
    @Test
    fun `alle autobrev fins i templates`() {
        val brukteKoder = AlderTemplates.hentAutobrevmaler().map { it.kode }
        val ubrukteKoder = Aldersbrevkoder.AutoBrev.entries.filterNot { brukteKoder.contains(it) }
        Assertions.assertEquals(ubrukteKoder, listOf<Brevkode.Automatisk>())
    }

    @Test
    fun `alle redigerbare brev fins i templates`() {
        val brukteKoder = AlderTemplates.hentRedigerbareMaler().map { it.kode }
        val ubrukteKoder = Aldersbrevkoder.Redigerbar.entries.filterNot { brukteKoder.contains(it) }
        Assertions.assertEquals(ubrukteKoder, listOf<Brevkode.Redigerbart>())
    }

    @Test
    fun `alle redigerbare brev har displaytext for alle saksbehandlervalg`() {
        AlderTemplates.hentRedigerbareMaler().map { mal ->
            val clazz = mal.template.letterDataType.java
            val saksbehandlervalg =
                clazz.declaredFields
                    .map { it.type }
                    .filter { field -> SaksbehandlerValgBrevdata::class.java.isAssignableFrom(field) }
                    .map { it.kotlin }
            saksbehandlervalg
                .flatMap { it.members }
                .filter { it is KProperty<*> }
                .forEach { field ->
                    val hasDisplayText = field.annotations.filterIsInstance<DisplayText>().any()
                    assertTrue(
                        hasDisplayText,
                        "Alle saksbehandlervalg må ha displaytext, ${field.name} i klasse ${clazz.name} mangler det",
                    )
                }
        }
    }

    @Test
    fun `alle maler med brevdata har annotasjon som gjoer at vi genererer selectors`() {
        (AlderTemplates.hentAutobrevmaler() + AlderTemplates.hentRedigerbareMaler())
            .filterNot {
                it.template.letterDataType in
                    setOf(
                        EmptyBrevdata::class,
                        EmptyRedigerbarBrevdata::class,
                    )
            }.forEach {
                assertTrue(
                    it.javaClass.declaredAnnotations.any { annotation -> annotation.annotationClass == TemplateModelHelpers::class },
                    "Alle maler annoteres med @TemplateModelHelpers, for å få generert selectors. Det har ikke ${it.javaClass.simpleName}",
                )
            }
    }

    // Dette er ei hypotese vi på brevteamet har. Oppdater eller fjern testen hvis dere skulle finne et scenario hvor hypotesa ikke holder
    @Test
    fun `brev som er deklarert med brevtype vedtaksbrev skal ha brevkontekst vedtak`() {
        assertEquals(
            emptyList<String>(),
            AlderTemplates
                .hentRedigerbareMaler()
                .filter { it.template.letterMetadata.brevtype == LetterMetadata.Brevtype.VEDTAKSBREV }
                .filterNot { it.brevkontekst == TemplateDescription.Brevkontekst.VEDTAK }
                .map { it.javaClass.simpleName },
        )
    }
}
