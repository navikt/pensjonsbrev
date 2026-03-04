package no.nav.pensjon.brev.planleggepensjon

import no.nav.brev.brevbaker.BrevmodulTest
import no.nav.pensjon.brev.planleggepensjon.simulering.ApSimuleringBrev

class PlanleggePensjonTemplatesTest : BrevmodulTest(
    templates = PlanleggePensjonTemplates,
    auto = PlanleggePensjonBrevkoder.AutoBrev.entries,
    redigerbare = PlanleggePensjonBrevkoder.Redigerbar.entries,
    fixtures = Fixtures,
    filterForPDF = listOf(ApSimuleringBrev.kode),
)