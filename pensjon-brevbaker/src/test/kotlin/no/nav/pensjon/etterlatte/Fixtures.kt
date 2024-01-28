package no.nav.pensjon.etterlatte

import no.nav.pensjon.brevbaker.api.model.Bruker
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.NAVEnhet
import no.nav.pensjon.brevbaker.api.model.SignerendeSaksbehandlere
import no.nav.pensjon.brevbaker.api.model.Telefonnummer
import no.nav.pensjon.etterlatte.fixtures.createBarnepensjonAvslagDTO
import no.nav.pensjon.etterlatte.fixtures.createBarnepensjonInnvilgelseDTO
import no.nav.pensjon.etterlatte.fixtures.createBarnepensjonInnvilgelseRedigerbartUtfallDTO
import no.nav.pensjon.etterlatte.fixtures.createBarnepensjonOmregnetNyttRegelverkDTO
import no.nav.pensjon.etterlatte.fixtures.createBarnepensjonOmregnetNyttRegelverkFerdigDTO
import no.nav.pensjon.etterlatte.fixtures.createBarnepensjonOpphoerDTO
import no.nav.pensjon.etterlatte.fixtures.createBarnepensjonRevurderingDTO
import no.nav.pensjon.etterlatte.fixtures.createBarnepensjonRevurderingRedigerbartUtfallDTO
import no.nav.pensjon.etterlatte.fixtures.createManueltBrevDTO
import no.nav.pensjon.etterlatte.fixtures.createOmstillingsstoenadAvslagDTO
import no.nav.pensjon.etterlatte.fixtures.createOmstillingsstoenadInnvilgelseDTO
import no.nav.pensjon.etterlatte.fixtures.createOmstillingsstoenadInnvilgelseRedigerbartUtfallDTO
import no.nav.pensjon.etterlatte.fixtures.createOmstillingsstoenadRevurderingDTO
import no.nav.pensjon.etterlatte.fixtures.createOmstillingsstoenadpphoerDTO
import no.nav.pensjon.etterlatte.fixtures.createOmstillingsstoenadpphoerRedigerbartUtfallDTO
import no.nav.pensjon.etterlatte.fixtures.createTilbakekrevingFerdigDTO
import no.nav.pensjon.etterlatte.fixtures.createTilbakekrevingInnholdDTO
import no.nav.pensjon.etterlatte.fixtures.createTomMal
import no.nav.pensjon.etterlatte.fixtures.createTomMalInformasjonsbrev
import no.nav.pensjon.etterlatte.maler.ManueltBrevDTO
import no.nav.pensjon.etterlatte.maler.ManueltBrevMedTittelDTO
import no.nav.pensjon.etterlatte.maler.andre.TomMal
import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.BarnepensjonAvslagDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonInnvilgelseDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonInnvilgelseRedigerbartUtfallDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkFerdigDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.opphoer.BarnepensjonOpphoerDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingRedigerbartUtfallDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.OmstillingstoenadAvslagDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseRedigerbartUtfallDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer.OmstillingsstoenadOpphoerDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer.OmstillingsstoenadOpphoerRedigerbartUtfallDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingDTO
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingFerdigDTO
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingInnholdDTO
import java.time.LocalDate
import kotlin.reflect.KClass

object Fixtures {

    val felles = Felles(
        dokumentDato = LocalDate.of(2020, 1, 1),
        saksnummer = "1337123",
        avsenderEnhet = NAVEnhet(
            nettside = "nav.no",
            navn = "NAV Familie- og pensjonsytelser Porsgrunn",
            telefonnummer = Telefonnummer("55553334"),
        ),
        bruker = Bruker(
            fornavn = "Test",
            mellomnavn = "bruker",
            etternavn = "Testerson",
            foedselsnummer = Foedselsnummer("01019878910"),
        ),
        signerendeSaksbehandlere = SignerendeSaksbehandlere(
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
            BarnepensjonAvslagDTO::class -> createBarnepensjonAvslagDTO() as T
            BarnepensjonOpphoerDTO::class -> createBarnepensjonOpphoerDTO() as T
            BarnepensjonRevurderingDTO::class -> createBarnepensjonRevurderingDTO() as T
            BarnepensjonRevurderingRedigerbartUtfallDTO::class -> createBarnepensjonRevurderingRedigerbartUtfallDTO() as T
            BarnepensjonOmregnetNyttRegelverkDTO::class -> createBarnepensjonOmregnetNyttRegelverkDTO() as T
            BarnepensjonOmregnetNyttRegelverkFerdigDTO::class -> createBarnepensjonOmregnetNyttRegelverkFerdigDTO() as T

            OmstillingsstoenadInnvilgelseDTO::class -> createOmstillingsstoenadInnvilgelseDTO() as T
            OmstillingsstoenadInnvilgelseRedigerbartUtfallDTO::class -> createOmstillingsstoenadInnvilgelseRedigerbartUtfallDTO() as T
            OmstillingstoenadAvslagDTO::class -> createOmstillingsstoenadAvslagDTO() as T
            OmstillingsstoenadRevurderingDTO::class -> createOmstillingsstoenadRevurderingDTO() as T
            OmstillingsstoenadOpphoerDTO::class -> createOmstillingsstoenadpphoerDTO() as T
            OmstillingsstoenadOpphoerRedigerbartUtfallDTO::class -> createOmstillingsstoenadpphoerRedigerbartUtfallDTO() as T

            TilbakekrevingInnholdDTO::class -> createTilbakekrevingInnholdDTO() as T
            TilbakekrevingFerdigDTO::class -> createTilbakekrevingFerdigDTO() as T

            ManueltBrevDTO::class -> createManueltBrevDTO() as T
            ManueltBrevMedTittelDTO::class -> createTomMalInformasjonsbrev() as T
            TomMal::class -> createTomMal() as T
            Unit::class -> Unit as T

            else -> throw IllegalArgumentException("Don't know how to construct: ${letterDataType.qualifiedName}")
        }
}
