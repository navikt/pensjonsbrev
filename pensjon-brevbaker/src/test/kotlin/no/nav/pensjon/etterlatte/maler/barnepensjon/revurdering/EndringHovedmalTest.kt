package no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering

import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.Fixtures
import no.nav.pensjon.etterlatte.maler.EtterlatteMalTest
import no.nav.pensjon.etterlatte.maler.barnepensjon.endring.Endring
import no.nav.pensjon.etterlatte.maler.barnepensjon.endring.EndringHovedmalDTO

class EndringHovedmalTest : EtterlatteMalTest<EndringHovedmalDTO>(
    Endring.template,
    EtterlatteBrevKode.BARNEPENSJON_REVURDERING_ENDRING,
    Fixtures.create<EndringHovedmalDTO>(),
)
