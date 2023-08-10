package no.nav.pensjon.etterlatte

import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.etterlatte.maler.barnepensjon.BarnepensjonInnvilgelse
import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.AvslagFoerstegangsbehandling
import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.IkkeSomFoelgeAvYrkesskadeYrkessykdom
import no.nav.pensjon.etterlatte.maler.barnepensjon.endring.Endring
import no.nav.pensjon.etterlatte.maler.barnepensjon.endring.UtAvFengselsopphold
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.AdopsjonRevurdering
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.Fengselsopphold
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.HarStanset
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.OmgjoeringAvFarskapRevurdering
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.OpphoerRevurdering
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.SoeskenjusteringRevurdering
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.OMSInnvilgelseAuto
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.OMSInnvilgelseManuell
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.OMSOpphoerManuell

val prodAutobrevTemplates: Set<EtterlatteTemplate<*>> =
    setOf(
        AvslagFoerstegangsbehandling,
        IkkeSomFoelgeAvYrkesskadeYrkessykdom,
        BarnepensjonInnvilgelse,
        OMSInnvilgelseAuto,
        OMSInnvilgelseManuell,
        OMSOpphoerManuell,
        AdopsjonRevurdering,
        Endring,
        Fengselsopphold,
        HarStanset,
        OmgjoeringAvFarskapRevurdering,
        OpphoerRevurdering,
        SoeskenjusteringRevurdering,
        UtAvFengselsopphold,
    )

val prodRedigerbareTemplates: Set<EtterlatteTemplate<*>> = emptySet()

class TemplateResource(
    autobrevTemplates: Set<EtterlatteTemplate<*>> = prodAutobrevTemplates,
    redigerbareTemplates: Set<EtterlatteTemplate<*>> = prodRedigerbareTemplates,
) {
    private val autoBrevMap: Map<EtterlatteBrevKode, EtterlatteTemplate<*>> =
        autobrevTemplates.associateBy { it.kode }

    private val redigerbareBrevMap: Map<EtterlatteBrevKode, EtterlatteTemplate<*>> =
        redigerbareTemplates.associateBy { it.kode }

    fun getAutoBrev(): Set<EtterlatteBrevKode> =
        autoBrevMap.keys

    fun getAutoBrev(kode: EtterlatteBrevKode): LetterTemplate<*, *>? =
        autoBrevMap[kode]?.template

    fun getRedigerbareBrev(): Set<EtterlatteBrevKode> =
        redigerbareBrevMap.keys

    fun getRedigerbartBrev(kode: EtterlatteBrevKode): LetterTemplate<*, *>? =
        redigerbareBrevMap[kode]?.template
}
