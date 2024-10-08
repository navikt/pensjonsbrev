package no.nav.pensjon.etterlatte.maler.barnepensjon.migrering

import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregning
import no.nav.pensjon.etterlatte.maler.BarnepensjonEtterbetaling
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.FerdigstillingBrevDTO

data class BarnepensjonOmregnetNyttRegelverkDTO(
    val utbetaltFoerReform: Kroner,
    val utbetaltEtterReform: Kroner,
    val erForeldreloes: Boolean,
    val erBosattUtlandet: Boolean,
)

data class BarnepensjonOmregnetNyttRegelverkFerdigDTO(
    override val innhold: List<Element>,
    val beregning: BarnepensjonBeregning,
    val frivilligSkattetrekk: Boolean,
    val etterbetaling: BarnepensjonEtterbetaling?,
    val erUnder18Aar: Boolean,
    val erBosattUtlandet: Boolean,
) : FerdigstillingBrevDTO