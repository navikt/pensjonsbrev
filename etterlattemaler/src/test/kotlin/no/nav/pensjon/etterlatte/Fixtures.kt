package no.nav.pensjon.etterlatte

import no.nav.brev.brevbaker.LetterDataFactory
import no.nav.pensjon.etterlatte.fixtures.createAvvistKlageFerdigDTO
import no.nav.pensjon.etterlatte.fixtures.createAvvistKlageInnholdDTO
import no.nav.pensjon.etterlatte.fixtures.createBarnepensjonAvslagDTO
import no.nav.pensjon.etterlatte.fixtures.createBarnepensjonAvslagRedigerbartUtfallDTO
import no.nav.pensjon.etterlatte.fixtures.createBarnepensjonForeldreloesDTO
import no.nav.pensjon.etterlatte.fixtures.createBarnepensjonForeldreloesRedigerbarDTO
import no.nav.pensjon.etterlatte.fixtures.createBarnepensjonInformasjonDoedsfallDTO
import no.nav.pensjon.etterlatte.fixtures.createBarnepensjonInformasjonDoedsfallMellomAttenOgTjueVedReformtidspunktDTO
import no.nav.pensjon.etterlatte.fixtures.createBarnepensjonInformasjonMottattSoeknadDTO
import no.nav.pensjon.etterlatte.fixtures.createBarnepensjonInnhentingAvOpplysningerDTO
import no.nav.pensjon.etterlatte.fixtures.createBarnepensjonInnvilgelseDTO
import no.nav.pensjon.etterlatte.fixtures.createBarnepensjonInnvilgelseRedigerbartUtfallDTO
import no.nav.pensjon.etterlatte.fixtures.createBarnepensjonOmregnetNyttRegelverkDTO
import no.nav.pensjon.etterlatte.fixtures.createBarnepensjonOmregnetNyttRegelverkFerdigDTO
import no.nav.pensjon.etterlatte.fixtures.createBarnepensjonOpphoerDTO
import no.nav.pensjon.etterlatte.fixtures.createBarnepensjonOpphoerRedigerbartUtfallDTO
import no.nav.pensjon.etterlatte.fixtures.createBarnepensjonRevurderingDTO
import no.nav.pensjon.etterlatte.fixtures.createBarnepensjonRevurderingRedigerbartUtfallDTO
import no.nav.pensjon.etterlatte.fixtures.createBarnepensjonVarsel
import no.nav.pensjon.etterlatte.fixtures.createBarnepensjonVarselRedigerbartUtfall
import no.nav.pensjon.etterlatte.fixtures.createEtteroppgjoerBeregningVedleggRedigerbartUtfallBrevDTO
import no.nav.pensjon.etterlatte.fixtures.createEtteroppgjoerForhaandsvarselBrevDTO
import no.nav.pensjon.etterlatte.fixtures.createEtteroppgjoerForhaandsvarselRedigerbartBrevDTO
import no.nav.pensjon.etterlatte.fixtures.createEtteroppgjoerVedtakBrevDTO
import no.nav.pensjon.etterlatte.fixtures.createEtteroppgjoerVedtakRedigerbartUtfallBrevDTO
import no.nav.pensjon.etterlatte.fixtures.createKlageOversendelseBrukerDTO
import no.nav.pensjon.etterlatte.fixtures.createKlageSaksbehandlingstidDtoTestI
import no.nav.pensjon.etterlatte.fixtures.createManueltBrevDTO
import no.nav.pensjon.etterlatte.fixtures.createOmstillingsstoenadAktivitetspliktInformasjon10mndInnholdDTO
import no.nav.pensjon.etterlatte.fixtures.createOmstillingsstoenadAktivitetspliktInformasjon4mndInnholdDTO
import no.nav.pensjon.etterlatte.fixtures.createOmstillingsstoenadAktivitetspliktInformasjon6mndDto
import no.nav.pensjon.etterlatte.fixtures.createOmstillingsstoenadAvslagDTO
import no.nav.pensjon.etterlatte.fixtures.createOmstillingsstoenadAvslagRedigerbartUtfallDTO
import no.nav.pensjon.etterlatte.fixtures.createOmstillingsstoenadBeregningRedigerbartVedlegg
import no.nav.pensjon.etterlatte.fixtures.createOmstillingsstoenadInformasjonDoedsfallDto
import no.nav.pensjon.etterlatte.fixtures.createOmstillingsstoenadInnhentingAvOpplysningerDTO
import no.nav.pensjon.etterlatte.fixtures.createOmstillingsstoenadInntektsjusteringVedtakDTO
import no.nav.pensjon.etterlatte.fixtures.createOmstillingsstoenadInntektsjusteringVedtakRedigerbartUtfallDTO
import no.nav.pensjon.etterlatte.fixtures.createOmstillingsstoenadInnvilgelseDTO
import no.nav.pensjon.etterlatte.fixtures.createOmstillingsstoenadInnvilgelseRedigerbartUtfallDTO
import no.nav.pensjon.etterlatte.fixtures.createOmstillingsstoenadMotattSoekdnadDTO
import no.nav.pensjon.etterlatte.fixtures.createOmstillingsstoenadOpphoerDTO
import no.nav.pensjon.etterlatte.fixtures.createOmstillingsstoenadRevurderingDTO
import no.nav.pensjon.etterlatte.fixtures.createOmstillingsstoenadRevurderingRedigerbartUtfallDTO
import no.nav.pensjon.etterlatte.fixtures.createOmstillingsstoenadVarselAktivitetspliktDTO
import no.nav.pensjon.etterlatte.fixtures.createOmstillingsstoenadVarselAktivitetspliktRedigerbartUtfallDTO
import no.nav.pensjon.etterlatte.fixtures.createOmstillingsstoenadpphoerRedigerbartUtfallDTO
import no.nav.pensjon.etterlatte.fixtures.createTilbakekrevingFerdigDTO
import no.nav.pensjon.etterlatte.fixtures.createTilbakekrevingRedigerbartBrevDTO
import no.nav.pensjon.etterlatte.fixtures.createTomMal
import no.nav.pensjon.etterlatte.fixtures.createTomMalInformasjonsbrev
import no.nav.pensjon.etterlatte.fixtures.lagBeregning
import no.nav.pensjon.etterlatte.fixtures.lagTilbakekrevingDTO
import no.nav.pensjon.etterlatte.fixtures.vedlegg.lagBeregningsVedleggData
import no.nav.pensjon.etterlatte.fixtures.vedlegg.lagInformasjonOmOmstillingsstoenadData
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregning
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregning
import no.nav.pensjon.etterlatte.maler.andre.TomMal
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.lagOmstillingsstoenadBeregning
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingDTO
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.InformasjonOmOmstillingsstoenadData
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.BeregningsVedleggData
import kotlin.reflect.KClass
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.template.BrevTemplate
import no.nav.pensjon.etterlatte.maler.andre.*
import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.*
import no.nav.pensjon.etterlatte.maler.barnepensjon.informasjon.*
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.*
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.*
import no.nav.pensjon.etterlatte.maler.barnepensjon.opphoer.*
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.*
import no.nav.pensjon.etterlatte.maler.barnepensjon.varsel.*
import no.nav.pensjon.etterlatte.maler.klage.*
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.*
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.*
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.*
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon.*
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.*
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.*
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer.*
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.*
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.varsel.*
import no.nav.pensjon.etterlatte.maler.tilbakekreving.*
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.redigerbar.*
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.EtteroppgjoerBeregningVedleggRedigerbartUtfall
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.redigerbar.*

