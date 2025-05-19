package no.nav.pensjon.etterlatte

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
import no.nav.pensjon.etterlatte.fixtures.createKlageOversendelseBlankettDTO
import no.nav.pensjon.etterlatte.fixtures.createKlageOversendelseBrukerDTO
import no.nav.pensjon.etterlatte.fixtures.createKlageSaksbehandlingstidDtoTestI
import no.nav.pensjon.etterlatte.fixtures.createManueltBrevDTO
import no.nav.pensjon.etterlatte.fixtures.createOmstillingsstoenadAktivitetspliktInformasjon10mndInnholdDTO
import no.nav.pensjon.etterlatte.fixtures.createOmstillingsstoenadAktivitetspliktInformasjon4mndInnholdDTO
import no.nav.pensjon.etterlatte.fixtures.createOmstillingsstoenadAktivitetspliktInformasjon6mndDto
import no.nav.pensjon.etterlatte.fixtures.createOmstillingsstoenadAvslagDTO
import no.nav.pensjon.etterlatte.fixtures.createOmstillingsstoenadAvslagRedigerbartUtfallDTO
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
import no.nav.pensjon.etterlatte.maler.ManueltBrevDTO
import no.nav.pensjon.etterlatte.maler.ManueltBrevMedTittelDTO
import no.nav.pensjon.etterlatte.maler.andre.TomMal
import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.BarnepensjonAvslagDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.BarnepensjonAvslagRedigerbartUtfallDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.informasjon.BarnepensjonInformasjonDoedsfallDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.informasjon.BarnepensjonInformasjonDoedsfallMellomAttenOgTjueVedReformtidspunktDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.informasjon.BarnepensjonInnhentingAvOpplysningerDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.informasjon.BarnepensjonMottattSoeknadDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesRedigerbarDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonInnvilgelseDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonInnvilgelseRedigerbartUtfallDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkFerdigDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.opphoer.BarnepensjonOpphoerDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.opphoer.BarnepensjonOpphoerRedigerbartUtfallDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingRedigerbartUtfallDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.varsel.BarnepensjonVarselDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.varsel.BarnepensjonVarselRedigerbartUtfallDTO
import no.nav.pensjon.etterlatte.maler.klage.AvvistKlageFerdigDTO
import no.nav.pensjon.etterlatte.maler.klage.AvvistKlageInnholdDTO
import no.nav.pensjon.etterlatte.maler.klage.KlageOversendelseBlankettDTO
import no.nav.pensjon.etterlatte.maler.klage.KlageOversendelseBrukerDTO
import no.nav.pensjon.etterlatte.maler.klage.KlageSaksbehandlingstidDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.OmstillingsstoenadAktivitetspliktInformasjon10mndInnholdDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.OmstillingsstoenadAktivitetspliktInformasjon4mndInnholdDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.OmstillingsstoenadAktivitetspliktInformasjon6mndInnholdDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.OmstillingstoenadAvslagDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.OmstillingstoenadAvslagRedigerbartUtfallDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerForhaandsvarselBrevDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerForhaandsvarselRedigerbartBrevDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerVedtakBrevDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerVedtakRedigerbartUtfallBrevDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon.OmstillingsstoenadInnhentingAvOpplysningerDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon.OmstillingsstoenadMottattSoeknadDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon.OmstillingstoenadInformasjonDoedsfallDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.AarligInntektsjusteringVedtakDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.OmstillingsstoenadVedtakInntektsjusteringRedigerbartUtfallDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseRedigerbartUtfallDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer.OmstillingsstoenadOpphoerDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer.OmstillingsstoenadOpphoerRedigerbartUtfallDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingRedigerbartUtfallDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.varsel.OmstillingsstoenadVarselAktivitetspliktDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.varsel.OmstillingsstoenadVarselAktivitetspliktRedigerbartUtfallDTO
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBrevDTO
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingRedigerbartBrevDTO
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.EtteroppgjoerBeregningVedleggRedigerbartUtfallBrevDTO
import kotlin.reflect.KClass

object Fixtures {

    val felles = no.nav.brev.brevbaker.Fixtures.felles

