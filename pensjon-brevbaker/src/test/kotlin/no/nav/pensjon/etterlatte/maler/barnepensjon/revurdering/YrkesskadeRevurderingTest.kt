package no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering

import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.Fixtures
import no.nav.pensjon.etterlatte.maler.EtterlatteMalTest
import org.junit.jupiter.api.Tag

@Tag(TestTags.INTEGRATION_TEST)
class YrkesskadeRevurderingTest : EtterlatteMalTest<BarnepensjonRevurderingYrkesskadeDTO>(
    YrkesskadeRevurdering.template,
    EtterlatteBrevKode.BARNEPENSJON_REVURDERING_YRKESSKADE,
    Fixtures.create<BarnepensjonRevurderingYrkesskadeDTO>(),
)
