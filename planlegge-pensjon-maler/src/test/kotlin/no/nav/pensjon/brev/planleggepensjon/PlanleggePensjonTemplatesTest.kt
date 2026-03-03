package no.nav.pensjon.brev.planleggepensjon

import no.nav.brev.brevbaker.BrevmodulTest

class PlanleggePensjonTemplatesTest : BrevmodulTest(
    templates = PlanleggePensjonTemplates,
    auto = PlanleggePensjonBrevkoder.AutoBrev.entries,
    redigerbare = PlanleggePensjonBrevkoder.Redigerbar.entries,
    fixtures = Fixtures,
    filterForPDF = listOf(),
)