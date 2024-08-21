package no.nav.pensjon.etterlatte

import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.etterlatte.maler.andre.TomDelmal
import no.nav.pensjon.etterlatte.maler.andre.TomMal
import no.nav.pensjon.etterlatte.maler.andre.TomMalInformasjonsbrev
import no.nav.pensjon.etterlatte.maler.andre.UtsattKlagefrist
import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.BarnepensjonAvslag
import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.BarnepensjonAvslagRedigerbartUtfall
import no.nav.pensjon.etterlatte.maler.barnepensjon.informasjon.BarnepensjonInformasjonDoedsfall
import no.nav.pensjon.etterlatte.maler.barnepensjon.informasjon.BarnepensjonInformasjonDoedsfallMellomAttenOgTjueVedReformtidspunkt
import no.nav.pensjon.etterlatte.maler.barnepensjon.informasjon.BarnepensjonInnhentingAvOpplysninger
import no.nav.pensjon.etterlatte.maler.barnepensjon.informasjon.BarnepensjonMottattSoeknad
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonInnvilgelse
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonInnvilgelseForeldreloes
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonInnvilgelseForeldreloesRedigerbartUfall
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonInnvilgelseRedigerbartUfall
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.EnkeltVedtakOmregningNyttRegelverk
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.EnkeltVedtakOmregningNyttRegelverkFerdig
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.ForhaandsvarselOmregningBP
import no.nav.pensjon.etterlatte.maler.barnepensjon.opphoer.BarnepensjonOpphoer
import no.nav.pensjon.etterlatte.maler.barnepensjon.opphoer.BarnepensjonOpphoerRedigerbartUtfall
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurdering
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingRedigerbartUtfall
import no.nav.pensjon.etterlatte.maler.barnepensjon.varsel.BarnepensjonVarsel
import no.nav.pensjon.etterlatte.maler.barnepensjon.varsel.BarnepensjonVarselRedigerbartUtfall
import no.nav.pensjon.etterlatte.maler.klage.AvvistKlageFerdigstilling
import no.nav.pensjon.etterlatte.maler.klage.AvvistKlageInnhold
import no.nav.pensjon.etterlatte.maler.klage.BlankettKlageinstans
import no.nav.pensjon.etterlatte.maler.klage.KlageOversendelsesbrevBruker
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.OmstillingsstoenadAktivitetspliktInformasjon6mndInnhold
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.OmstillingsstoenadAktivitetspliktInformasjon4mndInnhold
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.OmstillingsstoenadAvslag
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.OmstillingsstoenadAvslagRedigerbartUtfall
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon.OmstillingsstoenadAktivitetspliktVarselInnhold
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon.OmstillingsstoenadInformasjonDoedsfall
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon.OmstillingsstoenadInnhentingAvOpplysninger
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon.OmstillingsstoenadMottattSoeknad
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelse
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseRedigerbartUtfall
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer.OmstillingsstoenadOpphoer
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer.OmstillingsstoenadOpphoerRedigerbartUtfall
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurdering
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingRedigerbartUtfall
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.varsel.OmstillingsstoenadVarsel
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.varsel.OmstillingsstoenadVarselAktivitetsplikt
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.varsel.OmstillingsstoenadVarselAktivitetspliktRedigerbartUtfall
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.varsel.OmstillingsstoenadVarselRedigerbartUtfall
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingFerdig
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingInnhold
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.redigerbar.BarnepensjonVedleggBeregningTrygdetidRedigerbartUtfall
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.redigerbar.BarnepensjonVedleggForhaandsvarselRedigerbartUtfall
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.redigerbar.OmstillingsstoenadVedleggBeregningRedigerbartUtfall
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.redigerbar.OmstillingsstoenadVedleggForhaandsvarselRedigerbartUtfall
val prodAutobrevTemplates: Set<EtterlatteTemplate<*>> =
    setOf(
        // Barnepensjon
        BarnepensjonAvslag,
        BarnepensjonAvslagRedigerbartUtfall,
        BarnepensjonInnvilgelse,
        BarnepensjonInnvilgelseRedigerbartUfall,
        BarnepensjonInnvilgelseForeldreloes,
        BarnepensjonInnvilgelseForeldreloesRedigerbartUfall,
        BarnepensjonOpphoer,
        BarnepensjonOpphoerRedigerbartUtfall,
        BarnepensjonRevurdering,
        BarnepensjonRevurderingRedigerbartUtfall,
        BarnepensjonVarsel,
        BarnepensjonVarselRedigerbartUtfall,
        BarnepensjonVedleggBeregningTrygdetidRedigerbartUtfall,
        BarnepensjonVedleggForhaandsvarselRedigerbartUtfall,
        BarnepensjonInformasjonDoedsfall,
        BarnepensjonInformasjonDoedsfallMellomAttenOgTjueVedReformtidspunkt,
        BarnepensjonMottattSoeknad,
        BarnepensjonInnhentingAvOpplysninger,
        // Omstillingsstønad
        OmstillingsstoenadAvslag,
        OmstillingsstoenadAvslagRedigerbartUtfall,
        OmstillingsstoenadInnvilgelse,
        OmstillingsstoenadInnvilgelseRedigerbartUtfall,
        OmstillingsstoenadOpphoer,
        OmstillingsstoenadOpphoerRedigerbartUtfall,
        OmstillingsstoenadRevurdering,
        OmstillingsstoenadRevurderingRedigerbartUtfall,
        OmstillingsstoenadVedleggBeregningRedigerbartUtfall,
        OmstillingsstoenadVedleggForhaandsvarselRedigerbartUtfall,
        OmstillingsstoenadVarsel,
        OmstillingsstoenadVarselRedigerbartUtfall,
        OmstillingsstoenadVarselAktivitetsplikt,
        OmstillingsstoenadVarselAktivitetspliktRedigerbartUtfall,
        OmstillingsstoenadInformasjonDoedsfall,
        OmstillingsstoenadMottattSoeknad,
        OmstillingsstoenadInnhentingAvOpplysninger,
        OmstillingsstoenadAktivitetspliktVarselInnhold,
        OmstillingsstoenadAktivitetspliktInformasjon4mndInnhold,
        OmstillingsstoenadAktivitetspliktInformasjon6mndInnhold,
        // Tilbakekreving
        TilbakekrevingInnhold,
        TilbakekrevingFerdig,
        // Klage
        AvvistKlageInnhold,
        AvvistKlageFerdigstilling,
        BlankettKlageinstans,
        KlageOversendelsesbrevBruker,
        // Informasjonsbrev
        TomMal,
        TomDelmal,
        TomMalInformasjonsbrev,
        UtsattKlagefrist,
        // Div migrering mm.
        ForhaandsvarselOmregningBP,
        EnkeltVedtakOmregningNyttRegelverk,
        EnkeltVedtakOmregningNyttRegelverkFerdig,
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

    fun getAutoBrev(): Set<EtterlatteBrevKode> = autoBrevMap.keys

    fun getAutoBrev(kode: EtterlatteBrevKode): LetterTemplate<*, *>? = autoBrevMap[kode]?.template

    fun getRedigerbareBrev(): Set<EtterlatteBrevKode> = redigerbareBrevMap.keys

    fun getRedigerbartBrev(kode: EtterlatteBrevKode): LetterTemplate<*, *>? = redigerbareBrevMap[kode]?.template
}
