package no.nav.pensjon.brev.api.model.maler.vedlegg

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.Institusjon
import no.nav.pensjon.brev.api.model.maler.MetaforceSivilstand

data class OrienteringOmRettigheterOgPlikterDto(
    val sakstype: Sakstype,
    val brukerBorINorge: Boolean,
    val institusjonsoppholdGjeldende: Institusjon,
    val sivilstand: MetaforceSivilstand,
    val borSammenMedBruker: Boolean,
    val epsPaInstitusjon: Boolean,
    val epsOppholdSykehjem: Boolean?,
    val harBarnetillegg: Boolean?,
    val brukerUnder18Aar: Boolean?,
)


