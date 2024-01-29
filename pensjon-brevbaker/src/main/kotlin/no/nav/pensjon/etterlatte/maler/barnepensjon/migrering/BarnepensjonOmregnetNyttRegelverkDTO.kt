package no.nav.pensjon.etterlatte.maler.barnepensjon.migrering

import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregning
import no.nav.pensjon.etterlatte.maler.BarnepensjonEtterbetaling
import no.nav.pensjon.etterlatte.maler.BrevDTO
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.IntBroek

data class BarnepensjonOmregnetNyttRegelverkDTO(
    val utbetaltFoerReform: Kroner,
    val utbetaltEtterReform: Kroner,
    val grunnbeloep: Kroner,
    val anvendtTrygdetid: Int,
    val prorataBroek: IntBroek?,
    val erBosattUtlandet: Boolean,
    val erYrkesskade: Boolean,
    val erForeldreloes: Boolean,
    val erUnder18Aar: Boolean,
    val beregning: BarnepensjonBeregning,
    val etterbetaling: BarnepensjonEtterbetaling?,
)

data class BarnepensjonOmregnetNyttRegelverkFerdigDTO(
    override val innhold: List<Element>,
    val data: BarnepensjonOmregnetNyttRegelverkDTO
) : BrevDTO