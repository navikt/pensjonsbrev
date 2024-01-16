package no.nav.pensjon.etterlatte

import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.etterlatte.maler.TomMal
import no.nav.pensjon.etterlatte.maler.andre.TomDelmal
import no.nav.pensjon.etterlatte.maler.andre.TomMalInformasjonsbrev
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonInnvilgelseMVP
import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.AvslagFoerstegangsbehandling
import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.AvslagFoerstegangsbehandlingEnkel
import no.nav.pensjon.etterlatte.maler.barnepensjon.endring.Endring
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonInnvilgelseEnkel
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.EnkeltVedtakOmregningNyttRegelverk
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.EnkeltVedtakOmregningNyttRegelverkFerdig
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.ForhaandsvarselOmregningBP
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonInnvilgelseNy
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.AdopsjonRevurdering
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.OmgjoeringAvFarskapRevurdering
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.OpphoerRevurdering
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.SoeskenjusteringRevurdering
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.OMSOpphoerManuell
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.OMSAvslag
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.OMSAvslagBegrunnelse
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseRedigerbartUtfall
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelse
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer.Opphoer
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer.OpphoerGenerell
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.RevurderingEndring
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingFerdig
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingInnhold

val prodAutobrevTemplates: Set<EtterlatteTemplate<*>> =
    setOf(
        // Barnepensjon
        AvslagFoerstegangsbehandling,
        AvslagFoerstegangsbehandlingEnkel,
        BarnepensjonInnvilgelseMVP,
        BarnepensjonInnvilgelseEnkel,
        BarnepensjonInnvilgelseNy,
        AdopsjonRevurdering,
        Endring,
        OmgjoeringAvFarskapRevurdering,
        OpphoerRevurdering,
        SoeskenjusteringRevurdering,

        // Omstillingsstønad
        OMSAvslag,
        OMSAvslagBegrunnelse,
        OmstillingsstoenadInnvilgelse,
        OmstillingsstoenadInnvilgelseRedigerbartUtfall,
        OMSOpphoerManuell,
        RevurderingEndring,
        Opphoer,
        OpphoerGenerell,

        // Tilbakekreving
        TilbakekrevingInnhold,
        TilbakekrevingFerdig,

        // Div migrering mm.
        TomMal,
        TomDelmal,
        TomMalInformasjonsbrev,
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
