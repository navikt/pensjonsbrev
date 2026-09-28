package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.Institusjon
import no.nav.pensjon.brev.api.model.MetaforceSivilstand
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sakstype.ALDER
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterOgPlikterDto

fun createOrienteringOmRettigheterOgPlikterDto(sakstype: Sakstype = ALDER) = OrienteringOmRettigheterOgPlikterDto(
    sakstype = sakstype,
    brukerBorINorge = true,
    institusjonsoppholdGjeldende = Institusjon.INGEN,
    sivilstand = MetaforceSivilstand.GIFT,
    borSammenMedBruker = true,
    epsPaInstitusjon = false,
    epsOppholdSykehjem = null,
    harBarnetillegg = false,
    brukerUnder18Aar = false,
)