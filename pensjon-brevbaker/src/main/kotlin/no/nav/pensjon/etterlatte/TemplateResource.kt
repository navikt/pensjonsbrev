package no.nav.pensjon.etterlatte

import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.etterlatte.maler.TomMal
import no.nav.pensjon.etterlatte.maler.andre.TomDelmal
import no.nav.pensjon.etterlatte.maler.andre.TomMalInformasjonsbrev
import no.nav.pensjon.etterlatte.maler.andre.UtsattKlagefrist
import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.BarnepensjonAvslag
import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.BarnepensjonAvslagRedigerbartUtfall
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonInnvilgelse
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonInnvilgelseRedigerbartUfall
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.EnkeltVedtakOmregningNyttRegelverk
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.EnkeltVedtakOmregningNyttRegelverkFerdig
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.ForhaandsvarselOmregningBP
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.AdopsjonRevurdering
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonOpphoerRevurdering
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurdering
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.OmgjoeringAvFarskapRevurdering
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.SoeskenjusteringRevurdering
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.OmstillingsstoenadAvslag
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.OmstillingsstoenadAvslagRedigerbartUtfall
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelse
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseRedigerbartUtfall
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadOpphoer
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadOpphoerRedigerbartUtfall
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurdering
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingFerdig
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingInnhold

val prodAutobrevTemplates: Set<EtterlatteTemplate<*>> =
    setOf(
        // Barnepensjon
        BarnepensjonAvslag,
        BarnepensjonAvslagRedigerbartUtfall,
        BarnepensjonInnvilgelse,
        BarnepensjonInnvilgelseRedigerbartUfall,
        BarnepensjonRevurdering,
        BarnepensjonOpphoerRevurdering,
        AdopsjonRevurdering,
        OmgjoeringAvFarskapRevurdering,
        SoeskenjusteringRevurdering,

        // Omstillingsstønad
        OmstillingsstoenadAvslag,
        OmstillingsstoenadAvslagRedigerbartUtfall,
        OmstillingsstoenadInnvilgelse,
        OmstillingsstoenadInnvilgelseRedigerbartUtfall,
        OmstillingsstoenadOpphoer,
        OmstillingsstoenadOpphoerRedigerbartUtfall,
        OmstillingsstoenadRevurdering,

        // Tilbakekreving
        TilbakekrevingInnhold,
        TilbakekrevingFerdig,

        // Informasjonsbrev
        TomMal,
        TomDelmal,
        TomMalInformasjonsbrev,
        UtsattKlagefrist,

        // Div migrering mm.
        ForhaandsvarselOmregningBP,
        EnkeltVedtakOmregningNyttRegelverk,
        EnkeltVedtakOmregningNyttRegelverkFerdig
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
