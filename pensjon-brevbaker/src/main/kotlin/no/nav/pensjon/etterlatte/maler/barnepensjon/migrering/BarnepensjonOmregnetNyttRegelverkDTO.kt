package no.nav.pensjon.etterlatte.maler.barnepensjon.migrering

import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.IntBroek
import no.nav.pensjon.etterlatte.maler.BrevDTO
import no.nav.pensjon.etterlatte.maler.Element

data class BarnepensjonOmregnetNyttRegelverkDTO(
    val utbetaltFoerReform: Kroner,
    val utbetaltEtterReform: Kroner,
    val grunnbeloep: Kroner,
    val anvendtTrygdetid: Int,
    val prorataBroek: IntBroek?,
    val erBosattUtlandet: Boolean,
    val erYrkesskade: Boolean
)

data class BarnepensjonOmregnetNyttRegelverkFerdigDTO(
    override val innhold: List<Element>,
    val data: BarnepensjonOmregnetNyttRegelverkDTO
) : BrevDTO