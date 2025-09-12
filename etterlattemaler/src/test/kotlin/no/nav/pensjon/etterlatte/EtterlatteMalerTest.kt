package no.nav.pensjon.etterlatte

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class EtterlatteMalerTest {

    @Test
    fun `alle maler med brevdata har annotasjon som gjoer at vi genererer selectors`() {
        (EtterlatteMaler.hentAutobrevmaler() + EtterlatteMaler.hentRedigerbareMaler())
            .map { it.template }
            .filterNot { it.template.letterDataType in setOf(EmptyBrevdata::class, EmptyRedigerbarBrevdata::class) }
            .forEach {
                assertTrue(
                    it.javaClass.declaredAnnotations.any { annotation -> annotation.annotationClass == TemplateModelHelpers::class },
                    "Alle maler annoteres med @TemplateModelHelpers, for å få generert selectors. Det har ikke ${it.javaClass.simpleName}"
                )
            }
    }
}