    inline fun <reified T : Any> create(): T = create(T::class)

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> create(letterDataType: KClass<T>): T =
        when (letterDataType) {
            BarnepensjonInnvilgelseDTO::class -> createBarnepensjonInnvilgelseDTO() as T
            BarnepensjonInnvilgelseRedigerbartUtfallDTO::class -> createBarnepensjonInnvilgelseRedigerbartUtfallDTO() as T
            BarnepensjonForeldreloesDTO::class -> createBarnepensjonForeldreloesDTO() as T
            BarnepensjonForeldreloesRedigerbarDTO::class -> createBarnepensjonForeldreloesRedigerbarDTO() as T
            BarnepensjonAvslagDTO::class -> createBarnepensjonAvslagDTO() as T
            BarnepensjonOpphoerDTO::class -> createBarnepensjonOpphoerDTO() as T
            BarnepensjonAvslagRedigerbartUtfallDTO::class -> createBarnepensjonAvslagRedigerbartUtfallDTO() as T
            BarnepensjonOpphoerRedigerbartUtfallDTO::class -> createBarnepensjonOpphoerRedigerbartUtfallDTO() as T
            BarnepensjonRevurderingDTO::class -> createBarnepensjonRevurderingDTO() as T
            BarnepensjonRevurderingRedigerbartUtfallDTO::class -> createBarnepensjonRevurderingRedigerbartUtfallDTO() as T
            BarnepensjonOmregnetNyttRegelverkDTO::class -> createBarnepensjonOmregnetNyttRegelverkDTO() as T
            BarnepensjonOmregnetNyttRegelverkFerdigDTO::class -> createBarnepensjonOmregnetNyttRegelverkFerdigDTO() as T
            BarnepensjonInformasjonDoedsfallDTO::class -> createBarnepensjonInformasjonDoedsfallDTO() as T
            BarnepensjonInformasjonDoedsfallMellomAttenOgTjueVedReformtidspunktDTO::class -> createBarnepensjonInformasjonDoedsfallMellomAttenOgTjueVedReformtidspunktDTO() as T
            BarnepensjonMottattSoeknadDTO::class -> createBarnepensjonInformasjonMottattSoeknadDTO() as T
            BarnepensjonInnhentingAvOpplysningerDTO::class -> createBarnepensjonInnhentingAvOpplysningerDTO() as T

            BarnepensjonVarselDTO::class -> createBarnepensjonVarsel() as T
            BarnepensjonVarselRedigerbartUtfallDTO::class -> createBarnepensjonVarselRedigerbartUtfall() as T

            OmstillingstoenadInformasjonDoedsfallDTO::class -> createOmstillingsstoenadInformasjonDoedsfallDto() as T
            OmstillingsstoenadMottattSoeknadDTO::class -> createOmstillingsstoenadMotattSoekdnadDTO() as T
            OmstillingsstoenadInnhentingAvOpplysningerDTO::class -> createOmstillingsstoenadInnhentingAvOpplysningerDTO() as T
            OmstillingsstoenadInnvilgelseDTO::class -> createOmstillingsstoenadInnvilgelseDTO() as T
            OmstillingsstoenadInnvilgelseRedigerbartUtfallDTO::class -> createOmstillingsstoenadInnvilgelseRedigerbartUtfallDTO() as T
            OmstillingstoenadAvslagDTO::class -> createOmstillingsstoenadAvslagDTO() as T
            OmstillingstoenadAvslagRedigerbartUtfallDTO::class -> createOmstillingsstoenadAvslagRedigerbartUtfallDTO() as T
            OmstillingsstoenadRevurderingDTO::class -> createOmstillingsstoenadRevurderingDTO() as T
            OmstillingsstoenadRevurderingRedigerbartUtfallDTO::class -> createOmstillingsstoenadRevurderingRedigerbartUtfallDTO() as T
            OmstillingsstoenadOpphoerDTO::class -> createOmstillingsstoenadOpphoerDTO() as T
            OmstillingsstoenadOpphoerRedigerbartUtfallDTO::class -> createOmstillingsstoenadpphoerRedigerbartUtfallDTO() as T
            OmstillingsstoenadAktivitetspliktInformasjon4mndInnholdDTO::class -> createOmstillingsstoenadAktivitetspliktInformasjon4mndInnholdDTO() as T
            OmstillingsstoenadAktivitetspliktInformasjon10mndInnholdDTO::class -> createOmstillingsstoenadAktivitetspliktInformasjon10mndInnholdDTO() as T
            OmstillingsstoenadAktivitetspliktInformasjon6mndInnholdDTO::class -> createOmstillingsstoenadAktivitetspliktInformasjon6mndDto() as T
            OmstillingsstoenadVarselAktivitetspliktDTO::class -> createOmstillingsstoenadVarselAktivitetspliktDTO() as T
            OmstillingsstoenadVarselAktivitetspliktRedigerbartUtfallDTO::class -> createOmstillingsstoenadVarselAktivitetspliktRedigerbartUtfallDTO() as T

            // Inntektsjustering
            AarligInntektsjusteringVedtakDTO::class -> createOmstillingsstoenadInntektsjusteringVedtakDTO() as T
            OmstillingsstoenadVedtakInntektsjusteringRedigerbartUtfallDTO::class -> createOmstillingsstoenadInntektsjusteringVedtakRedigerbartUtfallDTO() as T

            //TilbakekrevingBrevDTO::class -> createTilbakekrevingFerdigDTO() as T
            TilbakekrevingBrevDTO::class -> createTilbakekrevingFerdigDTO() as T
            TilbakekrevingRedigerbartBrevDTO::class -> createTilbakekrevingRedigerbartBrevDTO() as T

            // Etteroppgjør
            EtteroppgjoerForhaandsvarselBrevDTO::class -> createEtteroppgjoerForhaandsvarselBrevDTO() as T
            EtteroppgjoerForhaandsvarselRedigerbartBrevDTO::class -> createEtteroppgjoerForhaandsvarselRedigerbartBrevDTO() as T
            EtteroppgjoerBeregningVedleggRedigerbartUtfallBrevDTO::class -> createEtteroppgjoerBeregningVedleggRedigerbartUtfallBrevDTO() as T
            EtteroppgjoerVedtakBrevDTO::class -> createEtteroppgjoerVedtakBrevDTO() as T
            EtteroppgjoerVedtakRedigerbartUtfallBrevDTO::class -> createEtteroppgjoerVedtakRedigerbartUtfallBrevDTO() as T

            AvvistKlageInnholdDTO::class -> createAvvistKlageInnholdDTO() as T
            KlageSaksbehandlingstidDTO::class -> createKlageSaksbehandlingstidDtoTestI() as T
            AvvistKlageFerdigDTO::class -> createAvvistKlageFerdigDTO() as T
            KlageOversendelseBlankettDTO::class -> createKlageOversendelseBlankettDTO() as T
            KlageOversendelseBrukerDTO::class -> createKlageOversendelseBrukerDTO() as T

            ManueltBrevDTO::class -> createManueltBrevDTO() as T
            ManueltBrevMedTittelDTO::class -> createTomMalInformasjonsbrev() as T
            TomMal::class -> createTomMal() as T
            Unit::class -> Unit as T

            else -> throw IllegalArgumentException("Don't know how to construct: ${letterDataType.qualifiedName}")
        }



}

