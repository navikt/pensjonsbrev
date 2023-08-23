package no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering

import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.Fixtures
import no.nav.pensjon.etterlatte.maler.EtterlatteMalTest
import no.nav.pensjon.etterlatte.maler.barnepensjon.endring.BarnepensjonEndringInstitusjonsoppholdDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.endring.EndringInstitusjonsopphold

class InstitusjonsoppholdTest : EtterlatteMalTest<BarnepensjonEndringInstitusjonsoppholdDTO>(
    EndringInstitusjonsopphold.template,
    EtterlatteBrevKode.BARNEPENSJON_REVURDERING_INSTITUSJONSOPPHOLD,
    Fixtures.create<BarnepensjonEndringInstitusjonsoppholdDTO>(),
)
