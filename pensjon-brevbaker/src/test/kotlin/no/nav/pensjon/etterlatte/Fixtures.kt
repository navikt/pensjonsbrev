package no.nav.pensjon.etterlatte

import no.nav.pensjon.brevbaker.api.model.Bruker
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.NAVEnhet
import no.nav.pensjon.brevbaker.api.model.SignerendeSaksbehandlere
import no.nav.pensjon.brevbaker.api.model.Telefonnummer
import no.nav.pensjon.etterlatte.fixtures.*
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
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.OmstillingsstoenadAktivitetspliktInformasjon10mndInnholdDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.OmstillingsstoenadAktivitetspliktInformasjon4mndInnholdDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.OmstillingsstoenadAktivitetspliktInformasjon6mndInnholdDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.OmstillingstoenadAvslagDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.OmstillingstoenadAvslagRedigerbartUtfallDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon.OmstillingsstoenadAktivitetspliktVarselbrevInnholdDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon.OmstillingsstoenadInnhentingAvOpplysningerDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon.OmstillingsstoenadMottattSoeknadDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon.OmstillingstoenadInformasjonDoedsfallDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.*
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
import java.time.LocalDate
import kotlin.reflect.KClass

object Fixtures {
    val felles =
        Felles(
            dokumentDato = LocalDate.of(2020, 1, 1),
            saksnummer = "1337123",
            avsenderEnhet =
                NAVEnhet(
                    nettside = "nav.no",
                    navn = "Nav familie- og pensjonsytelser Porsgrunn",
                    telefonnummer = Telefonnummer("55553334"),
                ),
            bruker =
                Bruker(
                    fornavn = "Test",
                    mellomnavn = "bruker",
                    etternavn = "Testerson",
                    foedselsnummer = Foedselsnummer("01019878910"),
                ),
            signerendeSaksbehandlere =
                SignerendeSaksbehandlere(
                    saksbehandler = "Ole Saksbehandler",
                    attesterendeSaksbehandler = "Per Attesterende",
                ),
            vergeNavn = null,
        )

    val fellesAuto = felles.copy(signerendeSaksbehandlere = null)

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
            OmstillingsstoenadAktivitetspliktVarselbrevInnholdDTO::class -> createOmstillingsstoenadAktivitetspliktVarselbrevInnholdDTO() as T
            OmstillingsstoenadAktivitetspliktInformasjon4mndInnholdDTO::class -> createOmstillingsstoenadAktivitetspliktInformasjon4mndInnholdDTO() as T
            OmstillingsstoenadAktivitetspliktInformasjon10mndInnholdDTO::class -> createOmstillingsstoenadAktivitetspliktInformasjon10mndInnholdDTO() as T
            OmstillingsstoenadAktivitetspliktInformasjon6mndInnholdDTO::class -> createOmstillingsstoenadAktivitetspliktInformasjon6mndDto() as T
            OmstillingsstoenadVarselAktivitetspliktDTO::class -> createOmstillingsstoenadVarselAktivitetspliktDTO() as T
            OmstillingsstoenadVarselAktivitetspliktRedigerbartUtfallDTO::class -> createOmstillingsstoenadVarselAktivitetspliktRedigerbartUtfallDTO() as T

            OmstillingsstoenadInntektsjusteringVedtakDTO::class -> createOmstillingsstoenadInntektsjusteringVedtakDTO() as T
            OmstillingsstoenadInntektsjusteringVarselDTO::class -> createOmstillingsstoenadInntektsjusteringVarselDTO() as T
            OmstillingsstoenadVedtakInntektsjusteringRedigerbartUtfallDTO::class -> createOmstillingsstoenadInntektsjusteringVedtakRedigerbartUtfallDTO() as T

            TilbakekrevingBrevDTO::class -> createTilbakekrevingFerdigDTO() as T
            TilbakekrevingRedigerbartBrevDTO::class -> createTilbakekrevingRedigerbartBrevDTO() as T

            AvvistKlageInnholdDTO::class -> createAvvistKlageInnholdDTO() as T
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

