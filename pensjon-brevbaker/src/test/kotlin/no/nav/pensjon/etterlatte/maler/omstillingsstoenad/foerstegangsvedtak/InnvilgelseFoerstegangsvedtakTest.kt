package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.foerstegangsvedtak

import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.Fixtures
import no.nav.pensjon.etterlatte.maler.EtterlatteMalTest
import org.junit.jupiter.api.Tag

@Tag(TestTags.INTEGRATION_TEST)
class OMSInnvilgelseFoerstegangsvedtakITest : EtterlatteMalTest<OMSInnvilgelseFoerstegangsvedtakDTO>(
    InnvilgelseFoerstegangsvedtak.template,
    EtterlatteBrevKode.OMS_FOERSTEGANGSVEDTAK_INNVILGELSE,
    Fixtures.create<OMSInnvilgelseFoerstegangsvedtakDTO>(),
)