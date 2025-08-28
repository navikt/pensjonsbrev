package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ProductionTemplatesTest {

    @Test
    fun `alle autobrev fins i templates`() {
        val brukteKoder = ProductionTemplates.hentAutobrevmaler().map { it.kode }
        val ubrukteKoder = Pesysbrevkoder.AutoBrev.entries.filterNot { brukteKoder.contains(it) }
        Assertions.assertEquals(ubrukteKoder, listOf<Brevkode.Automatisk>())
    }

    @Test
    fun `alle redigerbare brev fins i templates`() {
        val brukteKoder = ProductionTemplates.hentRedigerbareMaler().map { it.kode }
        val ubrukteKoder = Pesysbrevkoder.Redigerbar.entries.filterNot { brukteKoder.contains(it) }
        Assertions.assertEquals(ubrukteKoder, listOf<Brevkode.Redigerbart>())
    }

    @Test
    fun `alle maler med brevdata har annotasjon som gjoer at vi genererer selectors`() {
        (ProductionTemplates.hentAutobrevmaler() + ProductionTemplates.hentRedigerbareMaler())
            .filterNot { it.template.letterDataType in setOf(EmptyBrevdata::class, EmptyRedigerbarBrevdata::class) }
            .forEach {
                assertTrue(
                    it.javaClass.declaredAnnotations.any { annotation -> annotation.annotationClass == TemplateModelHelpers::class },
                    "Alle maler annoteres med @TemplateModelHelpers, for å få generert selectors. Det har ikke ${it.javaClass.simpleName}"
                )
            }
    }

    @Test
    fun `brev som er deklarert med brevtype vedtaksbrev skal ha brevkontekst vedtak`() {
        assertEquals(
            emptyList<String>(),
            ProductionTemplates.hentRedigerbareMaler()
                .filter { it.template.letterMetadata.brevtype == LetterMetadata.Brevtype.VEDTAKSBREV }
                .filterNot { it.brevkontekst == TemplateDescription.Brevkontekst.VEDTAK }
                .map { it.javaClass.simpleName }
            ,
        )
    }
}