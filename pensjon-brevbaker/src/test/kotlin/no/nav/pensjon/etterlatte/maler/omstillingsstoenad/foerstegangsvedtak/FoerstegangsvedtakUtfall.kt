package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.foerstegangsvedtak

import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.render.*
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.Fixtures
import no.nav.pensjon.etterlatte.maler.*
import org.junit.jupiter.api.Tag

@Tag(TestTags.INTEGRATION_TEST)
class OMSFoerstegangsvedtakUtfallITest : EtterlatteMalTest<FoerstegangsvedtakUtfallDTO>(
    FoerstegangsvedtakUtfall.template,
    EtterlatteBrevKode.OMS_FOERSTEGANGSVEDTAK_INNVILGELSE_UTFALL,
    Fixtures.create<FoerstegangsvedtakUtfallDTO>(),
)