object Fixtures : LetterDataFactory {

    val felles = no.nav.brev.brevbaker.FellesFactory.felles

    @Suppress("UNCHECKED_CAST")
    override fun <T : BrevbakerBrevdata> create(templateType: KClass<out BrevTemplate<T, *>>): T =
        when (templateType) {
            BarnepensjonInnvilgelse::class -> createBarnepensjonInnvilgelseDTO() as T
            BarnepensjonInnvilgelseRedigerbartUfall::class -> createBarnepensjonInnvilgelseRedigerbartUtfallDTO() as T
            BarnepensjonInnvilgelseForeldreloes::class -> createBarnepensjonForeldreloesDTO() as T
            BarnepensjonInnvilgelseForeldreloesRedigerbartUfall::class -> createBarnepensjonForeldreloesRedigerbarDTO() as T
            BarnepensjonAvslag::class -> createBarnepensjonAvslagDTO() as T
            BarnepensjonOpphoer::class -> createBarnepensjonOpphoerDTO() as T
            BarnepensjonAvslagRedigerbartUtfall::class -> createBarnepensjonAvslagRedigerbartUtfallDTO() as T
            BarnepensjonOpphoerRedigerbartUtfall::class -> createBarnepensjonOpphoerRedigerbartUtfallDTO() as T
            BarnepensjonRevurdering::class -> createBarnepensjonRevurderingDTO() as T
            BarnepensjonRevurderingRedigerbartUtfall::class -> createBarnepensjonRevurderingRedigerbartUtfallDTO() as T
            EnkeltVedtakOmregningNyttRegelverk::class -> createBarnepensjonOmregnetNyttRegelverkDTO() as T
            ForhaandsvarselOmregningBP::class -> createBarnepensjonOmregnetNyttRegelverkDTO() as T
            EnkeltVedtakOmregningNyttRegelverkFerdig::class -> createBarnepensjonOmregnetNyttRegelverkFerdigDTO() as T
            BarnepensjonInformasjonDoedsfall::class -> createBarnepensjonInformasjonDoedsfallDTO() as T
            BarnepensjonInformasjonDoedsfallMellomAttenOgTjueVedReformtidspunkt::class -> createBarnepensjonInformasjonDoedsfallMellomAttenOgTjueVedReformtidspunktDTO() as T
            BarnepensjonMottattSoeknad::class -> createBarnepensjonInformasjonMottattSoeknadDTO() as T
            BarnepensjonInnhentingAvOpplysninger::class -> createBarnepensjonInnhentingAvOpplysningerDTO() as T
            BarnepensjonVarsel::class -> createBarnepensjonVarsel() as T
            BarnepensjonVarselRedigerbartUtfall::class -> createBarnepensjonVarselRedigerbartUtfall() as T
            OmstillingsstoenadInformasjonDoedsfall::class -> createOmstillingsstoenadInformasjonDoedsfallDto() as T
            OmstillingsstoenadMottattSoeknad::class -> createOmstillingsstoenadMotattSoekdnadDTO() as T
            OmstillingsstoenadInnhentingAvOpplysninger::class -> createOmstillingsstoenadInnhentingAvOpplysningerDTO() as T
            OmstillingsstoenadInnvilgelse::class -> createOmstillingsstoenadInnvilgelseDTO() as T
            OmstillingsstoenadInnvilgelseRedigerbartUtfall::class -> createOmstillingsstoenadInnvilgelseRedigerbartUtfallDTO() as T
            OmstillingsstoenadAvslag::class -> createOmstillingsstoenadAvslagDTO() as T
            OmstillingsstoenadAvslagRedigerbartUtfall::class -> createOmstillingsstoenadAvslagRedigerbartUtfallDTO() as T
            OmstillingsstoenadRevurdering::class -> createOmstillingsstoenadRevurderingDTO() as T
            OmstillingsstoenadRevurderingRedigerbartUtfall::class -> createOmstillingsstoenadRevurderingRedigerbartUtfallDTO() as T
            OmstillingsstoenadOpphoer::class -> createOmstillingsstoenadOpphoerDTO() as T
            OmstillingsstoenadOpphoerRedigerbartUtfall::class -> createOmstillingsstoenadpphoerRedigerbartUtfallDTO() as T
            OmstillingsstoenadAktivitetspliktInformasjon4mndInnhold::class -> createOmstillingsstoenadAktivitetspliktInformasjon4mndInnholdDTO() as T
            OmstillingsstoenadAktivitetspliktInformasjon10mndInnhold::class -> createOmstillingsstoenadAktivitetspliktInformasjon10mndInnholdDTO() as T
            OmstillingsstoenadAktivitetspliktInformasjon6mndInnhold::class -> createOmstillingsstoenadAktivitetspliktInformasjon6mndDto() as T
            OmstillingsstoenadVarselAktivitetsplikt::class -> createOmstillingsstoenadVarselAktivitetspliktDTO() as T
            OmstillingsstoenadVarselAktivitetspliktRedigerbartUtfall::class -> createOmstillingsstoenadVarselAktivitetspliktRedigerbartUtfallDTO() as T
            OmstillingsstoenadVedleggBeregningRedigerbartUtfall::class -> createOmstillingsstoenadBeregningRedigerbartVedlegg() as T
            OmstillingsstoenadInntektsjusteringVarsel::class -> createOmstillingsstoenadInntektsjusteringVedtakDTO() as T
            OmstillingsstoenadInntektsjusteringVedtak::class -> createOmstillingsstoenadInntektsjusteringVedtakDTO() as T
            OmstillingsstoenadInntektsjusteringVedtakRedigerbartUtfall::class -> createOmstillingsstoenadInntektsjusteringVedtakRedigerbartUtfallDTO() as T
            TilbakekrevingFerdig::class -> createTilbakekrevingFerdigDTO() as T
            TilbakekrevingInnhold::class -> createTilbakekrevingRedigerbartBrevDTO() as T
            EtteroppgjoerForhaandsvarsel::class -> createEtteroppgjoerForhaandsvarselBrevDTO() as T
            EtteroppgjoerForhaandsvarselInnhold::class -> createEtteroppgjoerForhaandsvarselRedigerbartBrevDTO() as T
            EtteroppgjoerBeregningVedleggRedigerbartUtfall::class -> createEtteroppgjoerBeregningVedleggRedigerbartUtfallBrevDTO() as T
            EtteroppgjoerVedtak::class -> createEtteroppgjoerVedtakBrevDTO() as T
            EtteroppgjoerVedtakRedigerbartUtfall::class -> createEtteroppgjoerVedtakRedigerbartUtfallBrevDTO() as T
            AvvistKlageInnhold::class -> createAvvistKlageInnholdDTO() as T
            KlageSaksbehandlingstid::class -> createKlageSaksbehandlingstidDtoTestI() as T
            AvvistKlageFerdigstilling::class -> createAvvistKlageFerdigDTO() as T
            KlageOversendelsesbrevBruker::class -> createKlageOversendelseBrukerDTO() as T
            BarnepensjonVedleggBeregningTrygdetidRedigerbartUtfall::class -> createManueltBrevDTO() as T
            BarnepensjonVedleggForhaandsvarselRedigerbartUtfall::class -> createManueltBrevDTO() as T
            OmstillingsstoenadVedleggForhaandsvarselRedigerbartUtfall::class -> createManueltBrevDTO() as T
            OmstillingsstoenadVarselRedigerbartUtfall::class -> createManueltBrevDTO() as T
            UtsattKlagefrist::class -> createManueltBrevDTO() as T
            TomDelmal::class -> createManueltBrevDTO() as T
            OmstillingsstoenadVarsel::class -> createTomMalInformasjonsbrev() as T
            TomMalInformasjonsbrev::class -> createTomMalInformasjonsbrev() as T
            TomMal::class -> createTomMal() as T
            else -> throw IllegalArgumentException("Don't know how to construct: ${templateType.qualifiedName}")
        }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> createVedlegg(letterDataType: KClass<T>): T = when(letterDataType) {
        BarnepensjonBeregning::class -> lagBeregning() as T
        BeregningsVedleggData::class -> lagBeregningsVedleggData() as T
        OmstillingsstoenadBeregning::class -> lagOmstillingsstoenadBeregning() as T
        InformasjonOmOmstillingsstoenadData::class -> lagInformasjonOmOmstillingsstoenadData() as T
        TilbakekrevingDTO::class -> lagTilbakekrevingDTO() as T
        else -> throw IllegalArgumentException("Don't know how to construct: ${letterDataType.qualifiedName}")
    }


}

